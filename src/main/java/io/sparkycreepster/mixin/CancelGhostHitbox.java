package io.sparkycreepster.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Box;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static io.sparkycreepster.Stained.bannedUuids;
import static io.sparkycreepster.Stained.vanishEnabled;

@Mixin(Entity.class)
public abstract class CancelGhostHitbox {

    @Shadow public abstract EntityType<?> getType();

    @Inject(method = "isCollidable", at = @At("HEAD"), cancellable = true)
    private void removeTargetBox(CallbackInfoReturnable<Boolean> cir) {
        Entity entity = (Entity) (Object) this;

        if (entity instanceof PlayerEntity && entity.getUuid().equals(bannedUuids) && vanishEnabled) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "getBoundingBox", at = @At("HEAD"), cancellable = true)
    private void zeroOutHitbox(CallbackInfoReturnable<Box> cir) {
        Entity entity = (Entity) (Object) this;
        if (entity instanceof PlayerEntity && entity.getUuid().equals(bannedUuids) && vanishEnabled) {
            cir.setReturnValue(new Box(entity.getX(), entity.getY(), entity.getZ(), entity.getX(), entity.getY(), entity.getZ()));
        }
    }

}
