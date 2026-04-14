package com.project.hirescopebackend.domain.resume.controller;

import com.project.hirescopebackend.domain.resume.dto.ResumeCreateRequest;
import com.project.hirescopebackend.domain.resume.dto.ResumeResponse;
import com.project.hirescopebackend.global.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/resumes")
@RequiredArgsConstructor
public class ResumeController {

    // TODO: ResumeService 주입

    @PostMapping
    public ResponseEntity<ApiResponse<ResumeResponse>> create(@RequestBody ResumeCreateRequest request) {
        // TODO
        return ResponseEntity.ok(ApiResponse.ok(null));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ResumeResponse>>> findAll() {
        // TODO
        return ResponseEntity.ok(ApiResponse.ok(List.of()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ResumeResponse>> findById(@PathVariable Long id) {
        // TODO
        return ResponseEntity.ok(ApiResponse.ok(null));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        // TODO
        return ResponseEntity.ok(ApiResponse.ok(null));
    }
}
