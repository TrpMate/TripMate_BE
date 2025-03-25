package com.project.tripmate.user.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {

    USER("USER"), ADMIN("ADMIN");

    private final String key;
}