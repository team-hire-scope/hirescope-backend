package com.project.hirescopebackend.domain.application.dto;

import com.project.hirescopebackend.domain.application.entity.Application;
import com.project.hirescopebackend.domain.application.entity.ApplicationStatus;
import com.project.hirescopebackend.domain.resume.dto.ResumeResponse;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

/**
 * GET /api/applications/{id} 응답 DTO (HR 전용 - 이력서 + 분석 결과 전체)
 */
@Getter
@Builder
public class ApplicationDetailResponse {

    private Long applicationId;
    private Long resumeId;
    private Long jobPostingId;
    private ApplicationStatus status;
    private LocalDateTime appliedAt;

    // 이력서 전체 내용
    private ResumeResponse resume;

    // 분석 결과 (PENDING/FAILED 이면 null)
    private AnalysisDetail analysisResult;

    public static ApplicationDetailResponse from(Application application,
                                                 List<AnalysisResultResponse.InterviewQuestionDto> questions) {
        var builder = ApplicationDetailResponse.builder()
                .applicationId(application.getId())
                .resumeId(application.getResume().getId())
                .jobPostingId(application.getJobPosting().getId())
                .status(application.getStatus())
                .appliedAt(application.getAppliedAt())
                .resume(ResumeResponse.from(application.getResume()));

        if (application.getAnalysisResult() != null) {
            var result = application.getAnalysisResult();
            builder.analysisResult(AnalysisDetail.builder()
                    .totalScore(result.getTotalScore())
                    .scoreJobFit(result.getScoreJobFit())
                    .scoreCareerConsistency(result.getScoreCareerConsistency())
                    .scoreSkillMatch(result.getScoreSkillMatch())
                    .scoreQuantitativeAchievement(result.getScoreQuantitativeAchievement())
                    .scoreDocumentQuality(result.getScoreDocumentQuality())
                    .summary(result.getSummary())
                    .interviewQuestions(questions)
                    .analyzedAt(result.getAnalyzedAt())
                    .build());
        }

        return builder.build();
    }

    @Getter
    @Builder
    public static class AnalysisDetail {
        private Double totalScore;
        private Double scoreJobFit;
        private Double scoreCareerConsistency;
        private Double scoreSkillMatch;
        private Double scoreQuantitativeAchievement;
        private Double scoreDocumentQuality;
        private String summary;
        private List<AnalysisResultResponse.InterviewQuestionDto> interviewQuestions;
        private LocalDateTime analyzedAt;
    }
}
