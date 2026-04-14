package com.project.hirescopebackend.domain.job.controller;

import com.project.hirescopebackend.domain.job.dto.JobCreateRequest;
import com.project.hirescopebackend.domain.job.dto.JobResponse;
import com.project.hirescopebackend.global.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/jobs")
@RequiredArgsConstructor
public class JobController {

    // TODO: JobService 주입

    @PostMapping
    public ResponseEntity<ApiResponse<JobResponse>> create(@RequestBody JobCreateRequest request) {
        // TODO
        return ResponseEntity.ok(ApiResponse.ok(null));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<JobResponse>>> findAll() {
        // TODO
        return ResponseEntity.ok(ApiResponse.ok(List.of()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<JobResponse>> findById(@PathVariable Long id) {
        // TODO
        return ResponseEntity.ok(ApiResponse.ok(null));
    }
}
