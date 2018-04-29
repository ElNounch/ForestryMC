package forestry.core.entities;

import net.minecraft.client.particle.ParticleRedstone;
import net.minecraft.world.World;

import forestry.api.climate.ClimateType;
import forestry.api.climate.IClimateState;

public class ParticleClimate extends ParticleRedstone {
	public ParticleClimate(World world, double x, double y, double z, ClimateType type, IClimateState state) {
		super(world, x, y, z, 0.0F, 0.0F, 0.0F);
		particleRed = 0.4F + world.rand.nextFloat() * 0.2F;
		particleGreen = 0.4F + world.rand.nextFloat() * 0.2F;
		particleBlue = 0.9F + world.rand.nextFloat() * 0.1F;
	}
}
