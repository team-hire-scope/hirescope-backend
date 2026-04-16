package com.project.hirescopebackend.domain.application.dto.ai;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class AiAnalysisResponse {

    @JsonProperty("application_id")
    private Long applicationId;

    // AI 서버의 total_score는 무시하고 Spring Boot에서 가중치 기반으로 재계산
    @JsonProperty("total_score")
    private Double totalScore;

    // 중첩 점수 객체
    private ScoresData scores;

    private String summary;

    @JsonProperty("interview_questions")
    private List<InterviewQuestionResponse> interviewQuestions;

    // 편의 메서드 - null safe
    public Double getScoreJobFit() {
        return scores != null && scores.getJobFit() != null ? scores.getJobFit().getScore() : null;
    }

    public Double getScoreCareerConsistency() {
        return scores != null && scores.getCareerConsistency() != null ? scores.getCareerConsistency().getScore() : null;
    }

    public Double getScoreSkillMatch() {
        return scores != null && scores.getSkillMatch() != null ? scores.getSkillMatch().getScore() : null;
    }

    public Double getScoreQuantitativeAchievement() {
        return scores != null && scores.getQuantitativeAchievement() != null ? scores.getQuantitativeAchievement().getScore() : null;
    }

    public Double getScoreDocumentQuality() {
        return scores != null && scores.getDocumentQuality() != null ? scores.getDocumentQuality().getScore() : null;
    }
}
