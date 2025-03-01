package com.project.tripmate.global.jwt;

import com.project.tripmate.global.oauth.token.OAuthAuthenticationToken;
import com.project.tripmate.user.domain.CustomUserDetails;
import com.project.tripmate.user.domain.User;
import com.project.tripmate.user.service.CustomUserDetailsService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            // JWT 토큰 추출
            String jwt = jwtTokenProvider.resolveToken(request);
            if (jwt != null && jwtTokenProvider.validateToken(jwt)) {
                // 토큰에서 사용자 정보 추출
                String email = jwtTokenProvider.getEmail(jwt);

                // JWT에서 auth_type 추출
                String socialType = jwtTokenProvider.getClaimFromToken(jwt, "socialType");

                // OAuth 인증 처리
                if (!socialType.isEmpty()) {
                    User user = userDetailsService.loadUserBySocialTypeAndEmail(socialType, email);
                    CustomUserDetails userDetails = new CustomUserDetails(user);

                    OAuthAuthenticationToken authenticationToken = new OAuthAuthenticationToken(
                            userDetails, userDetails.getAuthorities());
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                } else {
                    CustomUserDetails userDetails = (CustomUserDetails) userDetailsService.loadUserByUsername(email);
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        } catch (ExpiredJwtException e) {
            // 만약 JWT 토큰이 만료되면 handleTokenExpiration 메서드 호출
            handleTokenExpiration(request, response);
        } catch (Exception e) {
            SecurityContextHolder.clearContext();
        }

        filterChain.doFilter(request, response);
    }

    // 토큰 만료 처리 로직
    public void handleTokenExpiration(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String refreshToken = jwtTokenProvider.resolveRefreshToken(request);
        if (refreshToken != null && jwtTokenProvider.validateToken(refreshToken)) {
            if (!jwtTokenProvider.isRefreshTokenBlacklisted(refreshToken)) {
                String socialType = jwtTokenProvider.getClaimFromToken(refreshToken, "socialType");
                String jwtToken = jwtTokenProvider.createTokenFromRefreshToken(refreshToken, socialType);
                response.setHeader("Authorization", "Bearer " + jwtToken);

                String email = jwtTokenProvider.getEmail(jwtToken);
                CustomUserDetails userDetails = (CustomUserDetails) userDetailsService.loadUserByUsername(email);

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Refresh token is blacklisted");
            }
        } else {
            SecurityContextHolder.clearContext();
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid refresh token");
        }
    }
}