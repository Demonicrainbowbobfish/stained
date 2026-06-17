package io.sparkycreepster.custom.dataclasses;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.PersistentState;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
// THIS FILE IS INDEV!!! DOSENT PROPERLY WORK RIGHT NOW!!
// stores players who've gone near the observer block in a 64x64 radius excluding any of the banned UUIDS
public class BloodObserverRegistry extends PersistentState {

    // Makes a map of the UUID to player name
    private static final Map<UUID, String> PlayersVisited = new HashMap<>();
    public void addPlayer(UUID uuid, String name) {
        this.PlayersVisited.put(uuid, name);
        this.markDirty();
    }
    public String getPlayerName(UUID uuid) {
        return this.PlayersVisited.get(uuid);
    }
    // Writing data to NBT (persistence is important in hacking and MC)
    public NbtCompound writeNBT(NbtCompound nbt) {
        NbtCompound namesTag = new NbtCompound();
        for (Map.Entry<UUID, String> entry : PlayersVisited.entrySet()) {
            namesTag.putString(entry.getKey().toString(), entry.getValue());
        }
        nbt.put("PlayerNames", namesTag);
        return nbt;
    }



    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        return null;
    }
}
