package forestry.api.climatology;

import forestry.api.climate.IClimateProvider;
import forestry.api.climate.IClimateState;

public interface IExtendedClimateProvider extends IClimateProvider {
	IClimateState getClimate();
}
