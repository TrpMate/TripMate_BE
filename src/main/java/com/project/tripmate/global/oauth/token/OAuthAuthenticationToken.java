package com.project.tripmate.global.oauth.token;

import lombok.Setter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import java.util.Collection;

public class OAuthAuthenticationToken extends AbstractAuthenticationToken {

    private final Object principal;  // 사용자 정보(principal)
    private Object credentials;  // 자격 증명(credentials), OAuth에서는 사용되지 않을 수 있음
    // 인증 관련 추가 정보(details) 설정
    @Setter
    private WebAuthenticationDetails details;  // 인증 관련 추가 정보(details)

    // 생성자: OAuth 인증에서 필요한 사용자 정보와 권한을 설정
    public OAuthAuthenticationToken(Object principal, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);  // 권한 목록을 부모 클래스에 전달
        this.principal = principal;  // principal(사용자 정보) 설정
        setAuthenticated(true);  // OAuth 인증의 경우 인증된 상태로 설정
    }

    // 자격 증명(credentials) 반환 (OAuth 인증에서는 사용되지 않을 수 있음)
    @Override
    public Object getCredentials() {
        return credentials;
    }

    // 사용자 정보(principal) 반환
    @Override
    public Object getPrincipal() {
        return principal;
    }

    // 인증 관련 추가 정보(details) 반환
    @Override
    public Object getDetails() {
        return details;
    }

}
