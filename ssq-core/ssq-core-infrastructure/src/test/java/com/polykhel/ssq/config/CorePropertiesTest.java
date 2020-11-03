package com.polykhel.ssq.config;

import com.polykhel.ssq.constants.PropertyDefaults;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.util.Lists.newArrayList;

public class CorePropertiesTest {

    private CoreProperties properties;

    @BeforeEach
    public void setup() {
        properties = new CoreProperties();
    }

    @Test
    public void testComplete() throws Exception {
        // Slightly pedantic; this checks if there are tests for each of the properties.
        Set<String> set = new LinkedHashSet<>(64, 1F);
        reflect(properties, set, "test");
        for (String name : set) {
            assertThat(this.getClass().getDeclaredMethod(name)).isNotNull();
        }
    }

    private void reflect(Object obj, Set<String> dst, String prefix) throws Exception {
        Class<?> src = obj.getClass();
        for (Method method : src.getDeclaredMethods()) {
            String name = method.getName();
            if (name.startsWith("get")) {
                Object res = method.invoke(obj, (Object[]) null);
                if (res != null && src.equals(res.getClass().getDeclaringClass())) {
                    reflect(res, dst, prefix + name.substring(3));
                }
            } else if (name.startsWith("set")) {
                dst.add(prefix + name.substring(3));
            }
        }
    }

    @Test
    public void testAsyncCorePoolSize() {
        CoreProperties.Async obj = properties.getAsync();
        int val = PropertyDefaults.Async.corePoolSize;
        assertThat(obj.getCorePoolSize()).isEqualTo(val);
        val++;
        obj.setCorePoolSize(val);
        assertThat(obj.getCorePoolSize()).isEqualTo(val);
    }

    @Test
    public void testAsyncMaxPoolSize() {
        CoreProperties.Async obj = properties.getAsync();
        int val = PropertyDefaults.Async.maxPoolSize;
        assertThat(obj.getMaxPoolSize()).isEqualTo(val);
        val++;
        obj.setMaxPoolSize(val);
        assertThat(obj.getMaxPoolSize()).isEqualTo(val);
    }

    @Test
    public void testAsyncQueueCapacity() {
        CoreProperties.Async obj = properties.getAsync();
        int val = PropertyDefaults.Async.queueCapacity;
        assertThat(obj.getQueueCapacity()).isEqualTo(val);
        val++;
        obj.setQueueCapacity(val);
        assertThat(obj.getQueueCapacity()).isEqualTo(val);
    }

    @Test
    public void testHttpCacheTimeToLiveInDays() {
        CoreProperties.Http.Cache obj = properties.getHttp().getCache();
        int val = PropertyDefaults.Http.Cache.timeToLiveInDays;
        assertThat(obj.getTimeToLiveInDays()).isEqualTo(val);
        val++;
        obj.setTimeToLiveInDays(val);
        assertThat(obj.getTimeToLiveInDays()).isEqualTo(val);
    }

    @Test
    public void testDatabaseCouchbaseBucketName() {
        CoreProperties.Database.Couchbase obj = properties.getDatabase().getCouchbase();
        assertThat(obj.getBucketName()).isEqualTo(null);
        obj.setBucketName("bucketName");
        assertThat(obj.getBucketName()).isEqualTo("bucketName");
    }

    @Test
    public void testCacheHazelcastTimeToLiveSeconds() {
        CoreProperties.Cache.Hazelcast obj = properties.getCache().getHazelcast();
        int val = PropertyDefaults.Cache.Hazelcast.timeToLiveSeconds;
        assertThat(obj.getTimeToLiveSeconds()).isEqualTo(val);
        val++;
        obj.setTimeToLiveSeconds(val);
        assertThat(obj.getTimeToLiveSeconds()).isEqualTo(val);
    }

    @Test
    public void testCacheHazelcastBackupCount() {
        CoreProperties.Cache.Hazelcast obj = properties.getCache().getHazelcast();
        int val = PropertyDefaults.Cache.Hazelcast.backupCount;
        assertThat(obj.getBackupCount()).isEqualTo(val);
        val++;
        obj.setBackupCount(val);
        assertThat(obj.getBackupCount()).isEqualTo(val);
    }

