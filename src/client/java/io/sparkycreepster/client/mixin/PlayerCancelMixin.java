package io.sparkycreepster.client.mixin;

import io.sparkycreepster.Stained;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(net.minecraft.client.render.entity.PlayerEntityRenderer.class)
public abstract class PlayerCancelMixin {
    @Inject(
            method = "render(Lnet/minecraft/client/network/AbstractClientPlayerEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V",
            at = @At("HEAD"),
            cancellable = true
    )
    private void cancelPlayerRender(AbstractClientPlayerEntity player, float yaw, float tickDelta, MatrixStack matricies, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
        if (Stained.vanishEnabled && Stained.bannedUuids.contains(player.getUuid())) {
            ci.cancel();
        }
    }
}
