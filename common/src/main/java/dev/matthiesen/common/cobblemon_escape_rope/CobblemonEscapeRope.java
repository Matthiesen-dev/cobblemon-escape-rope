package dev.matthiesen.common.cobblemon_escape_rope;

import dev.architectury.event.events.common.TickEvent;
import dev.matthiesen.common.cobblemon_escape_rope.config.ConfigManager;
import dev.matthiesen.common.cobblemon_escape_rope.config.ModConfig;
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

        TickEvent.PLAYER_POST.register(player -> {
            if (!player.level().isClientSide) {
                ServerPlayer serverPlayer = (ServerPlayer) player;
                PlayerCoordsData.DataStoreEntry data = DataUtil.getSavedPlayerData(serverPlayer);
                BlockPos pos = data.pos;
                String dimension = data.dimension;

                if (data.cooldown > 0) {
                    int remainingTicks = data.cooldown;
                    if (!player.getCooldowns().isOnCooldown(ModItems.ESCAPE_ROPE.get())) {
                        player.getCooldowns().addCooldown(ModItems.ESCAPE_ROPE.get(), remainingTicks);
                    }
                    data.cooldown--;
                }

                if (player.tickCount % 20 == 0) {
                    Level level = player.level();
                    String currentDim = level.dimension().location().toString();
                    if (level.dimensionType().hasSkyLight() && level.canSeeSky(player.blockPosition()) && !isDimensionBlacklisted(currentDim)) {
                        pos = player.blockPosition();
                        dimension = currentDim;
                    }
                }

                DataUtil.setPlayerData(
                        serverPlayer,
                        new PlayerCoordsData.DataStoreEntry(
                                pos,
                                data.cooldown,
                                dimension
                        )
                );
            }
        });
    }

    public static void onShutdown() {
        Constants.createInfoLog("Server stopping, shutting down");
        new ConfigManager().saveConfig();
    }
}
