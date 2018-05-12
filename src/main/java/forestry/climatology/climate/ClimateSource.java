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
package forestry.climatology.climate;

import javax.annotation.Nullable;

import net.minecraft.nbt.NBTTagCompound;

import forestry.api.climate.ClimateType;
import forestry.api.climate.IClimateLogic;
import forestry.api.climate.IClimateState;
import forestry.api.climate.source.IClimateSource;
import forestry.api.climate.source.IClimateSourceProxy;
import forestry.core.climate.ClimateStateHelper;
import forestry.core.config.Config;

public abstract class ClimateSource<P extends IClimateSourceProxy> implements IClimateSource<P> {

	protected final P proxy;
	protected final float boundModifier;
	protected final ClimateSourceType sourceType;
	private IClimateState state;
	protected float change;
	protected ClimateSourceMode temperatureMode;
	protected ClimateSourceMode humidityMode;
	protected boolean isActive;

	public ClimateSource(P proxy, float change, float boundModifier, ClimateSourceType sourceType) {
		this.proxy = proxy;
		this.change = change;
		this.boundModifier = boundModifier;
		this.sourceType = sourceType;
		this.temperatureMode = ClimateSourceMode.NONE;
		this.humidityMode = ClimateSourceMode.NONE;
		this.state = ClimateStateHelper.ZERO_STATE;
	}

	@Override
	public P getProxy() {
		return proxy;
	}

	public void setHumidityMode(ClimateSourceMode humidityMode) {
		this.humidityMode = humidityMode;
	}

	public void setTemperatureMode(ClimateSourceMode temperatureMode) {
		this.temperatureMode = temperatureMode;
	}

	@Override
	public float getBoundaryModifier(ClimateType type, boolean boundaryUp) {
		if (type == ClimateType.HUMIDITY) {
			if (humidityMode == ClimateSourceMode.POSITIVE && boundaryUp || humidityMode == ClimateSourceMode.NEGATIVE && !boundaryUp) {
				return getBoundModifier(ClimateType.HUMIDITY);
			}
		} else {
			if (temperatureMode == ClimateSourceMode.POSITIVE && boundaryUp || temperatureMode == ClimateSourceMode.NEGATIVE && !boundaryUp) {
				return getBoundModifier(ClimateType.TEMPERATURE);
			}
		}
		return 0;
	}

	protected float getEnergyModifier(IClimateState currentState, @Nullable ClimateSourceType oppositeType) {
		float change;
		if (oppositeType != null) {
			if (oppositeType.canChangeTemperature()) {
				change = currentState.getTemperature();
			} else {
				change = currentState.getHumidity();
			}
		} else {
			change = 0.0F;
		}
		return (1.0F + change) * Config.habitatformerResourceModifier;
	}

	protected float getBoundModifier(ClimateType type) {
		return boundModifier;
	}

	protected float getChange(ClimateType type) {
		return change;
	}

	@Override
	public boolean isActive() {
		return isActive;
	}

	@Override
	public boolean affectsClimateType(ClimateType type) {
		return sourceType.affectClimateType(type);
	}

