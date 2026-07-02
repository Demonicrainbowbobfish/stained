package io.sparkycreepster.custom.abilities;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.*;

public class SlamManager {

    private static final Set<UUID> SLAMMED = new HashSet<>();

    public static void init() {
        Map<UUID, Double> lastY = new HashMap<>();

        System.out.println("SlamManager initialized");
        ServerTickEvents.END_WORLD_TICK.register(world -> {

            Iterator<UUID> iterator = SLAMMED.iterator();

            while (iterator.hasNext()) {

                UUID id = iterator.next();

                Entity entity = world.getEntity(id);

                if (entity == null) {
                    System.out.println("Entity not found in world: " + world.getRegistryKey().getValue());
                    continue;
                }

                System.out.println("Found entity in " + world.getRegistryKey().getValue());

                LivingEntity living = (LivingEntity) entity;
                BlockPos below = living.getBlockPos().down();

                boolean solidGround =
                        !world.getBlockState(below).isAir();
                double oldY = lastY.getOrDefault(id, living.getY());
                if (oldY > living.getY()) {
                    // still falling
                }
                lastY.put(id, living.getY());
                System.out.println(
                        "Y=" + living.getY() +
                                " velY=" + living.getVelocity().y +
                                " onGround=" + living.isOnGround()
                );
                if (living.isOnGround()) {
                    lastY.remove(id);
                    iterator.remove();
                    spawnImpact(world, living);
                }
            }

        });
    }

    public static void watch(LivingEntity entity) {
        SLAMMED.add(entity.getUuid());
    }

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
}