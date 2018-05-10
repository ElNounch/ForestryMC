package forestry.climatology.climate;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

import net.minecraftforge.common.capabilities.Capability;

import forestry.api.climate.IClimateState;
import forestry.api.climatology.ClimateCapabilities;
import forestry.api.climatology.IClimateHolder;
import forestry.api.climatology.IClimateTransformer;
import forestry.api.climatology.Position2D;
import forestry.core.climate.ClimateStates;

public class ClimateHolder implements IClimateHolder {
	private final Multimap<Position2D, IClimateTransformer> transformers = HashMultimap.create();
	private long lastUpdate;

	@Override
	public boolean addTransformer(IClimateTransformer transformer) {
		lastUpdate = transformer.getWorldObj().getTotalWorldTime();
		return transformers.put(new Position2D(transformer.getCoordinates()), transformer);
	}

	@Override
	public boolean removeTransformer(IClimateTransformer transformer) {
		lastUpdate = transformer.getWorldObj().getTotalWorldTime();
		return transformers.remove(new Position2D(transformer.getCoordinates()), transformer);
	}

	@Override
	public boolean hasTransformers() {
		return transformers.isEmpty();
	}

	@Override
	public IClimateState getState(BlockPos pos) {
		double transformerCount = 0;
		IClimateState state = ClimateStates.INSTANCE.mutableZero();
		for (IClimateTransformer transformer : transformers.values()) {
			if (transformer.isInRange(pos)) {
				state = state.add(transformer.getCurrent());
				transformerCount++;
			}
		}
		return transformerCount > 0 ? state.scale(1.0D / transformerCount).toImmutable() : ClimateStates.INSTANCE.absent();
	}

	@Override
	public long getLastUpdate() {
		return lastUpdate;
	}

	@Override
	public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
		return capability == ClimateCapabilities.CLIMATE_HOLDER;
	}

	@Nullable
	@Override
	public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
		return capability == ClimateCapabilities.CLIMATE_HOLDER ? ClimateCapabilities.CLIMATE_HOLDER.cast(this) : null;
	}
}
