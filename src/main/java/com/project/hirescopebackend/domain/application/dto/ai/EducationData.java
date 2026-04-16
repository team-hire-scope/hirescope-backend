package com.project.hirescopebackend.domain.application.dto.ai;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class EducationData {

    @JsonProperty("school_name")
    private String schoolName;

    private String major;
    private String degree;

    @JsonProperty("start_date")
    private String startDate;

    @JsonProperty("end_date")
    private String endDate;
}
