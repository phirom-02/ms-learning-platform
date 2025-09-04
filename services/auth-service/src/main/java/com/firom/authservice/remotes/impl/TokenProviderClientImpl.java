package com.firom.authservice.remotes.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.firom.authservice.exceptions.BusinessException;
import com.firom.authservice.remotes.client.TokenProviderClient;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class TokenProviderClientImpl implements TokenProviderClient {

    @Value("${application.remotes.oauth2-token-url}")
    private String oauth2TokenUrl;

    @Value("${application.oauth2.client-id}")
    private String clientId;

    @Value("${application.oauth2.client-secret}")
    private String clientSecret;

    private final OkHttpClient client = new OkHttpClient();

    @Override
    public String getAccessToken() {
        FormBody body = new FormBody.Builder()
                .add("grant_type", "client_credentials")
                .add("client_id", clientId)
                .add("client_secret", clientSecret)
                .add("scope", "read write")
                .build();

        Request request = new Request.Builder()
                .url(oauth2TokenUrl)
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new RuntimeException("Failed to get token" + response);
            }

            String responseBody = response.body().string();
            return new ObjectMapper()
                    .readTree(responseBody)
                    .get("access_token")
                    .asText();
        } catch (Exception e) {
            throw new BusinessException(e.getMessage());
        }
    }
}
