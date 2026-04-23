package dev.matthiesen.common.cobblemon_escape_rope.utils;

import dev.matthiesen.common.cobblemon_escape_rope.CobblemonEscapeRope;
import dev.matthiesen.common.cobblemon_escape_rope.data.CoordsHelper;
import dev.matthiesen.common.cobblemon_escape_rope.data.PlayerCoordsData;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

public final class DataUtil {
    private static MinecraftServer server(ServerPlayer player) {
        MinecraftServer s = player.getServer();
        return s != null ? s : CobblemonEscapeRope.currentServer;
    }

    public static PlayerCoordsData.DataStoreEntry getSavedPlayerData(ServerPlayer player) {
        return CoordsHelper.get(server(player)).getData(player.getUUID());
    }

    public static void setPlayerDataInMemory(ServerPlayer player, PlayerCoordsData.DataStoreEntry newData) {
        CoordsHelper.get(server(player)).setDataInMemory(player.getUUID(), newData);
    }

    public static void setCooldown(ServerPlayer player, int cooldown) {
        PlayerCoordsData data = CoordsHelper.get(server(player));
        PlayerCoordsData.DataStoreEntry entry = data.getData(player.getUUID());
        entry.cooldown = cooldown;
        data.setData(player.getUUID(), entry);
    }
}




