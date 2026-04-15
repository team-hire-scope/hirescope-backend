package com.project.hirescopebackend.domain.job.controller;

import com.project.hirescopebackend.domain.application.dto.ApplicantSummaryResponse;
import com.project.hirescopebackend.domain.application.entity.ApplicationStatus;
import com.project.hirescopebackend.domain.application.service.ApplicationService;
import com.project.hirescopebackend.domain.job.dto.JobCreateRequest;
import com.project.hirescopebackend.domain.job.dto.JobResponse;
import com.project.hirescopebackend.domain.job.service.JobService;
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

@Tag(name = "Job", description = "채용공고 관리 | 등록/수정은 HR 전용, 조회는 전체 가능")
@RestController
@RequestMapping("/api/jobs")
@RequiredArgsConstructor
public class JobController {

    private final JobService jobService;
    private final ApplicationService applicationService;

    @Operation(summary = "채용공고 등록 (HR 전용)",
            description = "5개 가중치 합계가 반드시 100이어야 합니다. 미입력 시 각 20으로 설정됩니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "등록 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "가중치 합계 오류"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "HR 권한 없음")
    })
    @PostMapping
    public ResponseEntity<ApiResponse<JobResponse>> create(
            @RequestBody @Valid JobCreateRequest request,
            HttpServletRequest httpRequest) {
        Long userId = SessionUtil.getLoginUserId(httpRequest);
        JobResponse response = jobService.create(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("채용공고 등록 완료", response));
    }

    @Operation(summary = "채용공고 목록 조회",
            description = "HR: 자신이 등록한 공고만 반환 / APPLICANT: 전체 공고 반환")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "조회 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "로그인 필요")
    })
    @GetMapping
    public ResponseEntity<ApiResponse<Page<JobResponse>>> findAll(
            @Parameter(description = "페이지 번호 (0부터)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "페이지 크기") @RequestParam(defaultValue = "10") int size,
            HttpServletRequest httpRequest) {
        Long userId = SessionUtil.getLoginUserId(httpRequest);
        Page<JobResponse> result = jobService.findAll(userId, PageRequest.of(page, size));
        return ResponseEntity.ok(ApiResponse.ok(result));
    }

    @Operation(summary = "채용공고 상세 조회", description = "가중치 포함 전체 정보를 반환합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "조회 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "공고 없음")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<JobResponse>> findById(
            @PathVariable Long id,
            HttpServletRequest httpRequest) {
        SessionUtil.getLoginUserId(httpRequest); // 로그인 검증만
        JobResponse response = jobService.findById(id);
        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    @Operation(summary = "채용공고 수정 (HR 전용)",
            description = "자신이 등록한 공고만 수정 가능. 가중치 합계 100 재검증.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "수정 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "가중치 합계 오류"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "HR 권한 없음 또는 타인 공고"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "공고 없음")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<JobResponse>> update(
            @PathVariable Long id,
            @RequestBody @Valid JobCreateRequest request,
            HttpServletRequest httpRequest) {
        Long userId = SessionUtil.getLoginUserId(httpRequest);
        JobResponse response = jobService.update(id, userId, request);
        return ResponseEntity.ok(ApiResponse.ok("채용공고 수정 완료", response));
    }

    @Operation(summary = "직무별 지원자 목록 조회 (HR 전용)",
            description = "totalScore 기준 내림차순 정렬. `status=COMPLETED` 필터로 완료된 건만 조회 가능.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "조회 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "HR 권한 없음 또는 타인 직무")
    })
    @GetMapping("/{jobId}/applicants")
    public ResponseEntity<ApiResponse<Page<ApplicantSummaryResponse>>> getApplicants(
            @PathVariable Long jobId,
            @Parameter(description = "상태 필터 (PENDING / PROCESSING / COMPLETED / FAILED), 미입력 시 전체")
            @RequestParam(required = false) ApplicationStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest httpRequest) {
        Long userId = SessionUtil.getLoginUserId(httpRequest);
        Page<ApplicantSummaryResponse> result = applicationService.getApplicants(
                jobId, userId, status, PageRequest.of(page, size));
        return ResponseEntity.ok(ApiResponse.ok(result));
    }
}
