package io.sparkycreepster.client.networking.particles.networked;

import io.sparkycreepster.custom.networking.packets.Blood1SpawnPacket;
import io.sparkycreepster.custom.networking.packets.Packets;
import io.sparkycreepster.custom.particles.CustomLodestoneParticles;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import team.lodestar.lodestone.systems.easing.Easing;
import team.lodestar.lodestone.systems.particle.builder.WorldParticleBuilder;
import team.lodestar.lodestone.systems.particle.data.color.ColorParticleData;
import team.lodestar.lodestone.systems.particle.render_types.LodestoneWorldParticleRenderType;

import java.awt.*;

public class ModClientPackets {


    // i'll use this later
    public static void spawnBloodSplash(World level, Vec3d pos, Color startingColor, Color endingColor) {
        WorldParticleBuilder.create(CustomLodestoneParticles.BLOOD1)
                .setLifetime(160)
                .setColorData(ColorParticleData.create(startingColor, endingColor).setCoefficient(3f).setEasing(Easing.SINE_IN_OUT).build())
                .setRenderType(LodestoneWorldParticleRenderType.PARTICLE_SHEET_OPAQUE)
                .spawn(level, pos.x, pos.y+0, pos.z);
    }

    public static void registerClientPackets() {
        // Put packets here
        ClientPlayNetworking.registerGlobalReceiver(Packets.BLOOD1_SPAWN, ((client, handler, buf, responseSender) -> {
            Blood1SpawnPacket packet = new Blood1SpawnPacket(buf);
            client.execute(() -> {
                World level = client.world;
                if (level != null) {



                }
            });
        }));
    }
}
