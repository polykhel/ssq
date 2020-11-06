package com.polykhel.ssq.registry.client;

import com.polykhel.ssq.registry.config.Constants;
import com.polykhel.ssq.registry.security.oauth2.AuthorizationHeaderUtil;
import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile(Constants.PROFILE_OAUTH2)
public class OAuth2InterceptedFeignConfig {

    @Bean(name = "oauth2RequestInterceptor")
    public RequestInterceptor getOAuth2RequestInterceptor(AuthorizationHeaderUtil headerUtil) {
        return new TokenRelayRequestInterceptor(headerUtil);
    }
}
