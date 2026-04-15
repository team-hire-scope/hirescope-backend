package com.project.hirescopebackend.domain.resume.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ResumeCreateRequest {

    @NotBlank(message = "이력서 제목은 필수입니다.")
    private String title;

    private String summary;

    private List<CareerRequest> careers = new ArrayList<>();
    private List<EducationRequest> educations = new ArrayList<>();
    private List<SkillRequest> skills = new ArrayList<>();
    private List<ProjectRequest> projects = new ArrayList<>();
    private List<CertificationRequest> certifications = new ArrayList<>();
}
