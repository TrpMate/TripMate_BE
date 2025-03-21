package com.project.tripmate.global.oauth.service;

import java.util.Collections;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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
        return extractAccessTokenFromResponse(response.getBody());
    }

    private String extractAccessTokenFromResponse(String responseBody) {
        try {
            // ObjectMapper를 사용하여 응답을 JSON 객체로 변환
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(responseBody);

            // 액세스 토큰이 존재하는지 체크
            if (jsonNode.has("access_token")) {
                return jsonNode.get("access_token").asText();  // 액세스 토큰 반환
            } else {
                // 액세스 토큰이 없다면 상세한 에러 메시지 포함
                throw new RuntimeException("Access token not found in response: " + responseBody);
            }
        } catch (Exception e) {
            // 예외가 발생했을 경우, 예외 메시지와 함께 RuntimeException을 던짐
            throw new RuntimeException("Failed to parse access token from response: " + responseBody, e);
        }
    }

}
