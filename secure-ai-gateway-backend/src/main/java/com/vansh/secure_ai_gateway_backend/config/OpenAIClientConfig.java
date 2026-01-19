package com.vansh.secure_ai_gateway_backend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class OpenAIClientConfig {

    @Bean
    public RestClient openAiRestClient(
            @Value("${openai.api.url}") String apiUrl,
            @Value("${openai.api.key}") String apiKey) {

        return RestClient.builder()
                .baseUrl(apiUrl)
                .defaultHeader("Authorization", "Bearer " + apiKey)
                .defaultHeader("Content-Type", "application/json")
                .build();
    }
}
