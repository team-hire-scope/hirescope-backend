package com.project.hirescopebackend.domain.resume.dto;

import lombok.Getter;

import java.time.YearMonth;

@Getter
public class EducationRequest {
    private String schoolName;
    private String major;
    private String degree;          // 학사/석사/박사
    private YearMonth enrollmentDate;
    private YearMonth graduationDate;
}
