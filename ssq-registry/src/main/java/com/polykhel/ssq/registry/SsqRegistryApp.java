package com.polykhel.ssq.registry;

import com.polykhel.ssq.registry.config.ConfigServerConfig;
import com.polykhel.ssq.registry.config.ApplicationProperties;
import com.polykhel.ssq.config.CoreProperties;
import com.polykhel.ssq.utils.DefaultProfileUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.core.env.Environment;

import javax.annotation.PostConstruct;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Collection;

import static com.polykhel.ssq.constants.ProfileConstants.SPRING_PROFILE_DEVELOPMENT;
import static com.polykhel.ssq.constants.ProfileConstants.SPRING_PROFILE_PRODUCTION;

@EnableEurekaServer
@EnableConfigServer
@SpringBootApplication
@EnableConfigurationProperties({ApplicationProperties.class, ConfigServerConfig.class, CoreProperties.class})
@EnableDiscoveryClient
@EnableZuulProxy
@Slf4j
public class SsqRegistryApp {

    private final Environment env;

    public SsqRegistryApp(Environment env) {
        this.env = env;
    }

    /**
     * Main method.
     *
     * @param args the command line arguments.
     */
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(SsqRegistryApp.class);
        DefaultProfileUtil.setDefaultProfile(app);
        Environment env = app.run(args).getEnvironment();
        logApplicationStartup(env);
    }

    private static void logApplicationStartup(Environment env) {
        String protocol = StringUtils.isNotEmpty(env.getProperty("server.ssl.key-store")) ? "https" : "http";
        String serverPort = env.getProperty("server.port");
        String contextPath = env.getProperty("server.servlet.context-path");
        if (StringUtils.isBlank(contextPath)) {
            contextPath = "/";
        }
        String hostAddress;
        try {
            hostAddress = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            log.warn("The host name could not be determined, defaulting to `localhost`");
            hostAddress = "localhost";
        }

        log.info(
                "\n----------------------------------------------------------\n\t"
                        + "Application '{}' is running! Access URLs:\n\t"
                        + "Local: \t\t{}://localhost:{}{}\n\t"
                        + "External: \t{}://{}:{}{}\n\t"
                        + "Profile(s): \t{}\n----------------------------------------------------------",
                env.getProperty("spring.application.name"),
                protocol,
                serverPort,
                contextPath,
                protocol,
                hostAddress,
                serverPort,
                contextPath,
                env.getActiveProfiles());

        String configServerStatus = env.getProperty("configserver.status");
        if (configServerStatus == null) {
            configServerStatus = "Not found or not setup for this application";
        }
        log.info(
                "\n----------------------------------------------------------\n\t"
                        + "Config Server: \t{}\n----------------------------------------------------------",
                configServerStatus);
    }

    /**
     * Initializer.
     */
    @PostConstruct
    public void initApplication() {
        Collection<String> activeProfiles = Arrays.asList(env.getActiveProfiles());
        Collection<String> devAndProdProfiles = Arrays.asList(SPRING_PROFILE_DEVELOPMENT, SPRING_PROFILE_PRODUCTION);
        if (activeProfiles.containsAll(devAndProdProfiles)) {
            log.error("'dev' and 'prod' profiles should not be run at the same time");
        }
    }
}
