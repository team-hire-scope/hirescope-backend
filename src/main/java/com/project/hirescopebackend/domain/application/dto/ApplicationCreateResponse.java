package com.project.hirescopebackend.domain.application.dto;

import com.project.hirescopebackend.domain.application.entity.Application;
import com.project.hirescopebackend.domain.application.entity.ApplicationStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * POST /api/applications 응답 DTO
 */
@Getter
@Builder
public class ApplicationCreateResponse {

    private Long id;
    private Long resumeId;
    private Long jobPostingId;
    private ApplicationStatus status;
    private LocalDateTime appliedAt;

    public static ApplicationCreateResponse from(Application application) {
        return ApplicationCreateResponse.builder()
                .id(application.getId())
                .resumeId(application.getResume().getId())
                .jobPostingId(application.getJobPosting().getId())
                .status(application.getStatus())
                .appliedAt(application.getAppliedAt())
                .build();
    }
}
