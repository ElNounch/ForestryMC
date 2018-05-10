package forestry.climatology.network.packets;

import java.io.IOException;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;

import forestry.api.climatology.ClimateCapabilities;
import forestry.api.climatology.IClimateListener;
import forestry.core.network.ForestryPacket;
import forestry.core.network.IForestryPacketHandlerServer;
import forestry.core.network.IForestryPacketServer;
import forestry.core.network.PacketBufferForestry;
import forestry.core.network.PacketIdServer;

public class PacketUpdateListenerEntityRequest extends ForestryPacket implements IForestryPacketServer {
	private final Entity entity;

	public PacketUpdateListenerEntityRequest(Entity entity) {
		this.entity = entity;
	}

	@Override
	protected void writeData(PacketBufferForestry data) throws IOException {
		data.writeEntityById(entity);
	}

	@Override
	public PacketIdServer getPacketId() {
		return PacketIdServer.UPDATE_LISTENER_REQUEST_ENTITY;
	}

	public static class Handler implements IForestryPacketHandlerServer {

		@Override
		public void onPacketData(PacketBufferForestry data, EntityPlayerMP player) throws IOException {
			Entity entity = data.readEntityById(player.world);
			if (entity != null && entity.hasCapability(ClimateCapabilities.CLIMATE_LISTENER, null)) {
				IClimateListener listener = entity.getCapability(ClimateCapabilities.CLIMATE_LISTENER, null);
				if (listener != null) {
					listener.syncToClient(player);
				}
			}
		}
	}
}
