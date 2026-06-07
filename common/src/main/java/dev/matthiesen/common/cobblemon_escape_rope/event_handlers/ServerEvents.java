package dev.matthiesen.common.cobblemon_escape_rope.event_handlers;

import dev.matthiesen.common.cobblemon_escape_rope.Constants;
import dev.matthiesen.common.cobblemon_escape_rope.utils.DataUtil;
import dev.matthiesen.common.matthiesen_lib_api.core.interfaces.MatthiesenLibServerEventHandler;
import net.minecraft.server.MinecraftServer;

public final class ServerEvents implements MatthiesenLibServerEventHandler {
    @Override
    public void onServerStart(MinecraftServer server) {
        Constants.createInfoLog("Server starting, Setting up");
    }

    @Override
    public void onServerStop(MinecraftServer server) {
        DataUtil.getCoordsData(server).setDirty();
        CobblemonPlatformEvents.teardown();
    }
}
