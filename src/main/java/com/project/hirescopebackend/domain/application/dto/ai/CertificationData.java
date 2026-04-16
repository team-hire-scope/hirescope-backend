package com.project.hirescopebackend.domain.application.dto.ai;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CertificationData {

    private String name;
    private String issuer;

    @JsonProperty("acquired_date")
    private String acquiredDate;
}