    @Test
    public void testCacheCaffeineTimeToLiveSeconds() {
        CoreProperties.Cache.Caffeine obj = properties.getCache().getCaffeine();
        int val = PropertyDefaults.Cache.Caffeine.timeToLiveSeconds;
        assertThat(obj.getTimeToLiveSeconds()).isEqualTo(val);
        val++;
        obj.setTimeToLiveSeconds(val);
        assertThat(obj.getTimeToLiveSeconds()).isEqualTo(val);
    }

    @Test
    public void testCacheCaffeineMaxEntries() {
        CoreProperties.Cache.Caffeine obj = properties.getCache().getCaffeine();
        long val = PropertyDefaults.Cache.Caffeine.maxEntries;
        assertThat(obj.getMaxEntries()).isEqualTo(val);
        val++;
        obj.setMaxEntries(val);
        assertThat(obj.getMaxEntries()).isEqualTo(val);
    }

    @Test
    public void testCacheEhcacheTimeToLiveSeconds() {
        CoreProperties.Cache.Ehcache obj = properties.getCache().getEhcache();
        int val = PropertyDefaults.Cache.Ehcache.timeToLiveSeconds;
        assertThat(obj.getTimeToLiveSeconds()).isEqualTo(val);
        val++;
        obj.setTimeToLiveSeconds(val);
        assertThat(obj.getTimeToLiveSeconds()).isEqualTo(val);
    }

    @Test
    public void testCacheEhcacheMaxEntries() {
        CoreProperties.Cache.Ehcache obj = properties.getCache().getEhcache();
        long val = PropertyDefaults.Cache.Ehcache.maxEntries;
        assertThat(obj.getMaxEntries()).isEqualTo(val);
        val++;
        obj.setMaxEntries(val);
        assertThat(obj.getMaxEntries()).isEqualTo(val);
    }

    @Test
    public void testCacheInfinispanConfigFile() {
        CoreProperties.Cache.Infinispan obj = properties.getCache().getInfinispan();
        String val = PropertyDefaults.Cache.Infinispan.configFile;
        assertThat(obj.getConfigFile()).isEqualTo(val);
        val = "1" + val;
        obj.setConfigFile(val);
        assertThat(obj.getConfigFile()).isEqualTo(val);
    }

    @Test
    public void testCacheInfinispanStatsEnabled() {
        CoreProperties.Cache.Infinispan obj = properties.getCache().getInfinispan();
        boolean val = PropertyDefaults.Cache.Infinispan.statsEnabled;
        assertThat(obj.isStatsEnabled()).isEqualTo(val);
        val = !val;
        obj.setStatsEnabled(val);
        assertThat(obj.isStatsEnabled()).isEqualTo(val);
    }

    @Test
    public void testCacheInfinispanLocalTimeToLiveSeconds() {
        CoreProperties.Cache.Infinispan.Local obj = properties.getCache().getInfinispan().getLocal();
        long val = PropertyDefaults.Cache.Infinispan.Local.timeToLiveSeconds;
        assertThat(obj.getTimeToLiveSeconds()).isEqualTo(val);
        val++;
        obj.setTimeToLiveSeconds(val);
        assertThat(obj.getTimeToLiveSeconds()).isEqualTo(val);
    }

    @Test
    public void testCacheInfinispanLocalMaxEntries() {
        CoreProperties.Cache.Infinispan.Local obj = properties.getCache().getInfinispan().getLocal();
        long val = PropertyDefaults.Cache.Infinispan.Local.maxEntries;
        assertThat(obj.getMaxEntries()).isEqualTo(val);
        val++;
        obj.setMaxEntries(val);
        assertThat(obj.getMaxEntries()).isEqualTo(val);
    }

    @Test
    public void testCacheInfinispanDistributedTimeToLiveSeconds() {
        CoreProperties.Cache.Infinispan.Distributed obj = properties.getCache().getInfinispan().getDistributed();
        long val = PropertyDefaults.Cache.Infinispan.Distributed.timeToLiveSeconds;
        assertThat(obj.getTimeToLiveSeconds()).isEqualTo(val);
        val++;
        obj.setTimeToLiveSeconds(val);
        assertThat(obj.getTimeToLiveSeconds()).isEqualTo(val);
    }

    @Test
    public void testCacheInfinispanDistributedMaxEntries() {
        CoreProperties.Cache.Infinispan.Distributed obj = properties.getCache().getInfinispan().getDistributed();
        long val = PropertyDefaults.Cache.Infinispan.Distributed.maxEntries;
        assertThat(obj.getMaxEntries()).isEqualTo(val);
        val++;
        obj.setMaxEntries(val);
        assertThat(obj.getMaxEntries()).isEqualTo(val);
    }

