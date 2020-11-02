package com.polykhel.ssq.config.apidoc.customizer;

import springfox.documentation.spring.web.plugins.Docket;

/**
 * Callback interface that can be implemented by beans wishing to further customize the
 * {@link springfox.documentation.spring.web.plugins.Docket} in Springfox.
 */
@FunctionalInterface
public interface SpringfoxCustomizer {

    /**
     * Customize the Springfox Docket.
     *
     * @param docket the Docket to customize
     */
    void customize(Docket docket);
}
