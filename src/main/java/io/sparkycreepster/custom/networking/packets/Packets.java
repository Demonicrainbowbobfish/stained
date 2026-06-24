package io.sparkycreepster.custom.networking.packets;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.util.Identifier;

public class Packets {
    public static final Identifier BLOOD1_SPAWN = new Identifier("stained", "blood1spawn");

    public static void registerPackets() {
        ServerPlayNetworking.registerGlobalReceiver(BLOOD1_SPAWN, ((server, player, handler, buf, responseSender) -> {
            Blood1SpawnPacket packet = new Blood1SpawnPacket(buf);
            server.execute(() -> {

            });
        }));
    }
}
