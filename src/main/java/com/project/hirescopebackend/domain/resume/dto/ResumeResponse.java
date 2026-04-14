package com.project.hirescopebackend.domain.resume.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ResumeResponse {

    private Long id;
    private String title;
    // TODO: 상세 필드 추가
}
