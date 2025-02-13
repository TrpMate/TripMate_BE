package com.project.tripmate.global.jwt.controller;

import com.project.tripmate.global.JsonResponse;
import com.project.tripmate.global.jwt.JwtAuthenticationFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/jwt")
@RequiredArgsConstructor
public class JwtController {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @PostMapping("/token/refresh")
    public ResponseEntity<JsonResponse<String>> refreshAccessToken(@RequestBody HttpServletRequest servletRequest,
            HttpServletResponse servletResponse) {
        try {
            jwtAuthenticationFilter.handleTokenExpiration(servletRequest, servletResponse);

            String newAccessToken = servletResponse.getHeader("Authorization");

            if (newAccessToken != null) {
                JsonResponse<String> response = new JsonResponse<>(
                        HttpStatus.OK.value(),
                        "토큰이 정상적으로 재발행되었습니다.",
                        newAccessToken);
                return ResponseEntity.ok().body(response);
            }
        } catch (IOException e) {
            JsonResponse<String> response = new JsonResponse<>(
                    HttpServletResponse.SC_UNAUTHORIZED,
                    "토큰 재발행 중 오류가 발생했습니다.",
                    e.getMessage());
            return ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED).body(response);
        }
        return null;
    }

    private ResponseEntity<JsonResponse<String>> createErrorResponse(HttpStatus status, String message) {
        JsonResponse<String> errorResponse = new JsonResponse<>(status.value(), message, null);
        return ResponseEntity.status(status).body(errorResponse);
    }

}
