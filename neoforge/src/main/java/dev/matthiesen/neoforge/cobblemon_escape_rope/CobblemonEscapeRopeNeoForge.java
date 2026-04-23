package dev.matthiesen.neoforge.cobblemon_escape_rope;

import dev.matthiesen.common.cobblemon_escape_rope.CobblemonEscapeRope;
import dev.matthiesen.common.cobblemon_escape_rope.Constants;
import net.minecraft.server.MinecraftServer;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.event.server.ServerStoppingEvent;

@Mod(Constants.MOD_ID)
public class CobblemonEscapeRopeNeoForge {
    public CobblemonEscapeRopeNeoForge(IEventBus modBus) {
        Constants.createInfoLog("Loading for NeoForge Mod Loader");
        CobblemonEscapeRope.initialize();
        NeoForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        MinecraftServer server = event.getServer();
        CobblemonEscapeRope.onStartup(server);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onServerStopping(ServerStoppingEvent event) {
        CobblemonEscapeRope.onShutdown();
    }
}
