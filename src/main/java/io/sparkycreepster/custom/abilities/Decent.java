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
        System.out.println(
                "World class: " + player.getWorld().getClass().getName()
        );
        System.out.println(
                "isClient = " + player.getWorld().isClient()
        );
        if (target.isOnGround()) {
            return;
        }
        // still crashes
        // don't ask me why i have no idea

        Color startColor = new Color(150, 0, 0);
        Color endColor = new Color(90, 0, 0);
        Blood1SpawnPacket packet = new Blood1SpawnPacket(
                target.getPos(),
                startColor.getRGB(),
                endColor.getRGB()

        );
        PacketByteBuf buf = PacketByteBufs.create();
        packet.toBytes(buf);
        for (ServerPlayerEntity playerII : PlayerLookup.tracking(target)) {
            ServerPlayNetworking.send(playerII, Packets.BLOOD1_SPAWN, buf);
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
