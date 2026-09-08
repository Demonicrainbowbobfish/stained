package io.sparkycreepster.custom.items;

import io.sparkycreepster.custom.items.data.BloodVialData;
import io.sparkycreepster.custom.items.data.LedgerData;
import io.sparkycreepster.general.Items;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import java.util.UUID;

public class Ledger extends Item {
    public Ledger(Settings settings) {
        super(settings);
    }
    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        ItemStack offHand = user.getOffHandStack();


        if (offHand.isOf(Items.BLOOD_VIAL)) {

            UUID bloodOf = BloodVialData.getBloodOf(offHand);
            String name = BloodVialData.getBloodDisplayName(offHand);

            if (bloodOf != null && name != null) {
                LedgerData.addEntry(stack, name, bloodOf);
                user.playSound(
                        SoundEvents.ITEM_BOOK_PAGE_TURN,
                        1.0F,
                        1.0F
                );
            }
        }
        return TypedActionResult.success(stack);
    }
}