    @Test
    public void testCacheInfinispanDistributedInstanceCount() {
        CoreProperties.Cache.Infinispan.Distributed obj = properties.getCache().getInfinispan().getDistributed();
        int val = PropertyDefaults.Cache.Infinispan.Distributed.instanceCount;
        assertThat(obj.getInstanceCount()).isEqualTo(val);
        val++;
        obj.setInstanceCount(val);
        assertThat(obj.getInstanceCount()).isEqualTo(val);
    }

    @Test
    public void testCacheInfinispanReplicatedTimeToLiveSeconds() {
        CoreProperties.Cache.Infinispan.Replicated obj = properties.getCache().getInfinispan().getReplicated();
        long val = PropertyDefaults.Cache.Infinispan.Replicated.timeToLiveSeconds;
        assertThat(obj.getTimeToLiveSeconds()).isEqualTo(val);
        val++;
        obj.setTimeToLiveSeconds(val);
        assertThat(obj.getTimeToLiveSeconds()).isEqualTo(val);
    }

    @Test
    public void testCacheInfinispanReplicatedMaxEntries() {
        CoreProperties.Cache.Infinispan.Replicated obj = properties.getCache().getInfinispan().getReplicated();
        long val = PropertyDefaults.Cache.Infinispan.Replicated.maxEntries;
        assertThat(obj.getMaxEntries()).isEqualTo(val);
        val++;
        obj.setMaxEntries(val);
        assertThat(obj.getMaxEntries()).isEqualTo(val);
    }

    @Test
    public void testCacheMemcachedEnabled() {
        CoreProperties.Cache.Memcached obj = properties.getCache().getMemcached();
        boolean val = PropertyDefaults.Cache.Memcached.enabled;
        assertThat(obj.isEnabled()).isEqualTo(val);
        val = true;
        obj.setEnabled(val);
        assertThat(obj.isEnabled()).isEqualTo(val);
    }

    @Test
    public void testCacheMemcachedServers() {
        CoreProperties.Cache.Memcached obj = properties.getCache().getMemcached();
        String val = PropertyDefaults.Cache.Memcached.servers;
        assertThat(obj.getServers()).isEqualTo(val);
        val = "myserver:1337";
        obj.setServers(val);
        assertThat(obj.getServers()).isEqualTo(val);
    }

    @Test
    public void testCacheMemcachedExpiration() {
        CoreProperties.Cache.Memcached obj = properties.getCache().getMemcached();
        int val = PropertyDefaults.Cache.Memcached.expiration;
        assertThat(obj.getExpiration()).isEqualTo(val);
        val++;
        obj.setExpiration(val);
        assertThat(obj.getExpiration()).isEqualTo(val);
    }

    @Test
    public void testCacheMemcachedUseBinaryProtocol() {
        CoreProperties.Cache.Memcached obj = properties.getCache().getMemcached();
        boolean val = PropertyDefaults.Cache.Memcached.useBinaryProtocol;
        assertThat(obj.isUseBinaryProtocol()).isEqualTo(val);
        val = false;
        obj.setUseBinaryProtocol(val);
        assertThat(obj.isUseBinaryProtocol()).isEqualTo(val);
    }

    @Test
    public void testCacheMemcachedAuthenticationEnabled() {
        CoreProperties.Cache.Memcached.Authentication obj = properties.getCache().getMemcached().getAuthentication();
        boolean val = PropertyDefaults.Cache.Memcached.Authentication.enabled;
        assertThat(obj.isEnabled()).isEqualTo(val);
        val = false;
        obj.setEnabled(val);
        assertThat(obj.isEnabled()).isEqualTo(val);
    }

    @Test
    public void testCacheMemcachedAuthenticationPassword() {
        CoreProperties.Cache.Memcached.Authentication obj = properties.getCache().getMemcached().getAuthentication();
        assertThat(obj.getPassword()).isEqualTo(null);
        obj.setPassword("MEMCACHEPASSWORD");
        assertThat(obj.getPassword()).isEqualTo("MEMCACHEPASSWORD");
    }

