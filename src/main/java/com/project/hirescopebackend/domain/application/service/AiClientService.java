package com.project.hirescopebackend.domain.application.service;

import com.project.hirescopebackend.domain.application.dto.ai.AiAnalysisRequest;
import com.project.hirescopebackend.domain.application.dto.ai.AiAnalysisResponse;
import com.project.hirescopebackend.domain.application.dto.ai.JobPostingData;
import com.project.hirescopebackend.domain.application.dto.ai.ResumeData;
import com.project.hirescopebackend.domain.application.entity.Application;
import com.project.hirescopebackend.domain.application.repository.ApplicationRepository;
import com.project.hirescopebackend.global.exception.BusinessException;
import com.project.hirescopebackend.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Slf4j
@Service
@RequiredArgsConstructor
public class AiClientService {

    private final WebClient webClient;
    private final ApplicationRepository applicationRepository;
    private final AnalysisResultService analysisResultService;

    @Value("${ai-server.url}")
    private String aiServerUrl;

    @Async("aiAnalysisExecutor")
    @Transactional(readOnly = true)
    public void requestAnalysis(Long applicationId) {
        log.info("[AI 분석 요청 시작] applicationId={}", applicationId);

        try {
            Application application = applicationRepository.findById(applicationId)
                    .orElseThrow(() -> new BusinessException(ErrorCode.APPLICATION_NOT_FOUND));

            // 상태를 PROCESSING으로 변경 (별도 트랜잭션)
            analysisResultService.markApplicationProcessing(applicationId);

            AiAnalysisRequest request = buildAnalysisRequest(application);
            log.info("[AI 요청 객체 빌드 완료] applicationId={}, url={}/api/analysis", applicationId, aiServerUrl);

            webClient.post()
                    .uri("/api/analysis")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(AiAnalysisResponse.class)
                    .doOnSubscribe(sub -> log.info("[WebClient 구독 시작] applicationId={}", applicationId))
                    .doOnSuccess(res -> log.info("[WebClient 응답 수신] applicationId={}", applicationId))
                    .doOnError(err -> log.error("[WebClient 에러 감지] applicationId={}, error={}", applicationId, err.getMessage()))
                    .subscribe(
                            response -> handleAnalysisSuccess(applicationId, response),
                            error -> handleAnalysisError(applicationId, error)
                    );

        } catch (Exception e) {
            log.error("[AI 분석 요청 중 예외 발생] applicationId={}, error={}", applicationId, e.getMessage(), e);
            analysisResultService.markApplicationFailed(applicationId);
        }
    }

    private AiAnalysisRequest buildAnalysisRequest(Application application) {
        log.debug("[buildAnalysisRequest] resumeId={}, jobPostingId={}",
                application.getResume().getId(), application.getJobPosting().getId());
        return AiAnalysisRequest.builder()
                .applicationId(application.getId())
                .resume(ResumeData.from(application.getResume()))
                .jobPosting(JobPostingData.from(application.getJobPosting()))
                .build();
    }

    private void handleAnalysisSuccess(Long applicationId, AiAnalysisResponse response) {
        log.info("[AI 분석 완료] applicationId={}", applicationId);
        try {
            analysisResultService.saveAnalysisResult(response);
        } catch (Exception e) {
            log.error("[AI 분석 결과 저장 실패] applicationId={}, error={}", applicationId, e.getMessage(), e);
            analysisResultService.markApplicationFailed(applicationId);
        }
    }

    private void handleAnalysisError(Long applicationId, Throwable error) {
        if (error instanceof WebClientResponseException ex) {
            log.error("[AI 서버 HTTP 에러] applicationId={}, status={}, body={}",
                    applicationId, ex.getStatusCode(), ex.getResponseBodyAsString());
        } else {
            log.error("[AI 분석 요청 실패] applicationId={}, errorType={}, error={}",
                    applicationId, error.getClass().getSimpleName(), error.getMessage(), error);
        }
        analysisResultService.markApplicationFailed(applicationId);
    }
}
