package com.project.hirescopebackend.domain.application.dto.ai;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SkillData {

    @JsonProperty("skill_name")
    private String skillName;

    private String level;  // HIGH, MEDIUM, LOW

    @JsonProperty("duration_months")
    private Integer durationMonths;
}
