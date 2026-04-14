package com.project.hirescopebackend.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient webClient() {
        // TODO: AI 서버 base URL 등 설정
        return WebClient.builder().build();
    }
}
