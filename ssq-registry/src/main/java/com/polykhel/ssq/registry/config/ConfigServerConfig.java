package com.polykhel.ssq.registry.config;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@ConfigurationProperties(prefix = "spring.cloud.config.server")
public class ConfigServerConfig {

    @Getter
    private final List<Map<String, Object>> composite = new ArrayList<>();
}
