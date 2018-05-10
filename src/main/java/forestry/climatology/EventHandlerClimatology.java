package forestry.climatology;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.chunk.Chunk;

import net.minecraftforge.event.AttachCapabilitiesEvent;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import forestry.climatology.climate.ClimateHolder;
import forestry.core.config.Constants;

public class EventHandlerClimatology {

	private static final ResourceLocation CLIMATE_KEY = new ResourceLocation(Constants.MOD_ID, "climate_holder");

	@SubscribeEvent
	public void onGatherCapabilities(AttachCapabilitiesEvent<Chunk> event) {
		event.addCapability(CLIMATE_KEY, new ClimateHolder());
	}
}
