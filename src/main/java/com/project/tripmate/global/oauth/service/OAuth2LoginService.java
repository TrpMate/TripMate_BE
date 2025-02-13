package com.project.tripmate.global.oauth.service;

import com.project.tripmate.global.jwt.JwtTokenProvider;
import com.project.tripmate.global.oauth.domain.CustomOAuth2User;
import com.project.tripmate.global.oauth.token.OAuthAuthenticationToken;
import com.project.tripmate.global.oauth.userInfo.OAuth2UserInfo;
import com.project.tripmate.global.oauth.userInfo.OAuth2UserInfoFactory;
import com.project.tripmate.user.domain.User;
import com.project.tripmate.user.domain.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OAuth2LoginService {

    private final CustomOAuth2UserService oAuth2Service;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    private AuthenticationManager authenticationManager; // AuthenticationManager 주입

    public HttpHeaders handleOAuth2Login(String authorizationCode, String socialType) {
        // 액세스 토큰을 발급받고 사용자 정보를 가져옵니다.
        String accessToken = oAuth2Service.getAccessToken(authorizationCode, socialType);

        // AccessToken으로 사용자 정보 가져오기
        CustomOAuth2User customOAuth2User = CustomOAuth2User.fetchUserFromAccessToken(socialType, accessToken);

        // OAuth2UserInfo를 통해 사용자 정보 처리
        OAuth2UserInfo userInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(socialType, customOAuth2User.getAttributes());

        // 사용자 정보로 User 객체 생성
        User user = oAuth2Service.getUserByOAuth2UserInfo(userInfo, socialType);

        // CustomUserDetails로 Authentication 생성
        CustomUserDetails userDetails = new CustomUserDetails(user);
        System.out.println("OAuth 로그인 username: " + userDetails.getUsername());

        // OAuthAuthenticationToken으로 Authentication 생성
        Authentication auth = authenticationManager.authenticate(
                new OAuthAuthenticationToken(userDetails, userDetails.getAuthorities())
        );

        // SecurityContext에 Authentication 저장
        SecurityContextHolder.getContext().setAuthentication(auth);

        // JWT 토큰 생성
        String jwtToken = jwtTokenProvider.createToken(auth);
        String refreshToken = jwtTokenProvider.createRefreshToken(auth);

        // 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken);
        headers.add("Refresh-Token", refreshToken);

        return headers;
    }
}

