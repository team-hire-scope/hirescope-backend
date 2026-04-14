package com.project.hirescopebackend.domain.job.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class JobResponse {

    private Long id;
    private String title;
    private String company;
    private String location;
    private String techStack;
    private String deadline;
}
