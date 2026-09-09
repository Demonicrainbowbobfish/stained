package io.sparkycreepster.client.mixin;

import io.sparkycreepster.Stained;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xaero.hud.minimap.radar.render.element.RadarRenderer;

@Mixin(RadarRenderer.class)
public class XaeroRadarRendererMixin {

    @Inject(
            method = "renderElement(Lnet/minecraft/entity/Entity;ZZDFDLxaero/hud/minimap/element/render/MinimapElementRenderInfo;Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/client/render/VertexConsumerProvider$Immediate;)Z",
            at = @At("HEAD"),
            cancellable = true
    )
    private void stained$hideVanishedPlayer(
            Entity entity,
            CallbackInfoReturnable<Boolean> cir
    ) {
        if (!(entity instanceof PlayerEntity player)) {
            return;
        }

        if (!Stained.vanishEnabled) {
            return;
        }

        if (Stained.bannedUuids.contains(player.getUuid())) {
            cir.setReturnValue(false);
        }
    }
}