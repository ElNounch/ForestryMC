/*******************************************************************************
 * Copyright 2011-2014 SirSengir
 *
 * This work (the API) is licensed under the "MIT" License, see LICENSE.txt for details.
 ******************************************************************************/
package forestry.api.climatology;

import java.util.Collection;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import forestry.api.climate.IClimateState;
import forestry.api.climatology.source.IClimateSource;
import forestry.api.core.INbtReadable;
import forestry.api.core.INbtWritable;

public interface IClimateLogic extends INbtReadable, INbtWritable, IClimateTransformer {
	/**
	 * @return The parent of this container.
	 */
	IClimateHousing getHousing();

	/**
	 * Update the climate in a region.
	 */
	void update();

	/**
	 * Invalidates this logic and removes it from the surrounding chunks.
	 */
	void invalidate();

	/**
	 * Sets the targeted state of this logic.
	 */
	void setTarget(IClimateState target);

	void onAddSource(IClimateSource source);

	void onRemoveSource(IClimateSource source);

	/**
	 * @return All climate sources of this container.
	 */
	Collection<IClimateSource> getClimateSources();

	default World getWorldObj() {
		return getHousing().getWorldObj();
	}

	@Override
	default BlockPos getCoordinates() {
		return getHousing().getCoordinates();
	}
}
