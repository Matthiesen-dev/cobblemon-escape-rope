package dev.matthiesen.neoforge.cobblemon_escape_rope;

import dev.matthiesen.common.cobblemon_escape_rope.CobblemonEscapeRope;
import dev.matthiesen.common.cobblemon_escape_rope.Constants;
import net.neoforged.fml.common.Mod;

@Mod(Constants.MOD_ID)
public class CobblemonEscapeRopeNeoForge {
    public CobblemonEscapeRopeNeoForge() {
        Constants.createInfoLog("Loading for NeoForge Mod Loader");
        CobblemonEscapeRope.initialize();
    }
}
