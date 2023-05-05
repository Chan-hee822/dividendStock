package com.example.dividendstock.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

@RequiredArgsConstructor
@Configuration
public class CacheConfig {

    @Value("${spring.redis.host}")
    private String host;

    @Value("${spring.redis.port}")
    private int port;

    //Cache 에 적용시켜 사용하기 위한 빈
    @Bean
    public CacheManager redisCacheManager(
            RedisConnectionFactory redisConnectionFactory) {

        //직렬화(바이트화) 필요
        // redis 는 자바 시스템 외부 캐시서버이기 때문에 데이터 저장하기 위해서
        // redis 에서 데이터 불러올 때는 역직렬화 필요
        RedisCacheConfiguration conf =
                RedisCacheConfiguration.defaultCacheConfig()
                    .serializeKeysWith(RedisSerializationContext
                                    .SerializationPair
                                    .fromSerializer(new StringRedisSerializer()))
                    .serializeValuesWith(RedisSerializationContext
                                    .SerializationPair
                                    .fromSerializer(new GenericJackson2JsonRedisSerializer()))
                    ;

        return RedisCacheManager.RedisCacheManagerBuilder
                        .fromConnectionFactory(redisConnectionFactory)
                        .cacheDefaults(conf)
                        .build();
    }

    //redis 서버와 연결 connection 을 관리하기 위한 레디스 커넥션 펙토리 빈 생성
    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        // single instance server -> stand alone configuration instance 생성 -> 설정 정보 초기화
        RedisStandaloneConfiguration conf = new RedisStandaloneConfiguration();
        conf.setHostName(this.host);
        conf.setPort(this.port);

        return new LettuceConnectionFactory(conf);
    }

}
