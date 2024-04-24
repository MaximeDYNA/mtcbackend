package com.catis.config;

import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair;
import java.time.Duration;

@Configuration
@EnableCaching
public class RedisCacheConfig{

    @Bean
    public RedisCacheConfiguration cacheConfiguration() {
    JdkSerializationRedisSerializer contextAwareRedisSerializer = new JdkSerializationRedisSerializer(getClass().getClassLoader());
    return RedisCacheConfiguration.defaultCacheConfig(Thread.currentThread().getContextClassLoader())
      .entryTtl(Duration.ofMinutes(10))
      .disableCachingNullValues()
      .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(contextAwareRedisSerializer));
    //   .serializeValuesWith(SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));
}

    @Bean
    public RedisCacheManagerBuilderCustomizer redisCacheManagerBuilderCustomizer() {
        return (builder) -> builder
        .withCacheConfiguration("catproductsCache",
            RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofHours(12)))
        .withCacheConfiguration("productsCache",
            RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofHours(12)))
        .withCacheConfiguration("VisiteCache",
            RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofMinutes(30)))
        .withCacheConfiguration("cgenergyCache",
            RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofHours(12)))
        .withCacheConfiguration("lignesCache",
            RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofHours(12)))
        .withCacheConfiguration("cgbrandCache",
            RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofHours(12)));
    }

}

// @Bean(name="RedisCacheManager")
// public CacheManager getManager() {
//     JdkSerializationRedisSerializer contextAwareRedisSerializer = new JdkSerializationRedisSerializer(getClass().getClassLoader());

//     RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
//             .disableCachingNullValues()
//             .entryTtl(Duration.ofHours(12)
//             .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(contextAwareRedisSerializer));


//      return  RedisCacheManager.RedisCacheManagerBuilder.fromConnectionFactory(connectionFactory())
//             .cacheDefaults(redisCacheConfiguration)
//             .build();
   
// }
// .defaultCacheConfig()


// import org.springframework.cache.CacheManager;
// import org.springframework.cache.annotation.EnableCaching;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.data.redis.cache.RedisCacheConfiguration;
// import org.springframework.data.redis.cache.RedisCacheManager;
// import org.springframework.data.redis.connection.RedisConnectionFactory;
// import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
// import org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair;

// import java.time.Duration;

// @Configuration
// @EnableCaching
// public class CacheConfiguration {

//     @Bean
//     public CacheManager cacheManager(RedisConnectionFactory connectionFactory) {
//         RedisCacheConfiguration defaultConfig = RedisCacheConfiguration.defaultCacheConfig()
//                 .entryTtl(Duration.ofMinutes(60))
//                 .disableCachingNullValues()
//                 .serializeValuesWith(SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));

//         return RedisCacheManager.builder(connectionFactory)
//                 .cacheDefaults(defaultConfig)
//                 .build();
//     }

//     @Bean
//     public RedisCacheManagerBuilderCustomizer redisCacheManagerBuilderCustomizer() {
//         return builder -> builder
//                 .withCacheConfiguration("itemCache",
//                         RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofMinutes(10)))
//                 .withCacheConfiguration("customerCache",
//                         RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofMinutes(5)));
//     }
// }
