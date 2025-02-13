package com.project.tripmate.global.oauth.token;

import com.project.tripmate.user.domain.CustomUserDetails;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
public class OAuthAuthenticationProvider implements AuthenticationProvider {

    @Override
    public org.springframework.security.core.Authentication authenticate(
            org.springframework.security.core.Authentication authentication) throws AuthenticationException {
        if (!(authentication instanceof OAuthAuthenticationToken)) {
            return null;
        }

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return OAuthAuthenticationToken.class.isAssignableFrom(authentication);
    }
}

