package io.sparkycreepster.client.mixin;

import io.sparkycreepster.Stained;
import io.sparkycreepster.client.networking.particles.render.flipside.FlipsideParticleRenderer;
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
    private void cancelPlayerRender(
            AbstractClientPlayerEntity player,
            float yaw,
            float tickDelta,
            MatrixStack matrices,
            VertexConsumerProvider vertexConsumers,
            int light,
            CallbackInfo ci
    ) {

        /*
         * Normal vanished state.
         */
        if (Stained.vanishEnabled
                && Stained.bannedUuids.contains(player.getUuid())) {

            ci.cancel();
            return;
        }

        /*
         * Transition state.
         *
         * The player model is hidden for the entire
         * particle transition so it never visibly
         * pops in or out.
         */
        if (Stained.bannedUuids.contains(player.getUuid())
                && FlipsideParticleRenderer.isTransitioning(player)) {

            ci.cancel();
        }
    }
}