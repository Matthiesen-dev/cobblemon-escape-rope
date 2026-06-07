package dev.matthiesen.common.cobblemon_escape_rope.config;

import dev.matthiesen.common.cobblemon_escape_rope.Constants;
import dev.matthiesen.common.matthiesen_lib_api.config.ConfigManager;

public class EscapeRopeConfigManager<T> extends ConfigManager<T> {
    public EscapeRopeConfigManager(Class<T> configClass, String configName) {
        super(configClass, configName, Constants.MOD_ID);
    }
}
