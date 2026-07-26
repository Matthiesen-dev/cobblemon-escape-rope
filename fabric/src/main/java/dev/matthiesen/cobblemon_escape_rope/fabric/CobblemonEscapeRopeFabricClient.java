package dev.matthiesen.cobblemon_escape_rope.fabric;

import dev.matthiesen.cobblemon_escape_rope.common.CobblemonEscapeRopeClient;
import net.fabricmc.api.ClientModInitializer;

public final class CobblemonEscapeRopeFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        var instance = CobblemonEscapeRopeClient.INSTANCE;
        instance.initialize();
    }
}
