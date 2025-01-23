package com.project.tripmate.global.oauth.domain;

import lombok.Getter;

@Getter
public class OAuthRequestDto {

    private String authorizationCode;
    private String socialType;

}
