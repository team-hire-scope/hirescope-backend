package com.project.hirescopebackend.domain.job.dto;

import com.project.hirescopebackend.domain.job.entity.JobPosting;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class JobResponse {

    private Long id;
    private Long userId;        // 작성 HR의 userId
    private String companyName;
    private String jobTitle;
    private String jobDescription;
    private String requiredSkills;
    private String preferredQualifications;

    // 5대 평가 가중치
    private Integer weightJobFit;
    private Integer weightCareerConsistency;
    private Integer weightSkillMatch;
    private Integer weightQuantitativeAchievement;
    private Integer weightDocumentQuality;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static JobResponse from(JobPosting job) {
        return JobResponse.builder()
                .id(job.getId())
                .userId(job.getUser().getId())
                .companyName(job.getCompanyName())
                .jobTitle(job.getJobTitle())
                .jobDescription(job.getJobDescription())
                .requiredSkills(job.getRequiredSkills())
                .preferredQualifications(job.getPreferredQualifications())
                .weightJobFit(job.getWeightJobFit())
                .weightCareerConsistency(job.getWeightCareerConsistency())
                .weightSkillMatch(job.getWeightSkillMatch())
                .weightQuantitativeAchievement(job.getWeightQuantitativeAchievement())
                .weightDocumentQuality(job.getWeightDocumentQuality())
                .createdAt(job.getCreatedAt())
                .updatedAt(job.getUpdatedAt())
                .build();
    }
}