    @Test
    public void testCacheMemcachedAuthenticationUsername() {
        CoreProperties.Cache.Memcached.Authentication obj = properties.getCache().getMemcached().getAuthentication();
        assertThat(obj.getUsername()).isEqualTo(null);
        obj.setUsername("MEMCACHEUSER");
        assertThat(obj.getUsername()).isEqualTo("MEMCACHEUSER");
    }

    @Test
    public void testCacheRedisServer() {
        CoreProperties.Cache.Redis obj = properties.getCache().getRedis();
        String[] val = PropertyDefaults.Cache.Redis.server;
        assertThat(obj.getServer()).isEqualTo(val);
        val = new String[]{"myserver:1337"};
        obj.setServer(val);
        assertThat(obj.getServer()).isEqualTo(val);
    }

    @Test
    public void testCacheRedisExpiration() {
        CoreProperties.Cache.Redis obj = properties.getCache().getRedis();
        int val = PropertyDefaults.Cache.Redis.expiration;
        assertThat(obj.getExpiration()).isEqualTo(val);
        val++;
        obj.setExpiration(val);
        assertThat(obj.getExpiration()).isEqualTo(val);
    }

    @Test
    public void testCacheRedisCluster() {
        CoreProperties.Cache.Redis obj = properties.getCache().getRedis();
        boolean val = PropertyDefaults.Cache.Redis.cluster;
        assertThat(obj.isCluster()).isEqualTo(val);
        val = !val;
        obj.setCluster(val);
        assertThat(obj.isCluster()).isEqualTo(val);
    }

    @Test
    public void testCacheRedisConnectionMinimumIdleSize() {
        CoreProperties.Cache.Redis obj = properties.getCache().getRedis();
        int val = PropertyDefaults.Cache.Redis.connectionMinimumIdleSize;
        assertThat(obj.getConnectionMinimumIdleSize()).isEqualTo(val);
        val++;
        obj.setConnectionMinimumIdleSize(val);
        assertThat(obj.getConnectionMinimumIdleSize()).isEqualTo(val);
    }

    @Test
    public void testCacheRedisConnectionPoolSize() {
        CoreProperties.Cache.Redis obj = properties.getCache().getRedis();
        int val = PropertyDefaults.Cache.Redis.connectionPoolSize;
        assertThat(obj.getConnectionPoolSize()).isEqualTo(val);
        val++;
        obj.setConnectionPoolSize(val);
        assertThat(obj.getConnectionPoolSize()).isEqualTo(val);
    }

    @Test
    public void testCacheRedisSubscriptionConnectionMinimumIdleSize() {
        CoreProperties.Cache.Redis obj = properties.getCache().getRedis();
        int val = PropertyDefaults.Cache.Redis.subscriptionConnectionMinimumIdleSize;
        assertThat(obj.getSubscriptionConnectionMinimumIdleSize()).isEqualTo(val);
        val++;
        obj.setSubscriptionConnectionMinimumIdleSize(val);
        assertThat(obj.getSubscriptionConnectionMinimumIdleSize()).isEqualTo(val);
    }

    @Test
    public void testCacheRedisSubscriptionConnectionPoolSize() {
        CoreProperties.Cache.Redis obj = properties.getCache().getRedis();
        int val = PropertyDefaults.Cache.Redis.subscriptionConnectionPoolSize;
        assertThat(obj.getSubscriptionConnectionPoolSize()).isEqualTo(val);
        val++;
        obj.setSubscriptionConnectionPoolSize(val);
        assertThat(obj.getSubscriptionConnectionPoolSize()).isEqualTo(val);

    }

    @Test
    public void testMailFrom() {
        CoreProperties.Mail obj = properties.getMail();
        String val = PropertyDefaults.Mail.from;
        assertThat(obj.getFrom()).isEqualTo(val);
        val = "1" + val;
        obj.setFrom(val);
        assertThat(obj.getFrom()).isEqualTo(val);
    }

    @Test
    public void testMailBaseUrl() {
        CoreProperties.Mail obj = properties.getMail();
        String val = PropertyDefaults.Mail.baseUrl;
        assertThat(obj.getBaseUrl()).isEqualTo(val);
        val = "1" + val;
        obj.setBaseUrl(val);
        assertThat(obj.getBaseUrl()).isEqualTo(val);
    }

    @Test
    public void testMailEnabled() {
        CoreProperties.Mail obj = properties.getMail();
        boolean val = PropertyDefaults.Mail.enabled;
        assertThat(obj.isEnabled()).isEqualTo(val);
        val = !val;
        obj.setEnabled(val);
        assertThat(obj.isEnabled()).isEqualTo(val);
    }

