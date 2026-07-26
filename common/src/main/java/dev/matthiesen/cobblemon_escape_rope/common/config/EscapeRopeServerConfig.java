package dev.matthiesen.cobblemon_escape_rope.common.config;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public final class EscapeRopeServerConfig {
    @SerializedName("serverSaveTicks")
    public int serverSaveTicks = 20;

    @SerializedName("escapeRopeItem")
    public EscapeRopeItemConfig escapeRopeItemConfig = new EscapeRopeItemConfig();

    public static class EscapeRopeItemConfig {
        @SerializedName("cooldownInSeconds")
        public int cooldownInSeconds = 300; // 5 minutes

        @SerializedName("consumeOnUse")
        public boolean consumeOnUse = false;

        @SerializedName("useTimeInSeconds")
        public int useTimeInSeconds = 3;

        @SerializedName("teleportSafeSearchRadius")
        public int teleportSafeSearchRadius = 5;

        @SerializedName("blacklistedDimensions")
        public List<String> blacklistedDimensions = new ArrayList<>();
    }
}
