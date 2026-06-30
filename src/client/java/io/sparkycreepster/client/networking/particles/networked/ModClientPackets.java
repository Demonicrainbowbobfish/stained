package io.sparkycreepster.client.networking.particles.networked;

import io.sparkycreepster.custom.networking.packets.Blood1SpawnPacket;
import io.sparkycreepster.custom.networking.packets.Packets;
import io.sparkycreepster.custom.particles.CustomLodestoneParticles;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import team.lodestar.lodestone.systems.easing.Easing;
import team.lodestar.lodestone.systems.particle.builder.WorldParticleBuilder;
import team.lodestar.lodestone.systems.particle.data.color.ColorParticleData;
import team.lodestar.lodestone.systems.particle.render_types.LodestoneWorldParticleRenderType;

import java.awt.*;

public class ModClientPackets {

    // i'll use this later
    public static void spawnBloodSplash(World level, Vec3d pos, Color startingColor, Color endingColor) {
        Random random = level.random;
        // spawns 40 particles per call
        for (int i = 0; i < 40; i++) {
            // generate random motion values
            double motionX = (random.nextDouble() - 0.5) * 0.4;
            double motionY = random.nextDouble() * 0.4;
            double motionZ = (random.nextDouble() - 0.5) * 0.4;
            WorldParticleBuilder.create(CustomLodestoneParticles.BLOOD1)
                    .setLifetime(40)
                    .setColorData(ColorParticleData.create(startingColor, endingColor).setCoefficient(3f).setEasing(Easing.SINE_IN_OUT).build())
                    .setRenderType(LodestoneWorldParticleRenderType.ADDITIVE)
                    .addMotion(motionX, motionY, motionZ)

                    .setGravityStrength(1)
                    .spawn(level, pos.x, pos.y+0, pos.z);

        }

    }

    public static void registerClientPackets() {
        // Put packets here
        ClientPlayNetworking.registerGlobalReceiver(Packets.BLOOD1_SPAWN, ((client, handler, buf, responseSender) -> {
            Blood1SpawnPacket packet = new Blood1SpawnPacket(buf);
            client.execute(() -> {
                if (client.world == null) return;
                World level = client.world;
                Color startColor = new Color(packet.startingColor);
                Color endColor = new Color(packet.endingColor);
                spawnBloodSplash(client.world, packet.position, startColor, endColor);
            });
        }));
    }
}
