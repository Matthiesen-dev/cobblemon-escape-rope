package dev.matthiesen.common.cobblemon_escape_rope;

import dev.matthiesen.common.cobblemon_escape_rope.config.CobblemonEscapeRopeConfig;
import dev.matthiesen.common.cobblemon_escape_rope.config.EscapeRopeConfigManager;
import dev.matthiesen.common.cobblemon_escape_rope.event_handlers.CobblemonPlatformEvents;
import dev.matthiesen.common.cobblemon_escape_rope.event_handlers.ServerEvents;
import dev.matthiesen.common.cobblemon_escape_rope.registry.CreativeTabRegistry;
import dev.matthiesen.common.cobblemon_escape_rope.registry.ItemRegistry;
import dev.matthiesen.common.cobblemon_escape_rope.utils.MetricManager;
import dev.matthiesen.common.matthiesen_lib.MatthiesenLib;

import java.util.List;
import java.util.Locale;

public class CobblemonEscapeRope {
    private static final EscapeRopeConfigManager<CobblemonEscapeRopeConfig> CONFIG_MANAGER =
            new EscapeRopeConfigManager<>(CobblemonEscapeRopeConfig.class, "config");

    public static CobblemonEscapeRopeConfig getConfig() {
        return CONFIG_MANAGER.getConfig();
    }

    public static void initialize() {
        reload();
        MetricManager.ready();
        ItemRegistry.init();
        CreativeTabRegistry.init();
        MatthiesenLib.registerServerEventHandler(Constants.MOD_ID, new ServerEvents());
        MatthiesenLib.registerReloadRunnable(Constants.MOD_ID, CobblemonEscapeRope::reload);
        CobblemonPlatformEvents.init();
        Constants.createInfoLog("Initialized");
    }

    public static void reload() {
        CONFIG_MANAGER.loadConfig();
    }

    public static boolean isDimensionBlacklisted(String dimensionId) {
        var config = getConfig();
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
}


