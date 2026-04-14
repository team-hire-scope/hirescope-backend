package com.project.hirescopebackend.domain.job.service;

import com.project.hirescopebackend.domain.job.dto.JobCreateRequest;
import com.project.hirescopebackend.domain.job.dto.JobResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class JobService {

    // TODO: JobRepository 주입

    public JobResponse create(JobCreateRequest request) {
        // TODO: 채용공고 생성
        return null;
    }

    public List<JobResponse> findAll() {
        // TODO: 채용공고 목록 조회
        return List.of();
    }

    public JobResponse findById(Long id) {
        // TODO: 채용공고 단건 조회
        return null;
    }
}
