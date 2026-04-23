package dev.matthiesen.fabric.cobblemon_escape_rope;

import dev.matthiesen.common.cobblemon_escape_rope.CobblemonEscapeRope;
import dev.matthiesen.common.cobblemon_escape_rope.Constants;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;

public class CobblemonEscapeRopeFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        Constants.createInfoLog("Loading for Fabric Mod Loader");
        CobblemonEscapeRope.initialize();
        ServerLifecycleEvents.SERVER_STARTING.register(server -> {
            MinecraftServer runningServer = server.createCommandSourceStack().getServer();
            CobblemonEscapeRope.onStartup(runningServer);
        });
        ServerLifecycleEvents.SERVER_STOPPING.register(server -> CobblemonEscapeRope.onShutdown());
    }

}
