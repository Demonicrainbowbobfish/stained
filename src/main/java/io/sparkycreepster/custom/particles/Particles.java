package io.sparkycreepster.custom.particles;

import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import static io.sparkycreepster.Stained.MOD_ID;

public class Particles {
    public static final DefaultParticleType BLOODEXP1 = Registry.register(Registries.PARTICLE_TYPE, new Identifier(MOD_ID, "blood"), FabricParticleTypes.simple());

    public static void registerParticles() {

    }
}