    @Test
    public void testSecurityClientAuthorizationAccessTokenUri() {
        CoreProperties.Security.ClientAuthorization obj = properties.getSecurity().getClientAuthorization();
        String val = PropertyDefaults.Security.ClientAuthorization.accessTokenUri;
        assertThat(obj.getAccessTokenUri()).isEqualTo(val);
        val = "1" + val;
        obj.setAccessTokenUri(val);
        assertThat(obj.getAccessTokenUri()).isEqualTo(val);
    }

    @Test
    public void testSecurityClientAuthorizationTokenServiceId() {
        CoreProperties.Security.ClientAuthorization obj = properties.getSecurity().getClientAuthorization();
        String val = PropertyDefaults.Security.ClientAuthorization.tokenServiceId;
        assertThat(obj.getTokenServiceId()).isEqualTo(val);
        val = "1" + val;
        obj.setTokenServiceId(val);
        assertThat(obj.getTokenServiceId()).isEqualTo(val);
    }

    @Test
    public void testSecurityClientAuthorizationClientId() {
        CoreProperties.Security.ClientAuthorization obj = properties.getSecurity().getClientAuthorization();
        String val = PropertyDefaults.Security.ClientAuthorization.clientId;
        assertThat(obj.getClientId()).isEqualTo(val);
        val = "1" + val;
        obj.setClientId(val);
        assertThat(obj.getClientId()).isEqualTo(val);
    }

    @Test
    public void testSecurityClientAuthorizationClientSecret() {
        CoreProperties.Security.ClientAuthorization obj = properties.getSecurity().getClientAuthorization();
        String val = PropertyDefaults.Security.ClientAuthorization.clientSecret;
        assertThat(obj.getClientSecret()).isEqualTo(val);
        val = "1" + val;
        obj.setClientSecret(val);
        assertThat(obj.getClientSecret()).isEqualTo(val);
    }

    @Test
    public void testSecurityAuthenticationJwtSecret() {
        CoreProperties.Security.Authentication.Jwt obj = properties.getSecurity().getAuthentication().getJwt();
        String val = PropertyDefaults.Security.Authentication.Jwt.secret;
        assertThat(obj.getSecret()).isEqualTo(val);
        val = "1" + val;
        obj.setSecret(val);
        assertThat(obj.getSecret()).isEqualTo(val);
    }

    @Test
    public void testSecurityAuthenticationJwtBase64Secret() {
        CoreProperties.Security.Authentication.Jwt obj = properties.getSecurity().getAuthentication().getJwt();
        String val = PropertyDefaults.Security.Authentication.Jwt.base64Secret;
        assertThat(obj.getSecret()).isEqualTo(val);
        val = "1" + val;
        obj.setBase64Secret(val);
        assertThat(obj.getBase64Secret()).isEqualTo(val);
    }

    @Test
    public void testSecurityAuthenticationJwtTokenValidityInSeconds() {
        CoreProperties.Security.Authentication.Jwt obj = properties.getSecurity().getAuthentication().getJwt();
        long val = PropertyDefaults.Security.Authentication.Jwt.tokenValidityInSeconds;
        assertThat(obj.getTokenValidityInSeconds()).isEqualTo(val);
        val++;
        obj.setTokenValidityInSeconds(val);
        assertThat(obj.getTokenValidityInSeconds()).isEqualTo(val);
    }

    @Test
    public void testSecurityAuthenticationJwtTokenValidityInSecondsForRememberMe() {
        CoreProperties.Security.Authentication.Jwt obj = properties.getSecurity().getAuthentication().getJwt();
        long val = PropertyDefaults.Security.Authentication.Jwt.tokenValidityInSecondsForRememberMe;
        assertThat(obj.getTokenValidityInSecondsForRememberMe()).isEqualTo(val);
        val++;
        obj.setTokenValidityInSecondsForRememberMe(val);
        assertThat(obj.getTokenValidityInSecondsForRememberMe()).isEqualTo(val);
    }

    @Test
    public void testSecurityRememberMeKey() {
        CoreProperties.Security.RememberMe obj = properties.getSecurity().getRememberMe();
        String val = PropertyDefaults.Security.RememberMe.key;
        assertThat(obj.getKey()).isEqualTo(val);
        val = "1" + val;
        obj.setKey(val);
        assertThat(obj.getKey()).isEqualTo(val);
    }

