package forestry.climatology.api.climate;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import forestry.api.climate.IClimateState;
import forestry.api.core.ILocatable;

public interface IClimateListener extends ILocatable {
	/*void onClimateChange(IClimateLogic logic, IClimateState oldState, IClimateState newState);

	void onDetach(IClimateLogic logic);

	void onAttach(IClimateLogic logic);*/

	IClimateState getState();

	@SideOnly(Side.CLIENT)
	void updateClient();

	@SideOnly(Side.CLIENT)
	void setClientState(IClimateState clientState);

	@SideOnly(Side.CLIENT)
	IClimateState getClientState();
}
