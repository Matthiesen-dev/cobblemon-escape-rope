package dev.matthiesen.cobblemon_escape_rope.neoforge;

import dev.matthiesen.cobblemon_escape_rope.common.CobblemonEscapeRope;
import net.neoforged.fml.common.Mod;

@Mod(CobblemonEscapeRope.MOD_ID)
public final class CobblemonEscapeRopeNeoForge {
    public CobblemonEscapeRopeNeoForge() {
        var instance = CobblemonEscapeRope.INSTANCE;
        instance.createInfoLog("Loading for NeoForge Mod Loader");
        instance.initialize();
    }
}
