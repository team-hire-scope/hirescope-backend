package com.project.hirescopebackend.domain.application.dto;

import com.project.hirescopebackend.domain.application.entity.Application;
import com.project.hirescopebackend.domain.application.entity.ApplicationStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * GET /api/jobs/{jobId}/applicants 응답 DTO (HR 전용)
 */
@Getter
@Builder
public class ApplicantSummaryResponse {

    private Long applicationId;
    private Long resumeId;
    private String applicantName;
    private ApplicationStatus status;

    // 점수 (분석 완료 전에는 null)
    private Double totalScore;
    private Double scoreJobFit;
    private Double scoreCareerConsistency;
    private Double scoreSkillMatch;
    private Double scoreQuantitativeAchievement;
    private Double scoreDocumentQuality;

    private LocalDateTime appliedAt;

    public static ApplicantSummaryResponse from(Application application) {
        var builder = ApplicantSummaryResponse.builder()
                .applicationId(application.getId())
                .resumeId(application.getResume().getId())
                .applicantName(application.getUser().getName())
                .status(application.getStatus())
                .appliedAt(application.getAppliedAt());

        // 분석 결과가 있으면 점수 포함
        if (application.getAnalysisResult() != null) {
            var result = application.getAnalysisResult();
            builder.totalScore(result.getTotalScore())
                    .scoreJobFit(result.getScoreJobFit())
                    .scoreCareerConsistency(result.getScoreCareerConsistency())
                    .scoreSkillMatch(result.getScoreSkillMatch())
                    .scoreQuantitativeAchievement(result.getScoreQuantitativeAchievement())
                    .scoreDocumentQuality(result.getScoreDocumentQuality());
        }

        return builder.build();
    }
}
