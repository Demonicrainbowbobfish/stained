package io.sparkycreepster.mixin;

import io.sparkycreepster.custom.networking.packets.EndPortalEffectManager;
import net.minecraft.block.Blocks;
import net.minecraft.item.EnderEyeItem;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EnderEyeItem.class)
public class EndPortalFrameMixin {

    @Inject(
            method = "useOnBlock",
            at = @At("HEAD"),
            cancellable = true
    )
    private void stained$blockEndPortal(
            ItemUsageContext context,
            CallbackInfoReturnable<ActionResult> cir
    ) {
        if (!context.getWorld()
                .getBlockState(context.getBlockPos())
                .isOf(Blocks.END_PORTAL_FRAME)) {
            return;
        }

        // Client: prevent vanilla portal behavior.
        if (context.getWorld().isClient()) {
            cir.setReturnValue(ActionResult.SUCCESS);
            return;
        }

        // Server: start the 5-second countdown.
        EndPortalEffectManager.schedule(
                context.getWorld(),
                context.getBlockPos()
        );

        cir.setReturnValue(ActionResult.SUCCESS);
    }
}