package com.project.hirescopebackend.domain.application.controller;

import com.project.hirescopebackend.domain.application.dto.AnalysisResponse;
import com.project.hirescopebackend.domain.application.dto.ApplicationRequest;
import com.project.hirescopebackend.global.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/applications")
@RequiredArgsConstructor
public class ApplicationController {

    // TODO: ApplicationService 주입

    @PostMapping
    public ResponseEntity<ApiResponse<AnalysisResponse>> apply(@RequestBody ApplicationRequest request) {
        // TODO
        return ResponseEntity.ok(ApiResponse.ok(null));
    }

    @GetMapping("/{id}/result")
    public ResponseEntity<ApiResponse<AnalysisResponse>> getResult(@PathVariable Long id) {
        // TODO
        return ResponseEntity.ok(ApiResponse.ok(null));
    }
}
