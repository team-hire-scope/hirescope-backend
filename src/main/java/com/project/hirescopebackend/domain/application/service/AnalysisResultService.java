package com.project.hirescopebackend.domain.application.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.hirescopebackend.domain.application.dto.ai.AiAnalysisResponse;
import com.project.hirescopebackend.domain.application.dto.ai.InterviewQuestionResponse;
import com.project.hirescopebackend.domain.application.entity.AnalysisResult;
import com.project.hirescopebackend.domain.application.entity.Application;
import com.project.hirescopebackend.domain.application.entity.ApplicationStatus;
import com.project.hirescopebackend.domain.application.repository.AnalysisResultRepository;
import com.project.hirescopebackend.domain.application.repository.ApplicationRepository;
import com.project.hirescopebackend.domain.job.entity.JobPosting;
import com.project.hirescopebackend.global.exception.BusinessException;
import com.project.hirescopebackend.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AnalysisResultService {

    private final ApplicationRepository applicationRepository;
    private final AnalysisResultRepository analysisResultRepository;
    private final ObjectMapper objectMapper;

    /**
     * AI 분석 결과를 저장합니다.
     * AI 서버의 total_score는 무시하고 가중치 기반으로 재계산합니다.
     *
     * @param response AI 서버 응답
     */
    @Transactional
    public void saveAnalysisResult(AiAnalysisResponse response) {
        Application application = applicationRepository.findById(response.getApplicationId())
                .orElseThrow(() -> new BusinessException(ErrorCode.APPLICATION_NOT_FOUND));

        JobPosting jobPosting = application.getJobPosting();

        // 가중치 기반 총점 재계산 (AI 서버의 total_score 무시)
        double calculatedTotalScore = calculateWeightedScore(response, jobPosting);

        // 면접 질문 JSON 직렬화
        String interviewQuestionsJson = serializeInterviewQuestions(response.getInterviewQuestions());

        AnalysisResult analysisResult = AnalysisResult.builder()
                .application(application)
                .totalScore(calculatedTotalScore)
                .scoreJobFit(response.getScoreJobFit())
                .scoreCareerConsistency(response.getScoreCareerConsistency())
                .scoreSkillMatch(response.getScoreSkillMatch())
                .scoreQuantitativeAchievement(response.getScoreQuantitativeAchievement())
                .scoreDocumentQuality(response.getScoreDocumentQuality())
                .summary(response.getSummary())
                .interviewQuestions(interviewQuestionsJson)
                .analyzedAt(LocalDateTime.now())
                .build();

        analysisResultRepository.save(analysisResult);

        // 지원 상태를 COMPLETED로 업데이트
        application.updateStatus(ApplicationStatus.COMPLETED);

        log.info("[분석 결과 저장 완료] applicationId={}, totalScore={}",
                application.getId(), calculatedTotalScore);
    }

    /**
     * 지원 상태를 FAILED로 변경 (AI 분석 실패 시)
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void markApplicationFailed(Long applicationId) {
        applicationRepository.findById(applicationId).ifPresent(application -> {
            application.updateStatus(ApplicationStatus.FAILED);
            log.warn("[지원서 상태 FAILED 처리] applicationId={}", applicationId);
        });
    }

    /**
     * 지원 상태를 PROCESSING으로 변경 (AI 분석 시작 시)
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void markApplicationProcessing(Long applicationId) {
        applicationRepository.findById(applicationId).ifPresent(application -> {
            application.updateStatus(ApplicationStatus.PROCESSING);
            log.info("[지원서 상태 PROCESSING 처리] applicationId={}", applicationId);
        });
    }

    /**
     * 가중치 기반 총점 계산
     * formula: (score_x * weight_x + ...) / 100
     */
    private double calculateWeightedScore(AiAnalysisResponse response, JobPosting jobPosting) {
        double weightedSum =
                safeScore(response.getScoreJobFit()) * jobPosting.getWeightJobFit()
                + safeScore(response.getScoreCareerConsistency()) * jobPosting.getWeightCareerConsistency()
                + safeScore(response.getScoreSkillMatch()) * jobPosting.getWeightSkillMatch()
                + safeScore(response.getScoreQuantitativeAchievement()) * jobPosting.getWeightQuantitativeAchievement()
                + safeScore(response.getScoreDocumentQuality()) * jobPosting.getWeightDocumentQuality();

        double totalScore = weightedSum / 100.0;

        log.debug("[가중치 점수 계산] applicationId={}, weightedSum={}, totalScore={}",
                response.getApplicationId(), weightedSum, totalScore);

        return Math.round(totalScore * 100.0) / 100.0; // 소수점 2자리 반올림
    }

    private double safeScore(Double score) {
        return score != null ? score : 0.0;
    }

    /**
     * 면접 질문 리스트를 JSON 문자열로 직렬화
     */
    private String serializeInterviewQuestions(List<InterviewQuestionResponse> questions) {
        if (questions == null || questions.isEmpty()) return null;
        try {
            return objectMapper.writeValueAsString(questions);
        } catch (Exception e) {
            log.warn("[면접 질문 직렬화 실패]", e);
            return null;
        }
    }
}
