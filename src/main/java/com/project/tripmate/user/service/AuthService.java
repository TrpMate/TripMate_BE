package com.project.tripmate.user.service;

import com.project.tripmate.global.jwt.JwtTokenProvider;
import com.project.tripmate.global.oauth.token.OAuthAuthenticationToken;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final CustomUserDetailsService userDetailsService;
    private final UserService userService;

    public HttpHeaders login(String email, String password) {
        try {
            // 인증 수행
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password));

            // 로그인 성공 처리
            userDetailsService.processSuccessfulLogin(email);
            userService.setOnlineStatus(email, true); // 로그인 성공 시 온라인 상태로 변경

            // 토큰 생성
            String socialType = "";
            String accessToken = jwtTokenProvider.createToken(authentication, socialType);
            String refreshToken = jwtTokenProvider.createRefreshToken(authentication, socialType);

            // 헤더 설정
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
            headers.add("Refresh-Token", refreshToken);

            return headers;
        } catch (AuthenticationException e) {
            System.out.println("로그인 실패: " + e.getMessage());
            // 로그인 실패 처리
            userDetailsService.handleAccountStatus(email);
            userDetailsService.processFailedLogin(email);
            // 예외를 던짐
            throw e;
        }
    }

    public boolean logout(String token) {
        // JWT에서 이메일 추출
        String email = jwtTokenProvider.getEmail(token);
        // 사용자 상태를 오프라인으로 설정
        userService.setOnlineStatus(email, false);
        // 리프레시 토큰 블랙리스트 추가
        return jwtTokenProvider.blacklistRefreshToken(token);
    }

    public int getRemainingLoginAttempts(String email) {
        return userDetailsService.getRemainingLoginAttempts(email);
    }
}
