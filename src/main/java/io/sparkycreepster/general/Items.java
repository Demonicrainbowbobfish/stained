package io.sparkycreepster.general;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import static io.sparkycreepster.Stained.MOD_ID;

public class Items {
    // Register items here
    public static final Item BLOOD_VIAL = registerItem("blood_vial",
            new Item(new FabricItemSettings()));


    private static net.minecraft.item.Item registerItem(String name, net.minecraft.item.Item item) {
        return Registry.register(Registries.ITEM, new Identifier(MOD_ID, name), item);
    }


    public static void registerModItems() {


    }
}