    @Test
    public void testSecurityOauth2Audience() {
        CoreProperties.Security.OAuth2 obj = properties.getSecurity().getOauth2();
        assertThat(obj).isNotNull();
        assertThat(obj.getAudience()).isNotNull().isEmpty();

        obj.setAudience(Arrays.asList("default", "account"));
        assertThat(obj.getAudience()).isNotEmpty().size().isEqualTo(2);
        assertThat(obj.getAudience()).contains("default", "account");
    }

    @Test
    public void testApiDocsTitle() {
        CoreProperties.ApiDocs obj = properties.getApiDocs();
        String val = PropertyDefaults.ApiDocs.title;
        assertThat(obj.getTitle()).isEqualTo(val);
        val = "1" + val;
        obj.setTitle(val);
        assertThat(obj.getTitle()).isEqualTo(val);
    }

    @Test
    public void testApiDocsDescription() {
        CoreProperties.ApiDocs obj = properties.getApiDocs();
        String val = PropertyDefaults.ApiDocs.description;
        assertThat(obj.getDescription()).isEqualTo(val);
        val = "1" + val;
        obj.setDescription(val);
        assertThat(obj.getDescription()).isEqualTo(val);
    }

    @Test
    public void testApiDocsVersion() {
        CoreProperties.ApiDocs obj = properties.getApiDocs();
        String val = PropertyDefaults.ApiDocs.version;
        assertThat(obj.getVersion()).isEqualTo(val);
        val = "1" + val;
        obj.setVersion(val);
        assertThat(obj.getVersion()).isEqualTo(val);
    }

    @Test
    public void testApiDocsTermsOfServiceUrl() {
        CoreProperties.ApiDocs obj = properties.getApiDocs();
        String val = PropertyDefaults.ApiDocs.termsOfServiceUrl;
        assertThat(obj.getTermsOfServiceUrl()).isEqualTo(val);
        val = "1" + val;
        obj.setTermsOfServiceUrl(val);
        assertThat(obj.getTermsOfServiceUrl()).isEqualTo(val);
    }

    @Test
    public void testApiDocsContactName() {
        CoreProperties.ApiDocs obj = properties.getApiDocs();
        String val = PropertyDefaults.ApiDocs.contactName;
        assertThat(obj.getContactName()).isEqualTo(val);
        val = "1" + val;
        obj.setContactName(val);
        assertThat(obj.getContactName()).isEqualTo(val);
    }

    @Test
    public void testApiDocsContactUrl() {
        CoreProperties.ApiDocs obj = properties.getApiDocs();
        String val = PropertyDefaults.ApiDocs.contactUrl;
        assertThat(obj.getContactUrl()).isEqualTo(val);
        val = "1" + val;
        obj.setContactUrl(val);
        assertThat(obj.getContactUrl()).isEqualTo(val);
    }

    @Test
    public void testApiDocsContactEmail() {
        CoreProperties.ApiDocs obj = properties.getApiDocs();
        String val = PropertyDefaults.ApiDocs.contactEmail;
        assertThat(obj.getContactEmail()).isEqualTo(val);
        val = "1" + val;
        obj.setContactEmail(val);
        assertThat(obj.getContactEmail()).isEqualTo(val);
    }

    @Test
    public void testApiDocsLicense() {
        CoreProperties.ApiDocs obj = properties.getApiDocs();
        String val = PropertyDefaults.ApiDocs.license;
        assertThat(obj.getLicense()).isEqualTo(val);
        val = "1" + val;
        obj.setLicense(val);
        assertThat(obj.getLicense()).isEqualTo(val);
    }

    @Test
    public void testApiDocsLicenseUrl() {
        CoreProperties.ApiDocs obj = properties.getApiDocs();
        String val = PropertyDefaults.ApiDocs.licenseUrl;
        assertThat(obj.getLicenseUrl()).isEqualTo(val);
        val = "1" + val;
        obj.setLicenseUrl(val);
        assertThat(obj.getLicenseUrl()).isEqualTo(val);
    }

    @Test
    public void testApiDocsDefaultIncludePattern() {
        CoreProperties.ApiDocs obj = properties.getApiDocs();
        String val = PropertyDefaults.ApiDocs.defaultIncludePattern;
        assertThat(obj.getDefaultIncludePattern()).isEqualTo(val);
        val = "1" + val;
        obj.setDefaultIncludePattern(val);
        assertThat(obj.getDefaultIncludePattern()).isEqualTo(val);
    }

