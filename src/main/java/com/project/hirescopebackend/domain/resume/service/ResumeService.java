package com.project.hirescopebackend.domain.resume.service;

import com.project.hirescopebackend.domain.resume.dto.*;
import com.project.hirescopebackend.domain.resume.entity.*;
import com.project.hirescopebackend.domain.resume.repository.ResumeRepository;
import com.project.hirescopebackend.domain.user.entity.User;
import com.project.hirescopebackend.domain.user.repository.UserRepository;
import com.project.hirescopebackend.global.exception.BusinessException;
import com.project.hirescopebackend.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ResumeService {

    private final ResumeRepository resumeRepository;
    private final UserRepository userRepository;

    // ── CREATE ────────────────────────────────────────────────────────

    @Transactional
    public ResumeResponse create(Long userId, ResumeCreateRequest request) {
        User user = findUser(userId);

        // Resume 생성
        Resume resume = Resume.builder()
                .user(user)
                .title(request.getTitle())
                .summary(request.getSummary())
                .build();

        // 하위 엔티티 매핑 후 추가 (Cascade로 함께 저장됨)
        mapCareers(request.getCareers(), resume);
        mapEducations(request.getEducations(), resume);
        mapSkills(request.getSkills(), resume);
        mapProjects(request.getProjects(), resume);
        mapCertifications(request.getCertifications(), resume);

        return ResumeResponse.from(resumeRepository.save(resume));
    }

    // ── READ (목록) ───────────────────────────────────────────────────

    @Transactional(readOnly = true)
    public Page<ResumeSummaryResponse> findAll(Long userId, Pageable pageable) {
        return resumeRepository.findAllByUserId(userId, pageable)
                .map(ResumeSummaryResponse::from);
    }

    // ── READ (단건) ───────────────────────────────────────────────────

    @Transactional(readOnly = true)
    public ResumeResponse findById(Long resumeId, Long userId) {
        Resume resume = findResume(resumeId);
        checkOwner(resume, userId);
        return ResumeResponse.from(resume);
    }

    // ── UPDATE ────────────────────────────────────────────────────────

    @Transactional
    public ResumeResponse update(Long resumeId, Long userId, ResumeCreateRequest request) {
        Resume resume = findResume(resumeId);
        checkOwner(resume, userId);

        // 기본 정보 수정
        resume.updateInfo(request.getTitle(), request.getSummary());

        // 하위 엔티티 전체 교체 (orphanRemoval로 기존 것 삭제 후 새로 추가)
        resume.getCareers().clear();
        resume.getEducations().clear();
        resume.getSkills().clear();
        resume.getProjects().clear();
        resume.getCertifications().clear();

        mapCareers(request.getCareers(), resume);
        mapEducations(request.getEducations(), resume);
        mapSkills(request.getSkills(), resume);
        mapProjects(request.getProjects(), resume);
        mapCertifications(request.getCertifications(), resume);

        // @Transactional dirty checking으로 자동 저장
        return ResumeResponse.from(resume);
    }

    // ── DELETE ────────────────────────────────────────────────────────

    @Transactional
    public void delete(Long resumeId, Long userId) {
        Resume resume = findResume(resumeId);
        checkOwner(resume, userId);
        resumeRepository.delete(resume); // Cascade로 하위 엔티티 자동 삭제
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

    private void checkOwner(Resume resume, Long userId) {
        if (!resume.getUser().getId().equals(userId)) {
            throw new BusinessException(ErrorCode.FORBIDDEN);
        }
    }

    private void mapCareers(List<CareerRequest> requests, Resume resume) {
        if (requests == null) return;
        requests.stream()
                .map(r -> Career.builder()
                        .resume(resume)
                        .companyName(r.getCompanyName())
                        .position(r.getPosition())
                        .rank(r.getRank())
                        .startDate(r.getStartDate())
                        .endDate(r.getEndDate())
                        .description(r.getDescription())
                        .quantitativeAchievement(r.getQuantitativeAchievement())
                        .build())
                .forEach(resume.getCareers()::add);
    }

    private void mapEducations(List<EducationRequest> requests, Resume resume) {
        if (requests == null) return;
        requests.stream()
                .map(r -> Education.builder()
                        .resume(resume)
                        .schoolName(r.getSchoolName())
                        .major(r.getMajor())
                        .degree(r.getDegree())
                        .enrollmentDate(r.getEnrollmentDate())
                        .graduationDate(r.getGraduationDate())
                        .build())
                .forEach(resume.getEducations()::add);
    }

    private void mapSkills(List<SkillRequest> requests, Resume resume) {
        if (requests == null) return;
        requests.stream()
                .map(r -> Skill.builder()
                        .resume(resume)
                        .skillName(r.getSkillName())
                        .proficiency(r.getProficiency())
                        .monthsOfExperience(r.getMonthsOfExperience())
                        .build())
                .forEach(resume.getSkills()::add);
    }

    private void mapProjects(List<ProjectRequest> requests, Resume resume) {
        if (requests == null) return;
        requests.stream()
                .map(r -> Project.builder()
                        .resume(resume)
                        .projectName(r.getProjectName())
                        .role(r.getRole())
                        .period(r.getPeriod())
                        .technologies(r.getTechnologies())
                        .achievement(r.getAchievement())
                        .build())
                .forEach(resume.getProjects()::add);
    }

    private void mapCertifications(List<CertificationRequest> requests, Resume resume) {
        if (requests == null) return;
        requests.stream()
                .map(r -> Certification.builder()
                        .resume(resume)
                        .certificationName(r.getCertificationName())
                        .issuingOrganization(r.getIssuingOrganization())
                        .acquiredDate(r.getAcquiredDate())
                        .build())
                .forEach(resume.getCertifications()::add);
    }
}
