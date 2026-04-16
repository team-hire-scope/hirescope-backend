package com.project.hirescopebackend.domain.application.repository;

import com.project.hirescopebackend.domain.application.entity.Application;
import com.project.hirescopebackend.domain.application.entity.ApplicationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ApplicationRepository extends JpaRepository<Application, Long> {

    List<Application> findAllByUserId(Long userId);

    Page<Application> findByUserIdOrderByAppliedAtDesc(Long userId, Pageable pageable);

    // 중복 지원 검사
    Optional<Application> findByResumeIdAndJobPostingId(Long resumeId, Long jobPostingId);

    // HR - 직무별 지원자 목록 (totalScore 내림차순, 전체)
    @Query(value = "SELECT a FROM Application a LEFT JOIN a.analysisResult ar " +
                   "WHERE a.jobPosting.id = :jobPostingId " +
                   "ORDER BY CASE WHEN ar.totalScore IS NULL THEN -1 ELSE ar.totalScore END DESC",
           countQuery = "SELECT COUNT(a) FROM Application a WHERE a.jobPosting.id = :jobPostingId")
    Page<Application> findByJobPostingIdOrderByScoreDesc(
            @Param("jobPostingId") Long jobPostingId, Pageable pageable);

    // HR - 직무별 지원자 목록 (totalScore 내림차순, 상태 필터)
    @Query(value = "SELECT a FROM Application a LEFT JOIN a.analysisResult ar " +
                   "WHERE a.jobPosting.id = :jobPostingId AND a.status = :status " +
                   "ORDER BY CASE WHEN ar.totalScore IS NULL THEN -1 ELSE ar.totalScore END DESC",
           countQuery = "SELECT COUNT(a) FROM Application a WHERE a.jobPosting.id = :jobPostingId AND a.status = :status")
    Page<Application> findByJobPostingIdAndStatusOrderByScoreDesc(
            @Param("jobPostingId") Long jobPostingId,
            @Param("status") ApplicationStatus status,
            Pageable pageable);
}
