package com.polykhel.ssq.registry.config;

import com.polykhel.ssq.config.CoreProperties;
import com.polykhel.ssq.constants.PropertyDefaults;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.CacheControl;
import org.springframework.mock.web.MockServletContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;

import java.util.concurrent.TimeUnit;

import static com.polykhel.ssq.registry.config.StaticResourcesWebConfig.RESOURCE_LOCATIONS;
import static com.polykhel.ssq.registry.config.StaticResourcesWebConfig.RESOURCE_PATHS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class StaticResourcesWebConfigurerTest {
    public static final int MAX_AGE_TEST = 5;
    public StaticResourcesWebConfig StaticResourcesWebConfig;
    private ResourceHandlerRegistry resourceHandlerRegistry;
    private CoreProperties props;

    @BeforeEach
    void setUp() {
        MockServletContext servletContext = spy(new MockServletContext());
        WebApplicationContext applicationContext = mock(WebApplicationContext.class);
        resourceHandlerRegistry = spy(new ResourceHandlerRegistry(applicationContext, servletContext));
        props = new CoreProperties();
        StaticResourcesWebConfig = spy(new StaticResourcesWebConfig(props));
    }

    @Test
    public void shouldAppendResourceHandlerAndInitializeIt() {
        StaticResourcesWebConfig.addResourceHandlers(resourceHandlerRegistry);

        verify(resourceHandlerRegistry, times(1))
                .addResourceHandler(RESOURCE_PATHS);
        verify(StaticResourcesWebConfig, times(1))
                .initializeResourceHandler(any(ResourceHandlerRegistration.class));
        for (String testingPath : RESOURCE_PATHS) {
            assertThat(resourceHandlerRegistry.hasMappingForPattern(testingPath)).isTrue();
        }
    }

    @Test
    public void shouldInitializeResourceHandlerWithCacheControlAndLocations() {
        CacheControl ccExpected = CacheControl.maxAge(5, TimeUnit.DAYS).cachePublic();
        when(StaticResourcesWebConfig.getCacheControl()).thenReturn(ccExpected);
        ResourceHandlerRegistration resourceHandlerRegistration = spy(new ResourceHandlerRegistration(RESOURCE_PATHS));

        StaticResourcesWebConfig.initializeResourceHandler(resourceHandlerRegistration);

        verify(StaticResourcesWebConfig, times(1)).getCacheControl();
        verify(resourceHandlerRegistration, times(1)).setCacheControl(ccExpected);
        verify(resourceHandlerRegistration, times(1)).addResourceLocations(RESOURCE_LOCATIONS);
    }


    @Test
    public void shouldCreateCacheControlBasedOnDefaultProperties() {
        CacheControl cacheExpected = CacheControl.maxAge(PropertyDefaults.Http.Cache.timeToLiveInDays, TimeUnit.DAYS).cachePublic();
        assertThat(StaticResourcesWebConfig.getCacheControl())
                .extracting(CacheControl::getHeaderValue)
                .isEqualTo(cacheExpected.getHeaderValue());
    }

    @Test
    public void shouldCreateCacheControlWithSpecificConfigurationInProperties() {
        props.getHttp().getCache().setTimeToLiveInDays(MAX_AGE_TEST);
        CacheControl cacheExpected = CacheControl.maxAge(MAX_AGE_TEST, TimeUnit.DAYS).cachePublic();
        assertThat(StaticResourcesWebConfig.getCacheControl())
                .extracting(CacheControl::getHeaderValue)
                .isEqualTo(cacheExpected.getHeaderValue());
    }
}
