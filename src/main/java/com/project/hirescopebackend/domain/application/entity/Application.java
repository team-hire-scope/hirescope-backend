package com.project.hirescopebackend.domain.application.entity;

import com.project.hirescopebackend.domain.job.entity.JobPosting;
import com.project.hirescopebackend.domain.resume.entity.Resume;
import com.project.hirescopebackend.domain.user.entity.User;
import com.project.hirescopebackend.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "applications",
        uniqueConstraints = @UniqueConstraint(columnNames = {"resume_id", "job_posting_id"}))
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Application extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resume_id", nullable = false)
    private Resume resume;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_posting_id", nullable = false)
    private JobPosting jobPosting;

    @Column(nullable = false)
    private LocalDateTime appliedAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ApplicationStatus status;

    // 분석 결과 (nullable - 분석 전에는 없음)
    @OneToOne(mappedBy = "application", fetch = FetchType.LAZY)
    private AnalysisResult analysisResult;

    public void updateStatus(ApplicationStatus status) {
        this.status = status;
    }
}
