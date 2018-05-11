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

import net.minecraft.nbt.NBTTagCompound;

import forestry.api.climate.IClimateState;
import forestry.api.climate.IClimateStateHelper;

public final class ClimateStateHelper implements IClimateStateHelper {

	public static final ClimateStateHelper INSTANCE = new ClimateStateHelper();
	public static final IClimateState ZERO_STATE = ImmutableClimateState.MIN;
	private static final float CLIMATE_CHANGE = 0.01F;

	private ClimateStateHelper() {
	}

	public static IClimateState of(float temperature, float humidity, boolean mutable) {
		return INSTANCE.create(temperature, humidity, mutable);
	}

	public static IClimateState of(float temperature, float humidity) {
		return INSTANCE.create(temperature, humidity, false);
	}

	public static IClimateState mutableOf(float temperature, float humidity) {
		return INSTANCE.create(temperature, humidity, true);
	}

	public static boolean isNearTarget(IClimateState state, IClimateState target) {
		return target.getHumidity() - CLIMATE_CHANGE < state.getHumidity()
			&& target.getHumidity() + CLIMATE_CHANGE > state.getHumidity()
			&& target.getTemperature() - CLIMATE_CHANGE < state.getTemperature()
			&& target.getTemperature() + CLIMATE_CHANGE > state.getTemperature();
	}

	public static boolean isZero(IClimateState state) {
		return state.getHumidity() == ZERO_STATE.getHumidity() && state.getTemperature() == ZERO_STATE.getTemperature();
	}

	public static boolean isNearZero(IClimateState state) {
		return isNearTarget(state, ZERO_STATE);
	}

	@Override
	public IClimateState create(float temperature, float humidity) {
		return create(temperature, humidity, false);
	}

	@Override
	public IClimateState create(IClimateState climateState) {
		return create(climateState, false);
	}

	@Override
	public IClimateState create(IClimateState climateState, boolean mutable) {
		IClimateState state;
		if (mutable) {
			state = new MutableClimateState(climateState);
		} else {
			state = new ImmutableClimateState(climateState);
		}
		return checkState(state);
	}

	@Override
	public IClimateState create(float temperature, float humidity, boolean mutable) {
		IClimateState state;
		if (mutable) {
			state = new MutableClimateState(temperature, humidity);
		} else {
			state = new ImmutableClimateState(temperature, humidity);
		}
		return checkState(state);
	}

	@Override
	public IClimateState create(NBTTagCompound compound, boolean mutable) {
		if (compound.getBoolean(ImmutableClimateState.ABSENT_NBT_KEY)) {
			return AbsentClimateState.INSTANCE;
		}
		IClimateState state;
		if (mutable) {
			state = new MutableClimateState(compound);
		} else {
			state = new ImmutableClimateState(compound);
		}
		return checkState(state);
	}

	@Override
	public IClimateState create(NBTTagCompound compound) {
		if (compound.getBoolean(ImmutableClimateState.ABSENT_NBT_KEY)) {
			return AbsentClimateState.INSTANCE;
		}
		return create(compound, compound.getBoolean(ImmutableClimateState.MUTABLE_NBT_KEY));
	}

	@Override
	public IClimateState checkState(IClimateState climateState) {
		return !climateState.isPresent() ? absent() : climateState;
	}

	@Override
	public IClimateState absent() {
		return AbsentClimateState.INSTANCE;
	}

	@Override
	public IClimateState min() {
		return ImmutableClimateState.MIN;
	}

	@Override
	public IClimateState max() {
		return ImmutableClimateState.MAX;
	}

	@Override
	public IClimateState zero() {
		return ZERO_STATE;
	}

	@Override
	public IClimateState mutableZero() {
		return create(0.0F, 0.0F, true);
	}
}
