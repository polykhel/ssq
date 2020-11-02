package com.polykhel.ssq.security.uaa;

import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;

import java.net.URI;
import java.net.URISyntaxException;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Load Balanced Resource Details
 */
@ConditionalOnMissingBean
public class LoadBalancedResourceDetails extends ClientCredentialsResourceDetails {

    /**
     * Constant <code>EXCEPTION_MESSAGE="Returning an invalid URI: {}"</code>
     */
    public static final String EXCEPTION_MESSAGE = "Returning an invalid URI: {}";
    private static final Logger log = getLogger(LoadBalancedResourceDetails.class);
    private final LoadBalancerClient loadBalancerClient;

    private String tokenServiceId;

    public LoadBalancedResourceDetails(LoadBalancerClient loadBalancerClient) {
        this.loadBalancerClient = loadBalancerClient;
    }

    public String getTokenServiceId() {
        return tokenServiceId;
    }

    public void setTokenServiceId(String tokenServiceId) {
        this.tokenServiceId = tokenServiceId;
    }

    public String getAccessTokenUri() {
        if (ObjectUtils.allNotNull(loadBalancerClient, tokenServiceId) && !tokenServiceId.isEmpty()) {
            try {
                return loadBalancerClient.reconstructURI(loadBalancerClient.choose(tokenServiceId), new URI(super.getAccessTokenUri())).toString();
            } catch (URISyntaxException e) {
                log.error(EXCEPTION_MESSAGE, e.getMessage());
                return super.getAccessTokenUri();
            }
        } else {
            return super.getAccessTokenUri();
        }
    }
}
