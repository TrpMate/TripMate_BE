package com.project.tripmate.global.oauth.controller;

import com.project.tripmate.global.JsonResponse;
import com.project.tripmate.user.dto.UserResponseDTO;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/oauth")
public class OAuthController {

    @PostMapping("/login")
    public ResponseEntity<JsonResponse<UserResponseDTO>> oauthLogin(HttpHeaders headers, JsonResponse<UserResponseDTO> jsonResponse) {
        return ResponseEntity.ok().headers(headers).body(jsonResponse);
    }
}
