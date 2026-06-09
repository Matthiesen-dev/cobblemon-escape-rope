package dev.matthiesen.common.cobblemon_escape_rope;

import com.cobblemon.mod.common.ResourcePackActivationBehaviour;
import dev.matthiesen.common.cobblemon_escape_rope.config.CobblemonEscapeRopeConfig;
import dev.matthiesen.common.cobblemon_escape_rope.config.EscapeRopeConfigManager;
import dev.matthiesen.common.cobblemon_escape_rope.event_handlers.CobblemonPlatformEvents;
import dev.matthiesen.common.cobblemon_escape_rope.event_handlers.ServerEvents;
import dev.matthiesen.common.cobblemon_escape_rope.registry.CreativeTabRegistry;
import dev.matthiesen.common.cobblemon_escape_rope.registry.ItemRegistry;
import dev.matthiesen.common.cobblemon_escape_rope.utils.MetricManager;
import dev.matthiesen.common.cobblemon_escape_rope.utils.ResourcePackDef;
import dev.matthiesen.common.matthiesen_lib.MatthiesenLib;
import net.minecraft.server.packs.PackType;

import java.util.List;

public final class CobblemonEscapeRope {
    private static final EscapeRopeConfigManager<CobblemonEscapeRopeConfig> CONFIG_MANAGER =
            new EscapeRopeConfigManager<>(CobblemonEscapeRopeConfig.class, "config");

    public static CobblemonEscapeRopeConfig getConfig() {
        return CONFIG_MANAGER.getConfig();
    }

    public static final List<ResourcePackDef> BuiltInResourcePacks = List.of(
            new ResourcePackDef("legacyitemmodel", "Legacy Item Model", PackType.CLIENT_RESOURCES, ResourcePackActivationBehaviour.NORMAL)
    );

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
}


