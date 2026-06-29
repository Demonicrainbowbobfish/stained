package io.sparkycreepster.custom.abilities;

import io.sparkycreepster.custom.networking.packets.Blood1SpawnPacket;
import io.sparkycreepster.custom.networking.packets.Packets;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;

import java.awt.*;

public class Decent implements BloodAbility{

    @Override
    public void use(PlayerEntity player, LivingEntity target) {

        if (target.isOnGround()) {
            return;
        }
        Color startColor = new Color(150, 0, 0);
        Color endColor = new Color(90, 0, 0);
        for (ServerPlayerEntity player2 : PlayerLookup.tracking(target)) {
            PacketByteBuf buf = PacketByteBufs.create();

            new Blood1SpawnPacket(
                    target.getPos(),
                    startColor.getRGB(),
                    endColor.getRGB()
            ).toBytes(buf);

            ServerPlayNetworking.send(player2, Packets.BLOOD1_SPAWN, buf);
        }

        target.addVelocity(0, -3, 0);

    }
    @Override
    public String getName() {
        return "Decent";
    }

    @Override
    public int getCooldownTicks() {
        return 400;
    }
}
