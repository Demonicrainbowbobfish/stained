package io.sparkycreepster.client.mixin;


import net.minecraft.client.network.ClientPlayNetworkHandler;
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
    private final Map<String, UUID> nameToUuid = new HashMap<>();

    @Inject(method = "onGameMessage", at = @At("HEAD"), cancellable = true)
    private void stopTheBigBeatdownce(GameMessageS2CPacket packet, CallbackInfo ci) {
        Text content = packet.content();
        if (content.getContent() instanceof TranslatableTextContent translatable) {
            String key = translatable.getKey();
            if (key.equals("multiplayer.player.joined") || key.equals("multiplayer.player.left")) {
                ci.cancel();
            }
        }
    }
}
