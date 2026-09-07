package io.sparkycreepster.client.endStopper;

import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;

public class GoldenWorldBorderRenderer {

    private static final Identifier BORDER_TEXTURE =
            new Identifier("minecraft", "textures/misc/forcefield.png");

    /*
     * 0.501 = 1 mm of extra space on each side
     * compared to the normal 1x1 block bounds.
     */
    private static final double HALF_SIZE = 0.501;

    /*
     * Custom translucent layer.
     *
     * IMPORTANT:
     * We use the entity translucent vertex format because our vertices
     * contain:
     *
     * POSITION
     * COLOR
     * TEXTURE
     * OVERLAY
     * LIGHT
     * NORMAL
     *
     * Culling is disabled so the forcefield is visible from BOTH sides.
     */
    private static final RenderLayer GOLDEN_BORDER_LAYER =
            RenderLayer.of(
                    "golden_world_border",
                    VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL,
                    VertexFormat.DrawMode.QUADS,
                    256,
                    true,
                    true,
                    RenderLayer.MultiPhaseParameters.builder()
                            .program(RenderPhase.ENTITY_TRANSLUCENT_PROGRAM)
                            .texture(
                                    new RenderPhase.Texture(
                                            BORDER_TEXTURE,
                                            false,
                                            false
                                    )
                            )
                            .transparency(RenderPhase.TRANSLUCENT_TRANSPARENCY)
                            .cull(RenderPhase.DISABLE_CULLING)
                            .lightmap(RenderPhase.ENABLE_LIGHTMAP)
                            .overlay(RenderPhase.ENABLE_OVERLAY_COLOR)
                            .build(false)
            );

    public static void render(WorldRenderContext context) {

        if (!GoldenCubeRenderer.isActive()) {
            return;
        }

        MatrixStack matrices = context.matrixStack();

        if (matrices == null) {
            return;
        }

        Vec3d camera = context.camera().getPos();

        VertexConsumerProvider.Immediate consumers =
                VertexConsumerProvider.immediate(
                        Tessellator.getInstance().getBuffer()
                );

        VertexConsumer consumer =
                consumers.getBuffer(GOLDEN_BORDER_LAYER);

        for (BlockPos pos : GoldenCubeRenderer.getActivePositions()) {

            float fade =
                    GoldenCubeRenderer.getFadeFor(pos);

            if (fade <= 0.0f) {
                continue;
            }

            renderCube(
                    matrices,
                    camera,
                    pos,
                    fade,
                    consumer
            );
        }

        consumers.draw();
    }

    private static void renderCube(
            MatrixStack matrices,
            Vec3d camera,
            BlockPos pos,
            float fade,
            VertexConsumer consumer
    ) {

        double centerX = pos.getX() + 0.5;
        double centerY = pos.getY() + 0.5;
        double centerZ = pos.getZ() + 0.5;

        double minX = centerX - HALF_SIZE - camera.x;
        double maxX = centerX + HALF_SIZE - camera.x;

        double minY = centerY - HALF_SIZE - camera.y;
        double maxY = centerY + HALF_SIZE - camera.y;

        double minZ = centerZ - HALF_SIZE - camera.z;
        double maxZ = centerZ + HALF_SIZE - camera.z;

        /*
         * Bright gold.
         */
        float red = 1.0f;
        float green = 0.85f;
        float blue = 0.1f;

        /*
         * Higher alpha = stronger/more visible forcefield.
         */
        float alpha = 0.85f * fade;

        Matrix4f matrix =
                matrices.peek().getPositionMatrix();

        /*
         * Continuously scroll the forcefield texture.
         */
        float animation =
                (System.currentTimeMillis() % 4000L) / 4000.0f;

        /*
         * NORTH
         */
        drawFace(
                consumer,
                matrix,
                minX, minY, minZ,
                maxX, maxY, minZ,
                red, green, blue, alpha,
                animation,
                0.0f, 0.0f, -1.0f
        );

        /*
         * SOUTH
         */
        drawFace(
                consumer,
                matrix,
                maxX, minY, maxZ,
                minX, maxY, maxZ,
                red, green, blue, alpha,
                animation,
                0.0f, 0.0f, 1.0f
        );

        /*
         * WEST
         */
        drawFace(
                consumer,
                matrix,
                minX, minY, maxZ,
                minX, maxY, minZ,
                red, green, blue, alpha,
                animation,
                -1.0f, 0.0f, 0.0f
        );

        /*
         * EAST
         */
        drawFace(
                consumer,
                matrix,
                maxX, minY, minZ,
                maxX, maxY, maxZ,
                red, green, blue, alpha,
                animation,
                1.0f, 0.0f, 0.0f
        );

        /*
         * TOP
         */
        drawFace(
                consumer,
                matrix,
                minX, maxY, minZ,
                maxX, maxY, maxZ,
                red, green, blue, alpha,
                animation,
                0.0f, 1.0f, 0.0f
        );

        /*
         * BOTTOM
         */
        drawFace(
                consumer,
                matrix,
                minX, minY, maxZ,
                maxX, minY, minZ,
                red, green, blue, alpha,
                animation,
                0.0f, -1.0f, 0.0f
        );
    }

    private static void drawFace(
            VertexConsumer consumer,
            Matrix4f matrix,

            double x1,
            double y1,
            double z1,

            double x2,
            double y2,
            double z2,

            float red,
            float green,
            float blue,
            float alpha,

            float animation,

            float normalX,
            float normalY,
            float normalZ
    ) {

        drawVertex(
                consumer,
                matrix,
                x1, y1, z1,
                red, green, blue, alpha,
                0.0f, animation,
                normalX, normalY, normalZ
        );

        drawVertex(
                consumer,
                matrix,
                x2, y1, z2,
                red, green, blue, alpha,
                1.0f, animation,
                normalX, normalY, normalZ
        );

        drawVertex(
                consumer,
                matrix,
                x2, y2, z2,
                red, green, blue, alpha,
                1.0f, animation + 1.0f,
                normalX, normalY, normalZ
        );

        drawVertex(
                consumer,
                matrix,
                x1, y2, z1,
                red, green, blue, alpha,
                0.0f, animation + 1.0f,
                normalX, normalY, normalZ
        );
    }

    private static void drawVertex(
            VertexConsumer consumer,
            Matrix4f matrix,

            double x,
            double y,
            double z,

            float red,
            float green,
            float blue,
            float alpha,

            float u,
            float v,

            float normalX,
            float normalY,
            float normalZ
    ) {

        consumer.vertex(
                        matrix,
                        (float) x,
                        (float) y,
                        (float) z
                )
                .color(
                        red,
                        green,
                        blue,
                        alpha
                )
                .texture(
                        u,
                        v
                )
                .overlay(
                        OverlayTexture.DEFAULT_UV
                )
                .light(
                        0xF000F0
                )
                .normal(
                        normalX,
                        normalY,
                        normalZ
                )
                .next();
    }
}