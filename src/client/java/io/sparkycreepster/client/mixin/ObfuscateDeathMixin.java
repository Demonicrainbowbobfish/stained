package io.sparkycreepster.client.mixin;

import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableTextContent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(ClientPlayNetworkHandler.class)
public class ObfuscateDeathMixin {

    @ModifyArg(
            method = "onGameMessage",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/network/message/MessageHandler;onGameMessage(Lnet/minecraft/text/Text;Z)V"
            ),
            index = 0
    )
    private Text modifyGameMessage(Text message) {

        if (!(message.getContent() instanceof TranslatableTextContent content)) {
            return message;
        }

        String key = content.getKey();

        // Only modify death messages
        if (!key.startsWith("death.attack.")) {
            return message;
        }

        Object[] args = content.getArgs();

        // Death messages have the victim as the first argument
        if (args.length > 0 && args[0] instanceof Text playerName && playerName.getString().equals("Emanguis")) {

            MutableText obfuscatedName = Text.literal(playerName.getString())
                    .setStyle(playerName.getStyle().withObfuscated(true));

            return Text.translatable(key, obfuscatedName);
        }

        return message;
    }
}