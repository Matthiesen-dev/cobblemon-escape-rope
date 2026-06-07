package dev.matthiesen.fabric.cobblemon_escape_rope;

import dev.matthiesen.common.cobblemon_escape_rope.CobblemonEscapeRope;
import dev.matthiesen.common.cobblemon_escape_rope.Constants;
import net.fabricmc.api.ModInitializer;

public class CobblemonEscapeRopeFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        Constants.createInfoLog("Loading for Fabric Mod Loader");
        CobblemonEscapeRope.initialize();
    }

}
