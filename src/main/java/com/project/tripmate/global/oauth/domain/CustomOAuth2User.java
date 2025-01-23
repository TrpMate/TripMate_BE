package com.project.tripmate.global.oauth.domain;

import java.util.List;
import lombok.Getter;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import org.springframework.web.client.RestTemplate;

@Getter
public class CustomOAuth2User implements OAuth2User {

    private final OAuth2User oAuth2User;
    private final String socialType;
    private final String id;
    private final String name;
    private final String email;

    public CustomOAuth2User(OAuth2User oAuth2User, String socialType) {
        this.oAuth2User = oAuth2User;
        this.socialType = socialType;
        this.id = extractId(oAuth2User, socialType);
        this.name = extractName(oAuth2User, socialType);
        this.email = extractEmail(oAuth2User, socialType);
    }

    // Access Token을 통해 CustomOAuth2User를 생성하는 메서드 추가
    public static CustomOAuth2User fetchUserFromAccessToken(String socialType, String accessToken) {
        // API를 통해 사용자 정보를 가져옴
        Map<String, Object> userInfo = fetchUserInfoFromApi(socialType, accessToken);

        // 추출한 정보를 CustomOAuth2User 생성자에 전달하여 사용자 객체를 생성
        return new CustomOAuth2User(new OAuth2User() {
            @Override
            public Map<String, Object> getAttributes() {
                return userInfo;
            }

            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return List.of();
            }

            @Override
            public Object getAttribute(String name) {
                return userInfo.get(name);
            }

            @Override
            public String getName() {
                return (String) userInfo.get("name");
            }
        }, socialType);
    }

    private static Map<String, Object> fetchUserInfoFromApi(String socialType, String accessToken) {
        // Access Token을 이용해 소셜 로그인 API 호출
        String apiUrl = getApiUrl(socialType);

        // RestTemplate 객체 생성
        RestTemplate restTemplate = new RestTemplate();

        // API 요청 URL
        String url = apiUrl; // URL 파라미터로 access_token을 전달하지 않습니다.

        // 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken); // Bearer token 형식으로 Authorization 헤더에 설정

        // HttpEntity 객체로 요청 헤더 설정
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // 실제 API 호출 (Google, Kakao, Naver에 맞는 요청 URL로 대체)
        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);

        // 응답 반환
        return response.getBody();
    }


    private static String getApiUrl(String socialType) {
        switch (socialType) {
            case "google":
                return "https://www.googleapis.com/oauth2/v2/userinfo";
            case "kakao":
                return "https://kapi.kakao.com/v2/user/me";
            case "naver":
                return "https://openapi.naver.com/v1/nid/me";
            default:
                throw new IllegalArgumentException("Unsupported social type: " + socialType);
        }
    }

    private String extractId(OAuth2User oAuth2User, String socialType) {
        switch (socialType) {
            case "google":
                return (String) oAuth2User.getAttribute("sub");
            case "kakao":
                Object id = oAuth2User.getAttribute("id");
                return id != null ? id.toString() : null; // Long 타입을 String으로 변환
            case "naver":
                Map<String, Object> response = (Map<String, Object>) oAuth2User.getAttribute("response");
                return (String) response.get("id");
            default:
                throw new IllegalArgumentException("Unsupported social type: " + socialType);
        }
    }


    private String extractName(OAuth2User oAuth2User, String socialType) {
        switch (socialType) {
            case "google":
                return (String) oAuth2User.getAttribute("name");
            case "kakao":
                Map<String, Object> account = (Map<String, Object>) oAuth2User.getAttribute("kakao_account");
                Map<String, Object> profile = (Map<String, Object>) account.get("profile");
                return (String) profile.get("nickname");
            case "naver":
                Map<String, Object> response = (Map<String, Object>) oAuth2User.getAttribute("response");
                return (String) response.get("name");
            default:
                throw new IllegalArgumentException("Unsupported social type: " + socialType);
        }
    }

    private String extractEmail(OAuth2User oAuth2User, String socialType) {
        switch (socialType) {
            case "google":
                return (String) oAuth2User.getAttribute("email");
            case "kakao":
                Map<String, Object> account = (Map<String, Object>) oAuth2User.getAttribute("kakao_account");
                return (String) account.get("email");
            case "naver":
                Map<String, Object> response = (Map<String, Object>) oAuth2User.getAttribute("response");
                return (String) response.get("email");
            default:
                throw new IllegalArgumentException("Unsupported social type: " + socialType);
        }
    }

    @Override
    public Map<String, Object> getAttributes() {
        return oAuth2User.getAttributes();
    }

    @Override
    public Object getAttribute(String name) {
        return oAuth2User.getAttribute(name);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 빈 권한 목록을 반환합니다.
        return Set.of();
    }
}