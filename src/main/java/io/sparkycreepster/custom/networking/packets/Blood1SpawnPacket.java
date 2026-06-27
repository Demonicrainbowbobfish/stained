package io.sparkycreepster.custom.networking.packets;

import com.mojang.authlib.minecraft.client.MinecraftClient;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.loader.impl.lib.sat4j.core.Vec;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.Vec3d;

public class Blood1SpawnPacket {
    public final Vec3d position;
    public final int startingColor;
    public final int endingColor;

    public Blood1SpawnPacket(Vec3d position, int startingColor, int endingColor) {
        this.position = position;
        this.startingColor = startingColor;
        this.endingColor = endingColor;

    }

    public Blood1SpawnPacket(PacketByteBuf buf) {
        this.position = new Vec3d(buf.readDouble(), buf.readDouble(), buf.readDouble());
        this.startingColor = buf.readInt();
        this.endingColor = buf.readInt();
    }
    public void toBytes(PacketByteBuf buf) {
        buf.writeDouble(position.x);
        buf.writeDouble(position.y);
        buf.writeDouble(position.z);
        buf.writeInt(startingColor);
        buf.writeInt(endingColor);
    }
}
    // go access the thing in partices client side

