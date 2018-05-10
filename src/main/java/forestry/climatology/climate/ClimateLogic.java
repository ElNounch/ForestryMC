/*******************************************************************************
 * Copyright (c) 2011-2014 SirSengir.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v3
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl-3.0.txt
 *
 * Various Contributors including, but not limited to:
 * SirSengir (original work), CovertJaguar, Player, Binnie, MysteriousAges
 ******************************************************************************/
package forestry.climatology.climate;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

import forestry.api.climate.IClimateState;
import forestry.api.climatology.ClimateCapabilities;
import forestry.api.climatology.IClimateHolder;
import forestry.api.climatology.IClimateHousing;
import forestry.api.climatology.IClimateLogic;
import forestry.api.climatology.source.IClimateSource;
import forestry.api.core.ForestryAPI;
import forestry.core.climate.AbsentClimateState;
import forestry.core.climate.ClimateManager;
import forestry.core.climate.ClimateStates;
import forestry.core.network.IStreamable;
import forestry.core.network.PacketBufferForestry;
import forestry.core.utils.TickHelper;

public class ClimateLogic implements IClimateLogic, IStreamable {

	//The default radius of every climate logic.
	private static final int DEFAULT_BLOCK_RADIUS = 48;

	//The tile entity that provides this logic.
	protected final IClimateHousing housing;
	//All climate sources of this logic.
	protected final Set<IClimateSource> sources;
	//The state that this logic targets to reach.
	private IClimateState targetedState;
	//The current climate state of this logic.
	protected IClimateState currentState;
	private NBTTagCompound modifierData;
	private TickHelper tickHelper;
	//The climate state of the biome that is located at the position of this tile.
	private IClimateState biomeState;
	//A array that contains the position of every chunk that is in range of this former and loaded.
	private long[] chunks;
	//The radius of the habitatformer in blocks
	private int radius;
	//True if the logic added itself to the surrounding chunks, false if update() was never called or if the logic has
	//been invalidated.
	private boolean active;

	public ClimateLogic(IClimateHousing housing) {
		this.housing = housing;
		this.sources = new HashSet<>();
		this.currentState = ClimateStates.INSTANCE.absent();
		this.modifierData = new NBTTagCompound();
		this.biomeState = AbsentClimateState.INSTANCE;
		this.targetedState = AbsentClimateState.INSTANCE;
		this.tickHelper = new TickHelper();
		this.chunks = new long[0];
		this.radius = DEFAULT_BLOCK_RADIUS;
		this.active = false;
	}

	@Override
	public IClimateHousing getHousing() {
		return housing;
	}

	@Override
	public void update() {
		if (!active) {
			biomeState = ClimateManager.getInstance().getBiomeState(getWorldObj(), getCoordinates());
			if (!targetedState.isPresent()) {
				setState(biomeState.copy());
				setTarget(biomeState);
			}
			addChunks();
			active = true;
		}
		tickHelper.onTick();
		if (tickHelper.updateOnInterval(20)) {
			IClimateState previousState = currentState.copy(false);
			currentState = biomeState.copy(true);
			if (targetedState.isPresent()) {
				sources.forEach(source -> currentState.add(source.getState()));

				sources.forEach(source -> currentState.add(source.work(this, previousState, targetedState, currentState)));
			}
			currentState = currentState.toImmutable();
			/*if (!state.equals(lastState)) {
				BlockPos coordinates = housing.getCoordinates();
				NetworkUtil.sendNetworkPacket(new PacketUpdateClimate(coordinates, this), coordinates, housing.getWorldObj());
			}*/
		}
	}

	/* Climate Holders */
	@Override
	public void invalidate() {
		removeChunks();
	}

