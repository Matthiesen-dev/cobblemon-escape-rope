package dev.matthiesen.common.cobblemon_escape_rope;

import dev.architectury.event.events.common.TickEvent;
import dev.matthiesen.common.cobblemon_escape_rope.config.ConfigManager;
import dev.matthiesen.common.cobblemon_escape_rope.config.ModConfig;
import dev.matthiesen.common.cobblemon_escape_rope.data.CoordsHelper;
import dev.matthiesen.common.cobblemon_escape_rope.data.PlayerCoordsData;
import dev.matthiesen.common.cobblemon_escape_rope.items.ModItems;
import dev.matthiesen.common.cobblemon_escape_rope.utils.DataUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.Locale;

public class CobblemonEscapeRope {
    public static ModConfig config;
    public static MinecraftServer currentServer;

    public static void initialize() {
        Constants.createInfoLog("Initialized");
        config = new ConfigManager().loadConfig();
        ModItems.init();
        registerEvents();
    }

    public static boolean isDimensionBlacklisted(String dimensionId) {
        if (dimensionId == null || dimensionId.isBlank() || config == null || config.escapeRopeItemConfig == null) {
            return false;
        }

        List<String> blacklist = config.escapeRopeItemConfig.blacklistedDimensions;
        if (blacklist == null || blacklist.isEmpty()) {
            return false;
        }

        String normalizedDimensionId = dimensionId.trim().toLowerCase(Locale.ROOT);
        return blacklist.stream()
                .filter(id -> id != null && !id.isBlank())
                .map(id -> id.trim().toLowerCase(Locale.ROOT))
                .anyMatch(normalizedDimensionId::equals);
    }

    public static void onStartup(MinecraftServer server) {
        Constants.createInfoLog("Server starting, Setting up");
        currentServer = server;
    }

    public static void registerEvents() {
        // Server tick: decrement stored cooldowns for all online players in memory,
        // then flush to disk every serverSaveTicks ticks (configurable).
        TickEvent.SERVER_POST.register(server -> {
            int saveTicks = config != null ? config.serverSaveTicks : 20;
            PlayerCoordsData coordsData = CoordsHelper.get(server);
            boolean anyChanged = false;

            for (ServerPlayer player : server.getPlayerList().getPlayers()) {
                PlayerCoordsData.DataStoreEntry data = coordsData.getData(player.getUUID());

                if (data.cooldown > 0) {
                    // Re-sync item cooldown if it was lost (e.g. after relog).
                    if (!player.getCooldowns().isOnCooldown(ModItems.ESCAPE_ROPE.get())) {
                        player.getCooldowns().addCooldown(ModItems.ESCAPE_ROPE.get(), data.cooldown);
                    }
                    data.cooldown--;
                    coordsData.setDataInMemory(player.getUUID(), data);
                    anyChanged = true;
                }
            }

            // Flush in-memory changes to disk on the configured interval.
            if (anyChanged && server.getTickCount() % saveTicks == 0) {
                coordsData.setDirty();
            }
        });

        // Player tick: update the saved "last safe outdoor position" every second, in memory only.
        // The server tick above handles periodic flushing to disk.
        TickEvent.PLAYER_POST.register(player -> {
            if (player.level().isClientSide || player.tickCount % 20 != 0) return;

            ServerPlayer serverPlayer = (ServerPlayer) player;
            Level level = player.level();
            String currentDim = level.dimension().location().toString();

            if (level.dimensionType().hasSkyLight()
                    && level.canSeeSky(player.blockPosition())
                    && !isDimensionBlacklisted(currentDim)) {
                BlockPos currentPos = player.blockPosition();
                PlayerCoordsData.DataStoreEntry data = DataUtil.getSavedPlayerData(serverPlayer);
                if (!currentPos.equals(data.pos) || !currentDim.equals(data.dimension)) {
                    data.pos = currentPos;
                    data.dimension = currentDim;
                    DataUtil.setPlayerDataInMemory(serverPlayer, data);
                }
            }
        });
    }

    public static void onShutdown() {
        Constants.createInfoLog("Server stopping, shutting down");
        if (currentServer != null) {
            CoordsHelper.get(currentServer).setDirty();
        }
        new ConfigManager().saveConfig();
    }
}


