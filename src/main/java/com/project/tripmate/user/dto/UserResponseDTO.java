package com.project.tripmate.user.dto;

import com.project.tripmate.user.domain.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserResponseDTO {
    private final Long id;
    private final String username;
    private final String nickname;
    private final String email;

    // User 엔티티를 UserResponseDTO로 변환하는 메서드
    public static UserResponseDTO from(User user) {
        return UserResponseDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .nickname(user.getNickname())
                .email(user.getEmail())
                .build();
    }
}
