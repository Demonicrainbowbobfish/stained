package io.sparkycreepster.general;

import io.sparkycreepster.custom.StainedSwordMaterialorsmt;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterials;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import static io.sparkycreepster.Stained.MOD_ID;

public class Items {
    // Register items here
    public static final Item BLOOD_VIAL = registerItem("blood_vial",
            new Item(new FabricItemSettings()));
    public static final Item STAINED_SWORD = registerItem("stained_sword",
            new StainedSwordMaterialorsmt(
                    ToolMaterials.NETHERITE,
                    10,
                    -2.4F,
                    new FabricItemSettings()
            ));

    private static net.minecraft.item.Item registerItem(String name, net.minecraft.item.Item item) {
        return Registry.register(Registries.ITEM, new Identifier(MOD_ID, name), item);
    }


    public static void registerModItems() {


    }
}
