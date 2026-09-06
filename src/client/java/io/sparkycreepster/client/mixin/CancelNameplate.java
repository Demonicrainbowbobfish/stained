package io.sparkycreepster.client.mixin;

import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;

@Mixin(PlayerEntityRenderer.class)
public class CancelNameplate {

    private static final UUID HIDDEN_UUID =
            UUID.fromString("5c35627a-2d28-49c4-82fc-e64ec85be5c4");

    @Inject(
            method = "renderLabelIfPresent",
            at = @At("HEAD"),
            cancellable = true
    )
    private void stained$hideNametag(
            AbstractClientPlayerEntity player,
            Text text,
            MatrixStack matrices,
            VertexConsumerProvider vertexConsumers,
            int light,
            CallbackInfo ci
    ) {
        if (player.getUuid().equals(HIDDEN_UUID)) {
            ci.cancel();
        }
    }
}