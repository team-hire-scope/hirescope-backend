package com.project.hirescopebackend.domain.job.dto;

import lombok.Getter;

@Getter
public class JobCreateRequest {

    private String title;
    private String description;
    private String company;
    private String location;
    private String techStack;
    private String deadline;
}
