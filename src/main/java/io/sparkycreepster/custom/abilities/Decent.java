package io.sparkycreepster.custom.abilities;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;

public class Decent implements BloodAbility{

    @Override
    public void use(PlayerEntity player, LivingEntity target) {

        if (target.isOnGround()) {
            return;
        }

    }
    @Override
    public String getName() {
        return "Decent";
    }

    @Override
    public int getCooldownTicks() {
        return 400;
    }
}
