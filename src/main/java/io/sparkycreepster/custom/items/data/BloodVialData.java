package io.sparkycreepster.custom.items.data;

import net.minecraft.item.ItemStack;

import java.util.UUID;

public class BloodVialData {

    public static UUID getBloodOf(ItemStack stack) {
        if (!stack.hasNbt()) {
            return null;
        }

        if (!stack.getNbt().containsUuid("BloodOf")) {
            return null;
        }

        return stack.getNbt().getUuid("BloodOf");
    }
    public static String getBloodDisplayName(ItemStack stack) {
        if (!stack.hasNbt()) {
            return null;
        }

        return stack.getNbt().getString("BloodDisplayName");
    }
}