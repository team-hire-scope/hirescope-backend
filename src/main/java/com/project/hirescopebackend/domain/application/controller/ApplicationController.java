package com.project.hirescopebackend.domain.application.controller;

import com.project.hirescopebackend.domain.application.dto.*;
import com.project.hirescopebackend.domain.application.entity.ApplicationStatus;
import com.project.hirescopebackend.domain.application.service.ApplicationService;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Application", description = "지원 및 AI 분석 결과 조회")
@RestController
@RequestMapping("/api/applications")
@RequiredArgsConstructor
public class ApplicationController {

    private final ApplicationService applicationService;

    @Operation(summary = "채용공고 지원",
            description = "지원 즉시 PENDING 상태로 생성되고, AI 분석이 비동기로 시작됩니다. (중복 지원 불가)")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "지원 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "중복 지원"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "타인 이력서로 지원 시도")
    })
    @PostMapping
    public ResponseEntity<ApiResponse<ApplicationCreateResponse>> apply(
            @RequestBody @Valid ApplicationRequest request,
            HttpServletRequest httpRequest) {
        Long userId = SessionUtil.getLoginUserId(httpRequest);
        ApplicationCreateResponse response = applicationService.apply(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("지원이 완료되었습니다. AI 분석이 시작됩니다.", response));
    }

    @Operation(summary = "내 지원 목록 조회 (APPLICANT)",
            description = "현재 로그인한 사용자가 지원한 채용공고 목록을 최신순으로 반환합니다. " +
                    "(기본 정보 요약만 제공)\n\n" +
                    "점수 및 면접 질문 등 분석 결과 상세는 " +
                    "`GET /api/applications/{id}/result` 를 사용해주세요.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "조회 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "로그인 필요")
    })
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<Page<MyApplicationResponse>>> getMyApplications(
            @Parameter(description = "페이지 번호 (0부터)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "페이지 크기") @RequestParam(defaultValue = "10") int size,
            HttpServletRequest httpRequest) {
        Long userId = SessionUtil.getLoginUserId(httpRequest);
        Page<MyApplicationResponse> result = applicationService.getMyApplications(
                userId, PageRequest.of(page, size));
        return ResponseEntity.ok(ApiResponse.ok(result));
    }

    @Operation(summary = "분석 결과 조회",
            description = "APPLICANT: 면접 질문만 반환 / HR: 점수 + 면접 질문 전체 반환\n\n" +
                    "- PENDING/PROCESSING: 분석 중 메시지 반환\n" +
                    "- FAILED: 실패 메시지 반환")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "조회 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "타인의 지원서 조회 시도 (APPLICANT)"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "지원서 없음")
    })
    @GetMapping("/{id}/result")
    public ResponseEntity<ApiResponse<AnalysisResultResponse>> getResult(
            @PathVariable Long id,
            HttpServletRequest httpRequest) {
        Long userId = SessionUtil.getLoginUserId(httpRequest);
        AnalysisResultResponse response = applicationService.getResult(id, userId);
        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    @Operation(summary = "지원자 상세 조회 (HR 전용)",
            description = "이력서 전체 내용 + AI 분석 결과를 함께 반환합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "조회 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "HR 권한 없음"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "지원서 없음")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ApplicationDetailResponse>> getDetail(
            @PathVariable Long id,
            HttpServletRequest httpRequest) {
        Long userId = SessionUtil.getLoginUserId(httpRequest);
        ApplicationDetailResponse response = applicationService.getDetail(id, userId);
        return ResponseEntity.ok(ApiResponse.ok(response));
    }
}
