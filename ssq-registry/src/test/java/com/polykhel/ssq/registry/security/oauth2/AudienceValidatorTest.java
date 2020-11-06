package com.polykhel.ssq.registry.security.oauth2;

import org.junit.jupiter.api.Test;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Test class for the {@link AudienceValidator} utility class.
 */
public class AudienceValidatorTest {

    private final AudienceValidator validator = new AudienceValidator(Collections.singletonList("api://default"));

    @Test
    @SuppressWarnings("unchecked")
    public void testInvalidAudience() {
        Map<String, Object> claims = new HashMap<>();
        claims.put("aud", "bar");
        Jwt badJwt = mock(Jwt.class);
        when(badJwt.getAudience()).thenReturn(new ArrayList(claims.values()));
        assertThat(validator.validate(badJwt).hasErrors()).isTrue();
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testValidAudience() {
        Map<String, Object> claims = new HashMap<>();
        claims.put("aud", "api://default");
        Jwt jwt = mock(Jwt.class);
        when(jwt.getAudience()).thenReturn(new ArrayList(claims.values()));
        assertThat(validator.validate(jwt).hasErrors()).isFalse();
    }
}
