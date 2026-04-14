package com.project.hirescopebackend.domain.resume.service;

import com.project.hirescopebackend.domain.resume.dto.ResumeCreateRequest;
import com.project.hirescopebackend.domain.resume.dto.ResumeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ResumeService {

    // TODO: ResumeRepository 주입

    public ResumeResponse create(Long userId, ResumeCreateRequest request) {
        // TODO: 이력서 생성 로직
        return null;
    }

    public List<ResumeResponse> findAll(Long userId) {
        // TODO: 사용자 이력서 목록 조회
        return List.of();
    }

    public ResumeResponse findById(Long id) {
        // TODO: 이력서 단건 조회
        return null;
    }

    public void delete(Long id) {
        // TODO: 이력서 삭제
    }
}
