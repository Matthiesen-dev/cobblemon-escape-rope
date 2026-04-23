package dev.matthiesen.common.cobblemon_escape_rope.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class CobblemonEscapeRopeConfig {
    @SerializedName("serverSaveTicks")
    public int serverSaveTicks = 20;

    @SerializedName("escapeRopeItem")
    public EscapeRopeItemConfig escapeRopeItemConfig = new EscapeRopeItemConfig();

    public static class EscapeRopeItemConfig {
        @SerializedName("cooldownInSeconds")
        public int cooldownInSeconds = 300; // 5 minutes

        @SerializedName("consumeOnUse")
        public boolean consumeOnUse = true;

        @SerializedName("useTimeInSeconds")
        public int useTimeInSeconds = 3;

        @SerializedName("teleportSafeSearchRadius")
        public int teleportSafeSearchRadius = 2;

        @SerializedName("blacklistedDimensions")
        public List<String> blacklistedDimensions = new ArrayList<>();
    }

    public static final Gson GSON = new GsonBuilder()
            .disableHtmlEscaping()
            .setPrettyPrinting()
            .create();
}
