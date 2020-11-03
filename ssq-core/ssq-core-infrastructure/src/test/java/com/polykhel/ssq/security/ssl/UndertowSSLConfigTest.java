package com.polykhel.ssq.security.ssl;

import io.undertow.Undertow;
import io.undertow.UndertowOptions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.web.embedded.undertow.UndertowServletWebServerFactory;
import org.springframework.test.util.ReflectionTestUtils;
import org.xnio.OptionMap;

import static org.assertj.core.api.Assertions.assertThat;

public class UndertowSSLConfigTest {

    @Test
    public void testUndertowSSLConfigOK() {
        //Prepare
        UndertowServletWebServerFactory undertowServletWebServerFactory = new UndertowServletWebServerFactory();

        //Execute
        UndertowSSLConfig undertowSSLConfig = new UndertowSSLConfig(undertowServletWebServerFactory);

        //Verify
        Undertow.Builder builder = Undertow.builder();
        undertowServletWebServerFactory.getBuilderCustomizers().forEach(c -> c.customize(builder));
        OptionMap.Builder serverOptions = (OptionMap.Builder) ReflectionTestUtils.getField(builder, "socketOptions");
        assertThat(undertowServletWebServerFactory).isNotNull();
        assertThat(serverOptions.getMap().get(UndertowOptions.SSL_USER_CIPHER_SUITES_ORDER)).isTrue();
    }

}
