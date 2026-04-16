package com.project.hirescopebackend.domain.application.dto.ai;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.hirescopebackend.domain.resume.entity.*;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Getter
@Builder
public class ResumeData {

    @JsonProperty("name")
    private String name;

    private String summary;

    private List<CareerData> careers;
    private List<EducationData> educations;
    private List<SkillData> skills;
    private List<ProjectData> projects;
    private List<CertificationData> certifications;

    private static final DateTimeFormatter MONTH_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM");

    public static ResumeData from(Resume resume) {
        return ResumeData.builder()
                .name(resume.getTitle())
                .summary(resume.getSummary())
                .careers(resume.getCareers().stream().map(ResumeData::toCareerData).toList())
                .educations(resume.getEducations().stream().map(ResumeData::toEducationData).toList())
                .skills(resume.getSkills().stream().map(ResumeData::toSkillData).toList())
                .projects(resume.getProjects().stream().map(ResumeData::toProjectData).toList())
                .certifications(resume.getCertifications().stream().map(ResumeData::toCertificationData).toList())
                .build();
    }

    private static CareerData toCareerData(Career career) {
        return CareerData.builder()
                .companyName(career.getCompanyName())
                .jobTitle(career.getPosition())
                .rank(career.getRank())
                .startDate(formatDate(career.getStartDate()))
                .endDate(career.getEndDate() != null ? formatDate(career.getEndDate()) : "현재")
                .description(career.getDescription())
                .achievements(career.getQuantitativeAchievement())
                .build();
    }

    private static EducationData toEducationData(Education education) {
        return EducationData.builder()
                .schoolName(education.getSchoolName())
                .major(education.getMajor())
                .degree(education.getDegree())
                .startDate(formatDate(education.getEnrollmentDate()))
                .endDate(education.getGraduationDate() != null ? formatDate(education.getGraduationDate()) : "현재")
                .build();
    }

    private static SkillData toSkillData(Skill skill) {
        return SkillData.builder()
                .skillName(skill.getSkillName())
                .level(skill.getProficiency() != null ? skill.getProficiency().name() : null)
                .durationMonths(skill.getMonthsOfExperience())
                .build();
    }

    private static ProjectData toProjectData(Project project) {
        List<String> techStack = project.getTechnologies() != null
                ? List.of(project.getTechnologies().split(",\\s*"))
                : List.of();

        return ProjectData.builder()
                .projectName(project.getProjectName())
                .role(project.getRole())
                .period(project.getPeriod())
                .techStack(techStack)
                .achievementDescription(project.getAchievement())
                .build();
    }

    private static CertificationData toCertificationData(Certification certification) {
        return CertificationData.builder()
                .name(certification.getCertificationName())
                .issuer(certification.getIssuingOrganization())
                .acquiredDate(formatDate(certification.getAcquiredDate()))
                .build();
    }

    private static String formatDate(LocalDate date) {
        if (date == null) return null;
        return date.format(MONTH_FORMATTER);
    }
}
