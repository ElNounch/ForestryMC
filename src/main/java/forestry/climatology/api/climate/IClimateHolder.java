package forestry.climatology.api.climate;

import net.minecraft.util.math.BlockPos;

import net.minecraftforge.common.capabilities.ICapabilityProvider;

import forestry.api.climate.IClimateState;

public interface IClimateHolder extends ICapabilityProvider {

	boolean addTransformer(IClimateTransformer transformer);

	boolean removeTransformer(IClimateTransformer transformer);

	boolean hasTransformers();

	IClimateState getState(BlockPos pos);

	long getLastUpdate();

}
