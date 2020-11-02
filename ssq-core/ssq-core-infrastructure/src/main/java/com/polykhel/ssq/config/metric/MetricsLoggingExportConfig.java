package com.polykhel.ssq.config.metric;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Slf4jReporter;
import com.polykhel.ssq.config.CoreProperties;
import io.micrometer.core.instrument.Clock;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.dropwizard.DropwizardConfig;
import io.micrometer.core.instrument.dropwizard.DropwizardMeterRegistry;
import io.micrometer.core.instrument.util.HierarchicalNameMapper;
import org.slf4j.Logger;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Console Reporter Configuration
 * <p>
 * Pass the metrics to the logs with Dropwizard Reporter implementation
 * see https://github.com/micrometer-metrics/micrometer-docs/blob/9fedeb5/src/docs/guide/console-reporter.adoc
 */
@Configuration
@ConditionalOnProperty("ssa.metrics.logs.enabled")
public class MetricsLoggingExportConfig {

    private static final Logger log = getLogger(MetricsLoggingExportConfig.class);
    private final CoreProperties coreProperties;

    public MetricsLoggingExportConfig(CoreProperties coreProperties) {
        this.coreProperties = coreProperties;
    }

    /**
     * Dropwizard Registry
     *
     * @return a {@link MetricRegistry} object.
     */
    @Bean
    public MetricRegistry dropwizardRegistry() {
        return new MetricRegistry();
    }

    @Bean
    public Slf4jReporter consoleReporter(MetricRegistry dropwizardRegistry) {
        log.info("Initializing Metrics Log reporting");
        Marker metricsMarker = MarkerFactory.getMarker("metrics");
        final Slf4jReporter reporter = Slf4jReporter.forRegistry(dropwizardRegistry)
            .outputTo(getLogger("metrics"))
            .markWith(metricsMarker)
            .convertRatesTo(TimeUnit.SECONDS)
            .convertDurationsTo(TimeUnit.MILLISECONDS)
            .build();
        reporter.start(coreProperties.getMetrics().getLogs().getReportFrequency(), TimeUnit.SECONDS);
        return reporter;
    }

    /**
     * <p>consoleLoggingRegistry.</p>
     *
     * @param dropwizardRegistry a {@link MetricRegistry} object.
     * @return a {@link MeterRegistry} object.
     */
    @Bean
    public MeterRegistry consoleLoggingRegistry(MetricRegistry dropwizardRegistry) {
        DropwizardConfig dropwizardConfig = new DropwizardConfig() {
            @Override
            public String prefix() {
                return "console";
            }

            @Override
            public String get(String key) {
                return null;
            }
        };

        return new DropwizardMeterRegistry(dropwizardConfig, dropwizardRegistry, HierarchicalNameMapper.DEFAULT, Clock.SYSTEM) {
            @Override
            protected Double nullGaugeValue() {
                return null;
            }
        };
    }
}
