package com.project.hirescopebackend.domain.application.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.hirescopebackend.domain.application.dto.*;
import com.project.hirescopebackend.domain.application.entity.Application;
import com.project.hirescopebackend.domain.application.entity.ApplicationStatus;
import com.project.hirescopebackend.domain.application.entity.AnalysisResult;
import com.project.hirescopebackend.domain.application.repository.ApplicationRepository;
import com.project.hirescopebackend.domain.job.entity.JobPosting;
import com.project.hirescopebackend.domain.job.repository.JobRepository;
import com.project.hirescopebackend.domain.resume.entity.Resume;
import com.project.hirescopebackend.domain.resume.repository.ResumeRepository;
import com.project.hirescopebackend.domain.user.entity.User;
import com.project.hirescopebackend.domain.user.entity.UserRole;
import com.project.hirescopebackend.domain.user.repository.UserRepository;
import com.project.hirescopebackend.global.exception.BusinessException;
import com.project.hirescopebackend.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final ResumeRepository resumeRepository;
    private final JobRepository jobRepository;
    private final UserRepository userRepository;
    private final AiClientService aiClientService;
    private final ObjectMapper objectMapper;

    // ── 지원하기 ──────────────────────────────────────────────────────

    @Transactional
    public ApplicationCreateResponse apply(Long userId, ApplicationRequest request) {
        User user = findUser(userId);
        Resume resume = findResume(request.getResumeId());
        JobPosting jobPosting = findJobPosting(request.getJobPostingId());

        // 본인 이력서인지 확인
        if (!resume.getUser().getId().equals(userId)) {
            throw new BusinessException(ErrorCode.FORBIDDEN);
        }

        // 중복 지원 검사
        applicationRepository.findByResumeIdAndJobPostingId(
                request.getResumeId(), request.getJobPostingId())
                .ifPresent(a -> { throw new BusinessException(ErrorCode.DUPLICATE_APPLICATION); });

        Application application = Application.builder()
                .user(user)
                .resume(resume)
                .jobPosting(jobPosting)
                .appliedAt(LocalDateTime.now())
                .status(ApplicationStatus.PENDING)
                .build();

        Application saved = applicationRepository.save(application);

        // 트랜잭션 커밋 완료 후 AI 분석 요청 (@Async 새 스레드가 DB에서 데이터를 조회할 수 있도록)
        Long savedId = saved.getId();
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                aiClientService.requestAnalysis(savedId);
            }
        });

        return ApplicationCreateResponse.from(saved);
    }

    // ── 분석 결과 조회 (역할별 필터링) ───────────────────────────────

    @Transactional(readOnly = true)
    public AnalysisResultResponse getResult(Long applicationId, Long userId) {
        User user = findUser(userId);
        Application application = findApplication(applicationId);

        // APPLICANT: 본인 지원서만 조회 가능
        if (user.getRole() == UserRole.APPLICANT
                && !application.getUser().getId().equals(userId)) {
            throw new BusinessException(ErrorCode.FORBIDDEN);
        }

        return switch (application.getStatus()) {
            case PENDING, PROCESSING -> AnalysisResultResponse.pending();
            case FAILED -> AnalysisResultResponse.failed();
            case COMPLETED -> {
                AnalysisResult result = application.getAnalysisResult();
                List<AnalysisResultResponse.InterviewQuestionDto> questions =
                        parseInterviewQuestions(result.getInterviewQuestions());

                yield user.getRole() == UserRole.HR
                        ? AnalysisResultResponse.forHr(result)
                        : AnalysisResultResponse.forApplicant(result, questions);
            }
        };
    }

    // ── 직무별 지원자 목록 (HR 전용) ──────────────────────────────────

    @Transactional(readOnly = true)
    public Page<ApplicantSummaryResponse> getApplicants(Long jobId, Long userId,
                                                        ApplicationStatus statusFilter,
                                                        Pageable pageable) {
        User user = findUser(userId);
        if (user.getRole() != UserRole.HR) {
            throw new BusinessException(ErrorCode.FORBIDDEN);
        }

        JobPosting jobPosting = findJobPosting(jobId);
        if (!jobPosting.getUser().getId().equals(userId)) {
            throw new BusinessException(ErrorCode.FORBIDDEN);
        }

        Page<Application> applications = (statusFilter != null)
                ? applicationRepository.findByJobPostingIdAndStatusOrderByScoreDesc(jobId, statusFilter, pageable)
                : applicationRepository.findByJobPostingIdOrderByScoreDesc(jobId, pageable);

        return applications.map(ApplicantSummaryResponse::from);
    }

    // ── 지원자 상세 조회 (HR 전용 - 이력서 + 분석 결과) ───────────────

    @Transactional(readOnly = true)
    public ApplicationDetailResponse getDetail(Long applicationId, Long userId) {
        User user = findUser(userId);
        if (user.getRole() != UserRole.HR) {
            throw new BusinessException(ErrorCode.FORBIDDEN);
        }

        Application application = findApplication(applicationId);

        return ApplicationDetailResponse.from(application);
    }

    // ── 내부 헬퍼 ─────────────────────────────────────────────────────

    private User findUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
    }

    private Resume findResume(Long resumeId) {
        return resumeRepository.findById(resumeId)
                .orElseThrow(() -> new BusinessException(ErrorCode.RESUME_NOT_FOUND));
    }

    private JobPosting findJobPosting(Long jobPostingId) {
        return jobRepository.findById(jobPostingId)
                .orElseThrow(() -> new BusinessException(ErrorCode.JOB_NOT_FOUND));
    }

    private Application findApplication(Long applicationId) {
        return applicationRepository.findById(applicationId)
                .orElseThrow(() -> new BusinessException(ErrorCode.APPLICATION_NOT_FOUND));
    }

    /**
     * DB에 JSON 문자열로 저장된 면접 질문을 List로 파싱
     * 예: [{"question":"...","intent":"...","answerGuide":"..."}]
     */
    private List<AnalysisResultResponse.InterviewQuestionDto> parseInterviewQuestions(String json) {
        if (json == null || json.isBlank()) return Collections.emptyList();
        try {
            return objectMapper.readValue(json,
                    new TypeReference<List<AnalysisResultResponse.InterviewQuestionDto>>() {});
        } catch (Exception e) {
            log.warn("[면접 질문 파싱 실패] json={}", json, e);
            return Collections.emptyList();
        }
    }
}
