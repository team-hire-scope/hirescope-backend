package com.project.hirescopebackend.domain.application.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AnalysisResponse {

    private Long applicationId;
    private Integer matchScore;
    private String strengths;
    private String weaknesses;
    private String suggestions;
}