	/**
	 * Adds this logic from the surrounding chunks.
	 */
	private void addChunks() {
		BlockPos pos = housing.getCoordinates();
		World world = housing.getWorldObj();
		Set<Long> chunkSet = new HashSet<>();
		Vector center = new Vector(pos.getX() >> 4, pos.getZ() >> 4);
		Vector start = new Vector(center.x - radius, center.z - radius);
		Vector area = new Vector(radius * 2.0f + 1.0f);
		for (int x = (int) start.x; x < (int) start.x + area.x; ++x) {
			for (int z = (int) start.z; z < (int) start.z + area.z; ++z) {
				Vector current = new Vector(x, z);
				if (current.distance(center) <= radius + 0.01f && current.distance(center) < radius - 0.5f) {
					Chunk chunk = world.getChunkProvider().getLoadedChunk((int) current.x, (int) current.z);
					if (chunk != null) {
						IClimateHolder holder = chunk.getCapability(ClimateCapabilities.CLIMATE_HOLDER, null);
						if (holder != null) {
							holder.addTransformer(this);
						}
					}
					chunkSet.add(current.toChunkPos());
				}
			}
		}
		this.chunks = chunkSet.stream().mapToLong(l -> l).toArray();
		active = false;
	}

	/**
	 * Removes this logic from the surrounding chunks.
	 */
	private void removeChunks() {
		World world = housing.getWorldObj();
		for (long chungPos : chunks) {
			int x = (int) (chungPos & 4294967295L);
			int y = (int) ((chungPos >> 32) & 4294967295L);
			Chunk chunk = world.getChunkProvider().getLoadedChunk(x, y);
			if (chunk != null) {
				IClimateHolder holder = chunk.getCapability(ClimateCapabilities.CLIMATE_HOLDER, null);
				if (holder != null) {
					holder.removeTransformer(this);
				}
			}
		}
		this.chunks = new long[0];
		this.active = false;
	}

	/* Save and Load */
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		currentState.writeToNBT(nbt);
		nbt.setTag("Target", targetedState.writeToNBT(new NBTTagCompound()));
		nbt.setTag("modifierData", modifierData.copy());
		return nbt;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		currentState = ForestryAPI.states.create(nbt);
		targetedState = ForestryAPI.states.create(nbt.getCompoundTag("Target"));
		modifierData = nbt.getCompoundTag("modifierData");
	}

	@Override
	public void setTarget(IClimateState target) {
		this.targetedState = target;
	}

	public void setState(IClimateState state) {
		this.currentState = state;
	}

	@Override
	public void onAddSource(IClimateSource source) {
		sources.add(source);
	}

	@Override
	public void onRemoveSource(IClimateSource source) {
		sources.remove(source);
	}

	@Override
	public Collection<IClimateSource> getClimateSources() {
		return sources;
	}

	@Override
	public void writeData(PacketBufferForestry data) {
		data.writeClimateState(currentState);
		data.writeClimateState(targetedState);
		data.writeCompoundTag(modifierData);
	}

	@Override
	public void readData(PacketBufferForestry data) throws IOException {
		currentState = data.readClimateState();
		targetedState = data.readClimateState();
		NBTTagCompound modifierTag = data.readCompoundTag();
		if (modifierTag == null) {
			modifierTag = new NBTTagCompound();
		}
		modifierData = modifierTag;
	}

	@Override
	public IClimateState getCurrent() {
		return currentState;
	}

	@Override
	public IClimateState getTarget() {
		return targetedState;
	}

	@Override
	public IClimateState getBiome() {
		return biomeState;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof IClimateLogic)) {
			return false;
		}
		IClimateLogic container = (IClimateLogic) obj;
		IClimateHousing parent = container.getHousing();
		if (parent.getCoordinates() == null || this.housing.getCoordinates() == null) {
			return false;
		}
		return this.housing.getCoordinates().equals(parent.getCoordinates());
	}

	@Override
	public int hashCode() {
		return housing.getCoordinates().hashCode();
	}

	@Override
	public int getRadius() {
		return radius;
	}

	private static class Vector {
		private final float x;
		private final float z;

		private Vector(float value) {
			this(value, value);
		}

		private Vector(float x, float z) {
			this.x = x;
			this.z = z;
		}

		public double distance(Vector other) {
			return Math.sqrt(Math.pow(x - other.x, 2.0) + Math.pow(z - other.z, 2.0));
		}

		private long toChunkPos() {
			return ChunkPos.asLong((int) x, (int) z);
		}
	}
}
