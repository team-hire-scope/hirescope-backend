package com.project.hirescopebackend.domain.resume.dto;

import com.project.hirescopebackend.domain.resume.entity.*;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;

/**
 * 이력서 상세 응답 DTO (하위 엔티티 전체 포함)
 * 목록 조회 시에는 ResumeSummaryResponse 사용
 */
@Getter
@Builder
public class ResumeResponse {

    private Long id;
    private Long userId;
    private String title;
    private String summary;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private List<CareerDto> careers;
    private List<EducationDto> educations;
    private List<SkillDto> skills;
    private List<ProjectDto> projects;
    private List<CertificationDto> certifications;

    public static ResumeResponse from(Resume resume) {
        return ResumeResponse.builder()
                .id(resume.getId())
                .userId(resume.getUser().getId())
                .title(resume.getTitle())
                .summary(resume.getSummary())
                .createdAt(resume.getCreatedAt())
                .updatedAt(resume.getUpdatedAt())
                .careers(resume.getCareers().stream().map(CareerDto::from).toList())
                .educations(resume.getEducations().stream().map(EducationDto::from).toList())
                .skills(resume.getSkills().stream().map(SkillDto::from).toList())
                .projects(resume.getProjects().stream().map(ProjectDto::from).toList())
                .certifications(resume.getCertifications().stream().map(CertificationDto::from).toList())
                .build();
    }

    // ── 하위 응답 DTO ──────────────────────────────────────────────────

    @Getter
    @Builder
    public static class CareerDto {
        private Long id;
        private String companyName;
        private String position;
        private String rank;
        private YearMonth startDate;
        private YearMonth endDate;
        private String description;
        private String quantitativeAchievement;

        public static CareerDto from(Career career) {
            return CareerDto.builder()
                    .id(career.getId())
                    .companyName(career.getCompanyName())
                    .position(career.getPosition())
                    .rank(career.getRank())
                    .startDate(career.getStartDate())
                    .endDate(career.getEndDate())
                    .description(career.getDescription())
                    .quantitativeAchievement(career.getQuantitativeAchievement())
                    .build();
        }
    }

    @Getter
    @Builder
    public static class EducationDto {
        private Long id;
        private String schoolName;
        private String major;
        private String degree;
        private YearMonth enrollmentDate;
        private YearMonth graduationDate;

        public static EducationDto from(Education education) {
            return EducationDto.builder()
                    .id(education.getId())
                    .schoolName(education.getSchoolName())
                    .major(education.getMajor())
                    .degree(education.getDegree())
                    .enrollmentDate(education.getEnrollmentDate())
                    .graduationDate(education.getGraduationDate())
                    .build();
        }
    }

    @Getter
    @Builder
    public static class SkillDto {
        private Long id;
        private String skillName;
        private Proficiency proficiency;
        private Integer monthsOfExperience;

        public static SkillDto from(Skill skill) {
            return SkillDto.builder()
                    .id(skill.getId())
                    .skillName(skill.getSkillName())
                    .proficiency(skill.getProficiency())
                    .monthsOfExperience(skill.getMonthsOfExperience())
                    .build();
        }
    }

    @Getter
    @Builder
    public static class ProjectDto {
        private Long id;
        private String projectName;
        private String role;
        private String period;
        private String technologies;
        private String achievement;

        public static ProjectDto from(Project project) {
            return ProjectDto.builder()
                    .id(project.getId())
                    .projectName(project.getProjectName())
                    .role(project.getRole())
                    .period(project.getPeriod())
                    .technologies(project.getTechnologies())
                    .achievement(project.getAchievement())
                    .build();
        }
    }

    @Getter
    @Builder
    public static class CertificationDto {
        private Long id;
        private String certificationName;
        private String issuingOrganization;
        private YearMonth acquiredDate;

        public static CertificationDto from(Certification certification) {
            return CertificationDto.builder()
                    .id(certification.getId())
                    .certificationName(certification.getCertificationName())
                    .issuingOrganization(certification.getIssuingOrganization())
                    .acquiredDate(certification.getAcquiredDate())
                    .build();
        }
    }
}
