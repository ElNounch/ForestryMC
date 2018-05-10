package forestry.climatology.climate;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import forestry.api.climate.IClimateState;
import forestry.api.climatology.IClimateListener;
import forestry.core.climate.ClimateStates;

public class FakeClimateListener implements IClimateListener {

	public static final FakeClimateListener INSTANCE = new FakeClimateListener();

	private FakeClimateListener() {
	}

	@Override
	public IClimateState getState(boolean update, boolean syncToClient) {
		return ClimateStates.INSTANCE.absent();
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void updateClientSide() {

	}

	@SideOnly(Side.CLIENT)
	@Override
	public void setClientState(IClimateState clientState) {
	}

	@SideOnly(Side.CLIENT)
	@Override
	public IClimateState getClientState() {
		return ClimateStates.INSTANCE.absent();
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void syncToClient() {

	}

	@SideOnly(Side.CLIENT)
	@Override
	public void syncToClient(EntityPlayerMP player) {

	}

	@Override
	public BlockPos getCoordinates() {
		return BlockPos.ORIGIN;
	}

	@Override
	public World getWorldObj() {
		return null;
	}
}
