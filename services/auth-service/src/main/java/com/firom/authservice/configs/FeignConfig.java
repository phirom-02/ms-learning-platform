package com.firom.authservice.configs;

import com.firom.authservice.remotes.client.TokenProviderClient;
import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfig {
    @Bean
    public RequestInterceptor requestInterceptor(TokenProviderClient tokenProviderClient) {
        return template -> {
            String token = tokenProviderClient.getAccessToken();
            template.header("Authorization", "Bearer " + token);
        };
    }
}
