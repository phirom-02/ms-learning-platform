package com.firom.authservice.configs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.firom.authservice.entRepo.RefreshToken;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, RefreshToken> refreshTokenRedisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, RefreshToken> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        // Use Jackson JSON serializer specifically for RefreshToken class
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules(); // handles Java 8 time, UUID, etc.
        Jackson2JsonRedisSerializer<RefreshToken> serializer = new Jackson2JsonRedisSerializer<>(objectMapper, RefreshToken.class);

        template.setDefaultSerializer(serializer);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(serializer);
        template.afterPropertiesSet();

        return template;
    }
}
