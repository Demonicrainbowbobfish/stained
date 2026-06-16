package io.sparkycreepster.client.mixin;

import io.sparkycreepster.Stained;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.WorldView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderDispatcher.class)
public class CancelShadowRender {
    @Inject(method = "renderShadow", at = @At("HEAD"), cancellable = true)
    private static void cancelPlayerShadow(MatrixStack matricies, VertexConsumerProvider vertexConsumer, Entity entity, float opacity, float tickDelta, WorldView wrldView, float radius, CallbackInfo ci) {
        if (entity instanceof PlayerEntity && Stained.vanishEnabled && Stained.bannedUuids.contains(entity.getUuid())) {
            ci.cancel();

        }
    }
}
