package io.sparkycreepster.general;
import io.sparkycreepster.Stained;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import static io.sparkycreepster.Stained.MOD_ID;

public class Blocks {

    public static final Block BLOOD_OBSERVER = registerBlocks("blood_observer",
            new Block(FabricBlockSettings.copyOf(net.minecraft.block.Blocks.DEEPSLATE)));

    // register actual block, these are both helper methods btw
    private static Block registerBlocks(String name, Block block) {
        registerBlockItem(name, block);
        return Registry.register(Registries.BLOCK, new Identifier(MOD_ID, name), block);
    }
    // Register the BlockItem (idek why mojang decided to use a BlockItem and not just one class bruh)
    private static Item registerBlockItem(String name, Block block) {
        return Registry.register(Registries.ITEM, new Identifier(MOD_ID, name),
                new BlockItem(block, new FabricItemSettings()));
    }
    public static void registerModBlocks() {
        Stained.LOGGER.info("Registering mod blocks, hang tight.");
    }
}
