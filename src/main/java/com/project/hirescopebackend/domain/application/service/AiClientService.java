package com.project.hirescopebackend.domain.application.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Slf4j
@Service
public class AiClientService {

    private final WebClient webClient;

    @Value("${ai-server.url}")
    private String aiServerUrl;

    public AiClientService() {
        this.webClient = WebClient.create();
    }

    /**
     * FastAPI AI 서버에 분석 요청 (fire-and-forget 비동기)
     * 응답을 기다리지 않고 즉시 반환 (분석은 최대 5분 소요)
     *
     * @param applicationId 분석 대상 지원서 ID
     */
    public void requestAnalysis(Long applicationId) {
        log.info("[AI 분석 요청] applicationId={}", applicationId);

        webClient.post()
                .uri(aiServerUrl + "/api/analyze")
                .bodyValue(Map.of("applicationId", applicationId))
                .retrieve()
                .bodyToMono(Void.class)
                .subscribe(
                        null, // 성공 콜백 (fire-and-forget 이므로 처리 없음)
                        error -> log.error("[AI 분석 요청 실패] applicationId={}, error={}",
                                applicationId, error.getMessage())
                );
    }
}
