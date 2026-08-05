package dev.matthiesen.cobblemon_escape_rope.common;

import dev.matthiesen.cobblemon_escape_rope.common.config.EscapeRopeConfig;
import dev.matthiesen.cobblemon_escape_rope.common.data.PlayerCoordsData;
import dev.matthiesen.cobblemon_escape_rope.common.registry.CreativeTabRegistry;
import dev.matthiesen.cobblemon_escape_rope.common.registry.ItemRegistry;
import dev.matthiesen.libs.faststats.Token;
import dev.matthiesen.matthiesen_core.common.AbstractCommonMod;
import dev.matthiesen.matthiesen_core.common.api.events.PlatformEvents;
import dev.matthiesen.matthiesen_core.common.api.events.server.PlayerEvent;
import dev.matthiesen.matthiesen_core.common.api.events.server.ServerEvent;
import dev.matthiesen.matthiesen_core.common.api.platform.loader.ModConfigType;
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

    public CobblemonEscapeRope() {
        super(MOD_ID, MOD_NAME);
    }

    @Override
    public void initialize() {
        super.initialize();
        registerModConfig(MOD_ID, ModConfigType.SERVER, EscapeRopeConfig.SERVER_SPEC, "cobblemon_escape_rope/server.toml");

        ItemRegistry.initialize();
        CreativeTabRegistry.initialize();

        PlatformEvents.SERVER_STOPPING.subscribe(this::onServerStopping);
        PlatformEvents.SERVER_END_TICK.subscribe(this::onServerEndTick);
        PlatformEvents.PLAYER_END_TICK.subscribe(this::onPlayerEndTick);

        createInfoLog("Initialized Cobblemon Escape Rope");
    }

    @Override
    public @NotNull @Token String getMetricsToken() {
        return METRICS_TOKEN;
    }

    public boolean isDimensionBlacklisted(String dimensionId) {
        if (dimensionId == null || dimensionId.isBlank()) {
            return false;
        }

        List<? extends String> blacklist = EscapeRopeConfig.SERVER_CONFIG.escaperope_blacklistedWorlds.get();
        if (blacklist == null || blacklist.isEmpty()) {
            return false;
        }

        String normalizedDimensionId = dimensionId.trim().toLowerCase(Locale.ROOT);
        return blacklist.stream()
                .filter(id -> id != null && !id.isBlank())
                .map(id -> id.trim().toLowerCase(Locale.ROOT))
                .anyMatch(normalizedDimensionId::equals);
    }

    public void onServerStopping(ServerEvent.Stopping event) {
        PlayerCoordsData.getCoordsData().setDirty();
    }

    public void onServerEndTick(ServerEvent.EndTick event) {
        MinecraftServer server = event.server();

        PlayerCoordsData coordsData = PlayerCoordsData.getCoordsData();
        boolean anyChanged = false;

        for (ServerPlayer player : server.getPlayerList().getPlayers()) {
            PlayerCoordsData.DataStoreEntry data = coordsData.getData(player.getUUID());

            if (data.cooldown > 0) {
                data.cooldown--;
                // Re-sync item cooldown if it was lost (e.g. after relog).
                if (!player.getCooldowns().isOnCooldown(ItemRegistry.ESCAPE_ROPE.get())) {
                    player.getCooldowns().addCooldown(ItemRegistry.ESCAPE_ROPE.get(), data.cooldown);
                }
                coordsData.setDataInMemory(player.getUUID(), data);
                anyChanged = true;
            }
        }

        // Flush in-memory changes to disk on the configured interval.
        int saveTicks = EscapeRopeConfig.SERVER_CONFIG.serverSaveTicks.getAsInt();
        if (anyChanged && server.getTickCount() % saveTicks == 0) {
            coordsData.setDirty();
        }
    }

    public void onPlayerEndTick(PlayerEvent.EndTick event) {
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
    }
}
