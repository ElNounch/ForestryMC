package forestry.climatology.climate;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import forestry.api.climate.IClimateState;
import forestry.api.climatology.IClimateTransformer;
import forestry.core.climate.ClimateStates;

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
		return ClimateStates.INSTANCE.absent();
	}

	@Override
	public IClimateState getCurrent() {
		return ClimateStates.INSTANCE.absent();
	}

	@Override
	public IClimateState getBiome() {
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
