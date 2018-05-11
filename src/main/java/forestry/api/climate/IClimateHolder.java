package forestry.api.climate;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import net.minecraftforge.common.capabilities.ICapabilityProvider;

/**
 * A climate holder contains different climate transformer that can change the climate in an chunk.
 */
public interface IClimateHolder extends ICapabilityProvider {

	/**
	 * Adds a transformer to this holder.
	 */
	boolean addTransformer(IClimateTransformer transformer);

	/**
	 * Removes a transformer from this holder.
	 */
	boolean removeTransformer(IClimateTransformer transformer);

	/**
	 * @return True if this holder has any transformers.
	 */
	boolean hasTransformers();

	/**
	 * @return The difference between the climate of the biome at the given position and climate of the transformers
	 * that are in range of this location.
	 */
	IClimateState getState(BlockPos pos);

	/**
	 * @return the {@link World#getTotalWorldTime()} at the moment the last transformer changed its
	 * {@link IClimateTransformer#getCurrent()} state or a {@link IClimateTransformer} has been removed or added to the
	 * holder.
	 */
	long getLastUpdate();

}
