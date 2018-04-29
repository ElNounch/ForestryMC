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
package forestry.climatology.network;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import forestry.climatology.network.packets.PacketSelectClimateTargeted;
import forestry.climatology.network.packets.PacketUpdateListener;
import forestry.climatology.network.packets.PacketUpdateListenerEntity;
import forestry.core.network.IPacketRegistry;
import forestry.core.network.PacketIdClient;
import forestry.core.network.PacketIdServer;

public class PacketRegistryClimatology implements IPacketRegistry {
	@Override
	public void registerPacketsServer() {
		PacketIdServer.SELECT_CLIMATE_TARGETED.setPacketHandler(new PacketSelectClimateTargeted.Handler());
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerPacketsClient() {
		PacketIdClient.UPDATE_LISTENER.setPacketHandler(new PacketUpdateListener.Handler());
		PacketIdClient.UPDATE_LISTENER_ENTITY.setPacketHandler(new PacketUpdateListenerEntity.Handler());
	}
}
