package com.project.hirescopebackend.domain.application.dto;

import com.project.hirescopebackend.domain.application.entity.AnalysisResult;
import com.project.hirescopebackend.domain.application.entity.ApplicationStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * GET /api/applications/{id}/result 응답 DTO
 * - APPLICANT: status, interviewQuestions, analyzedAt 만 반환 (HR 점수 필드는 null → JSON 미포함)
 * - HR: 전체 필드 반환
 * - PENDING/FAILED: status, message 만 반환
 * (application.yml jackson.default-property-inclusion: non_null 로 null 필드 자동 제외)
 */
@Getter
@Builder
public class AnalysisResultResponse {

    private ApplicationStatus status;
    private String message;             // PENDING / FAILED 시 사용

    // HR 전용 점수 필드 (APPLICANT에게는 null → 미포함)
    private Double totalScore;
    private Double scoreJobFit;
    private Double scoreCareerConsistency;
    private Double scoreSkillMatch;
    private Double scoreQuantitativeAchievement;
    private Double scoreDocumentQuality;
    private String summary;

    // 공통 (APPLICANT, HR 모두 반환)
    private List<InterviewQuestionDto> interviewQuestions;
    private LocalDateTime analyzedAt;

    // ── PENDING ──────────────────────────────────────────────────────

    public static AnalysisResultResponse pending() {
        return AnalysisResultResponse.builder()
                .status(ApplicationStatus.PENDING)
                .message("분석 중입니다. 잠시 후 다시 조회해 주세요.")
                .build();
    }

    // ── FAILED ───────────────────────────────────────────────────────

    public static AnalysisResultResponse failed() {
        return AnalysisResultResponse.builder()
                .status(ApplicationStatus.FAILED)
                .message("AI 분석에 실패했습니다. 관리자에게 문의하세요.")
                .build();
    }

    // ── APPLICANT용 (면접 질문만) ─────────────────────────────────────

    public static AnalysisResultResponse forApplicant(AnalysisResult result,
                                                      List<InterviewQuestionDto> questions) {
        return AnalysisResultResponse.builder()
                .status(ApplicationStatus.COMPLETED)
                .interviewQuestions(questions)
                .analyzedAt(result.getAnalyzedAt())
                .build();
    }

    // ── HR용 (전체 점수 + 면접 질문) ─────────────────────────────────

    public static AnalysisResultResponse forHr(AnalysisResult result,
                                               List<InterviewQuestionDto> questions) {
        return AnalysisResultResponse.builder()
                .status(ApplicationStatus.COMPLETED)
                .totalScore(result.getTotalScore())
                .scoreJobFit(result.getScoreJobFit())
                .scoreCareerConsistency(result.getScoreCareerConsistency())
                .scoreSkillMatch(result.getScoreSkillMatch())
                .scoreQuantitativeAchievement(result.getScoreQuantitativeAchievement())
                .scoreDocumentQuality(result.getScoreDocumentQuality())
                .summary(result.getSummary())
                .interviewQuestions(questions)
                .analyzedAt(result.getAnalyzedAt())
                .build();
    }

    // ── 면접 질문 내부 DTO ────────────────────────────────────────────

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class InterviewQuestionDto {
        private String question;
        private String intent;
        @JsonProperty("answer_guide")
        private String answerGuide;
    }
}
