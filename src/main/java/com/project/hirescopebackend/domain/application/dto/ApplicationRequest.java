package com.project.hirescopebackend.domain.application.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ApplicationRequest {

    @NotNull(message = "이력서 ID는 필수입니다.")
    private Long resumeId;

    @NotNull(message = "채용공고 ID는 필수입니다.")
    private Long jobPostingId;
}
