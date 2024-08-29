package com.project.tripmate.global.exception;

import org.springframework.security.core.AuthenticationException;

public class UserNotEnabledException extends AuthenticationException {
    public UserNotEnabledException(String message) {
        super(message);
    }
}