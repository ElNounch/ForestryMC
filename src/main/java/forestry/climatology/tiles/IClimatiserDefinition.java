package forestry.climatology.tiles;

import forestry.api.climatology.source.IClimateSourceProxy;
import forestry.climatology.climate.source.ClimateSource;

public interface IClimatiserDefinition<P extends IClimateSourceProxy> {
	ClimateSource createSource(P proxy);
}
