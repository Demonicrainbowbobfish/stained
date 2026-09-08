package io.sparkycreepster.client.networking.particles.render;

import com.mojang.blaze3d.systems.RenderSystem;
import io.sparkycreepster.custom.networking.packets.Packets;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.math.RotationAxis;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GhostMessageRenderer {

    private static final List<GhostMessage> MESSAGES =
            new ArrayList<>();

    public static void register() {

        ClientPlayNetworking.registerGlobalReceiver(
                Packets.GHOST_MESSAGE,
                (client, handler, buf, responseSender) -> {

                    String message = buf.readString(256);

                    double x = buf.readDouble();
                    double y = buf.readDouble();
                    double z = buf.readDouble();

                    float yaw = buf.readFloat();
                    float pitch = buf.readFloat();

                    client.execute(() -> {

                        MESSAGES.add(
                                new GhostMessage(
                                        message,
                                        x,
                                        y,
                                        z,
                                        yaw,
                                        pitch
                                )
                        );

                    });
                }
        );
    }

    public static void tick() {

        Iterator<GhostMessage> iterator =
                MESSAGES.iterator();

        while (iterator.hasNext()) {

            GhostMessage message = iterator.next();

            message.tick();

            if (message.isDead()) {
                iterator.remove();
            }
        }
    }

    public static void render(WorldRenderContext context) {

        if (MESSAGES.isEmpty()) {
            return;
        }

        MinecraftClient client =
                MinecraftClient.getInstance();

        if (client.player == null) {
            return;
        }

        MatrixStack matrices = context.matrixStack();

        if (matrices == null) {
            return;
        }

        double cameraX =
                context.camera().getPos().x;

        double cameraY =
                context.camera().getPos().y;

        double cameraZ =
                context.camera().getPos().z;

        TextRenderer textRenderer =
                client.textRenderer;

        matrices.push();

        for (GhostMessage message : MESSAGES) {

            float alpha = message.getAlpha();

            if (alpha <= 0.0F) {
                continue;
            }

            matrices.push();

            matrices.translate(
                    message.x - cameraX,
                    message.y + 1.5D - cameraY,
                    message.z - cameraZ
            );

            /*
             * Face the opposite direction from the
             * player's view direction.
             */
            matrices.multiply(
                    RotationAxis.POSITIVE_Y.rotationDegrees(
                            -message.yaw
                    )
            );

            matrices.multiply(
                    RotationAxis.POSITIVE_X.rotationDegrees(
                            message.pitch
                    )
            );

            float scale = 0.025F;

            matrices.scale(
                    -scale,
                    -scale,
                    scale
            );

            int alphaInt =
                    (int) (alpha * 255.0F);

            int color =
                    (alphaInt << 24) | 0xFF2020;

            Text text =
                    Text.literal(message.message);

            float textWidth =
                    textRenderer.getWidth(text);

            textRenderer.draw(
                    text,
                    -textWidth / 2.0F,
                    0,
                    color,
                    false,
                    matrices.peek().getPositionMatrix(),
                    context.consumers(),
                    TextRenderer.TextLayerType.SEE_THROUGH,
                    0,
                    LightmapTextureManager.MAX_LIGHT_COORDINATE
            );

            matrices.multiply(
                    RotationAxis.POSITIVE_Y.rotationDegrees(180.0F)
            );

            textRenderer.draw(
                    text,
                    -textWidth / 2.0F,
                    0,
                    color,
                    false,
                    matrices.peek().getPositionMatrix(),
                    context.consumers(),
                    TextRenderer.TextLayerType.SEE_THROUGH,
                    0,
                    LightmapTextureManager.MAX_LIGHT_COORDINATE
            );
            matrices.pop();
        }

        matrices.pop();
    }
}