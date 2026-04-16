package com.project.hirescopebackend.domain.resume.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.YearMonth;

@Entity
@Table(name = "careers")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Career {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resume_id", nullable = false)
    private Resume resume;

    private String companyName;
    private String position;
    private String rank;

    private YearMonth startDate;
    private YearMonth endDate; // nullable (재직 중인 경우)

    @Column(columnDefinition = "TEXT")
    private String description;

    // 정량적 성과 (예: "매출 30% 증가")
    @Column(columnDefinition = "TEXT")
    private String quantitativeAchievement;
}
