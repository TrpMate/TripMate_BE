package com.project.tripmate.user.domain;

import com.project.tripmate.user.dto.UserRequestDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String password;

    private String email;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "account_non_expired")
    private boolean accountNonExpired; // 계정 만료 여부

    @Column(name = "account_non_locked")
    private boolean accountNonLocked; // 계정 잠김 여부

    @Column(name = "credentials_non_expired")
    private boolean credentialsNonExpired; // 자격 증명 만료 여부

    @Column(name = "account_enabled")
    private boolean accountEnabled; // 계정 활성화 여부

    @Column(name = "mail_verification_token")
    private String mailVerificationToken; // 이메일 인증 토큰

    @Column(name = "failed_login_attempts")
    private int failedLoginAttempts; // 로그인 시도 횟수

    private LocalDateTime lockTime; // 계정 잠금 해제 시간

    @Column(name = "social_type")
    private String socialType; // 소셜 타입 (자체 로그인의 경우 Null)


    @Column(name = "social_id")
    private String socialId; // 소셜 ID  (자체 로그인의 경우 Null)

    // 사용자의 온라인 상태
    @Column(name = "online_status")
    private boolean onlineStatus; // true: 온라인, false: 오프라인

//    @ManyToMany(mappedBy = "users")
//    private Set<ChatRoom> chatRooms = new HashSet<>(); // 사용자가 참여하는 채팅방
//
//    // 채팅방 추가
//    public void addChatRoom(ChatRoom chatRoom) {
//        this.chatRooms.add(chatRoom);
//        chatRoom.getUsers().add(this); // 양방향 연관관계 유지
//    }
//
//    // 채팅방 제거
//    public void removeChatRoom(ChatRoom chatRoom) {
//        this.chatRooms.remove(chatRoom);
//        chatRoom.getUsers().remove(this); // 양방향 연관관계 유지
//    }


    public void updateUser(UserRequestDTO requestDTO, PasswordEncoder passwordEncoder,String mailVerificationToken) {
        this.username = requestDTO.getUsername();
        this.email = requestDTO.getEmail();
        this.mailVerificationToken = mailVerificationToken;
        this.accountEnabled = false;

        // 새로운 비밀번호가 null이 아니고, 기존 비밀번호와 다를 때만 인코딩하여 업데이트
        if (requestDTO.getPassword() != null && !requestDTO.getPassword().equals(this.password)) {
            this.password = passwordEncoder.encode(requestDTO.getPassword());
        }
    }

    // 로그인 시 true, 로그아웃 시 false로 설정
    public void setOnlineStatus(boolean status) {
        this.onlineStatus = status;
    }

    // 인증 받으면 계정 활성화
    public void enableAccount() {
        this.accountEnabled = true;
        this.mailVerificationToken = null;
    }

    // 로그인 실패 시 로그인 시도 횟수 증가
    public void incrementFailedLoginAttempts() {
        this.failedLoginAttempts++;
    }

    // 로그인 성공 시 로그인 시도 횟수 초기화
    public void resetFailedLoginAttempts() {
        this.failedLoginAttempts = 0;
    }

    // 계정 잠금
    public void lockAccount() {
        this.accountNonLocked = false;
        this.lockTime = LocalDateTime.now();
    }

    // 계정 잠금 풀기
    public void unlockAccount() {
        this.accountNonLocked = true;
        this.lockTime = null;
    }

    public boolean isLockTimeExpired(int lockDurationMinutes) {
        if (this.lockTime == null) {
            return true; // 잠금 시간이 없으면 바로 해제 가능
        }
        LocalDateTime expiryTime = this.lockTime.plusMinutes(lockDurationMinutes);
        return expiryTime.isBefore(LocalDateTime.now()); // 현재 시간이 잠금 만료 시간 이전이면 해제 가능
    }

    public void updateOAuthUser(String username, String email, String socialType, String socialId) {
        this.username = username;
        if (email != null) {
            this.email = email;
        }
        this.socialType = socialType;
        this.socialId = socialId;
        this.accountEnabled = true; // OAuth 로그인 후 사용자는 활성화 상태
    }

}
