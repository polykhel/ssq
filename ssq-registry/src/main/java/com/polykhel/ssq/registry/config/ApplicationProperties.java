package com.polykhel.ssq.registry.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Properties specific to the Registry.
 * <p>
 * Properties are configured in the {@code application.yml} file.
 */
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties {

    @Getter
    private final Oauth2 oauth2 = new Oauth2();

    @Getter
    @Setter
    public static class Oauth2 {

        private String principalAttribute;

        private String authoritiesAttribute;

    }
}
