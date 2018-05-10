package forestry.api.climatology.prefab;

import javax.annotation.Nullable;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import forestry.api.climate.IClimateState;
import forestry.api.climatology.ClimateCapabilities;
import forestry.api.climatology.IClimateHolder;
import forestry.api.climatology.IClimateListener;
import forestry.api.core.ForestryAPI;
import forestry.api.core.ILocatable;
import forestry.climatology.network.packets.PacketUpdateListener;
import forestry.climatology.network.packets.PacketUpdateListenerEntity;
import forestry.climatology.network.packets.PacketUpdateListenerEntityRequest;
import forestry.climatology.network.packets.PacketUpdateListenerRequest;
import forestry.core.render.ParticleRender;
import forestry.core.utils.NetworkUtil;
import forestry.core.utils.TickHelper;

public class ClimateListener implements IClimateListener {
	public static final int SERVER_UPDATE = 250;
	public static final int CLIENT_UPDATE = 250;

	private final Object locationProvider;
	@Nullable
	private IClimateHolder holder = null;
	private IClimateState cachedState = ForestryAPI.states.absent();
	@SideOnly(Side.CLIENT)
	private IClimateState clientState = ForestryAPI.states.absent();
	@SideOnly(Side.CLIENT)
	private TickHelper tickHelper = new TickHelper();
	@SideOnly(Side.CLIENT)
	protected boolean needsClimateUpdate = true;
	@SideOnly(Side.CLIENT)
	private long lastClientUpdate = 0;
	//The total world time at the moment the cached state has been updated
	private long cacheTime = 0;
	private long lastUpdate = 0;
	@Nullable
	protected World world;
	@Nullable
	protected BlockPos pos;

	public ClimateListener(Object locationProvider) {
		this.locationProvider = locationProvider;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void updateClientSide() {
		long totalTime = getWorldObj().getTotalWorldTime();
		tickHelper.onTick();
		if (clientState.isPresent()) {
			if (tickHelper.updateOnInterval(10)) {
				World worldObj = getWorldObj();
				BlockPos coordinates = getCoordinates();
				IBlockState blockState = worldObj.getBlockState(coordinates);
				ParticleRender.addClimateParticles(worldObj, coordinates, blockState, worldObj.rand, cachedState);
			}
		} else if (needsClimateUpdate || lastClientUpdate + CLIENT_UPDATE < totalTime) {
			if (locationProvider instanceof Entity) {
				NetworkUtil.sendToServer(new PacketUpdateListenerEntityRequest((Entity) locationProvider));
			} else {
				NetworkUtil.sendToServer(new PacketUpdateListenerRequest(getCoordinates()));
			}
			needsClimateUpdate = false;
		}
	}

	@Nullable
	private IClimateHolder getHolder() {
		if (holder == null) {
			IChunkProvider chunkProvider = getWorldObj().getChunkProvider();
			BlockPos coordinates = getCoordinates();
			Chunk chunk = chunkProvider.getLoadedChunk(coordinates.getX() >> 4, coordinates.getZ() >> 4);
			if (chunk != null) {
				holder = chunk.getCapability(ClimateCapabilities.CLIMATE_HOLDER, null);
			}
		}
		return holder;
	}

	private void updateState(boolean syncToClient) {
		IClimateHolder chunk = getHolder();
		if (chunk == null) {
			return;
		}
		long totalTime = getWorldObj().getTotalWorldTime();
		if (cacheTime + SERVER_UPDATE > totalTime && chunk.getLastUpdate() == lastUpdate) {
			return;
		}
		lastUpdate = chunk.getLastUpdate();
		cachedState = chunk.getState(getCoordinates());
		cacheTime = totalTime;
		if (syncToClient) {
			syncToClient();
		}
	}

	@Override
	public IClimateState getState(boolean update, boolean syncToClient) {
		World worldObj = getWorldObj();
		if (worldObj.isRemote) {
			return getClientState();
		}
		if (update) {
			updateState(syncToClient);
		}
		return cachedState;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void setClientState(IClimateState clientState) {
		this.clientState = clientState;
		this.lastClientUpdate = getWorldObj().getTotalWorldTime();
	}

	@SideOnly(Side.CLIENT)
	@Override
	public IClimateState getClientState() {
		return clientState;
	}

	@Override
	public void syncToClient() {
		World worldObj = getWorldObj();
		if (!worldObj.isRemote) {
			BlockPos coordinates = getCoordinates();
			if (locationProvider instanceof Entity) {
				NetworkUtil.sendNetworkPacket(new PacketUpdateListenerEntity((Entity) locationProvider, cachedState), coordinates, worldObj);
			} else {
				NetworkUtil.sendNetworkPacket(new PacketUpdateListener(getCoordinates(), cachedState), coordinates, getWorldObj());
			}
		}
	}

	@Override
	public void syncToClient(EntityPlayerMP player) {
		World worldObj = getWorldObj();
		if (!worldObj.isRemote) {
			if (locationProvider instanceof Entity) {
				NetworkUtil.sendToPlayer(new PacketUpdateListenerEntity((Entity) locationProvider, getState(true, false)), player);
			} else {
				NetworkUtil.sendToPlayer(new PacketUpdateListener(getCoordinates(), getState(true, false)), player);
			}
		}
	}

	@Override
	public BlockPos getCoordinates() {
		if (this.pos == null) {
			initLocation();
		}
		return this.pos;
	}

	@Override
	public World getWorldObj() {
		if (this.world == null) {
			initLocation();
		}
		return this.world;
	}

	private void initLocation() {
		if ((this.locationProvider instanceof ILocatable)) {
			ILocatable provider = (ILocatable) this.locationProvider;
			this.world = provider.getWorldObj();
			this.pos = provider.getCoordinates();
		} else if ((this.locationProvider instanceof TileEntity)) {
			TileEntity provider = (TileEntity) this.locationProvider;
			this.world = provider.getWorld();
			this.pos = provider.getPos();
		} else {
			throw new IllegalStateException("no/incompatible location provider");
		}
	}
}
