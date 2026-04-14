package com.project.hirescopebackend.domain.job.repository;

import com.project.hirescopebackend.domain.job.entity.JobPosting;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JobRepository extends JpaRepository<JobPosting, Long> {

    // HR - 자신이 등록한 직무 목록
    List<JobPosting> findByUserId(Long userId);
}
