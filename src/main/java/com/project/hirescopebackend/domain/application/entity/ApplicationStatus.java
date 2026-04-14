package com.project.hirescopebackend.domain.application.entity;

public enum ApplicationStatus {
    PENDING,     // 분석 대기
    PROCESSING,  // 분석 중
    COMPLETED,   // 분석 완료
    FAILED       // 분석 실패
}
