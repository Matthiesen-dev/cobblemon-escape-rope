package dev.matthiesen.cobblemon_escape_rope.common;

import dev.matthiesen.cobblemon_escape_rope.common.config.EscapeRopeServerConfig;
import dev.matthiesen.cobblemon_escape_rope.common.data.PlayerCoordsData;
import dev.matthiesen.cobblemon_escape_rope.common.registry.CreativeTabRegistry;
import dev.matthiesen.cobblemon_escape_rope.common.registry.ItemRegistry;
import dev.matthiesen.libs.faststats.Token;
import dev.matthiesen.matthiesen_core.common.AbstractCommonMod;
import dev.matthiesen.matthiesen_core.common.api.events.PlatformEvents;
import dev.matthiesen.matthiesen_core.common.utility.config.ConfigManager;
import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Locale;

public final class CobblemonEscapeRope extends AbstractCommonMod {
    public static final String MOD_ID = "cobblemon_escape_rope";
    public static final String MOD_NAME = "Cobblemon Escape Rope";
    public static @Token final String METRICS_TOKEN = "bb6f2545505c4c133b504e1db61c184e";

    public static final CobblemonEscapeRope INSTANCE = new CobblemonEscapeRope();

    private static final ConfigManager<EscapeRopeServerConfig> SERVER_CONFIG_MANAGER =
            new ConfigManager<>(EscapeRopeServerConfig.class, "server", MOD_ID);

    public CobblemonEscapeRope() {
        super(MOD_ID, MOD_NAME);
    }

    @Override
    public void initialize() {
        super.initialize();
        SERVER_CONFIG_MANAGER.loadConfig();

        ItemRegistry.init();
        CreativeTabRegistry.init();

        registerEventSubscriptions();
    }

    @Override
    public @NotNull @Token String getMetricsToken() {
        return METRICS_TOKEN;
    }

    public EscapeRopeServerConfig getServerConfig() {
        return SERVER_CONFIG_MANAGER.getConfig();
    }

    public void registerEventSubscriptions() {
        PlatformEvents.SERVER_RELOAD.subscribe(event -> {
            SERVER_CONFIG_MANAGER.loadConfig();
            createInfoLog("Server config reloaded");
        });

        PlatformEvents.SERVER_STOPPING.subscribe(event ->
                PlayerCoordsData.getCoordsData().setDirty());

        PlatformEvents.SERVER_END_TICK.subscribe(event -> {
            MinecraftServer server = event.server();
            var config = INSTANCE.getServerConfig();
            int saveTicks = config.serverSaveTicks;

            PlayerCoordsData coordsData = PlayerCoordsData.getCoordsData();
            boolean anyChanged = false;

            for (ServerPlayer player : server.getPlayerList().getPlayers()) {
                PlayerCoordsData.DataStoreEntry data = coordsData.getData(player.getUUID());

                if (data.cooldown > 0) {
                    // Re-sync item cooldown if it was lost (e.g. after relog).
                    if (!player.getCooldowns().isOnCooldown(ItemRegistry.ESCAPE_ROPE.get())) {
                        player.getCooldowns().addCooldown(ItemRegistry.ESCAPE_ROPE.get(), data.cooldown);
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

         PlatformEvents.PLAYER_END_TICK.subscribe(event -> {
             ServerPlayer player = event.player();
             Level playerLevel = player.level();
             String currentDimension = playerLevel.dimension().location().toString();

             if (playerLevel.isClientSide || player.tickCount % 20 != 0 || !playerLevel.dimensionType().hasSkyLight()) return;

             if (playerLevel.canSeeSky(player.blockPosition()) && !INSTANCE.isDimensionBlacklisted(currentDimension)) {
                 BlockPos currentPos = player.blockPosition();
                 PlayerCoordsData.DataStoreEntry data = PlayerCoordsData.getSavedPlayerData(player);

                 if (!currentPos.equals(data.pos) || !currentDimension.equals(data.dimension)) {
                     data.pos = currentPos;
                     data.dimension = currentDimension;
                     PlayerCoordsData.setPlayerDataInMemory(player, data);
                 }
             }
         });
    }

    public boolean isDimensionBlacklisted(String dimensionId) {
        var config = this.getServerConfig();
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
