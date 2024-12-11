package com.project.tripmate.user.controller;

import com.project.tripmate.global.JsonResponse;
import com.project.tripmate.global.exception.UserAccountLockedException;
import com.project.tripmate.global.exception.UserNotEnabledException;
import com.project.tripmate.user.service.AuthService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<JsonResponse<String>> login(@RequestBody AuthRequest authRequest) {
        try {
            HttpHeaders headers = authService.login(authRequest.getEmail(), authRequest.getPassword());
            return createLoginSuccessResponse(headers);
        } catch (Exception e) {
            return handleLoginException(e, authRequest.getEmail());
        }
    }

    @DeleteMapping("/logout")
    public ResponseEntity<JsonResponse<String>> logout(@RequestHeader(name = "Refresh-Token") String refreshToken) {
        boolean logoutSuccess = authService.logout(refreshToken);
        return logoutSuccess ? createLogoutSuccessResponse()
                : createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "잘못된 접근입니다.");
    }

    private ResponseEntity<JsonResponse<String>> createLoginSuccessResponse(HttpHeaders headers) {
        JsonResponse<String> response = new JsonResponse<>(HttpStatus.OK.value(), "로그인에 성공했습니다.", null);
        return ResponseEntity.ok().headers(headers).body(response);
    }

    private ResponseEntity<JsonResponse<String>> createLogoutSuccessResponse() {
        return ResponseEntity.noContent().build();
    }

    private ResponseEntity<JsonResponse<String>> handleLoginException(Exception e, String email) {
        if (e instanceof UsernameNotFoundException) {
            return createErrorResponse(HttpStatus.NOT_FOUND, e.getMessage());
        } else if (e instanceof UserNotEnabledException) {
            return createErrorResponse(HttpStatus.UNAUTHORIZED, e.getMessage());
        } else if (e instanceof UserAccountLockedException) {
            return createErrorResponse(HttpStatus.LOCKED, e.getMessage());
        } else if (e instanceof AuthenticationException) {
            return createAuthenticationErrorResponse(email);
        }
        return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "알 수 없는 오류가 발생했습니다.");
    }

    private ResponseEntity<JsonResponse<String>> createAuthenticationErrorResponse(String email) {
        int remainingAttempts = authService.getRemainingLoginAttempts(email);
        String message = "이메일 주소나 비밀번호가 올바르지 않습니다. " +
                remainingAttempts + "번 더 로그인에 실패하면 계정이 잠길 수 있습니다.";
        return createErrorResponse(HttpStatus.UNAUTHORIZED, message);
    }

    private ResponseEntity<JsonResponse<String>> createErrorResponse(HttpStatus status, String message) {
        JsonResponse<String> errorResponse = new JsonResponse<>(status.value(), message, null);
        return ResponseEntity.status(status).body(errorResponse);
    }

    @Getter
    public static class AuthRequest {

        private String email;
        private String password;
    }
}