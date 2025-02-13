package com.project.tripmate.global.oauth.token;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class OAuthAuthenticationToken extends AbstractAuthenticationToken {
    private final Object principal;

    public OAuthAuthenticationToken(Object principal, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return null; // OAuth 로그인에서는 비밀번호가 필요 없음
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }
}

