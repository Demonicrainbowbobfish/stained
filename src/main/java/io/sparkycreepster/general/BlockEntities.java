package io.sparkycreepster.general;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import static io.sparkycreepster.Stained.MOD_ID;

public class BlockEntities {

    // helper method, very confusing to write though gotta say T-T
    private static <T extends BlockEntity> BlockEntityType<T> registerBlockEntity(String name, BlockEntityType<T> type) {
        return Registry.register(Registries.BLOCK_ENTITY_TYPE, new Identifier(MOD_ID, name), type);
    }

    public static void registerBlockEntities() {
        // yipe!
    }
}
