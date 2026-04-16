package com.project.hirescopebackend.domain.resume.dto;

import lombok.Getter;

import java.time.YearMonth;

@Getter
public class CareerRequest {
    private String companyName;
    private String position;
    private String rank;
    private YearMonth startDate;
    private YearMonth endDate;              // nullable (재직 중)
    private String description;
    private String quantitativeAchievement; // 예: "매출 30% 증가"
}
