package com.project.hirescopebackend.domain.resume.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "projects")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resume_id", nullable = false)
    private Resume resume;

    private String projectName;
    private String role;
    private String period; // 예: "2024.01 ~ 2024.03"

    @Column(columnDefinition = "TEXT")
    private String technologies; // 쉼표 구분

    @Column(columnDefinition = "TEXT")
    private String achievement;
}
