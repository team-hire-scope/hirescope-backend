package com.project.hirescopebackend.domain.application.service;

import com.project.hirescopebackend.domain.application.dto.AnalysisResponse;
import com.project.hirescopebackend.domain.application.dto.ApplicationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ApplicationService {

    // TODO: ApplicationRepository, AiClientService 주입

    public AnalysisResponse apply(Long userId, ApplicationRequest request) {
        // TODO: 지원 생성 및 AI 분석 요청
        return null;
    }

    public AnalysisResponse getResult(Long applicationId) {
        // TODO: 분석 결과 조회
        return null;
    }
}
