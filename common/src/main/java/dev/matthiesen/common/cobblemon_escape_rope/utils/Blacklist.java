package dev.matthiesen.common.cobblemon_escape_rope.utils;

import dev.matthiesen.common.cobblemon_escape_rope.CobblemonEscapeRope;

import java.util.List;
import java.util.Locale;

public final class Blacklist {
    public static boolean isDimensionBlacklisted(String dimensionId) {
        var config = CobblemonEscapeRope.getConfig();
        if (dimensionId == null || dimensionId.isBlank() || config == null || config.escapeRopeItemConfig == null) {
            return false;
        }

        List<String> blacklist = config.escapeRopeItemConfig.blacklistedDimensions;
        if (blacklist == null || blacklist.isEmpty()) {
            return false;
        }

        String normalizedDimensionId = dimensionId.trim().toLowerCase(Locale.ROOT);
        return blacklist.stream()
                .filter(id -> id != null && !id.isBlank())
                .map(id -> id.trim().toLowerCase(Locale.ROOT))
                .anyMatch(normalizedDimensionId::equals);
    }
}
