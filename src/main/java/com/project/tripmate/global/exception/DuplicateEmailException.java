package com.project.tripmate.global.exception;

public class DuplicateEmailException extends RuntimeException {
    public DuplicateEmailException(String message) {
        super(message);  // 예외 메시지를 부모 클래스에 전달
    }
}

