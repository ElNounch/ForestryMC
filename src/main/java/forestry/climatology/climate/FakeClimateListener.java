package forestry.climatology.climate;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import forestry.api.climate.IClimateState;
import forestry.climatology.api.climate.IClimateListener;
import forestry.core.climate.ClimateStates;

public class FakeClimateListener implements IClimateListener {

	public static final FakeClimateListener INSTANCE = new FakeClimateListener();

	private FakeClimateListener() {
	}

	@Override
	public void update() {
	}

	@Override
	public void invalidate() {
	}

	@Override
	public IClimateState getState() {
		return ClimateStates.INSTANCE.absent();
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void updateClient() {

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

	@Override
	public BlockPos getCoordinates() {
		return BlockPos.ORIGIN;
	}

	@Override
	public World getWorldObj() {
		return null;
	}
}
