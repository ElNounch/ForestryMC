package forestry.core.climate;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import forestry.api.climate.IClimateState;
import forestry.api.climate.IClimateTransformer;

public class FakeClimateTransformer implements IClimateTransformer {
	public static final FakeClimateTransformer INSTANCE = new FakeClimateTransformer();

	private FakeClimateTransformer() {
	}

	@Override
	public int getRadius() {
		return 0;
	}

	@Override
	public IClimateState getTarget() {
		return ClimateStateHelper.INSTANCE.absent();
	}

	@Override
	public IClimateState getCurrent() {
		return ClimateStateHelper.INSTANCE.absent();
	}

	@Override
	public IClimateState getBiome() {
		return ClimateStateHelper.INSTANCE.absent();
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
