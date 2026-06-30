package io.sparkycreepster.client.networking.particles.networked;

import io.sparkycreepster.custom.networking.packets.Blood1SpawnPacket;
import io.sparkycreepster.custom.networking.packets.Packets;
import io.sparkycreepster.custom.networking.packets.TintedExplosion;
import io.sparkycreepster.custom.particles.CustomLodestoneParticles;
import io.sparkycreepster.custom.particles.Particles;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.particle.Particle;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import team.lodestar.lodestone.systems.easing.Easing;
import team.lodestar.lodestone.systems.particle.builder.WorldParticleBuilder;
import team.lodestar.lodestone.systems.particle.data.GenericParticleData;
import team.lodestar.lodestone.systems.particle.data.color.ColorParticleData;
import team.lodestar.lodestone.systems.particle.render_types.LodestoneWorldParticleRenderType;
import team.lodestar.lodestone.systems.particle.world.options.WorldParticleOptions;

import java.awt.*;

public class ModClientPackets {

    public static void spawnTintedExplosion(World level, Vec3d pos, Color startingColor, Color endingColor) {
        WorldParticleBuilder.create(CustomLodestoneParticles.SHOCKWAVE)
                .setScaleData(GenericParticleData.create(0.1f, 7.0f).build())
                .setTransparencyData(GenericParticleData.create(1.0f, 1.0f, 0f).build())
                .setColorData(ColorParticleData.create(startingColor, endingColor).setCoefficient(3f).setEasing(Easing.SINE_IN_OUT).build())
                .setLifetime(5)
                .setRenderType(LodestoneWorldParticleRenderType.TRANSPARENT)
                .addMotion(0, 0, 0)
                .enableNoClip()
                .spawn(level, pos.x, pos.y+1, pos.z);

    }
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
        ClientPlayNetworking.registerGlobalReceiver(Packets.TINTED_EXPLOSION, ((client, handler, buf, responseSender) -> {
            TintedExplosion packet = new TintedExplosion(buf);
            client.execute(() -> {
                if (client.world == null) return;
                World level = client.world;
                Color startColor = new Color(packet.startingColor);
                Color endColor = new Color(packet.endingColor);
                spawnTintedExplosion(client.world, packet.position, startColor, endColor);
            });
        }));
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
