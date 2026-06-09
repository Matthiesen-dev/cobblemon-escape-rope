package dev.matthiesen.common.cobblemon_escape_rope;

import dev.matthiesen.common.cobblemon_escape_rope.utils.MetricManager;
import dev.matthiesen.libs.faststats.Token;
import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@SuppressWarnings("unused")
public final class Constants {
    public static final String MOD_ID = "cobblemon_escape_rope";
    public static final String ModName = "Cobblemon Escape Rope";
    public static @Token final String METRICS_TOKEN = "bb6f2545505c4c133b504e1db61c184e";

    public static ResourceLocation modResource(String id) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, id);
    }

    public static Logger LOGGER = LogManager.getLogger(ModName);

    public static void createInfoLog(String message) {
        LOGGER.info(message);
    }

    public static void createErrorLog(String message) {
        LOGGER.error(message);
    }

    public static void createErrorLog(String message, Throwable throwable) {
        MetricManager.ERROR_TRACKER.trackError(throwable);
        LOGGER.error(message, throwable);
    }
}
