package com.project.tripmate.global.oauth.service;

import java.util.Collections;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;

@Service
public class GoogleOAuth2Service {

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String clientSecret;

    @Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
    private String redirectUri;

    @Value("${spring.security.oauth2.client.registration.google.authorization-grant-type}")
    private String authorizationGrantType;

    @Value("${spring.security.oauth2.client.provider.google.token_uri}")
    private String googleTokenUrl;

    public String getAccessToken(String authorizationCode) {
        RestTemplate restTemplate = new RestTemplate();

        // 구글의 token endpoint에 POST 요청을 보냄
        String url = googleTokenUrl;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));


        // 요청 본문 (폼 데이터)
        String body = "code=" + authorizationCode +
                "&client_id=" + clientId +
                "&client_secret=" + clientSecret +
                "&redirect_uri=" + redirectUri +
                "&grant_type=" + authorizationGrantType;

        HttpEntity<String> entity = new HttpEntity<>(body, headers);

        // 요청 보내기
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

        // 응답에서 액세스 토큰 추출
        String accessToken = extractAccessTokenFromResponse(response.getBody());

        return accessToken;
    }

    private String extractAccessTokenFromResponse(String responseBody) {
        // 응답에서 액세스 토큰을 추출하는 로직 (JSON 파싱)
        // 예시에서는 간단히 반환하는 방식으로 처리
        // 실제로는 JSON 파싱을 해야 하므로 라이브러리를 사용해야 함 (예: Jackson, Gson)
        String accessToken = responseBody.split("\"access_token\":\"")[1].split("\"")[0];
        return accessToken;
    }
}
