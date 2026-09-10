package io.sparkycreepster.custom.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;

import javax.swing.text.html.BlockView;

public class Altar extends Block {
    public Altar(Settings settings) {
        super(settings);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, net.minecraft.world.BlockView world, BlockPos pos, ShapeContext context) {
        VoxelShape base = VoxelShapes.cuboid(0, 0, 0, 1, 0.125, 1);
        VoxelShape otherBase = VoxelShapes.cuboid(0.1875, 0.125, 0.1875, 0.8125, 0.1875, 0.8125);
        VoxelShape column = VoxelShapes.cuboid(0.375, 0.1875, 0.375, 0.625, 0.625, 0.625);
        VoxelShape piece4 = VoxelShapes.cuboid(0.4375, 0.5625, 0.25, 0.5625, 0.6875, 0.5);
        VoxelShape piece5 = VoxelShapes.cuboid(0.4375, 0.625, 0.125, 0.5625, 0.75, 0.25);
        VoxelShape piece6 = VoxelShapes.cuboid(0.4375, 0.5625, 0.5, 0.5625, 0.6875, 0.75);
        VoxelShape piece7 = VoxelShapes.cuboid(0.4375, 0.625, 0.75, 0.5625, 0.75, 0.875);

        VoxelShape AltarShape = VoxelShapes.union(base, otherBase, column, piece4, piece5, piece6, piece7);
        return AltarShape;
    }





}
