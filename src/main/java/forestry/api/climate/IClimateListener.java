package forestry.api.climate;

import net.minecraft.entity.player.EntityPlayerMP;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import forestry.api.core.ILocatable;
import forestry.core.tiles.IClimatised;

public interface IClimateListener extends ILocatable, IClimateProvider, IClimatised {
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
	void setClimateState(IClimateState climateState);

	void syncToClient();

	void syncToClient(EntityPlayerMP player);
}
