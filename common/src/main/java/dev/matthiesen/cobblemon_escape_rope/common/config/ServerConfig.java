package dev.matthiesen.cobblemon_escape_rope.common.config;

import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.ArrayList;
import java.util.List;

public final class ServerConfig {

    // General Config
    public ModConfigSpec.IntValue serverSaveTicks;

    // Escape Rope Item Config
    public ModConfigSpec.IntValue escaperope_cooldownSeconds;
    public ModConfigSpec.IntValue escaperope_useTimeSeconds;
    public ModConfigSpec.IntValue escaperope_safeSearchRadius;
    public ModConfigSpec.BooleanValue escaperope_consumeOnUse;
    public ModConfigSpec.ConfigValue<List<? extends String>> escaperope_blacklistedWorlds;

    public ServerConfig(ModConfigSpec.Builder builder) {
        builder.comment("General Configuration").push("generalConfig");
        serverSaveTicks = builder.comment("The number of ticks between server saves. Default is 20 ticks")
                .defineInRange("serverSaveTicks", 20, 1, Integer.MAX_VALUE);
        builder.pop();

        builder.comment("Escape Rope Item Configuration").push("escapeRopeItemConfig");
        escaperope_cooldownSeconds = builder.comment("The number of seconds before the Escape Rope can be used again after use. Default is 60 seconds.")
                .defineInRange("cooldownSeconds", 60, 1, Integer.MAX_VALUE);
        escaperope_useTimeSeconds = builder.comment("The number of seconds it takes to use the Escape Rope. Default is 5 seconds.")
                .defineInRange("useTimeSeconds", 5, 1, Integer.MAX_VALUE);
        escaperope_safeSearchRadius = builder.comment("The radius in blocks to search for a safe teleport location when using the Escape Rope. Default is 5 blocks.")
                .defineInRange("safeSearchRadius", 5, 1, Integer.MAX_VALUE);
        escaperope_consumeOnUse = builder.comment("Whether the Escape Rope item is consumed on use. Default is false.")
                .define("consumeOnUse", false);
        escaperope_blacklistedWorlds = builder.comment("List of dimension IDs where the Escape Rope cannot be used. Default is an empty list.")
                .defineListAllowEmpty("blacklistedWorlds", new ArrayList<>(), () -> "minecraft:the_end", o -> o instanceof String);

        builder.pop();
    }
}
