package com.project.hirescopebackend.domain.resume.dto;

import lombok.Getter;

import java.time.YearMonth;

@Getter
public class CertificationRequest {
    private String certificationName;
    private String issuingOrganization;
    private YearMonth acquiredDate;
}