    @Test
    public void testApiDocsHost() {
        CoreProperties.ApiDocs obj = properties.getApiDocs();
        String val = PropertyDefaults.ApiDocs.host;
        assertThat(obj.getHost()).isEqualTo(val);
        val = "1" + val;
        obj.setHost(val);
        assertThat(obj.getHost()).isEqualTo(val);
    }

    @Test
    public void testApiDocsProtocols() {
        CoreProperties.ApiDocs obj = properties.getApiDocs();
        String[] def = PropertyDefaults.ApiDocs.protocols;
        ArrayList<String> val;
        if (def != null) {
            val = newArrayList(def);
            assertThat(obj.getProtocols()).containsExactlyElementsOf(newArrayList(val));
        } else {
            assertThat(obj.getProtocols()).isNull();
            def = new String[1];
            val = new ArrayList<>(1);
        }
        val.add("1");
        obj.setProtocols(val.toArray(def));
        assertThat(obj.getProtocols()).containsExactlyElementsOf(newArrayList(val));
    }

    @Test
    public void testApiDocsServers() {
        CoreProperties.ApiDocs obj = properties.getApiDocs();
        assertThat(obj.getServers().length).isEqualTo(0);
        CoreProperties.ApiDocs.Server server = new CoreProperties.ApiDocs.Server();
        server.setUrl("url");
        server.setDescription("description");
        server.setName("name");

        CoreProperties.ApiDocs.Server[] val = new CoreProperties.ApiDocs.Server[]{server};

        obj.setServers(val);
        assertThat(obj.getServers().length).isEqualTo(1);
        assertThat(obj.getServers()[0].getName()).isEqualTo(server.getName());
        assertThat(obj.getServers()[0].getUrl()).isEqualTo(server.getUrl());
        assertThat(obj.getServers()[0].getDescription()).isEqualTo(server.getDescription());
    }

    @Test
    public void testApiDocsUseDefaultResponseMessages() {
        CoreProperties.ApiDocs obj = properties.getApiDocs();
        boolean val = PropertyDefaults.ApiDocs.useDefaultResponseMessages;
        assertThat(obj.isUseDefaultResponseMessages()).isEqualTo(val);
        val = false;
        obj.setUseDefaultResponseMessages(val);
        assertThat(obj.isUseDefaultResponseMessages()).isEqualTo(val);
    }

    @Test
    public void testMetricsLogsEnabled() {
        CoreProperties.Metrics.Logs obj = properties.getMetrics().getLogs();
        boolean val = PropertyDefaults.Metrics.Logs.enabled;
        assertThat(obj.isEnabled()).isEqualTo(val);
        val = !val;
        obj.setEnabled(val);
        assertThat(obj.isEnabled()).isEqualTo(val);
    }

    @Test
    public void testMetricsLogsReportFrequency() {
        CoreProperties.Metrics.Logs obj = properties.getMetrics().getLogs();
        long val = PropertyDefaults.Metrics.Logs.reportFrequency;
        assertThat(obj.getReportFrequency()).isEqualTo(val);
        val++;
        obj.setReportFrequency(val);
        assertThat(obj.getReportFrequency()).isEqualTo(val);
    }

    @Test
    public void testLoggingUseJsonFormat() {
        CoreProperties.Logging obj = properties.getLogging();
        boolean val = PropertyDefaults.Logging.useJsonFormat;
        assertThat(obj.isUseJsonFormat()).isEqualTo(val);
        val = true;
        obj.setUseJsonFormat(val);
        assertThat(obj.isUseJsonFormat()).isEqualTo(val);
    }

    @Test
    public void testLoggingLogstashEnabled() {
        CoreProperties.Logging.Logstash obj = properties.getLogging().getLogstash();
        boolean val = PropertyDefaults.Logging.Logstash.enabled;
        assertThat(obj.isEnabled()).isEqualTo(val);
        val = !val;
        obj.setEnabled(val);
        assertThat(obj.isEnabled()).isEqualTo(val);
    }

    @Test
    public void testLoggingLogstashHost() {
        CoreProperties.Logging.Logstash obj = properties.getLogging().getLogstash();
        String val = PropertyDefaults.Logging.Logstash.host;
        assertThat(obj.getHost()).isEqualTo(val);
        val = "1" + val;
        obj.setHost(val);
        assertThat(obj.getHost()).isEqualTo(val);
    }

