package com.project.tripmate.config;

import com.project.tripmate.global.jwt.JwtAuthenticationFilter;
import com.project.tripmate.global.jwt.JwtTokenProvider;
import com.project.tripmate.global.oauth.handler.OAuth2LoginFailureHandler;
import com.project.tripmate.global.oauth.handler.OAuth2LoginSuccessHandler;
import com.project.tripmate.global.oauth.service.CustomOAuth2UserService;
import com.project.tripmate.user.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractAuthenticationFilterConfigurer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;
    private final JwtTokenProvider jwtTokenProvider;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2LoginSuccessHandler oauth2LoginSuccessHandler;
    private final OAuth2LoginFailureHandler oauth2LoginFailureHandler;

    @Bean // 비밀번호 암호화를 위한 PasswordEncoder 빈 생성
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean // AuthenticationManager 빈을 생성 (인증 요청 처리)
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean // SecurityFilterChain 설정 (스프링 부트 3부터는 FilterChain 방식 사용)
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                // REST API에서는 불필요한 CSRF 보안 비활성화
                .csrf(AbstractHttpConfigurer::disable)
                // JWT 토큰 인증 시스템을 사용할 것이기에 서버가 세션을 생성하지 않도록 한다.
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // HTTP 요청에 대한 인가 규칙 설정
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-resources/**").permitAll()
                        .requestMatchers("/login", "/auth/login", "/user/signup", "/user/verify/**", "/tourAPI/**").permitAll() // 로그인과 회원가입은 누구나 접근 가능
                        .requestMatchers(HttpMethod.GET, "/course/**").permitAll()
                        .anyRequest().authenticated() // 그 외 요청은 인증 필요
                )
                // 폼 로그인 설정
                .formLogin(AbstractAuthenticationFilterConfigurer::permitAll // 로그인 페이지는 누구나 접근 가능
                )
                // OAuth 로그인 설정
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/login")
                        .failureHandler(oauth2LoginFailureHandler)
                        .successHandler(oauth2LoginSuccessHandler)
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(customOAuth2UserService)
                        )
                )
                .cors(Customizer.withDefaults()) // CORS 설정 적용
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider, customUserDetailsService),
                        UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(
                List.of("http://localhost:3000", "http://localhost:9090", "http://tripmate-be.shop")); // 허용할 도메인 설정
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS")); // 허용할 HTTP 메서드 설정
        configuration.setAllowedHeaders(List.of("*")); // 허용할 헤더 설정
        configuration.setAllowCredentials(true); // 쿠키 허용 여부 설정

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // 모든 경로에 대해 CORS 설정 적용

        return source;
    }
}
