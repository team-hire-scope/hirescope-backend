package com.project.hirescopebackend.domain.application.repository;

import com.project.hirescopebackend.domain.application.entity.Application;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ApplicationRepository extends JpaRepository<Application, Long> {

    List<Application> findAllByUserId(Long userId);

    // 중복 지원 검사용
    Optional<Application> findByResumeIdAndJobPostingId(Long resumeId, Long jobPostingId);

    // HR - 직무별 지원자 목록 조회
    List<Application> findByJobPostingId(Long jobPostingId);
}
