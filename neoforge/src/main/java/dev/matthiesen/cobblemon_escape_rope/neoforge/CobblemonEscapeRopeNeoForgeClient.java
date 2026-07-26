package dev.matthiesen.cobblemon_escape_rope.neoforge;

import dev.matthiesen.cobblemon_escape_rope.common.CobblemonEscapeRope;
import dev.matthiesen.cobblemon_escape_rope.common.CobblemonEscapeRopeClient;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.Mod;

@Mod(value = CobblemonEscapeRope.MOD_ID, dist = Dist.CLIENT)
public final class CobblemonEscapeRopeNeoForgeClient {
    public CobblemonEscapeRopeNeoForgeClient() {
        var instance = CobblemonEscapeRopeClient.INSTANCE;
        instance.initialize();
    }
}
