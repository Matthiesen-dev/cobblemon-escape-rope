package dev.matthiesen.cobblemon_escape_rope.fabric;

import dev.matthiesen.cobblemon_escape_rope.common.CobblemonEscapeRope;
import net.fabricmc.api.ModInitializer;

public final class CobblemonEscapeRopeFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        var instance = CobblemonEscapeRope.INSTANCE;
        instance.createInfoLog("Loading for Fabric Mod Loader");
        instance.initialize();
    }

}
