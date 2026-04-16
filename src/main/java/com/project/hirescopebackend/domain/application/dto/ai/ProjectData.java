package com.project.hirescopebackend.domain.application.dto.ai;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ProjectData {

    @JsonProperty("project_name")
    private String projectName;

    private String role;
    private String period;

    @JsonProperty("tech_stack")
    private List<String> techStack;  // 쉼표 구분 문자열 → List로 변환

    @JsonProperty("achievement_description")
    private String achievementDescription;
}
