package dev.matthiesen.common.cobblemon_escape_rope.config;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dev.matthiesen.common.cobblemon_escape_rope.Constants;
import dev.matthiesen.common.cobblemon_escape_rope.interfaces.IConfigManager;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class ConfigManager implements IConfigManager {
    public static CobblemonEscapeRopeConfig config;

    @Override
    public CobblemonEscapeRopeConfig loadConfig() {
        String configFileLoc = System.getProperty("user.dir") + File.separator + "config" +
                File.separator + Constants.MOD_ID + File.separator + "config.json";
        Constants.createInfoLog("Loading config file found at: " + configFileLoc);
        File configFile = new File(configFileLoc);
        boolean madeDir = configFile.getParentFile().mkdirs();

        if (madeDir) {
            Constants.createInfoLog("Config Directory exists");
        }

        // Check config existence and load if it exists, otherwise create default.
        if (configFile.exists()) {
            try {
                FileReader fileReader = new FileReader(configFile);

                // Create a default config instance
                CobblemonEscapeRopeConfig defaultConfig = new CobblemonEscapeRopeConfig();
                String defaultConfigJson = CobblemonEscapeRopeConfig.GSON.toJson(defaultConfig);

                JsonElement fileConfigElement = JsonParser.parseReader(fileReader);

                // Convert default config JSON string to JsonElement
                JsonElement defaultConfigElement = JsonParser.parseString(defaultConfigJson);

                // Merge default config with the file config
                JsonElement mergedConfigElement = mergeConfigs(
                        defaultConfigElement.getAsJsonObject(),
                        fileConfigElement.getAsJsonObject()
                );

                // Deserialize the merged JsonElement back to PokemonToItemConfig
                CobblemonEscapeRopeConfig finalConfig;
                finalConfig = CobblemonEscapeRopeConfig.GSON.fromJson(
                        mergedConfigElement,
                        CobblemonEscapeRopeConfig.class
                );

                config = finalConfig;

                fileReader.close();
            } catch (Exception e) {
                Constants.createErrorLog("Failed to load the config! Using default config as fallback");
                e.printStackTrace();
                config = new CobblemonEscapeRopeConfig();
            }
        } else {
            config = new CobblemonEscapeRopeConfig();
        }

        saveConfig();

        return config;
    }

    @Override
    public JsonElement mergeConfigs(JsonObject defaultConfig, JsonObject fileConfig) {
        // For every entry in the default config, check if it exists in the file config
        Constants.createInfoLog("Checking for config merge.");
        boolean merged = false;

        for (String key : defaultConfig.keySet()) {
            if (!fileConfig.has(key)) {
                // If the file config does not have the key, add it from the default config
                fileConfig.add(key, defaultConfig.get(key));
                Constants.createInfoLog(key + " not found in file config, adding from default.");
                merged = true;
            } else {
                // If it's a nested object, recursively merge it
                if (defaultConfig.get(key).isJsonObject() && fileConfig.get(key).isJsonObject()) {
                    mergeConfigs(defaultConfig.getAsJsonObject(key), fileConfig.getAsJsonObject(key));
                }
            }
        }

        if (merged) {
            Constants.createInfoLog("Successfully merged config.");
        }

        return fileConfig;
    }

    @Override
    public void saveConfig() {
        try {
            String configFileLoc = System.getProperty("user.dir") + File.separator + "config" +
                    File.separator + Constants.MOD_ID + File.separator + "config.json";
            Constants.createInfoLog("Saving config to: " + configFileLoc);
            File configFile = new File(configFileLoc);
            FileWriter fileWriter = new FileWriter(configFile);
            CobblemonEscapeRopeConfig.GSON.toJson(config, fileWriter);
            fileWriter.flush();
            fileWriter.close();
        } catch (Exception e) {
            Constants.createErrorLog("Failed to save config");
            e.printStackTrace();
        }
    }
}
