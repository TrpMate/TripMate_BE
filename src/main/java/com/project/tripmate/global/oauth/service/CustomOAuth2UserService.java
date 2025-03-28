package com.project.tripmate.global.oauth.service;

import com.project.tripmate.global.oauth.domain.CustomOAuth2User;
import com.project.tripmate.global.oauth.userInfo.OAuth2UserInfo;
import com.project.tripmate.global.oauth.userInfo.OAuth2UserInfoFactory;
import com.project.tripmate.user.domain.User;
import com.project.tripmate.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;
    private final GoogleOAuth2Service googleOAuth2Service;
    private final KakaoOAuth2Service kakaoOAuth2Service;
    private final NaverOAuth2Service naverOAuth2Service;

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        OAuth2User oauth2User = getOAuth2User(userRequest);

        OAuth2UserInfo userInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(registrationId, oauth2User.getAttributes());

        saveOrUpdateUser(userInfo, registrationId);


        return new CustomOAuth2User(oauth2User, registrationId);
    }

    private OAuth2User getOAuth2User(OAuth2UserRequest userRequest) {
        // 기본 OAuth2UserService를 사용하여 사용자 정보를 가져온다.
        return new DefaultOAuth2UserService().loadUser(userRequest);
    }


    private User saveOrUpdateUser(OAuth2UserInfo userInfo, String registrationId) {
        User user = userRepository.findBySocialId(userInfo.getId())
                .orElseGet(() -> createNewUser(userInfo, registrationId));

        // 업데이트할 필요가 있는 경우 필드 업데이트
        user.updateOAuthUser(userInfo.getName(), userInfo.getEmail(), registrationId, userInfo.getId());

        return userRepository.save(user);
    }

    private User createNewUser(OAuth2UserInfo userInfo, String registrationId) {
        return User.builder()
                .username(userInfo.getName())
                .nickname(userInfo.getName())
                .email(userInfo.getEmail()) // 이메일 추가
                .socialType(registrationId)
                .socialId(userInfo.getId())
                .password("") // OAuth 로그인에서는 비밀번호가 필요 없으므로 빈 문자열로 설정
                .accountEnabled(true)
                .build();
    }

    public User getUserByOAuth2UserInfo(OAuth2UserInfo userInfo, String registrationId) {
        return userRepository.findBySocialId(userInfo.getId())
                .orElseGet(() -> createNewUser(userInfo, registrationId));
    }

    // 소셜 타입에 맞는 액세스 토큰을 발급받는 메서드
    public String getAccessToken(String authorizationCode, String socialType) {
        switch (socialType.toLowerCase()) {
            case "google":
                return googleOAuth2Service.getAccessToken(authorizationCode);
            case "kakao":
                return kakaoOAuth2Service.getAccessToken(authorizationCode);
            case "naver":
                return naverOAuth2Service.getAccessToken(authorizationCode);
            default:
                throw new IllegalArgumentException("Unsupported social type: " + socialType);
        }
    }
}