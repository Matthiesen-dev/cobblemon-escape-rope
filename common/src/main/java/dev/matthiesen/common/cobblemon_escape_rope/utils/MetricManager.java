package dev.matthiesen.common.cobblemon_escape_rope.utils;

import dev.matthiesen.common.cobblemon_escape_rope.Constants;
import dev.matthiesen.common.matthiesen_lib_api.MatthiesenLibApi;
import dev.matthiesen.common.matthiesen_lib_api.core.MatthiesenLibApiMetricsManager;
import dev.matthiesen.common.matthiesen_lib_api.core.metric.UniversalMetricContext;
import dev.matthiesen.libs.faststats.ErrorTracker;

public final class MetricManager {
    public static final ErrorTracker ERROR_TRACKER = MatthiesenLibApiMetricsManager.getErrorTracker();

    @SuppressWarnings("unused")
    private static final UniversalMetricContext metricContext = MatthiesenLibApiMetricsManager.makeErrorMetricsContext(
            Constants.MOD_ID,
            Constants.METRICS_TOKEN,
            ERROR_TRACKER
    );

    public static void ready() {
        MatthiesenLibApi.registerModToApiMetrics(Constants.MOD_ID);
    }
}
