package io.sparkycreepster.custom.networking.packets;

import io.sparkycreepster.Stained;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;

import static io.sparkycreepster.Stained.MOD_ID;

public class Packets {

    public static final Identifier GOLDEN_CUBE =
            new Identifier("stained", "golden_cube");

    public static final Identifier GHOST_MESSAGE =
            new Identifier(MOD_ID, "ghost_message");

    public static final Identifier OPEN_GHOST_MESSAGE =
            new Identifier(MOD_ID, "open_ghost_message");

    public static final Identifier BLOOD1_SPAWN =
            new Identifier("stained", "blood1spawn");

    public static final Identifier TINTED_EXPLOSION =
            new Identifier("stained", "tintedexp");

    public static void registerPackets() {

        ServerPlayNetworking.registerGlobalReceiver(
                GHOST_MESSAGE,
                (server, player, handler, buf, responseSender) -> {

                    String message = buf.readString(256);

                    server.execute(() -> {

                        /*
                         * Get the exact direction the player is looking.
                         */
                        Vec3d look = player.getRotationVec(1.0F);

                        /*
                         * Place the message 5 blocks in front
                         * of the player's eyes.
                         */
                        double distance = 5.0D;

                        double x = player.getEyePos().x
                                + look.x * distance;

                        double y = player.getEyePos().y
                                + look.y * distance;

                        double z = player.getEyePos().z
                                + look.z * distance;

                        /*
                         * Keep the player's viewing rotation
                         * so the message rotates with them.
                         */
                        float yaw = player.getYaw();
                        float pitch = player.getPitch();

                        Stained.LOGGER.info(
                                "{} sent ghost message: {}",
                                player.getName().getString(),
                                message
                        );

                        /*
                         * Send the message to every player.
                         */
                        for (ServerPlayerEntity target :
                                server.getPlayerManager().getPlayerList()) {

                            PacketByteBuf out =
                                    PacketByteBufs.create();

                            out.writeString(message, 256);

                            out.writeDouble(x);
                            out.writeDouble(y);
                            out.writeDouble(z);

                            out.writeFloat(yaw);
                            out.writeFloat(pitch);

                            ServerPlayNetworking.send(
                                    target,
                                    GHOST_MESSAGE,
                                    out
                            );
                        }
                    });
                }
        );

        ServerPlayNetworking.registerGlobalReceiver(
                BLOOD1_SPAWN,
                (server, player, handler, buf, responseSender) -> {

                    Blood1SpawnPacket packet =
                            new Blood1SpawnPacket(buf);

                    server.execute(() -> {

                    });
                }
        );

        ServerPlayNetworking.registerGlobalReceiver(
                TINTED_EXPLOSION,
                (server, player, handler, buf, responseSender) -> {

                    TintedExplosion packet =
                            new TintedExplosion(buf);

                    server.execute(() -> {

                    });
                }
        );
    }
}