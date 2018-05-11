package forestry.api.climate;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * @since Forestry 5.8.1
 */
public interface IClimateRoot {

	/**
	 * Gets the current state of a container at this position or setSettings one with the datas from the biome.
	 */
	IClimateState getClimateState(World world, BlockPos pos);

	/**
	 * Creates a climate state with the help of the biome on this position.
	 */
	IClimateState getBiomeState(World world, BlockPos pos);

	/**
	 * @return Create a climate manager.
	 */
	IClimateProvider getDefaultClimate(World world, BlockPos pos);
}
