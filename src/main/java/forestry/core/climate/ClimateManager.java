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
package forestry.core.climate;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;

import forestry.api.climate.IClimateManager;
import forestry.api.climate.IClimateProvider;
import forestry.api.climate.IClimateState;
import forestry.api.climatology.ClimateCapabilities;
import forestry.api.climatology.IClimateHolder;
import forestry.core.DefaultClimateProvider;

public class ClimateManager implements IClimateManager {

	private static final ClimateManager INSTANCE = new ClimateManager();

	private static final Map<Biome, IClimateState> BIOME_STATES = new HashMap<>();
	
	public static ClimateManager getInstance(){
		return INSTANCE;
	}

	@Override
	public IClimateState getBiomeState(World world, BlockPos pos) {
		Biome biome = world.getBiome(pos);
		return getBiomeState(biome);
	}

	private IClimateState getBiomeState(Biome biome) {
		if (!BIOME_STATES.containsKey(biome)) {
			BIOME_STATES.put(biome, ClimateStates.of(biome.getDefaultTemperature(), biome.getRainfall()));
		}
		return BIOME_STATES.get(biome);
	}
	
	@Override
	public IClimateState getClimateState(World world, BlockPos pos) {
		Chunk chunk = world.getChunkProvider().getLoadedChunk(pos.getX() >> 4, pos.getZ() >> 4);
		if(chunk != null && chunk.hasCapability(ClimateCapabilities.CLIMATE_HOLDER, null)){
			IClimateHolder holder = chunk.getCapability(ClimateCapabilities.CLIMATE_HOLDER, null);
			if(holder != null){
				IClimateState state = holder.getState(pos);
				if(state.isPresent()){
					return state;
				}
			}
		}
		return getBiomeState(world, pos);
	}

	@Override
	public IClimateProvider getDefaultClimate(World world, BlockPos pos) {
		return new DefaultClimateProvider(world, pos);
	}

}
