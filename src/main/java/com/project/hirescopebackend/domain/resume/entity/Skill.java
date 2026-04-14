package com.project.hirescopebackend.domain.resume.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "skills")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Skill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resume_id", nullable = false)
    private Resume resume;

    private String skillName;

    @Enumerated(EnumType.STRING)
    private Proficiency proficiency; // HIGH, MEDIUM, LOW

    private Integer monthsOfExperience;
}
