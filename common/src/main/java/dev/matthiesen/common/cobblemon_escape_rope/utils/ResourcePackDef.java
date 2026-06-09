package dev.matthiesen.common.cobblemon_escape_rope.utils;

import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.PackType;

public record ResourcePackDef(String id, Component displayName, PackType packType,
                              ResourcePackActivationBehaviour activationBehaviour) {
    public ResourcePackDef(
            String id,
            String displayName,
            PackType packType,
            ResourcePackActivationBehaviour activationBehaviour
    ) {
        this(id, Component.literal(displayName), packType, activationBehaviour);
    }


    public enum ResourcePackActivationBehaviour {

        /**
         * The resource pack will start disabled.
         */
        NORMAL,

        /**
         * The resource pack will start enabled.
         */
        DEFAULT_ENABLED,

        /**
         * The resource pack will always be enabled.
         * The user can reorder it but cannot remove it.
         */
        ALWAYS_ENABLED
    }
}