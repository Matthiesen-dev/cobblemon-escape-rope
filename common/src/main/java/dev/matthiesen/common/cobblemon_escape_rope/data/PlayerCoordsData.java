package dev.matthiesen.common.cobblemon_escape_rope.data;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.saveddata.SavedData;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerCoordsData extends SavedData {
    public static class DataStoreEntry {
        public BlockPos pos;
        public int cooldown;
        public String dimension;
        public DataStoreEntry(BlockPos pos, int cooldown, String dimension) {
            this.pos = pos;
            this.cooldown = cooldown;
            this.dimension = dimension;
        }
    }

    private final Map<UUID, DataStoreEntry> playerCoords = new HashMap<>();

    public PlayerCoordsData() {}

    public void setData(UUID playerUuid, DataStoreEntry data) {
        playerCoords.put(playerUuid, data);
        this.setDirty();
    }

    public void setDataInMemory(UUID playerUuid, DataStoreEntry data) {
        playerCoords.put(playerUuid, data);
    }

    public DataStoreEntry getData(UUID playerUuid) {
        return playerCoords.getOrDefault(playerUuid, new DataStoreEntry(new BlockPos(0, 0, 0), 0, "minecraft:overworld"));
    }

    public static PlayerCoordsData load(CompoundTag nbt, HolderLookup.Provider registries) {
        PlayerCoordsData data = new PlayerCoordsData();
        ListTag list = nbt.getList("coords", Tag.TAG_COMPOUND);
        for (int i = 0; i < list.size(); i++) {
            CompoundTag entry = list.getCompound(i);
            data.playerCoords.put(
                    entry.getUUID("uuid"),
                    new DataStoreEntry(
                            new BlockPos(entry.getInt("x"), entry.getInt("y"), entry.getInt("z")),
                            entry.getInt("cooldown"),
                            entry.getString("dimension")
                    )
            );
        }
        return data;
    }

    @Override
    public @NotNull CompoundTag save(CompoundTag nbt, HolderLookup.Provider registries) {
        ListTag list = new ListTag();
        playerCoords.forEach((uuid, item) -> {
            CompoundTag entry = new CompoundTag();
            entry.putUUID("uuid", uuid);
            entry.putInt("x", item.pos.getX());
            entry.putInt("y", item.pos.getY());
            entry.putInt("z", item.pos.getZ());
            entry.putInt("cooldown", item.cooldown);
            entry.putString("dimension", item.dimension);
            list.add(entry);
        });
        nbt.put("coords", list);
        return nbt;
    }
}
