package com.project.hirescopebackend.domain.job.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class JobCreateRequest {

    @NotBlank(message = "회사명은 필수입니다.")
    private String companyName;

    @NotBlank(message = "직무명은 필수입니다.")
    private String jobTitle;

    private String jobDescription;
    private String requiredSkills;          // 쉼표 구분
    private String preferredQualifications;

    // 5대 평가 가중치 (미입력 시 각 20, 합계 = 100 검증)
    @Min(0) @Max(100)
    private Integer weightJobFit;

    @Min(0) @Max(100)
    private Integer weightCareerConsistency;

    @Min(0) @Max(100)
    private Integer weightSkillMatch;

    @Min(0) @Max(100)
    private Integer weightQuantitativeAchievement;

    @Min(0) @Max(100)
    private Integer weightDocumentQuality;

    // null 이면 기본값 20 적용
    public int getWeightJobFitOrDefault()                  { return weightJobFit != null ? weightJobFit : 20; }
    public int getWeightCareerConsistencyOrDefault()       { return weightCareerConsistency != null ? weightCareerConsistency : 20; }
    public int getWeightSkillMatchOrDefault()              { return weightSkillMatch != null ? weightSkillMatch : 20; }
    public int getWeightQuantitativeAchievementOrDefault() { return weightQuantitativeAchievement != null ? weightQuantitativeAchievement : 20; }
    public int getWeightDocumentQualityOrDefault()         { return weightDocumentQuality != null ? weightDocumentQuality : 20; }

    public int getTotalWeight() {
        return getWeightJobFitOrDefault()
                + getWeightCareerConsistencyOrDefault()
                + getWeightSkillMatchOrDefault()
                + getWeightQuantitativeAchievementOrDefault()
                + getWeightDocumentQualityOrDefault();
    }
}
