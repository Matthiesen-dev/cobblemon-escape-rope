package dev.matthiesen.fabric.cobblemon_escape_rope;

import dev.matthiesen.common.cobblemon_escape_rope.CommonModExample;
import dev.matthiesen.common.cobblemon_escape_rope.Constants;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;

public class FabricModExample implements ModInitializer {

    @Override
    public void onInitialize() {
        Constants.createInfoLog("Loading for Fabric Mod Loader");
        CommonModExample.initialize();
        CommandRegistrationCallback.EVENT.register(CommonModExample::registerCommands);
        ServerLifecycleEvents.SERVER_STARTING.register(server -> {
            MinecraftServer runningServer = server.createCommandSourceStack().getServer();
            CommonModExample.onStartup(runningServer);
        });
        ServerLifecycleEvents.SERVER_STOPPING.register(server -> CommonModExample.onShutdown());
    }

}
