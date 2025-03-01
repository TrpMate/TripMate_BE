package com.project.tripmate.global.oauth.service;

import com.project.tripmate.global.jwt.JwtTokenProvider;
import com.project.tripmate.global.oauth.domain.CustomOAuth2User;
import com.project.tripmate.global.oauth.token.OAuthAuthenticationToken;
import com.project.tripmate.global.oauth.userInfo.OAuth2UserInfo;
import com.project.tripmate.global.oauth.userInfo.OAuth2UserInfoFactory;
import com.project.tripmate.user.domain.User;
import com.project.tripmate.user.domain.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OAuth2LoginService {

    private final CustomOAuth2UserService oAuth2Service;
    private final JwtTokenProvider jwtTokenProvider;

    public HttpHeaders handleOAuth2Login(String authorizationCode, String socialType) {

        // 1. 액세스 토큰 발급
        String accessToken = oAuth2Service.getAccessToken(authorizationCode, socialType);

        // 2. OAuth AccessToken으로 사용자 정보 가져오기
        CustomOAuth2User customOAuth2User = CustomOAuth2User.fetchUserFromAccessToken(socialType, accessToken);
        OAuth2UserInfo userInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(socialType, customOAuth2User.getAttributes());

        // 3. DB에서 기존 유저 조회 or 신규 생성
        User user = oAuth2Service.getUserByOAuth2UserInfo(userInfo, socialType);

        // 4. CustomUserDetails로 변환 (Spring Security에서 인식 가능하게)
        CustomUserDetails userDetails = new CustomUserDetails(user);

        // 5. OAuthAuthenticationToken 생성 후 SecurityContext에 저장
        Authentication authentication = new OAuthAuthenticationToken(userDetails, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 6. JWT 생성
        String jwtToken = jwtTokenProvider.createToken(authentication, socialType);
        String refreshToken = jwtTokenProvider.createRefreshToken(authentication, socialType);

        // 7. 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken);
        headers.add("Refresh-Token", refreshToken);

        return headers;
    }
}


