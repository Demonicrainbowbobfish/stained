package io.sparkycreepster.custom.weaponry;

import io.sparkycreepster.Stained;
import io.sparkycreepster.general.Items;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class TwistedDagger extends SwordItem {
    public TwistedDagger(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, Settings settings) {
        super(toolMaterial, attackDamage, attackSpeed, settings);
    }
    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        ItemStack offhand = user.getOffHandStack();
        if (offhand.isOf(Items.BLOOD_VIAL)) {
            ItemStack vial = new ItemStack(Items.BLOOD_VIAL);
            offhand.getOrCreateNbt().putUuid("BloodOf", user.getUuid());
            offhand.getOrCreateNbt().putBoolean("Filled", true);
            offhand.getOrCreateNbt().putString(
                    "BloodDisplayName",
                    user.getName().getString()
            );

        }

        return TypedActionResult.success(stack);
    }

    // for hitting mobs for crafting recipes
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {

        if (attacker instanceof PlayerEntity player) {
            ItemStack offhand = player.getOffHandStack();
            if (offhand.isOf(Items.BLOOD_VIAL)) {
                offhand.getOrCreateNbt().putBoolean("Filled", true);
                offhand.getOrCreateNbt().putBoolean("UsedOnNonPlayerEntity", true);
            }
        }

        return super.postHit(stack, target, attacker);
    }
}
