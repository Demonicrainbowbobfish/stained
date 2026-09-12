package io.sparkycreepster.custom.blocks.blockEntities;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import io.sparkycreepster.custom.blocks.blockEntities.AltarBlockEntity;
import io.sparkycreepster.general.Blocks;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import static io.sparkycreepster.Stained.MOD_ID;

public class BlockEntities {
    public static final BlockEntityType<AltarBlockEntity> ALTAR = registerBlockEntities("altar", FabricBlockEntityTypeBuilder.create(AltarBlockEntity::new, Blocks.ALTAR).build());

    public static <T extends BlockEntity> BlockEntityType<T> registerBlockEntities(String name, BlockEntityType<T> BlockEntity) {
        return Registry.register(Registries.BLOCK_ENTITY_TYPE, new Identifier(MOD_ID, name), BlockEntity);
    }
}
