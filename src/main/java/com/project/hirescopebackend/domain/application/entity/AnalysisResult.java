package com.project.hirescopebackend.domain.application.entity;

import com.project.hirescopebackend.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "analysis_results")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class AnalysisResult extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "application_id", nullable = false, unique = true)
    private Application application;

    // 종합 점수
    private Double totalScore;

    // 5대 평가 기준별 점수
    private Double scoreJobFit;
    private Double scoreCareerConsistency;
    private Double scoreSkillMatch;
    private Double scoreQuantitativeAchievement;
    private Double scoreDocumentQuality;

    // AI 생성 요약 (3~5문장)
    @Column(columnDefinition = "TEXT")
    private String summary;

    // 면접 질문 (JSON 배열 형태로 저장)
    @Column(columnDefinition = "TEXT")
    private String interviewQuestions;

    private LocalDateTime analyzedAt;
}
