package io.sparkycreepster.client.networking.particles.render.flipside;

import io.sparkycreepster.Stained;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import org.joml.Matrix4f;
import org.joml.Vector4f;

public class FlipsideParticleRenderer {

    private static final int TRANSITION_TICKS = 20;

    private static final int PARTICLES_PER_TICK = 100;

    private static final double POSITION_JITTER = 0.1D;

    private static final double PARTICLE_VELOCITY = 0.025D;


    private static boolean transitioning = false;

    private static boolean targetVanishState = false;

    private static boolean appearing = false;

    private static AbstractClientPlayerEntity player;

    private static int transitionTicks = 0;


    public static void startTransition(
            AbstractClientPlayerEntity targetPlayer,
            boolean targetState
    ) {

        if (transitioning) {
            return;
        }

        if (targetPlayer == null) {
            return;
        }

        player = targetPlayer;

        targetVanishState = targetState;

        appearing = !targetState;

        transitionTicks = 0;

        transitioning = true;
    }


    public static void tick() {

        if (!transitioning) {
            return;
        }

        MinecraftClient client =
                MinecraftClient.getInstance();

        if (client.world == null || player == null) {
            finishTransition();
            return;
        }

        if (player.getWorld() != client.world) {
            finishTransition();
            return;
        }

        transitionTicks++;

        spawnPlayerModelParticles(client, player);

        if (transitionTicks >= TRANSITION_TICKS) {
            finishTransition();
        }
    }


    private static void finishTransition() {

        Stained.vanishEnabled = targetVanishState;

        transitioning = false;

        transitionTicks = 0;

        player = null;
    }


    private static void spawnPlayerModelParticles(
            MinecraftClient client,
            AbstractClientPlayerEntity player
    ) {

        if (client.world == null) {
            return;
        }

        EntityRenderer<? super AbstractClientPlayerEntity> renderer =
                client.getEntityRenderDispatcher().getRenderer(player);

        if (!(renderer instanceof PlayerEntityRenderer playerRenderer)) {
            return;
        }

        PlayerEntityModel<AbstractClientPlayerEntity> model =
                playerRenderer.getModel();

        double progress =
                (double) transitionTicks / TRANSITION_TICKS;

        double intensity =
                Math.sin(progress * Math.PI);

        intensity =
                0.35D + intensity * 0.65D;

        int particleCount =
                Math.max(
                        1,
                        (int) (PARTICLES_PER_TICK * intensity)
                );

        Random random = client.world.random;

        ModelPart[] parts = {
                model.head,
                model.body,
                model.leftArm,
                model.rightArm,
                model.leftLeg,
                model.rightLeg
        };

        for (int i = 0; i < particleCount; i++) {

            ModelPart part =
                    parts[random.nextInt(parts.length)];

            spawnParticleFromModelPart(
                    client,
                    player,
                    part,
                    random
            );
        }
    }


    private static void spawnParticleFromModelPart(
            MinecraftClient client,
            AbstractClientPlayerEntity player,
            ModelPart part,
            Random random
    ) {

        MatrixStack matrices = new MatrixStack();

        final boolean[] spawned = {false};

        part.forEachCuboid(
                matrices,
                (entry, path, index, cuboid) -> {

                    if (spawned[0]) {
                        return;
                    }

                    spawned[0] = true;

                    float localX =
                            MathHelper.nextFloat(
                                    random,
                                    cuboid.minX,
                                    cuboid.maxX
                            );

                    float localY =
                            MathHelper.nextFloat(
                                    random,
                                    cuboid.minY,
                                    cuboid.maxY
                            );

                    float localZ =
                            MathHelper.nextFloat(
                                    random,
                                    cuboid.minZ,
                                    cuboid.maxZ
                            );

                    Matrix4f matrix =
                            entry.getPositionMatrix();

                    Vector4f position =
                            new Vector4f(
                                    localX,
                                    localY,
                                    localZ,
                                    1.0F
                            );

                    position.mul(matrix);

                    double localWorldX =
                            position.x() / 16.0D;

                    double localWorldY =
                            position.y() / 16.0D;

                    double localWorldZ =
                            position.z() / 16.0D;

                    double worldX =
                            player.getX() + localWorldX;

                    double worldY =
                            player.getY() + 1.5D - localWorldY;

                    double worldZ =
                            player.getZ() + localWorldZ;

                    worldX +=
                            (random.nextDouble() - 0.5D)
                                    * POSITION_JITTER;

                    worldY +=
                            (random.nextDouble() - 0.5D)
                                    * POSITION_JITTER;

                    worldZ +=
                            (random.nextDouble() - 0.5D)
                                    * POSITION_JITTER;


                    double centerX =
                            player.getX();

                    double centerY =
                            player.getY() + 0.9D;

                    double centerZ =
                            player.getZ();

                    double directionX =
                            worldX - centerX;

                    double directionY =
                            worldY - centerY;

                    double directionZ =
                            worldZ - centerZ;

                    double length =
                            Math.sqrt(
                                    directionX * directionX +
                                            directionY * directionY +
                                            directionZ * directionZ
                            );

                    if (length > 0.001D) {

                        directionX /= length;
                        directionY /= length;
                        directionZ /= length;

                    } else {

                        directionX = 0.0D;
                        directionY = 1.0D;
                        directionZ = 0.0D;
                    }


                    double velocityX;
                    double velocityY;
                    double velocityZ;

                    if (appearing) {

                        velocityX =
                                -directionX * PARTICLE_VELOCITY;

                        velocityY =
                                -directionY * PARTICLE_VELOCITY;

                        velocityZ =
                                -directionZ * PARTICLE_VELOCITY;

                    } else {

                        velocityX =
                                directionX * PARTICLE_VELOCITY;

                        velocityY =
                                directionY * PARTICLE_VELOCITY;

                        velocityZ =
                                directionZ * PARTICLE_VELOCITY;
                    }


                    double randomVelocity =
                            PARTICLE_VELOCITY * 0.35D;

                    velocityX +=
                            (random.nextDouble() - 0.5D)
                                    * randomVelocity;

                    velocityY +=
                            (random.nextDouble() - 0.5D)
                                    * randomVelocity;

                    velocityZ +=
                            (random.nextDouble() - 0.5D)
                                    * randomVelocity;


                    client.world.addParticle(
                            ParticleTypes.SOUL,
                            worldX,
                            worldY,
                            worldZ,
                            velocityX,
                            velocityY,
                            velocityZ
                    );
                }
        );
    }


    public static boolean isTransitioning() {
        return transitioning;
    }

    public static boolean isTransitioning(
            AbstractClientPlayerEntity targetPlayer
    ) {
        return transitioning && player == targetPlayer;
    }
}