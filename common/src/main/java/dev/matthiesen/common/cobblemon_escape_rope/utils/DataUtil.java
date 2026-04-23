package dev.matthiesen.common.cobblemon_escape_rope.utils;

import dev.matthiesen.common.cobblemon_escape_rope.data.CoordsHelper;
import dev.matthiesen.common.cobblemon_escape_rope.data.PlayerCoordsData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

public final class DataUtil {
    public static PlayerCoordsData.DataStoreEntry getSavedPlayerData(ServerPlayer player) {
        ServerLevel level = player.serverLevel();
        PlayerCoordsData data = CoordsHelper.get(level);
        return data.getData(player.getUUID());
    }

    public static void setPlayerData(ServerPlayer player, PlayerCoordsData.DataStoreEntry newData) {
        ServerLevel level = player.serverLevel();
        PlayerCoordsData data = CoordsHelper.get(level);
        data.setData(player.getUUID(), newData);
    }

    public static void setCooldown(ServerPlayer player, int cooldown) {
        ServerLevel level = player.serverLevel();
        PlayerCoordsData data = CoordsHelper.get(level);
        PlayerCoordsData.DataStoreEntry entry = data.getData(player.getUUID());
        data.setData(player.getUUID(), new PlayerCoordsData.DataStoreEntry(
                entry.pos,
                cooldown,
                entry.dimension
        ));
    }
}
