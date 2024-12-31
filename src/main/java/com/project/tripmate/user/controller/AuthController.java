package com.project.tripmate.user.controller;

import com.project.tripmate.global.JsonResponse;
import com.project.tripmate.global.exception.UserAccountLockedException;
import com.project.tripmate.global.exception.UserNotEnabledException;
import com.project.tripmate.user.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(summary = "로그인 API", description = "사용자의 이메일과 비밀번호를 사용하여 로그인합니다.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "로그인 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = JsonResponse.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "statusCode": 200,
                                                "message": "로그인에 성공했습니다.",
                                                "data": null
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "로그인 실패 - 잠금 상태가 될 때까지 남은 로그인 횟수 안내",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = JsonResponse.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "statusCode": 401,
                                                "message": "이메일 주소나 비밀번호가 올바르지 않습니다. 3번 더 로그인에 실패하면 계정이 잠길 수 있습니다.",
                                                "data": null
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "해당 이메일을 가진 사용자를 찾을 수 없음",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = JsonResponse.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "statusCode": 404,
                                                "message": "해당 이메일을 가진 사용자를 찾을 수 없습니다.",
                                                "data": null
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "423",
                    description = "로그인 시도 횟수를 초과하여 계정 잠금 처리됨",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = JsonResponse.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "statusCode": 423,
                                                "message": "계정이 잠금되었습니다. 2024-12-31T06:05:09.235521 이후에 다시 시도해주세요.",
                                                "data": null
                                            }
                                            """
                            )
                    )
            )
    })
    @PostMapping("/login")
    public ResponseEntity<JsonResponse<String>> login(@RequestBody AuthRequest authRequest) {
        try {
            HttpHeaders headers = authService.login(authRequest.getEmail(), authRequest.getPassword());
            return createLoginSuccessResponse(headers);
        } catch (Exception e) {
            return handleLoginException(e, authRequest.getEmail());
        }
    }

    @Operation(summary = "로그아웃 API", description = "Refresh Token을 사용하여 로그아웃합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "로그아웃 성공", content = @Content(examples = @ExampleObject(value = "204 No Content"))),
            @ApiResponse(
                    responseCode = "500",
                    description = "서버 오류",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = JsonResponse.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "statusCode": 500,
                                                "message": "잘못된 접근입니다.",
                                                "data": null
                                            }
                                            """
                            )
                    )
            )
    })
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