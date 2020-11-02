package com.polykhel.ssq.config.apidoc;

import com.polykhel.ssq.config.CoreProperties;
import com.polykhel.ssq.config.apidoc.customizer.CustomSpringfoxCustomizer;
import com.polykhel.ssq.config.apidoc.customizer.SpringfoxCustomizer;
import org.slf4j.Logger;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StopWatch;
import org.springframework.util.StringUtils;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.oas.configuration.OpenApiDocumentationConfiguration;
import springfox.documentation.schema.AlternateTypeRule;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Server;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.configuration.Swagger2DocumentationConfiguration;

import java.nio.ByteBuffer;
import java.util.*;

import static com.polykhel.ssq.constants.ProfileConstants.SPRING_PROFILE_API_DOCS;
import static org.slf4j.LoggerFactory.getLogger;
import static springfox.documentation.builders.PathSelectors.regex;

/**
 * Springfox OpenAPI configuration.
 * <p>
 * Warning! When having a lot of REST endpoints, Springfox can become a performance issue.
 * In that case, you can use the "no-api-docs" Spring profile, so that this bean is ignored.
 */
@Configuration
@ConditionalOnWebApplication
@ConditionalOnClass({
    ApiInfo.class,
    BeanValidatorPluginsConfiguration.class,
    Docket.class
})
@Profile(SPRING_PROFILE_API_DOCS)
@AutoConfigureAfter(CoreProperties.class)
@Import({
    OpenApiDocumentationConfiguration.class,
    Swagger2DocumentationConfiguration.class,
    BeanValidatorPluginsConfiguration.class
})
public class SpringfoxAutoConfig {

    static final String STARTING_MESSAGE = "Starting OpenAPI docs";
    static final String STARTED_MESSAGE = "Started OpenAPI docs in {} ms";
    static final String MANAGEMENT_TITLE_SUFFIX = "Management API";
    static final String MANAGEMENT_GROUP_NAME = "management";
    static final String MANAGEMENT_DESCRIPTION = "Management endpoints documentation";
    private static final Logger log = getLogger(SpringfoxAutoConfig.class);
    private final CoreProperties.ApiDocs properties;

    /**
     * <p>Constructor for SpringfoxAutoConfig.</p>
     *
     * @param properties a {@link com.polykhel.ssq.config.CoreProperties} object.
     */
    public SpringfoxAutoConfig(CoreProperties properties) {
        this.properties = properties.getApiDocs();
    }

    /**
     * Springfox configuration for the OpenAPI docs.
     *
     * @param springfoxCustomizers Springfox customizers
     * @param alternateTypeRules   alternate type rules
     * @return the Springfox configuration
     */
    @Bean
    @ConditionalOnMissingBean(name = "openAPISpringfoxApiDocket")
    public Docket openAPISpringfoxApiDocket(List<SpringfoxCustomizer> springfoxCustomizers,
                                            ObjectProvider<AlternateTypeRule[]> alternateTypeRules) {
        log.debug(STARTING_MESSAGE);
        StopWatch watch = new StopWatch();
        watch.start();

        Docket docket = createDocket();

        // Apply all OpenAPICustomizers orderly.
        springfoxCustomizers.forEach(customizer -> customizer.customize(docket));

        // Add all AlternateTypeRules if available in spring bean factory.
        // Also you can add your rules in a customizer bean above.
        Optional.ofNullable(alternateTypeRules.getIfAvailable()).ifPresent(docket::alternateTypeRules);

        watch.stop();
        log.debug(STARTED_MESSAGE, watch.getTotalTimeMillis());
        return docket;
    }

    @Bean
    public CustomSpringfoxCustomizer customSwaggerCustomizer() {
        return new CustomSpringfoxCustomizer(properties);
    }

    /**
     * Springfox configuration for the management endpoints (actuator) OpenAPI docs.
     *
     * @param appName               the application name
     * @param managementContextPath the path to access management endpoints
     * @return the Springfox configuration
     */
    @Bean
    @ConditionalOnClass(name = "org.springframework.boot.actuate.autoconfigure.web.server.ManagementServerProperties")
    @ConditionalOnProperty("management.endpoints.web.base-path")
    @ConditionalOnExpression("'${management.endpoints.web.base-path}'.length() > 0")
    @ConditionalOnMissingBean(name = "openAPISpringfoxManagementDocket")
    public Docket openAPISpringfoxManagementDocket(@Value("${spring.application.name:application}") String appName,
                                                   @Value("${management.endpoints.web.base-path}") String managementContextPath) {

        ApiInfo apiInfo = new ApiInfo(
            StringUtils.capitalize(appName) + " " + MANAGEMENT_TITLE_SUFFIX,
            MANAGEMENT_DESCRIPTION,
            properties.getVersion(),
            "",
            ApiInfo.DEFAULT_CONTACT,
            "",
            "",
            new ArrayList<>()
        );

        Docket docket = createDocket();

        for (CoreProperties.ApiDocs.Server server : properties.getServers()) {
            docket.servers(new Server(server.getName(), server.getUrl(), server.getDescription(),
                Collections.emptyList(), Collections.emptyList()));
        }

        return docket
            .apiInfo(apiInfo)
            .useDefaultResponseMessages(properties.isUseDefaultResponseMessages())
            .groupName(MANAGEMENT_GROUP_NAME)
            .host(properties.getHost())
            .protocols(new HashSet<>(Arrays.asList(properties.getProtocols())))
            .forCodeGeneration(true)
            .directModelSubstitute(ByteBuffer.class, String.class)
            .genericModelSubstitutes(ResponseEntity.class)
            .ignoredParameterTypes(Pageable.class)
            .select()
            .paths(regex(managementContextPath + ".*"))
            .build();
    }

    /**
     * <p>createDocket.</p>
     *
     * @return a {@link springfox.documentation.spring.web.plugins.Docket} object.
     */
    protected Docket createDocket() {
        return new Docket(DocumentationType.OAS_30);
    }

}
