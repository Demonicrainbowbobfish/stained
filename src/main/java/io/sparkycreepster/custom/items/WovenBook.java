package io.sparkycreepster.custom.items;

import io.sparkycreepster.general.Items;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class WovenBook extends Item {
    public WovenBook(Settings settings) {
        super(settings);
    }
    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        if (stack.isOf(Items.WOVEN_BOOK)) {
            stack.getOrCreateNbt().putUuid("Signed", user.getUuid());
        }
        return TypedActionResult.success(stack);
    }
}
