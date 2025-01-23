package com.project.tripmate.global.oauth.controller;

import com.project.tripmate.global.JsonResponse;
import com.project.tripmate.global.oauth.domain.OAuthRequestDto;
import com.project.tripmate.global.oauth.service.OAuth2LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/oauth")
@RequiredArgsConstructor
public class OAuthController {

    private final OAuth2LoginService oAuth2LoginService;

    @PostMapping("/token")
    public ResponseEntity<JsonResponse<String>> oauthLogin(@RequestBody OAuthRequestDto oAuthRequestDto) {
        HttpHeaders headers = oAuth2LoginService.handleOAuth2Login(oAuthRequestDto.getAuthorizationCode(), oAuthRequestDto.getSocialType());
        JsonResponse<String> response = new JsonResponse<>(HttpStatus.OK.value(), "로그인에 성공했습니다.", null);
        return ResponseEntity.ok().headers(headers).body(response);
    }
}
