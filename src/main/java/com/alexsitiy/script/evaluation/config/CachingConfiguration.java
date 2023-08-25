package com.alexsitiy.script.evaluation.config;

import com.alexsitiy.script.evaluation.dto.JSScriptFullReadDto;
import org.ehcache.config.CacheConfiguration;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.ExpiryPolicyBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.jsr107.Eh107Configuration;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.jcache.JCacheCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.cache.Caching;
import javax.cache.spi.CachingProvider;
import java.time.Duration;
import java.time.temporal.ChronoUnit;

/**
 * The configuration class that adds {@link CacheManager} bean to SpringContainer.
 * CacheManager is created via EhCache.
 *
 * @see org.springframework.context.ApplicationContext
 * @see Eh107Configuration
 * @see javax.cache.CacheManager
 */
@Profile("!test")
@Configuration
@EnableCaching
public class CachingConfiguration {

    /**
     * Creates a {@link CacheManager} bean that comprises js-tasks storage
     * that includes the following configurations:
     * <br/>
     * 1. Heap capacity equals 10
     * <br/>
     * 2. Expiration time is 30 SECONDS
     *
     * @return {@link CacheManager} that is used for caching JavaScript scripts.
     */
    @Bean
    public CacheManager cacheManager() {
        CacheConfiguration<String, JSScriptFullReadDto> config = CacheConfigurationBuilder
                .newCacheConfigurationBuilder(String.class, JSScriptFullReadDto.class, ResourcePoolsBuilder.heap(10))
                .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.of(30, ChronoUnit.SECONDS)))
                .build();

        var configuration = Eh107Configuration.fromEhcacheCacheConfiguration(config);
        CachingProvider provider = Caching.getCachingProvider();
        var cacheManager = provider.getCacheManager();

        cacheManager.createCache("js-tasks", configuration);

        return new JCacheCacheManager(cacheManager);
    }
}
