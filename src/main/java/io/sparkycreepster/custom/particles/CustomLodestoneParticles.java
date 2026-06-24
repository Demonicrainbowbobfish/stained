package io.sparkycreepster.custom.particles;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import team.lodestar.lodestone.systems.particle.world.type.LodestoneWorldParticleType;

import static io.sparkycreepster.Stained.MOD_ID;

public class CustomLodestoneParticles {

    public static final LodestoneWorldParticleType BLOOD1 = register("blood1",
            new LodestoneWorldParticleType());



    private static LodestoneWorldParticleType register(String name, LodestoneWorldParticleType particle) {
        return Registry.register(Registries.PARTICLE_TYPE,
                new Identifier(MOD_ID, name), particle);
    }
    public static void registerModParticles() {

        // Just calling this will load the static fields
    }
}