    @Test
    public void testLoggingLogstashPort() {
        CoreProperties.Logging.Logstash obj = properties.getLogging().getLogstash();
        int val = PropertyDefaults.Logging.Logstash.port;
        assertThat(obj.getPort()).isEqualTo(val);
        val++;
        obj.setPort(val);
        assertThat(obj.getPort()).isEqualTo(val);
    }

    @Test
    public void testLoggingLogstashQueueSize() {
        CoreProperties.Logging.Logstash obj = properties.getLogging().getLogstash();
        int val = PropertyDefaults.Logging.Logstash.queueSize;
        assertThat(obj.getQueueSize()).isEqualTo(val);
        val++;
        obj.setQueueSize(val);
        assertThat(obj.getQueueSize()).isEqualTo(val);
    }

    @Test
    public void testSocialRedirectAfterSignIn() {
        CoreProperties.Social obj = properties.getSocial();
        String val = PropertyDefaults.Social.redirectAfterSignIn;
        assertThat(obj.getRedirectAfterSignIn()).isEqualTo(val);
        val = "1" + val;
        obj.setRedirectAfterSignIn(val);
        assertThat(obj.getRedirectAfterSignIn()).isEqualTo(val);
    }

    @Test
    public void testGatewayAuthorizedMicroservicesEndpoints() {
        CoreProperties.Gateway obj = properties.getGateway();
        Map<String, List<String>> val = PropertyDefaults.Gateway.authorizedMicroservicesEndpoints;
        assertThat(obj.getAuthorizedMicroservicesEndpoints()).isEqualTo(val);
        val.put("1", null);
        obj.setAuthorizedMicroservicesEndpoints(val);
        assertThat(obj.getAuthorizedMicroservicesEndpoints()).isEqualTo(val);
    }

    @Test
    public void testGatewayRateLimitingEnabled() {
        CoreProperties.Gateway.RateLimiting obj = properties.getGateway().getRateLimiting();
        boolean val = PropertyDefaults.Gateway.RateLimiting.enabled;
        assertThat(obj.isEnabled()).isEqualTo(val);
        val = !val;
        obj.setEnabled(val);
        assertThat(obj.isEnabled()).isEqualTo(val);
    }

    @Test
    public void testGatewayRateLimitingLimit() {
        CoreProperties.Gateway.RateLimiting obj = properties.getGateway().getRateLimiting();
        long val = PropertyDefaults.Gateway.RateLimiting.limit;
        assertThat(obj.getLimit()).isEqualTo(val);
        val++;
        obj.setLimit(val);
        assertThat(obj.getLimit()).isEqualTo(val);
    }

    @Test
    public void testGatewayRateLimitingDurationInSeconds() {
        CoreProperties.Gateway.RateLimiting obj = properties.getGateway().getRateLimiting();
        int val = PropertyDefaults.Gateway.RateLimiting.durationInSeconds;
        assertThat(obj.getDurationInSeconds()).isEqualTo(val);
        val++;
        obj.setDurationInSeconds(val);
        assertThat(obj.getDurationInSeconds()).isEqualTo(val);
    }

    @Test
    public void testRegistryPassword() {
        CoreProperties.Registry obj = properties.getRegistry();
        String val = PropertyDefaults.Registry.password;
        assertThat(obj.getPassword()).isEqualTo(val);
        val = "1" + val;
        obj.setPassword(val);
        assertThat(obj.getPassword()).isEqualTo(val);
    }

    @Test
    public void testClientAppName() {
        CoreProperties.ClientApp obj = properties.getClientApp();
        String val = PropertyDefaults.ClientApp.name;
        assertThat(obj.getName()).isEqualTo(val);
        val = "1" + val;
        obj.setName(val);
        assertThat(obj.getName()).isEqualTo(val);
    }

    @Test
    public void testAuditEventsRetentionPeriod() {
        CoreProperties.AuditEvents obj = properties.getAuditEvents();
        int val = PropertyDefaults.AuditEvents.retentionPeriod;
        assertThat(obj.getRetentionPeriod()).isEqualTo(val);
        val++;
        obj.setRetentionPeriod(val);
        assertThat(obj.getRetentionPeriod()).isEqualTo(val);
    }
}
