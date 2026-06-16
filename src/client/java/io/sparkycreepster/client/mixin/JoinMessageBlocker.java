package io.sparkycreepster.client.mixin;


import io.sparkycreepster.Stained;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableTextContent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Mixin(ClientPlayNetworkHandler.class)
public abstract class JoinMessageBlocker {
    private static final Map<String, UUID> NAME_CACHE = new HashMap<>();

    @Inject(method = "onGameMessage", at = @At("HEAD"), cancellable = true)
    private void stopTheBigBeatdownce(GameMessageS2CPacket packet, CallbackInfo ci) {
        Text content = packet.content();
        if (content.getContent() instanceof TranslatableTextContent translatable) {
            System.out.println("KEY: " + translatable.getKey());
            System.out.println("FULL MESSAGE: " + content.getString());

            for (Object arg : translatable.getArgs()) {
                System.out.println("ARG: " + arg);

            }
            String key = translatable.getKey();
            if (key.equals("multiplayer.player.joined") || key.equals("multiplayer.player.left")) {
                Object arg = translatable.getArgs()[0];
                if (arg instanceof Text playerText) {
                    String playerName = playerText.getString();
                    if (playerName.equals("Emanguis")) {
                        ci.cancel();
                    }
                    ClientPlayNetworkHandler handler =
                            MinecraftClient.getInstance().getNetworkHandler();

                    if (handler == null) {
                        return;
                    }
                    System.out.println(
                            "Looking for: " + playerName
                    );
                    for (PlayerListEntry entry : handler.getPlayerList()) {
                        System.out.println(
                                "Entry: " + entry.getProfile().getName()
                        );
                        String entryName = entry.getProfile().getName();
                        if (entryName.equals(playerName)) {
                            System.out.println("Found matching player!");
                            UUID uuid = entry.getProfile().getId();
                            if (Stained.bannedUuids.contains(uuid)) {
                                ci.cancel();
                            }
                        }
                    }
                }
            }
        }
    }
}
