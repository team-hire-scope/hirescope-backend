package com.project.hirescopebackend.domain.resume.dto;

import com.project.hirescopebackend.domain.resume.entity.Proficiency;
import lombok.Getter;

@Getter
public class SkillRequest {
    private String skillName;
    private Proficiency proficiency;    // HIGH / MEDIUM / LOW
    private Integer monthsOfExperience;
}
