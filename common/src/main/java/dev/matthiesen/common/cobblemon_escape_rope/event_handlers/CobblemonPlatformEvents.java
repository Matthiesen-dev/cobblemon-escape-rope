package dev.matthiesen.common.cobblemon_escape_rope.event_handlers;

import com.cobblemon.mod.common.api.Priority;
import com.cobblemon.mod.common.api.reactive.ObservableSubscription;
import com.cobblemon.mod.common.platform.events.PlatformEvents;
import com.cobblemon.mod.common.platform.events.ServerPlayerTickEvent;
import com.cobblemon.mod.common.platform.events.ServerTickEvent;
import dev.matthiesen.common.cobblemon_escape_rope.CobblemonEscapeRope;
import dev.matthiesen.common.cobblemon_escape_rope.data.PlayerCoordsData;
import dev.matthiesen.common.cobblemon_escape_rope.items.ItemRegistry;
import dev.matthiesen.common.cobblemon_escape_rope.utils.DataUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;

public class CobblemonPlatformEvents {
    private static ObservableSubscription<ServerTickEvent.Post> serverTickSubscription;
    private static ObservableSubscription<ServerPlayerTickEvent.Post> playerTickSubscription;

    public static void init() {
        // Server tick: decrement stored cooldowns for all online players in memory,
        // then flush to disk every serverSaveTicks ticks (configurable).
        serverTickSubscription = PlatformEvents.SERVER_TICK_POST.subscribe(Priority.NORMAL, event -> {
            MinecraftServer server = event.getServer();
            var config = CobblemonEscapeRope.getConfig();
            int saveTicks = config != null ? config.serverSaveTicks : 20;
            PlayerCoordsData coordsData = DataUtil.getCoordsData(server);
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

        // Player tick: update the saved "last safe outdoor position" every second, in memory only.
        // The server tick above handles periodic flushing to disk.
        playerTickSubscription = PlatformEvents.SERVER_PLAYER_TICK_POST.subscribe(Priority.NORMAL, event -> {
            ServerPlayer player = event.getPlayer();
            Level playerLevel = player.level();
            if (playerLevel.isClientSide || player.tickCount % 20 != 0) return;
            String currentDim = playerLevel.dimension().location().toString();

            if (playerLevel.dimensionType().hasSkyLight()
                    && playerLevel.canSeeSky(player.blockPosition())
                    && !CobblemonEscapeRope.isDimensionBlacklisted(currentDim)) {
                BlockPos currentPos = player.blockPosition();
                PlayerCoordsData.DataStoreEntry data = DataUtil.getSavedPlayerData(player);
                if (!currentPos.equals(data.pos) || !currentDim.equals(data.dimension)) {
                    data.pos = currentPos;
                    data.dimension = currentDim;
                    DataUtil.setPlayerDataInMemory(player, data);
                }
            }
        });
    }

    public static void teardown() {
        if (serverTickSubscription != null) {
            serverTickSubscription.unsubscribe();
            serverTickSubscription = null;
        }
        if (playerTickSubscription != null) {
            playerTickSubscription.unsubscribe();
            playerTickSubscription = null;
        }
    }
}
