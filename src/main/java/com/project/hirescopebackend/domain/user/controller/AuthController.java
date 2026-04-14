package com.project.hirescopebackend.domain.user.controller;

import com.project.hirescopebackend.domain.user.dto.LoginRequest;
import com.project.hirescopebackend.domain.user.dto.SignupRequest;
import com.project.hirescopebackend.domain.user.dto.UserResponse;
import com.project.hirescopebackend.domain.user.entity.User;
import com.project.hirescopebackend.domain.user.service.UserService;
import com.project.hirescopebackend.global.common.ApiResponse;
import com.project.hirescopebackend.global.exception.BusinessException;
import com.project.hirescopebackend.global.exception.ErrorCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Auth", description = "회원가입 / 로그인 / 로그아웃")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private static final String SESSION_USER_ID = "userId";

    private final UserService userService;

    @Operation(summary = "회원가입", description = "role: APPLICANT(지원자) 또는 HR(채용담당자)")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "회원가입 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "이메일 중복"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "입력값 오류")
    })
    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<UserResponse>> signup(@RequestBody @Valid SignupRequest request) {
        User user = userService.signup(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("회원가입 성공", UserResponse.from(user)));
    }

    @Operation(summary = "로그인", description = "로그인 성공 시 세션 쿠키(JSESSIONID)가 발급됩니다. 이후 요청에 자동으로 포함됩니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "로그인 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "존재하지 않는 이메일"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "비밀번호 불일치")
    })
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<UserResponse>> login(
            @RequestBody @Valid LoginRequest request,
            HttpServletRequest httpRequest) {
        User user = userService.login(request);

        // 세션 고정 공격 방지: 기존 세션 무효화 후 새 세션 생성
        HttpSession oldSession = httpRequest.getSession(false);
        if (oldSession != null) {
            oldSession.invalidate();
        }
        HttpSession newSession = httpRequest.getSession(true);
        newSession.setAttribute(SESSION_USER_ID, user.getId());

        return ResponseEntity.ok(ApiResponse.ok("로그인 성공", UserResponse.from(user)));
    }

    @Operation(summary = "로그아웃", description = "세션을 삭제합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "로그아웃 성공")
    })
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(HttpServletRequest httpRequest) {
        HttpSession session = httpRequest.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return ResponseEntity.ok(ApiResponse.ok("로그아웃 성공", null));
    }

    @Operation(summary = "내 정보 조회", description = "현재 로그인된 사용자 정보를 반환합니다. 세션이 없으면 401.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "조회 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "로그인 필요")
    })
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> me(HttpServletRequest httpRequest) {
        HttpSession session = httpRequest.getSession(false);
        if (session == null) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED);
        }

        Long userId = (Long) session.getAttribute(SESSION_USER_ID);
        if (userId == null) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED);
        }

        User user = userService.findById(userId);
        return ResponseEntity.ok(ApiResponse.ok(UserResponse.from(user)));
    }
}
