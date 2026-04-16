package com.project.hirescopebackend.domain.application.dto.ai;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ScoresData {

    @JsonProperty("job_fit")
    private ScoreDetail jobFit;

    @JsonProperty("career_consistency")
    private ScoreDetail careerConsistency;

    @JsonProperty("skill_match")
    private ScoreDetail skillMatch;

    @JsonProperty("quantitative_achievement")
    private ScoreDetail quantitativeAchievement;

    @JsonProperty("document_quality")
    private ScoreDetail documentQuality;
}
