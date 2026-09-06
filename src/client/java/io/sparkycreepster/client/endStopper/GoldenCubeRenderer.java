package io.sparkycreepster.client.endStopper;

import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GoldenCubeRenderer {

    // =========================
    // TUNING
    // =========================

    private static double halfSize = 0.5;

    private static final float LAYER_THICKNESS = 0.04f;

    private static final float[][] LAYERS = {
            {0.00f, 1.00f},
            {0.04f, 0.75f},
            {0.08f, 0.45f},
            {0.12f, 0.20f},
            {0.16f, 0.08f}
    };

    private static final int FADE_IN_TICKS = 40;
    private static final int HOLD_TICKS = 60;
    private static final int FADE_OUT_TICKS = 40;

    private static final int TOTAL_TICKS =
            FADE_IN_TICKS + HOLD_TICKS + FADE_OUT_TICKS;

    // =========================
    // EFFECT STATE
    // =========================

    private static final List<Effect> ACTIVE_EFFECTS = new ArrayList<>();

    /**
     * Starts a new cube effect.
     *
     * Multiple effects can now exist at the same time.
     */
    public static void start(BlockPos position) {
        ACTIVE_EFFECTS.add(
                new Effect(position.toImmutable())
        );
    }

    /**
     * Advances every active animation by one client tick.
     */
    public static void tick() {

        Iterator<Effect> iterator = ACTIVE_EFFECTS.iterator();

        while (iterator.hasNext()) {

            Effect effect = iterator.next();

            effect.ticks++;

            if (effect.ticks >= TOTAL_TICKS) {
                iterator.remove();
            }
        }
    }

    /**
     * Returns true if at least one cube is currently active.
     */
    public static boolean isActive() {
        return !ACTIVE_EFFECTS.isEmpty();
    }

    /**
     * Renders every active cube.
     */
    public static void render(WorldRenderContext context) {

        if (ACTIVE_EFFECTS.isEmpty()) {
            return;
        }

        MatrixStack matrices = context.matrixStack();
        Camera camera = context.camera();

        Vec3d cameraPos = camera.getPos();

        VertexConsumerProvider.Immediate consumers =
                VertexConsumerProvider.immediate(
                        Tessellator.getInstance().getBuffer()
                );

        VertexConsumer vertexConsumer =
                consumers.getBuffer(RenderLayer.getLines());

        // Render every active effect.
        for (Effect effect : ACTIVE_EFFECTS) {

            BlockPos effectPosition = effect.position;

            double cx = effectPosition.getX() + 0.5;
            double cy = effectPosition.getY() + 0.5;
            double cz = effectPosition.getZ() + 0.5;

            double minX = cx - halfSize - cameraPos.x;
            double minY = cy - halfSize - cameraPos.y;
            double minZ = cz - halfSize - cameraPos.z;

            double maxX = cx + halfSize - cameraPos.x;
            double maxY = cy + halfSize - cameraPos.y;
            double maxZ = cz + halfSize - cameraPos.z;

            float fade = getFade(effect.ticks);

            // =========================
            // X EDGES
            // =========================

            for (float[] layer : LAYERS) {

                float offset = layer[0];
                float layerAlpha = layer[1] * fade;

                double thickness = LAYER_THICKNESS;

                // Bottom-front
                drawBox(
                        matrices,
                        vertexConsumer,
                        minX,
                        minY - offset - thickness,
                        minZ - offset - thickness,
                        maxX,
                        minY - offset,
                        minZ - offset,
                        layerAlpha
                );

                // Top-front
                drawBox(
                        matrices,
                        vertexConsumer,
                        minX,
                        maxY + offset,
                        minZ - offset - thickness,
                        maxX,
                        maxY + offset + thickness,
                        minZ - offset,
                        layerAlpha
                );

                // Bottom-back
                drawBox(
                        matrices,
                        vertexConsumer,
                        minX,
                        minY - offset - thickness,
                        maxZ + offset,
                        maxX,
                        minY - offset,
                        maxZ + offset + thickness,
                        layerAlpha
                );

                // Top-back
                drawBox(
                        matrices,
                        vertexConsumer,
                        minX,
                        maxY + offset,
                        maxZ + offset,
                        maxX,
                        maxY + offset + thickness,
                        maxZ + offset + thickness,
                        layerAlpha
                );
            }

            // =========================
            // Y EDGES
            // =========================

            for (float[] layer : LAYERS) {

                float offset = layer[0];
                float layerAlpha = layer[1] * fade;

                double thickness = LAYER_THICKNESS;

                // Front-left
                drawBox(
                        matrices,
                        vertexConsumer,
                        minX - offset - thickness,
                        minY,
                        minZ - offset - thickness,
                        minX - offset,
                        maxY,
                        minZ - offset,
                        layerAlpha
                );

                // Front-right
                drawBox(
                        matrices,
                        vertexConsumer,
                        maxX + offset,
                        minY,
                        minZ - offset - thickness,
                        maxX + offset + thickness,
                        maxY,
                        minZ - offset,
                        layerAlpha
                );

                // Back-left
                drawBox(
                        matrices,
                        vertexConsumer,
                        minX - offset - thickness,
                        minY,
                        maxZ + offset,
                        minX - offset,
                        maxY,
                        maxZ + offset + thickness,
                        layerAlpha
                );

                // Back-right
                drawBox(
                        matrices,
                        vertexConsumer,
                        maxX + offset,
                        minY,
                        maxZ + offset,
                        maxX + offset + thickness,
                        maxY,
                        maxZ + offset + thickness,
                        layerAlpha
                );
            }

            // =========================
            // Z EDGES
            // =========================

            for (float[] layer : LAYERS) {

                float offset = layer[0];
                float layerAlpha = layer[1] * fade;

                double thickness = LAYER_THICKNESS;

                // Bottom-left
                drawBox(
                        matrices,
                        vertexConsumer,
                        minX - offset - thickness,
                        minY - offset - thickness,
                        minZ,
                        minX - offset,
                        minY - offset,
                        maxZ,
                        layerAlpha
                );

                // Bottom-right
                drawBox(
                        matrices,
                        vertexConsumer,
                        maxX + offset,
                        minY - offset - thickness,
                        minZ,
                        maxX + offset + thickness,
                        minY - offset,
                        maxZ,
                        layerAlpha
                );

                // Top-left
                drawBox(
                        matrices,
                        vertexConsumer,
                        minX - offset - thickness,
                        maxY + offset,
                        minZ,
                        minX - offset,
                        maxY + offset + thickness,
                        maxZ,
                        layerAlpha
                );

                // Top-right
                drawBox(
                        matrices,
                        vertexConsumer,
                        maxX + offset,
                        maxY + offset,
                        minZ,
                        maxX + offset + thickness,
                        maxY + offset + thickness,
                        maxZ,
                        layerAlpha
                );
            }
        }

        consumers.draw();
    }

    /**
     * Calculates the animation opacity for one effect.
     */
    private static float getFade(int effectTicks) {

        // Fade in
        if (effectTicks < FADE_IN_TICKS) {

            float progress =
                    (float) effectTicks / FADE_IN_TICKS;

            return smoothStep(progress);
        }

        // Fully visible
        if (effectTicks < FADE_IN_TICKS + HOLD_TICKS) {
            return 1.0f;
        }

        // Fade out
        int fadeOutStart =
                FADE_IN_TICKS + HOLD_TICKS;

        float progress =
                (float) (effectTicks - fadeOutStart)
                        / FADE_OUT_TICKS;

        return 1.0f - smoothStep(progress);
    }

    private static float smoothStep(float value) {

        value = Math.max(0.0f, Math.min(1.0f, value));

        return value * value * (3.0f - 2.0f * value);
    }

    private static void drawBox(
            MatrixStack matrices,
            VertexConsumer consumer,
            double minX,
            double minY,
            double minZ,
            double maxX,
            double maxY,
            double maxZ,
            float alpha
    ) {

        WorldRenderer.drawBox(
                matrices,
                consumer,
                minX,
                minY,
                minZ,
                maxX,
                maxY,
                maxZ,
                1.0f,
                0.75f,
                0.05f,
                alpha
        );
    }

    // =========================
    // INDIVIDUAL EFFECT
    // =========================

    private static class Effect {

        private final BlockPos position;
        private int ticks;

        private Effect(BlockPos position) {
            this.position = position;
            this.ticks = 0;
        }
    }
}