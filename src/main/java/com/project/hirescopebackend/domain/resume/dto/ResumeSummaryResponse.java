package com.project.hirescopebackend.domain.resume.dto;

import com.project.hirescopebackend.domain.resume.entity.Resume;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * 이력서 목록 조회용 간략 응답 DTO
 */
@Getter
@Builder
public class ResumeSummaryResponse {

    private Long id;
    private String title;
    private String summary;
    private LocalDateTime createdAt;

    public static ResumeSummaryResponse from(Resume resume) {
        return ResumeSummaryResponse.builder()
                .id(resume.getId())
                .title(resume.getTitle())
                .summary(resume.getSummary())
                .createdAt(resume.getCreatedAt())
                .build();
    }
}
