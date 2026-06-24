package io.sparkycreepster.client.networking.particles;

import io.sparkycreepster.custom.networking.packets.Blood1SpawnPacket;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.world.World;

import java.awt.*;

public class Blood1Handle {
    private final int startingColor;
    private final int endingColor;
    public Blood1Handle(int startingColor, int endingColor) {
        this.startingColor = startingColor;
        this.endingColor = endingColor;
    }
    public void handle(MinecraftClient client) {


    }
}
