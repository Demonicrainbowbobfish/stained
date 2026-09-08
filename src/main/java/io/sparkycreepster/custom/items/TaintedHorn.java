package io.sparkycreepster.custom.items;

import io.sparkycreepster.custom.networking.packets.Packets;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class TaintedHorn extends Item {

    public TaintedHorn(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(
            World world,
            PlayerEntity user,
            Hand hand
    ) {
        if (!world.isClient && user instanceof net.minecraft.server.network.ServerPlayerEntity serverPlayer) {
            ServerPlayNetworking.send(
                    serverPlayer,
                    Packets.OPEN_GHOST_MESSAGE,
                    PacketByteBufs.empty()
            );
        }

        return TypedActionResult.success(
                user.getStackInHand(hand)
        );
    }
}