package com.project.hirescopebackend.domain.application.dto.ai;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.hirescopebackend.domain.job.entity.JobPosting;
import lombok.Builder;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
@Builder
public class JobPostingData {

    @JsonProperty("company_name")
    private String companyName;

    @JsonProperty("job_title")
    private String jobTitle;

    @JsonProperty("description")
    private String description;

    @JsonProperty("required_skills")
    private List<String> requiredSkills;  // 쉼표 구분 문자열 → List로 변환

    @JsonProperty("preferred_qualifications")
    private String preferredQualifications;

    public static JobPostingData from(JobPosting jobPosting) {
        List<String> skillList = (jobPosting.getRequiredSkills() != null && !jobPosting.getRequiredSkills().isBlank())
                ? Arrays.stream(jobPosting.getRequiredSkills().split(",\\s*")).toList()
                : List.of();

        return JobPostingData.builder()
                .companyName(jobPosting.getCompanyName())
                .jobTitle(jobPosting.getJobTitle())
                .description(jobPosting.getJobDescription())
                .requiredSkills(skillList)
                .preferredQualifications(jobPosting.getPreferredQualifications())
                .build();
    }
}
