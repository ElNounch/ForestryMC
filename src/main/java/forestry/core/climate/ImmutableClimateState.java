/*******************************************************************************
 * Copyright (c) 2011-2014 SirSengir.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v3
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl-3.0.txt
 *
 * Various Contributors including, but not limited to:
 * SirSengir (original work), CovertJaguar, Player, Binnie, MysteriousAges
 ******************************************************************************/
package forestry.core.climate;

import com.google.common.base.MoreObjects;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.MathHelper;

import forestry.api.climate.IClimateState;

class ImmutableClimateState implements IClimateState {

	// The minimum climate state.
	static final ImmutableClimateState MIN = new ImmutableClimateState(0.0F, 0.0F);
	// The maximum climate state.
	static final ImmutableClimateState MAX = new ImmutableClimateState(2.0F, 2.0F);

	public static final String TEMPERATURE_NBT_KEY = "TEMP";
	public static final String HUMIDITY_NBT_KEY = "HUMID";
	public static final String ABSENT_NBT_KEY = "ABSENT";
	public static final String MUTABLE_NBT_KEY = "MUTABLE";

	protected final float temperature;
	protected final float humidity;

	ImmutableClimateState(IClimateState climateState) {
		this(climateState.getTemperature(), climateState.getHumidity());
	}

	ImmutableClimateState(float temperature, float humidity) {
		this.temperature = MathHelper.clamp(temperature, 0.0F, 2.0F);
		this.humidity = MathHelper.clamp(humidity, 0.0F, 2.0F);
	}

	ImmutableClimateState(NBTTagCompound compound) {
		this.temperature = MathHelper.clamp(compound.getFloat(TEMPERATURE_NBT_KEY), 0.0F, 2.0F);
		this.humidity = MathHelper.clamp(compound.getFloat(HUMIDITY_NBT_KEY), 0.0F, 2.0F);
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
		return ClimateStateHelper.INSTANCE.create(this, mutable);
	}

	@Override
	public IClimateState copy() {
		return copy(false);
	}

	@Override
	public IClimateState toMutable() {
		return copy(true);
	}

	@Override
	public IClimateState toImmutable() {
		return this;
	}

	@Override
	public IClimateState addTemperature(float temperature) {
		return ClimateStateHelper.of(this.temperature + temperature, humidity);
	}

	@Override
	public IClimateState addHumidity(float humidity) {
		return ClimateStateHelper.of(temperature, this.humidity + humidity);
	}

	@Override
	public IClimateState add(IClimateState state) {
		return ClimateStateHelper.of(this.temperature + state.getTemperature(), this.humidity + state.getHumidity());
	}

	@Override
	public IClimateState scale(double factor) {
		return ClimateStateHelper.of((float) (this.temperature * factor), (float) (this.humidity * factor));
	}

	@Override
	public IClimateState remove(IClimateState state) {
		return ClimateStateHelper.of(this.temperature - state.getTemperature(), this.humidity - state.getHumidity());
	}

	@Override
	public boolean isPresent() {
		return !Float.isNaN(temperature) && !Float.isNaN(humidity);
	}

	@Override
	public boolean isMutable() {
		return false;
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
		if (!(obj instanceof IClimateState)) {
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