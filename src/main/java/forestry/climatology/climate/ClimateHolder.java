package forestry.climatology.climate;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

import net.minecraftforge.common.capabilities.Capability;

import forestry.api.climate.IClimateState;
import forestry.climatology.api.climate.ClimatologyCapabilities;
import forestry.climatology.api.climate.IClimateHolder;
import forestry.climatology.api.climate.IClimateTransformer;
import forestry.climatology.api.climate.Position2D;
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
		for(IClimateTransformer transformer : transformers.get(new Position2D(pos))){
			if(transformer.isInRange(pos)){
				state = state.add(transformer.getCurrent());
				transformerCount++;
			}
		}
		return transformerCount > 0 ? state.scale(1.0D / transformerCount).toImmutable() : ClimateStates.ZERO;
	}

	@Override
	public long getLastUpdate() {
		return lastUpdate;
	}

	@Override
	public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
		return capability == ClimatologyCapabilities.CLIMATE_HOLDER;
	}

	@Nullable
	@Override
	public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
		return capability == ClimatologyCapabilities.CLIMATE_HOLDER ? ClimatologyCapabilities.CLIMATE_HOLDER.cast(this) : null;
	}
}
