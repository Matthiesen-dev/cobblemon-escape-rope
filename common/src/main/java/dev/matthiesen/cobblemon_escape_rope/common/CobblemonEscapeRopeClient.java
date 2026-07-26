package dev.matthiesen.cobblemon_escape_rope.common;

import dev.matthiesen.matthiesen_core.common.AbstractCommonClientMod;
import dev.matthiesen.matthiesen_core.common.api.events.PlatformClientEvents;
import dev.matthiesen.matthiesen_core.common.api.platform.registry.ResourcePackActivationBehaviour;
import dev.matthiesen.matthiesen_core.common.api.platform.registry.ResourcePackDef;

public final class CobblemonEscapeRopeClient extends AbstractCommonClientMod {
    public static final CobblemonEscapeRopeClient INSTANCE = new CobblemonEscapeRopeClient();

    public static final ResourcePackDef legacyItemModelResources =
            new ResourcePackDef(
                    CobblemonEscapeRope.MOD_ID,
                    "legacyitemmodel",
                    "Legacy Item Model",
                    ResourcePackActivationBehaviour.NORMAL
            );

    public CobblemonEscapeRopeClient() {
        super(CobblemonEscapeRope.INSTANCE);
    }

    @Override
    public void initialize() {
        PlatformClientEvents.registerResourcePack(legacyItemModelResources);
    }
}
