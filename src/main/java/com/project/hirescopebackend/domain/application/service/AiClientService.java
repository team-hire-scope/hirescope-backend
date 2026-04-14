package com.project.hirescopebackend.domain.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AiClientService {

    // TODO: WebClient 주입

    public String analyzeResume(String resumeContent, String jobDescription) {
        // TODO: AI 서버 호출 로직 (WebClient 사용)
        return null;
    }
}
