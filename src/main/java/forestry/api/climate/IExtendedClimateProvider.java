package forestry.api.climate;

public interface IExtendedClimateProvider extends IClimateProvider {
	IClimateState getClimate();
}
