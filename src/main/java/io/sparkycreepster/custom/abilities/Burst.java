package io.sparkycreepster.custom.abilities;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;

public class Burst implements BloodAbility {
    @Override
    public void use(PlayerEntity player, LivingEntity target) {
        target.damage(
                player.getDamageSources().magic(),
                8.0f
        );
        target.addVelocity(0, 2.0, 0);
    }
    @Override
    public String getName() {
        return "Burst";
    }

    @Override
    public int getCooldownTicks() {
        return 900;
    }
}
