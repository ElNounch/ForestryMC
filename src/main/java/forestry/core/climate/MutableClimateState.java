package forestry.core.climate;

import com.google.common.base.MoreObjects;

import net.minecraft.nbt.NBTTagCompound;

import forestry.api.climate.IClimateState;

class MutableClimateState implements IClimateState {
	private static final String TEMPERATURE_NBT_KEY = "TEMP";
	private static final String HUMIDITY_NBT_KEY = "HUMID";
	public static final String ABSENT_NBT_KEY = "ABSENT";

	protected float temperature;
	protected float humidity;

	MutableClimateState(IClimateState climateState) {
		this(climateState.getTemperature(), climateState.getHumidity());
	}

	MutableClimateState(float temperature, float humidity) {
		this.temperature = temperature;
		this.humidity = humidity;
	}

	MutableClimateState(NBTTagCompound compound) {
		this.temperature = compound.getFloat(TEMPERATURE_NBT_KEY);
		this.humidity = compound.getFloat(HUMIDITY_NBT_KEY);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		compound.setFloat(TEMPERATURE_NBT_KEY, temperature);
		compound.setFloat(HUMIDITY_NBT_KEY, humidity);
		compound.setBoolean(ABSENT_NBT_KEY, !isPresent());
		return compound;
	}

	@Override
	public IClimateState copy(boolean mutable) {
		return ClimateStates.INSTANCE.create(this, mutable);
	}

	@Override
	public IClimateState copy() {
		return copy(true);
	}

	@Override
	public IClimateState toMutable() {
		return this;
	}

	@Override
	public IClimateState toImmutable() {
		return copy(false);
	}

	@Override
	public IClimateState addTemperature(float temperature){
		this.temperature += temperature;
		return ClimateStates.INSTANCE.checkState(this);
	}

	@Override
	public IClimateState addHumidity(float humidity){
		this.humidity += humidity;
		return ClimateStates.INSTANCE.checkState(this);
	}

	@Override
	public IClimateState add(IClimateState state){
		this.humidity += state.getHumidity();
		this.temperature += state.getTemperature();
		return ClimateStates.INSTANCE.checkState(this);
	}

	@Override
	public IClimateState scale(double factor) {
		this.humidity *= factor;
		this.temperature *= factor;
		return ClimateStates.INSTANCE.checkState(this);
	}

	@Override
	public IClimateState remove(IClimateState state){
		this.humidity -= state.getHumidity();
		this.temperature -= state.getTemperature();
		return ClimateStates.INSTANCE.checkState(this);
	}

	@Override
	public boolean isPresent() {
		return !Float.isNaN(temperature) && !Float.isNaN(humidity);
	}

	@Override
	public boolean isMutable() {
		return true;
	}

	@Override
	public float getTemperature() {
		return temperature;
	}

	@Override
	public float getHumidity() {
		return humidity;
	}

	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof IClimateState)){
			return false;
		}
		IClimateState otherState = (IClimateState) obj;
		return otherState.getTemperature() == temperature && otherState.getHumidity() == humidity;
	}

	@Override
	public int hashCode() {
		return Float.hashCode(temperature) * 31 + Float.hashCode(humidity);
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this).add("temperature", temperature).add("humidity", humidity).toString();
	}
}
