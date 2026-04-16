package com.project.hirescopebackend.domain.application.dto.ai;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ScoreDetail {
    private Double score;
    private String reason;
}
