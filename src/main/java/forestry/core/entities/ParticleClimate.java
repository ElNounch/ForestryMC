package forestry.core.entities;

import net.minecraft.client.particle.ParticleRedstone;
import net.minecraft.world.World;

import forestry.api.core.EnumTemperature;
import forestry.core.utils.ColourUtil;

public class ParticleClimate extends ParticleRedstone {
	public ParticleClimate(World world, double x, double y, double z, EnumTemperature temperature) {
		super(world, x, y, z, 0.0F, 0.0F, 0.0F);
		particleRed = ColourUtil.getRedAsFloat(temperature.color);
		particleGreen = ColourUtil.getGreenAsFloat(temperature.color);
		particleBlue = ColourUtil.getBlueAsFloat(temperature.color);
	}
}
