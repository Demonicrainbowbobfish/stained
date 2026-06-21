package io.sparkycreepster.custom.abilities;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;

public interface BloodAbility {
    void use(PlayerEntity player, LivingEntity target);
    String getName();
    int getCooldownTicks();
}
