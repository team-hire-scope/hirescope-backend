package com.project.hirescopebackend.domain.application.dto.ai;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AiAnalysisRequest {

    @JsonProperty("application_id")
    private Long applicationId;

    @JsonProperty("resume")
    private ResumeData resume;

    @JsonProperty("job_posting")
    private JobPostingData jobPosting;
}
