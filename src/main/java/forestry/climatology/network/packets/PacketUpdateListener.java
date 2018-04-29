package forestry.climatology.network.packets;

import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import forestry.api.climate.IClimateState;
import forestry.climatology.api.climate.ClimatologyCapabilities;
import forestry.climatology.api.climate.IClimateListener;
import forestry.core.network.ForestryPacket;
import forestry.core.network.IForestryPacketClient;
import forestry.core.network.IForestryPacketHandlerClient;
import forestry.core.network.PacketBufferForestry;
import forestry.core.network.PacketIdClient;

public class PacketUpdateListener extends ForestryPacket implements IForestryPacketClient{
	private final BlockPos pos;
	private final IClimateState state;

	public PacketUpdateListener(BlockPos pos, IClimateState state) {
		this.pos = pos;
		this.state = state;
	}

	@Override
	protected void writeData(PacketBufferForestry data) throws IOException {
		data.writeBlockPos(pos);
		data.writeClimateState(state);
	}

	@Override
	public PacketIdClient getPacketId() {
		return PacketIdClient.UPDATE_LISTENER;
	}

	@SideOnly(Side.CLIENT)
	public static class Handler implements IForestryPacketHandlerClient {
		@Override
		public void onPacketData(PacketBufferForestry data, EntityPlayer player) throws IOException {
			BlockPos pos = data.readBlockPos();
			IClimateState state = data.readClimateState();
			TileEntity tileEntity = player.world.getTileEntity(pos);
			if(tileEntity != null && tileEntity.hasCapability(ClimatologyCapabilities.CLIMATE_LISTENER, null)){
				IClimateListener listener = tileEntity.getCapability(ClimatologyCapabilities.CLIMATE_LISTENER, null);
				if(listener != null) {
					listener.setClientState(state);
				}
			}
		}
	}
}