	@Override
	public final IClimateState work(IClimateLogic logic, IClimateState previousState, IClimateState targetState, IClimateState currentState) {
		IClimateState newState = ClimateStateHelper.INSTANCE.create(getState(), true);
		IClimateState newChange = ClimateStateHelper.ZERO_STATE;
		IClimateState defaultState = logic.getBiome();
		ClimateSourceType validType = getWorkType(currentState, targetState);
		ClimateSourceType oppositeType = getOppositeWorkType(currentState, defaultState);
		beforeWork();
		boolean work = canWork(newState, oppositeType, logic.getResourceModifier());
		//Test if the source can work and test if the owner has enough resources to work.
		if (!work && oppositeType != null) {
			isActive = false;
			isNotValid();
			if (ClimateStateHelper.isZero(newState)) {
				return newChange;
			} else if (ClimateStateHelper.isNearZero(newState)) {
				setState(newChange);
				return newChange;
			} else if (ClimateStateHelper.isNearTarget(currentState, targetState)) {
				return newChange;
			}
			//If the state is not already zero, remove one change state from the state.
			newChange = getChange(oppositeType, defaultState, currentState).scale(logic.getChangeModifier());
			newChange = ClimateStateHelper.INSTANCE.create(-newChange.getTemperature(), -newChange.getHumidity());
		} else if (validType != null) {
			newChange = getChange(validType, targetState, previousState).scale(logic.getChangeModifier());
			IClimateState changedState = newState.add(newChange);
			boolean couldWork = canWork(changedState, oppositeType, logic.getResourceModifier());
			//Test if the owner could work with the changed state. If he can remove the resources for the changed state, if not only remove the resources for the old state.
			removeResources(couldWork ? changedState : newState, oppositeType, logic.getResourceModifier());
			if (!couldWork) {
				newChange = ClimateStateHelper.ZERO_STATE;
			}
		} else if (oppositeType != null) {
			//Remove the resources if the owner has enough resources and the state is not the default state.
			removeResources(newState, oppositeType, logic.getResourceModifier());
		}
		newState = newState.add(newChange);
		if (ClimateStateHelper.isZero(newState) || ClimateStateHelper.isNearZero(newState)) {
			newState = ClimateStateHelper.ZERO_STATE;
		}
		setState(newState);
		return newChange;
	}

	protected void isNotValid() {

	}

	protected void beforeWork() {
	}

	/**
	 * @return true if the source can work, false if it can not.
	 */
	protected abstract boolean canWork(IClimateState currentState, @Nullable ClimateSourceType oppositeType, float resourceModifier);

	protected abstract void removeResources(IClimateState currentState, @Nullable ClimateSourceType oppositeType, float resourceModifier);

	protected abstract IClimateState getChange(ClimateSourceType type, IClimateState target, IClimateState currentState);

	@Nullable
	private ClimateSourceType getOppositeWorkType(IClimateState state, IClimateState target) {
		boolean canChangeHumidity = sourceType.canChangeHumidity() && canChange(state.getHumidity(), target.getHumidity(), humidityMode.getOpposite());
		boolean canChangeTemperature = sourceType.canChangeTemperature() && canChange(state.getTemperature(), target.getTemperature(), temperatureMode.getOpposite());
		if (canChangeHumidity) {
			return canChangeTemperature ? ClimateSourceType.BOTH : ClimateSourceType.HUMIDITY;
		} else {
			return canChangeTemperature ? ClimateSourceType.TEMPERATURE : null;
		}
	}

	@Nullable
	private ClimateSourceType getWorkType(IClimateState state, IClimateState target) {
		boolean canChangeHumidity = sourceType.canChangeHumidity() && canChange(state.getHumidity(), target.getHumidity(), humidityMode);
		boolean canChangeTemperature = sourceType.canChangeTemperature() && canChange(state.getTemperature(), target.getTemperature(), temperatureMode);
		if (canChangeHumidity) {
			return canChangeTemperature ? ClimateSourceType.BOTH : ClimateSourceType.HUMIDITY;
		} else {
			return canChangeTemperature ? ClimateSourceType.TEMPERATURE : null;
		}
	}

	private boolean canChange(float value, float target, ClimateSourceMode mode) {
		return mode == ClimateSourceMode.POSITIVE && value < target || mode == ClimateSourceMode.NEGATIVE && value > target;
	}

	@Override
	public IClimateState getState() {
		return state.copy();
	}

	protected void setState(IClimateState state) {
		this.state = state;
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		NBTTagCompound sourceData = new NBTTagCompound();
		state.writeToNBT(sourceData);
		nbt.setTag("Source", sourceData);
		return nbt;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		NBTTagCompound sourceData = nbt.getCompoundTag("Source");
		if (sourceData.hasNoTags()) {
			return;
		}
		state = ClimateStateHelper.INSTANCE.create(sourceData);
	}

}
