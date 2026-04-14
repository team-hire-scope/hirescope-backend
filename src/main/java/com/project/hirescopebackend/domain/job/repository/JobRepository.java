package com.project.hirescopebackend.domain.job.repository;

import com.project.hirescopebackend.domain.job.entity.JobPosting;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobRepository extends JpaRepository<JobPosting, Long> {

    // HR - 자신이 등록한 직무 목록
    Page<JobPosting> findByUserId(Long userId, Pageable pageable);
}
