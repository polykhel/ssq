package com.polykhel.ssq.registry.security.uaa;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.polykhel.ssq.registry.config.Constants;
import com.polykhel.ssq.registry.security.jwt.JWTFilter;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.PRE_TYPE;

@Component
@Profile(Constants.PROFILE_UAA)
public class UaaAuthorizationHeaderFilter extends ZuulFilter {

    private final UaaAuthorizationHeaderUtil authorizationHeaderUtil;

    public UaaAuthorizationHeaderFilter(UaaAuthorizationHeaderUtil authorizationHeaderUtil) {
        this.authorizationHeaderUtil = authorizationHeaderUtil;
    }

    @Override
    public String filterType() {
        return PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        RequestContext ctx = RequestContext.getCurrentContext();
        return ctx.getRequest().getRequestURI().startsWith("/services");
    }

    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        // Add specific authorization headers for UAA
        ctx.addZuulRequestHeader(JWTFilter.AUTHORIZATION_HEADER, authorizationHeaderUtil.getAuthorizationHeader());
        return null;
    }
}
