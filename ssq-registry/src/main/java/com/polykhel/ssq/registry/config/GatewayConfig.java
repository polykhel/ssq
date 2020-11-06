package com.polykhel.ssq.registry.config;

import com.polykhel.ssq.registry.gateway.AccessControlFilter;
import com.polykhel.ssq.registry.gateway.SwaggerBasePathRewritingFilter;
import com.polykhel.ssq.config.CoreProperties;
import org.springframework.cloud.netflix.zuul.filters.RouteLocator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Configuration
    public static class SwaggerBasePathRewritingConfiguration {

        @Bean
        public SwaggerBasePathRewritingFilter swaggerBasePathRewritingFilter() {
            return new SwaggerBasePathRewritingFilter();
        }
    }

    @Configuration
    public static class AccessControlFilterConfiguration {

        @Bean
        public AccessControlFilter accessControlFilter(RouteLocator routeLocator, CoreProperties coreProperties) {
            return new AccessControlFilter(routeLocator, coreProperties);
        }
    }

}
