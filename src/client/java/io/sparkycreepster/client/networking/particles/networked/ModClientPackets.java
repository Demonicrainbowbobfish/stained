package io.sparkycreepster.client.networking.particles.networked;

import io.sparkycreepster.custom.particles.Particles;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import team.lodestar.lodestone.systems.easing.Easing;
import team.lodestar.lodestone.systems.particle.builder.WorldParticleBuilder;
import team.lodestar.lodestone.systems.particle.data.color.ColorParticleData;
import team.lodestar.lodestone.systems.particle.render_types.LodestoneWorldParticleRenderType;

import java.awt.*;

public class ModClientPackets {
    public static void registerClientPackets() {
        // Put packets here
    }

    // Actual particles dumbitch
    // i'll use this later
    public static void spawnBloodSplash(World level, Vec3d pos, Color startingColor, Color endingColor) {
        WorldParticleBuilder.create(Particles.BLOOD1)
                .setLifetime(160)
                .setColorData(ColorParticleData.create(startingColor, endingColor).setCoefficient(3f).setEasing(Easing.SINE_IN_OUT).build())
                .setRenderType(LodestoneWorldParticleRenderType.LUMITRANSPARENT)
                .spawn(level, pos.x, pos.y+0, pos.z);
    }
}
