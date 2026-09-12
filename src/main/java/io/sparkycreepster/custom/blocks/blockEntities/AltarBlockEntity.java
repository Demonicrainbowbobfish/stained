package io.sparkycreepster.custom.blocks.blockEntities;

import io.sparkycreepster.custom.blocks.Altar;
import io.sparkycreepster.general.Blocks;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.LecternBlockEntity;
import net.minecraft.util.math.BlockPos;
// AltarBlockEntity = The place where the data is stored
// BlockEntityType<AltarBlockEntity> = the definition that mc uses to figure out what an AltarBlockEntity is
// AltarBlockEntity::new = instructions to make a new one

public class AltarBlockEntity extends BlockEntity {


    public AltarBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntities.ALTAR, pos, state);
    }


}
