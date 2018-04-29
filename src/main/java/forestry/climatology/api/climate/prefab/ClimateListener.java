package forestry.climatology.api.climate.prefab;

import javax.annotation.Nullable;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import forestry.api.climate.IClimateState;
import forestry.api.core.ForestryAPI;
import forestry.api.core.ILocatable;
import forestry.climatology.api.climate.ClimatologyCapabilities;
import forestry.climatology.api.climate.IClimateHolder;
import forestry.climatology.api.climate.IClimateListener;
import forestry.climatology.network.packets.PacketUpdateListener;
import forestry.climatology.network.packets.PacketUpdateListenerEntity;
import forestry.core.render.ParticleRender;
import forestry.core.utils.NetworkUtil;
import forestry.core.utils.TickHelper;

public class ClimateListener implements IClimateListener {
	private final Object locationProvider;
	@Nullable
	private IClimateHolder holder = null;
	private IClimateState cachedState = ForestryAPI.states.absent();
	@SideOnly(Side.CLIENT)
	private IClimateState clientState = ForestryAPI.states.absent();
	@SideOnly(Side.CLIENT)
	private TickHelper tickHelper = new TickHelper();
	private long cacheTime = 0;
	private long lastUpdate = 0;
	@Nullable
	protected World world;
	@Nullable
	protected BlockPos pos;
	private boolean added;

	public ClimateListener(Object locationProvider) {
		this.locationProvider = locationProvider;
	}

	/*@Override
	public void onClimateChange(IClimateLogic logic, IClimateState oldState, IClimateState newState) {
	}

	@Override
	public void onDetach(IClimateLogic logic) {
	}

	@Override
	public void onAttach(IClimateLogic logic) {
	}*/

	@SideOnly(Side.CLIENT)
	@Override
	public void updateClient(){
		tickHelper.onTick();
		if(tickHelper.updateOnInterval(10)){
			World worldObj = getWorldObj();
			BlockPos coordinates = getCoordinates();
			IBlockState blockState = worldObj.getBlockState(coordinates);
			ParticleRender.addClimateParticles(worldObj, coordinates, blockState, worldObj.rand, cachedState);
		}
	}

	@Nullable
	private IClimateHolder getHolder() {
		if(holder == null) {
			Chunk chunk = getWorldObj().getChunkFromBlockCoords(getCoordinates());
			if (chunk != null) {
				holder = chunk.getCapability(ClimatologyCapabilities.CLIMATE_HOLDER, null);
			}
		}
		return holder;
	}

	@Override
	public IClimateState getState() {
		World worldObj = getWorldObj();
		if(worldObj.isRemote){
			return getClientState();
		}
		IClimateHolder chunk = getHolder();
		if(chunk == null){
			return ForestryAPI.states.absent();
		}
		long totalTime = getWorldObj().getTotalWorldTime();
		if(cacheTime + 250 > totalTime && chunk.getLastUpdate() == lastUpdate){
			return cachedState;
		}
		lastUpdate = chunk.getLastUpdate();
		cachedState = chunk.getState(getCoordinates());
		cacheTime = totalTime;
		if(locationProvider instanceof Entity){
			NetworkUtil.sendNetworkPacket(new PacketUpdateListenerEntity((Entity)locationProvider, cachedState), getCoordinates(), worldObj);
		}else {
			NetworkUtil.sendNetworkPacket(new PacketUpdateListener(getCoordinates(), cachedState), getCoordinates(), worldObj);
		}
		return cachedState;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void setClientState(IClimateState clientState) {
		this.clientState = clientState;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public IClimateState getClientState() {
		return clientState;
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
			ILocatable provider = (ILocatable)this.locationProvider;
			this.world = provider.getWorldObj();
			this.pos = provider.getCoordinates();
		} else if ((this.locationProvider instanceof TileEntity)) {
			TileEntity provider = (TileEntity)this.locationProvider;
			this.world = provider.getWorld();
			this.pos = provider.getPos();
		} else {
			throw new IllegalStateException("no/incompatible location provider");
		}
	}
}
