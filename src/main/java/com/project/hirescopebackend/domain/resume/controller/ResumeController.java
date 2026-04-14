package com.project.hirescopebackend.domain.resume.controller;

import com.project.hirescopebackend.domain.resume.dto.ResumeCreateRequest;
import com.project.hirescopebackend.domain.resume.dto.ResumeResponse;
import com.project.hirescopebackend.domain.resume.dto.ResumeSummaryResponse;
import com.project.hirescopebackend.domain.resume.service.ResumeService;
import com.project.hirescopebackend.global.common.ApiResponse;
import com.project.hirescopebackend.global.common.SessionUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Resume", description = "이력서 CRUD (로그인 필수 - APPLICANT 전용)")
@RestController
@RequestMapping("/api/resumes")
@RequiredArgsConstructor
public class ResumeController {

    private final ResumeService resumeService;

    @Operation(summary = "이력서 생성", description = "하위 항목(경력/학력/기술/프로젝트/자격증)을 포함해 한 번에 생성합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "생성 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "로그인 필요")
    })
    @PostMapping
    public ResponseEntity<ApiResponse<ResumeResponse>> create(
            @RequestBody @Valid ResumeCreateRequest request,
            HttpServletRequest httpRequest) {
        Long userId = SessionUtil.getLoginUserId(httpRequest);
        ResumeResponse response = resumeService.create(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("이력서 생성 완료", response));
    }

    @Operation(summary = "내 이력서 목록 조회", description = "로그인한 사용자의 이력서 목록을 페이징으로 반환합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "조회 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "로그인 필요")
    })
    @GetMapping
    public ResponseEntity<ApiResponse<Page<ResumeSummaryResponse>>> findAll(
            @Parameter(description = "페이지 번호 (0부터 시작)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "페이지 크기") @RequestParam(defaultValue = "10") int size,
            HttpServletRequest httpRequest) {
        Long userId = SessionUtil.getLoginUserId(httpRequest);
        Pageable pageable = PageRequest.of(page, size);
        Page<ResumeSummaryResponse> result = resumeService.findAll(userId, pageable);
        return ResponseEntity.ok(ApiResponse.ok(result));
    }

    @Operation(summary = "이력서 상세 조회", description = "경력/학력/기술/프로젝트/자격증 전체 포함. 본인 이력서만 조회 가능합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "조회 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "타인의 이력서"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "이력서 없음")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ResumeResponse>> findById(
            @PathVariable Long id,
            HttpServletRequest httpRequest) {
        Long userId = SessionUtil.getLoginUserId(httpRequest);
        ResumeResponse response = resumeService.findById(id, userId);
        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    @Operation(summary = "이력서 수정", description = "이력서 전체를 수정합니다. 하위 항목도 모두 교체됩니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "수정 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "타인의 이력서"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "이력서 없음")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ResumeResponse>> update(
            @PathVariable Long id,
            @RequestBody @Valid ResumeCreateRequest request,
            HttpServletRequest httpRequest) {
        Long userId = SessionUtil.getLoginUserId(httpRequest);
        ResumeResponse response = resumeService.update(id, userId, request);
        return ResponseEntity.ok(ApiResponse.ok("이력서 수정 완료", response));
    }

    @Operation(summary = "이력서 삭제", description = "이력서 및 하위 항목을 모두 삭제합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "삭제 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "타인의 이력서"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "이력서 없음")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable Long id,
            HttpServletRequest httpRequest) {
        Long userId = SessionUtil.getLoginUserId(httpRequest);
        resumeService.delete(id, userId);
        return ResponseEntity.ok(ApiResponse.ok("이력서 삭제 완료", null));
    }
}
