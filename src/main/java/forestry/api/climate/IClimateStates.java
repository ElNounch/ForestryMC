/*******************************************************************************
 * Copyright 2011-2014 SirSengir
 *
 * This work (the API) is licensed under the "MIT" License, see LICENSE.txt for details.
 ******************************************************************************/
package forestry.api.climate;

import net.minecraft.nbt.NBTTagCompound;

public interface IClimateStates {

	IClimateState create(NBTTagCompound compound);

	IClimateState create(NBTTagCompound compound, boolean mutable);

	IClimateState create(float temperature, float humidity);

	IClimateState create(float temperature, float humidity, boolean mutable);

	IClimateState create(IClimateState climateState);

	IClimateState create(IClimateState climateState, boolean mutable);

	/**
	 * Checks if the given state is valid and returns the absent state if the given state is not valid.
	 */
	IClimateState checkState(IClimateState climateState);

	IClimateState absent();

	IClimateState min();

	IClimateState max();

	IClimateState zero();

	IClimateState mutableZero();
}
