package com.polykhel.ssq.security.ssl;

import io.undertow.UndertowOptions;
import org.slf4j.Logger;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.embedded.undertow.UndertowServletWebServerFactory;
import org.springframework.context.annotation.Configuration;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * SSL configuration for Undertow.
 * <p>
 * SSL_USER_CIPHER_SUITES_ORDER : It will force the cipher suite defined by the user,
 * allowing to achieve perfect forward secrecy.
 * This can only be activated with HTTPS and a cipher suite defined by the user (server.ssl.ciphers).
 * <p>
 * Can be used by setting the `server.ssl.ciphers` property that is available
 * in the `application-tls.yml` file
 *
 * @see <a href="https://github.com/ssllabs/research/wiki/SSL-and-TLS-Deployment-Best-Practices#25-use-forward-secrecy" target="_blank">More explanation on perfect forward secrecy</a>
 */
@Configuration
@ConditionalOnBean({UndertowServletWebServerFactory.class})
@ConditionalOnClass(UndertowOptions.class)
@ConditionalOnProperty({"server.ssl.ciphers", "server.ssl.key-store"})
public class UndertowSSLConfig {

    private static final Logger log = getLogger(UndertowSSLConfig.class);
    private final UndertowServletWebServerFactory factory;

    public UndertowSSLConfig(UndertowServletWebServerFactory factory) {
        this.factory = factory;

        configuringUserCipherSuiteOrder();
    }

    private void configuringUserCipherSuiteOrder() {
        log.info("Configuring Undertow");
        log.info("Setting user cipher suite order to true");
        factory.addBuilderCustomizers(builder -> builder.setSocketOption(UndertowOptions
            .SSL_USER_CIPHER_SUITES_ORDER, Boolean.TRUE));
    }
}
