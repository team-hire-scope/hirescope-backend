package com.project.hirescopebackend.domain.resume.dto;

import lombok.Getter;

@Getter
public class ProjectRequest {
    private String projectName;
    private String role;
    private String period;       // 예: "2024.01 ~ 2024.03"
    private String technologies; // 쉼표 구분
    private String achievement;
}
