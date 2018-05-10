package forestry.api.climatology;

import net.minecraft.entity.player.EntityPlayerMP;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import forestry.api.climate.IClimateState;
import forestry.api.core.ILocatable;

public interface IClimateListener extends ILocatable {
	default IClimateState getState() {
		return getState(true);
	}

	default IClimateState getState(boolean update) {
		return getState(update, true);
	}

	IClimateState getState(boolean update, boolean syncToClient);

	/* CLIENT */
	@SideOnly(Side.CLIENT)
	void updateClientSide();

	@SideOnly(Side.CLIENT)
	void setClientState(IClimateState clientState);

	@SideOnly(Side.CLIENT)
	IClimateState getClientState();

	void syncToClient();

	void syncToClient(EntityPlayerMP player);
}
