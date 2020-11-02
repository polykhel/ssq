package com.polykhel.ssq.config.apidoc.customizer;

import com.polykhel.ssq.config.CoreProperties;
import org.springframework.core.Ordered;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Server;
import springfox.documentation.spring.web.plugins.Docket;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

import static springfox.documentation.builders.PathSelectors.regex;

/**
 * A swagger customizer to setup {@link springfox.documentation.spring.web.plugins.Docket} with custom settings.
 */
public class CustomSpringfoxCustomizer implements SpringfoxCustomizer, Ordered {

    /**
     * The default order for the customizer.
     */
    public static final int DEFAULT_ORDER = 0;
    private final CoreProperties.ApiDocs properties;
    private int order = DEFAULT_ORDER;

    /**
     * <p>Constructor for CustomSpringfoxCustomizer.</p>
     *
     * @param properties a {@link com.polykhel.ssq.config.CoreProperties.ApiDocs} object.
     */
    public CustomSpringfoxCustomizer(CoreProperties.ApiDocs properties) {
        this.properties = properties;
    }

    /**
     * {@inheritDoc}
     */
    public void customize(Docket docket) {
        Contact contact = new Contact(
            properties.getContactName(),
            properties.getContactUrl(),
            properties.getContactEmail()
        );

        ApiInfo apiInfo = new ApiInfo(
            properties.getTitle(),
            properties.getDescription(),
            properties.getVersion(),
            properties.getTermsOfServiceUrl(),
            contact,
            properties.getLicense(),
            properties.getLicenseUrl(),
            new ArrayList<>()
        );

        for (CoreProperties.ApiDocs.Server server : properties.getServers()) {
            docket.servers(new Server(server.getName(), server.getUrl(), server.getDescription(),
                Collections.emptyList(), Collections.emptyList()));
        }

        docket.host(properties.getHost())
            .protocols(new HashSet<>(Arrays.asList(properties.getProtocols())))
            .apiInfo(apiInfo)
            .useDefaultResponseMessages(properties.isUseDefaultResponseMessages())
            .forCodeGeneration(true)
            .directModelSubstitute(ByteBuffer.class, String.class)
            .genericModelSubstitutes(ResponseEntity.class)
            .ignoredParameterTypes(Pageable.class)
            .select()
            .paths(regex(properties.getDefaultIncludePattern()))
            .build();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getOrder() {
        return order;
    }

    /**
     * Setter for the field <code>order</code>
     *
     * @param order an integer
     */
    public void setOrder(int order) {
        this.order = order;
    }
}
