package io.sparkycreepster.custom.abilities;

import io.sparkycreepster.custom.networking.packets.Blood1SpawnPacket;
import io.sparkycreepster.custom.networking.packets.Packets;
import io.sparkycreepster.custom.particles.Particles;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.awt.*;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;

public class Decent implements BloodAbility{
    private static final Set<UUID> SLAMMED = new HashSet<>();
    private static void spawnImpact(ServerWorld world, LivingEntity living) {
        Vec3d pos = living.getPos();

        world.spawnParticles(
                ParticleTypes.EXPLOSION_EMITTER,
                pos.x,
                pos.y,
                pos.z,
                1,
                0,
                0,
                0,
                0
        );
        BlockPos blockPos = living.getBlockPos().down();
        BlockState state = world.getBlockState(blockPos);
        world.spawnParticles(
                new BlockStateParticleEffect(
                        ParticleTypes.BLOCK,
                        state
                ),
                pos.x,
                pos.y,
                pos.z,
                40,
                0.5,
                0.1,
                0.5,
                0.2

        );
    }
    @Override
    public void use(PlayerEntity player, LivingEntity target) {
        SLAMMED.add(target.getUuid());

        System.out.println(
                "World class: " + player.getWorld().getClass().getName()
        );
        System.out.println(
                "isClient = " + player.getWorld().isClient()
        );
        if (target.isOnGround()) {
            return;
        }
        // still crashes
        // don't ask me why i have no idea

        Color startColor = new Color(150, 0, 0);
        Color endColor = new Color(90, 0, 0);
        Blood1SpawnPacket packet = new Blood1SpawnPacket(
                target.getPos(),
                startColor.getRGB(),
                endColor.getRGB()

        );
        PacketByteBuf buf = PacketByteBufs.create();
        packet.toBytes(buf);
        for (ServerPlayerEntity playerII : PlayerLookup.tracking(target)) {
            ServerPlayNetworking.send(playerII, Packets.BLOOD1_SPAWN, buf);
            ServerPlayNetworking.send(playerII, Packets.TINTED_EXPLOSION, buf);
        }


        target.addVelocity(0, -3, 0);
        target.velocityModified = true;
        ServerTickEvents.END_WORLD_TICK.register(world -> {
            Iterator<UUID> iterator = SLAMMED.iterator();
            while (iterator.hasNext()) {
                UUID id = iterator.next();

                Entity entity = world.getEntity(id);

                if (!(entity instanceof LivingEntity living)) {
                    iterator.remove();
                    continue;
                }
                if (living.isOnGround()) {
                    iterator.remove();

                    spawnImpact((ServerWorld) target.getWorld(), target);
                }
            }
        });
    }
    @Override
    public String getName() {
        return "Decent";
    }

    @Override
    public int getCooldownTicks() {
        return 400;
    }
}
