package com.firom.lms.configs;

import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;

@Configuration
public class FeignConfig {

    @Value("${application.oauth2.enrollment-service-client.client-id}")
    private String enrollmentServiceClientId;

    @Bean
    public RequestInterceptor requestInterceptor(OAuth2AuthorizedClientManager manager) {
        return requestTemplate -> {
            OAuth2AuthorizeRequest authorizeRequest = OAuth2AuthorizeRequest.withClientRegistrationId(enrollmentServiceClientId)
                    .principal(enrollmentServiceClientId)
                    .build();

            OAuth2AuthorizedClient authorizedClient = manager.authorize(authorizeRequest);

            if (authorizeRequest != null && authorizedClient != null) {
                String token = authorizedClient.getAccessToken().getTokenValue();
                requestTemplate.header("Authorization", "Bearer " + token);
            }
        };
    }

}
