package com.polykhel.ssq.security;

import org.slf4j.Logger;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Returns a 403 error code (Unauthorized) to the client.
 */
@Component
public class UnauthorizedEntryPoint implements AuthenticationEntryPoint {

    private static final Logger log = getLogger(UnauthorizedEntryPoint.class);

    /**
     * Return a 401 error code to the client.
     */
    @Override
    public void commence(
        HttpServletRequest request,
        HttpServletResponse response,
        AuthenticationException authException)
        throws IOException {
        log.debug("Pre-authenticated entry point called. Rejected");
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Access Denied");
    }
}
