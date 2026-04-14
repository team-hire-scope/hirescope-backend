package com.project.hirescopebackend.domain.application.repository;

import com.project.hirescopebackend.domain.application.entity.AnalysisResult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AnalysisResultRepository extends JpaRepository<AnalysisResult, Long> {

    Optional<AnalysisResult> findByApplicationId(Long applicationId);
}
