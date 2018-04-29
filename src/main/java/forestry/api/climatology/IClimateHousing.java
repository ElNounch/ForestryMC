/*******************************************************************************
 * Copyright 2011-2014 SirSengir
 *
 * This work (the API) is licensed under the "MIT" License, see LICENSE.txt for details.
 ******************************************************************************/
package forestry.api.climatology;

import forestry.api.climate.IClimateState;
import forestry.api.core.ILocatable;

public interface IClimateHousing extends ILocatable {
	
	void onUpdateClimate();

	IClimateState getTarget();

	IClimateState getCurrent();

	IClimateState getBiome();

	boolean isTileInvalid();
}
