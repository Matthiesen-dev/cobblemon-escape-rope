package dev.matthiesen.common.cobblemon_escape_rope.interfaces;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.matthiesen.common.cobblemon_escape_rope.config.ModConfig;

public interface IConfigManager {
    ModConfig loadConfig();
    JsonElement mergeConfigs(JsonObject defaultConfig, JsonObject fileConfig);
    void saveConfig();
}
