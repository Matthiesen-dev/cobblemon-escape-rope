package dev.matthiesen.common.cobblemon_escape_rope.utils;

import dev.matthiesen.common.cobblemon_escape_rope.CobblemonEscapeRope;
import dev.matthiesen.common.cobblemon_escape_rope.Constants;
import dev.matthiesen.common.cobblemon_escape_rope.data.PlayerCoordsData;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;

public final class DataUtil {
    private static final String COORDS_DATA_ID = Constants.MOD_ID + "_player_coords";

    public static PlayerCoordsData getCoordsData(MinecraftServer server) {
        DimensionDataStorage storage = server.overworld().getDataStorage();
        SavedData.Factory<PlayerCoordsData> factory = new SavedData.Factory<>(
                PlayerCoordsData::new,
                PlayerCoordsData::load,
                null
        );
        return storage.computeIfAbsent(factory, COORDS_DATA_ID);
    }

    private static MinecraftServer server(ServerPlayer player) {
        MinecraftServer s = player.getServer();
        return s != null ? s : CobblemonEscapeRope.currentServer;
    }

    public static PlayerCoordsData.DataStoreEntry getSavedPlayerData(ServerPlayer player) {
        return getCoordsData(server(player)).getData(player.getUUID());
    }

    public static void setPlayerDataInMemory(ServerPlayer player, PlayerCoordsData.DataStoreEntry newData) {
        getCoordsData(server(player)).setDataInMemory(player.getUUID(), newData);
    }

    public static void setCooldown(ServerPlayer player, int cooldown) {
        PlayerCoordsData data = getCoordsData(server(player));
        PlayerCoordsData.DataStoreEntry entry = data.getData(player.getUUID());
        entry.cooldown = cooldown;
        data.setData(player.getUUID(), entry);
    }
}




