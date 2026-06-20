package io.sparkycreepster.custom.entity.blockEntity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;

import java.util.*;

public class BloodObserverBlockEntity extends BlockEntity {

    public BloodObserverBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    private final Set<UUID> recentVisitors = new HashSet<>();
    public void addVisitor(UUID uuid) {
        recentVisitors.add(uuid);
    }

    public Set<UUID> getRecentVisitors() {
        return recentVisitors;
    }


}
