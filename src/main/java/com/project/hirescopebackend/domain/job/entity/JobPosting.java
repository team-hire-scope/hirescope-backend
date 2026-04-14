package com.project.hirescopebackend.domain.job.entity;

import com.project.hirescopebackend.domain.user.entity.User;
import com.project.hirescopebackend.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "job_postings")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class JobPosting extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // HR 사용자 (작성자)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String companyName;

    @Column(nullable = false)
    private String jobTitle;

    @Column(columnDefinition = "TEXT")
    private String jobDescription;

    @Column(columnDefinition = "TEXT")
    private String requiredSkills; // 쉼표 구분

    @Column(columnDefinition = "TEXT")
    private String preferredQualifications;

    // 5대 평가 기준 가중치 (합계 = 100)
    @Column(nullable = false)
    @Builder.Default
    private Integer weightJobFit = 20;

    @Column(nullable = false)
    @Builder.Default
    private Integer weightCareerConsistency = 20;

    @Column(nullable = false)
    @Builder.Default
    private Integer weightSkillMatch = 20;

    @Column(nullable = false)
    @Builder.Default
    private Integer weightQuantitativeAchievement = 20;

    @Column(nullable = false)
    @Builder.Default
    private Integer weightDocumentQuality = 20;

    public int getTotalWeight() {
        return weightJobFit + weightCareerConsistency + weightSkillMatch
                + weightQuantitativeAchievement + weightDocumentQuality;
    }

    public boolean validateWeights() {
        return getTotalWeight() == 100;
    }
}
