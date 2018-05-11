package forestry.core.climate;

import net.minecraft.world.biome.Biome;

import forestry.api.climate.IClimateProvider;
import forestry.api.climate.IClimateState;
import forestry.api.climate.IExtendedClimateProvider;
import forestry.api.core.EnumHumidity;
import forestry.api.core.EnumTemperature;

public class ExtendedClimateProvider implements IExtendedClimateProvider {
	private final IClimateState climateState;
	private final EnumTemperature temperature;
	private final EnumHumidity humidity;
	private final Biome biome;

	public ExtendedClimateProvider(IClimateState climateState, IClimateProvider original) {
		this.temperature = EnumTemperature.getFromState(original.getBiome(), climateState);
		this.humidity = EnumHumidity.getFromValue(climateState.getHumidity());
		this.biome = original.getBiome();
		this.climateState = climateState.toImmutable();
	}

	@Override
	public IClimateState getClimate() {
		return climateState;
	}

	@Override
	public Biome getBiome() {
		return biome;
	}

	@Override
	public EnumTemperature getTemperature() {
		return temperature;
	}

	@Override
	public EnumHumidity getHumidity() {
		return humidity;
	}
}
