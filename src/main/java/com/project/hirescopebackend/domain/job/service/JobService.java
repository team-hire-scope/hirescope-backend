package com.project.hirescopebackend.domain.job.service;

import com.project.hirescopebackend.domain.job.dto.JobCreateRequest;
import com.project.hirescopebackend.domain.job.dto.JobResponse;
import com.project.hirescopebackend.domain.job.entity.JobPosting;
import com.project.hirescopebackend.domain.job.repository.JobRepository;
import com.project.hirescopebackend.domain.user.entity.User;
import com.project.hirescopebackend.domain.user.entity.UserRole;
import com.project.hirescopebackend.domain.user.repository.UserRepository;
import com.project.hirescopebackend.global.exception.BusinessException;
import com.project.hirescopebackend.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class JobService {

    private final JobRepository jobRepository;
    private final UserRepository userRepository;

    // ── CREATE (HR 전용) ──────────────────────────────────────────────

    @Transactional
    public JobResponse create(Long userId, JobCreateRequest request) {
        User user = findUser(userId);
        checkHrRole(user);
        validateWeights(request);

        JobPosting job = JobPosting.builder()
                .user(user)
                .companyName(request.getCompanyName())
                .jobTitle(request.getJobTitle())
                .jobDescription(request.getJobDescription())
                .requiredSkills(request.getRequiredSkills())
                .preferredQualifications(request.getPreferredQualifications())
                .weightJobFit(request.getWeightJobFitOrDefault())
                .weightCareerConsistency(request.getWeightCareerConsistencyOrDefault())
                .weightSkillMatch(request.getWeightSkillMatchOrDefault())
                .weightQuantitativeAchievement(request.getWeightQuantitativeAchievementOrDefault())
                .weightDocumentQuality(request.getWeightDocumentQualityOrDefault())
                .build();

        return JobResponse.from(jobRepository.save(job));
    }

    // ── READ (목록) ───────────────────────────────────────────────────
    // HR: 자신이 등록한 직무만 / APPLICANT: 전체 직무

    @Transactional(readOnly = true)
    public Page<JobResponse> findAll(Long userId, Pageable pageable) {
        User user = findUser(userId);

        if (user.getRole() == UserRole.HR) {
            return jobRepository.findByUserId(userId, pageable)
                    .map(JobResponse::from);
        }
        // APPLICANT: 전체 직무 조회
        return jobRepository.findAll(pageable)
                .map(JobResponse::from);
    }

    // ── READ (단건) ───────────────────────────────────────────────────

    @Transactional(readOnly = true)
    public JobResponse findById(Long jobId) {
        return JobResponse.from(findJob(jobId));
    }

    // ── UPDATE (HR 전용, 자신이 등록한 직무만) ────────────────────────

    @Transactional
    public JobResponse update(Long jobId, Long userId, JobCreateRequest request) {
        User user = findUser(userId);
        checkHrRole(user);
        validateWeights(request);

        JobPosting job = findJob(jobId);
        checkJobOwner(job, userId);

        job.update(
                request.getCompanyName(),
                request.getJobTitle(),
                request.getJobDescription(),
                request.getRequiredSkills(),
                request.getPreferredQualifications(),
                request.getWeightJobFitOrDefault(),
                request.getWeightCareerConsistencyOrDefault(),
                request.getWeightSkillMatchOrDefault(),
                request.getWeightQuantitativeAchievementOrDefault(),
                request.getWeightDocumentQualityOrDefault()
        );

        return JobResponse.from(job);
    }

    // ── 내부 헬퍼 ─────────────────────────────────────────────────────

    private User findUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
    }

    private JobPosting findJob(Long jobId) {
        return jobRepository.findById(jobId)
                .orElseThrow(() -> new BusinessException(ErrorCode.JOB_NOT_FOUND));
    }

    private void checkHrRole(User user) {
        if (user.getRole() != UserRole.HR) {
            throw new BusinessException(ErrorCode.FORBIDDEN);
        }
    }

    private void checkJobOwner(JobPosting job, Long userId) {
        if (!job.getUser().getId().equals(userId)) {
            throw new BusinessException(ErrorCode.FORBIDDEN);
        }
    }

    private void validateWeights(JobCreateRequest request) {
        if (request.getTotalWeight() != 100) {
            throw new BusinessException(ErrorCode.INVALID_WEIGHT);
        }
    }
}
