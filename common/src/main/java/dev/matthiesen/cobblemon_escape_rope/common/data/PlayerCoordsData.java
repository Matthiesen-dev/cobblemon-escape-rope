package dev.matthiesen.cobblemon_escape_rope.common.data;

import dev.matthiesen.cobblemon_escape_rope.common.CobblemonEscapeRope;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class PlayerCoordsData extends SavedData {
    private static final String COORDS_DATA_ID = CobblemonEscapeRope.MOD_ID + "_player_coords";

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

    @SuppressWarnings("unused")
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

    public static PlayerCoordsData getCoordsData() {
        MinecraftServer server = CobblemonEscapeRope.INSTANCE.getCommonUtils().getServer();
        DimensionDataStorage storage = server.overworld().getDataStorage();
        SavedData.Factory<PlayerCoordsData> factory = new SavedData.Factory<>(
                PlayerCoordsData::new,
                PlayerCoordsData::load,
                null
        );
        return storage.computeIfAbsent(factory, COORDS_DATA_ID);
    }

    public static PlayerCoordsData.DataStoreEntry getSavedPlayerData(ServerPlayer player) {
        return getCoordsData().getData(player.getUUID());
    }

    public static void setPlayerDataInMemory(ServerPlayer player, PlayerCoordsData.DataStoreEntry newData) {
        getCoordsData().setDataInMemory(player.getUUID(), newData);
    }

    public static void setCooldown(ServerPlayer player, int cooldown) {
        PlayerCoordsData data = getCoordsData();
        PlayerCoordsData.DataStoreEntry entry = data.getData(player.getUUID());
        entry.cooldown = cooldown;
        data.setData(player.getUUID(), entry);
    }
}
