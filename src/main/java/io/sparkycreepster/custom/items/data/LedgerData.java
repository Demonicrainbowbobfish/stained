package io.sparkycreepster.custom.items.data;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class LedgerData {

    private static final String ENTRIES_KEY = "Entries";
    private static final String NAME_KEY = "Name";
    private static final String UUID_KEY = "UUID";


    // Add an entry to the ledger
    public static void addEntry(ItemStack ledger, String name, UUID uuid) {

        NbtCompound nbt = ledger.getOrCreateNbt();

        NbtList entries;

        if (nbt.contains(ENTRIES_KEY)) {
            entries = nbt.getList(ENTRIES_KEY, 10);
        } else {
            entries = new NbtList();
        }

        NbtCompound entry = new NbtCompound();

        entry.putString(NAME_KEY, name);
        entry.putUuid(UUID_KEY, uuid);

        entries.add(entry);

        nbt.put(ENTRIES_KEY, entries);
    }


    // Get a UUID by player name
    public static UUID getUuid(ItemStack ledger, String name) {

        if (!ledger.hasNbt()) {
            return null;
        }

        NbtCompound nbt = ledger.getNbt();

        if (!nbt.contains(ENTRIES_KEY)) {
            return null;
        }

        NbtList entries = nbt.getList(ENTRIES_KEY, 10);

        for (int i = 0; i < entries.size(); i++) {

            NbtCompound entry = entries.getCompound(i);

            if (entry.getString(NAME_KEY).equals(name)) {
                return entry.getUuid(UUID_KEY);
            }
        }

        return null;
    }


    // Get every entry
    public static List<Entry> getEntries(ItemStack ledger) {

        List<Entry> result = new ArrayList<>();

        if (!ledger.hasNbt()) {
            return result;
        }

        NbtCompound nbt = ledger.getNbt();

        if (!nbt.contains(ENTRIES_KEY)) {
            return result;
        }

        NbtList entries = nbt.getList(ENTRIES_KEY, 10);

        for (int i = 0; i < entries.size(); i++) {

            NbtCompound entry = entries.getCompound(i);

            String name = entry.getString(NAME_KEY);
            UUID uuid = entry.getUuid(UUID_KEY);

            result.add(new Entry(name, uuid));
        }

        return result;
    }


    // Represents one ledger entry
    public record Entry(String name, UUID uuid) {
    }
}