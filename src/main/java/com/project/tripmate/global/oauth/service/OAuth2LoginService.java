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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OAuth2LoginService {

    private final CustomOAuth2UserService oAuth2Service;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;

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

        // 5. Spring Security 인증 프로세스를 통과하도록 Authentication 객체 생성
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities()) // 🔥 여기에 userDetails 사용!
        );

        // 6. SecurityContextHolder에 저장 (Spring Security에서 인식)
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 7. JWT 생성
        String jwtToken = jwtTokenProvider.createToken(authentication);
        String refreshToken = jwtTokenProvider.createRefreshToken(authentication);

        // 8. 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken);
        headers.add("Refresh-Token", refreshToken);

        return headers;
    }
}

