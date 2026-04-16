package com.project.hirescopebackend.domain.application.dto;

import com.project.hirescopebackend.domain.application.entity.Application;
import com.project.hirescopebackend.domain.application.entity.ApplicationStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * GET /api/applications/me 응답 DTO
 * 지원자 본인의 지원 내역 목록 (가벼운 요약)
 *
 * 점수 및 면접 질문 등 상세 정보는 GET /api/applications/{id}/result 를 사용.
 */
@Getter
@Builder
public class MyApplicationResponse {

    private Long applicationId;
    private Long jobPostingId;
    private String jobTitle;
    private String companyName;

    private Long resumeId;
    private String resumeTitle;

    private ApplicationStatus status;
    private LocalDateTime appliedAt;

    public static MyApplicationResponse from(Application application) {
        return MyApplicationResponse.builder()
                .applicationId(application.getId())
                .jobPostingId(application.getJobPosting().getId())
                .jobTitle(application.getJobPosting().getJobTitle())
                .companyName(application.getJobPosting().getCompanyName())
                .resumeId(application.getResume().getId())
                .resumeTitle(application.getResume().getTitle())
                .status(application.getStatus())
                .appliedAt(application.getAppliedAt())
                .build();
    }
}
