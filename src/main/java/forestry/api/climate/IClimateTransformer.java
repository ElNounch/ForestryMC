package forestry.api.climate;

import net.minecraft.util.math.BlockPos;

import forestry.api.core.ILocatable;

public interface IClimateTransformer extends ILocatable {
	int getRadius();

	IClimateState getTarget();

	IClimateState getCurrent();

	IClimateState getBiome();

	default boolean isInRange(BlockPos pos) {
		return isInRange(new Position2D(pos));
	}

	default boolean isInRange(Position2D pos) {
		double distance = pos.getDistance(getCoordinates());
		int range = getRadius();
		return distance <= range;
	}

	default double getDistance(BlockPos pos) {
		return getDistance(pos);
	}

	default double getDistance(Position2D pos) {
		return pos.getDistance(getCoordinates());
	}
}
