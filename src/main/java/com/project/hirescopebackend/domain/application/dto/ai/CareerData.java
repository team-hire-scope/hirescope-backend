package com.project.hirescopebackend.domain.application.dto.ai;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CareerData {

    @JsonProperty("company_name")
    private String companyName;

    @JsonProperty("job_title")
    private String jobTitle;

    private String rank;

    @JsonProperty("start_date")
    private String startDate;

    @JsonProperty("end_date")
    private String endDate;

    private String description;

    private String achievements;
}
