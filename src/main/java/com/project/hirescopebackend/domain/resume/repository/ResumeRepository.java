package com.project.hirescopebackend.domain.resume.repository;

import com.project.hirescopebackend.domain.resume.entity.Resume;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ResumeRepository extends JpaRepository<Resume, Long> {

    List<Resume> findAllByUserId(Long userId);
}
