package io.sparkycreepster.custom.networking.packets;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class EndPortalEffectManager {

    private static final List<PendingEffect> PENDING_EFFECTS = new ArrayList<>();

    public static void initialize() {
        ServerTickEvents.END_SERVER_TICK.register(server -> {

            Iterator<PendingEffect> iterator = PENDING_EFFECTS.iterator();

            while (iterator.hasNext()) {
                PendingEffect effect = iterator.next();

                effect.ticksRemaining--;

                if (effect.ticksRemaining <= 0) {

                    sendGoldenCube(
                            effect.world,
                            effect.position
                    );

                    iterator.remove();
                }
            }
        });
    }

    public static void schedule(World world, BlockPos position) {

        for (PendingEffect effect : PENDING_EFFECTS) {
            if (effect.world == world &&
                    effect.position.equals(position)) {
                return;
            }
        }

        PENDING_EFFECTS.add(
                new PendingEffect(
                        world,
                        position.toImmutable(),
                        100
                )
        );
    }

    private static void sendGoldenCube(
            World world,
            BlockPos position
    ) {
        PacketByteBuf buf = PacketByteBufs.create();

        buf.writeBlockPos(position);

        for (PlayerEntity player : world.getPlayers()) {
            if (!(player instanceof ServerPlayerEntity serverPlayer)) {
                continue;
            }

            if (serverPlayer.squaredDistanceTo(
                    position.getX() + 0.5,
                    position.getY() + 0.5,
                    position.getZ() + 0.5
            ) <= 64 * 64) {

                ServerPlayNetworking.send(
                        serverPlayer,
                        Packets.GOLDEN_CUBE,
                        buf
                );
            }
        }
    }

    private static class PendingEffect {

        private final World world;
        private final BlockPos position;
        private int ticksRemaining;

        private PendingEffect(
                World world,
                BlockPos position,
                int ticksRemaining
        ) {
            this.world = world;
            this.position = position;
            this.ticksRemaining = ticksRemaining;
        }
    }
}