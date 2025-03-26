package com.project.tripmate.user.service;

import com.project.tripmate.global.exception.DuplicateEmailException;
import com.project.tripmate.global.exception.UserNotFoundException;
import com.project.tripmate.global.mail.MailService;
import com.project.tripmate.user.domain.User;
import com.project.tripmate.user.dto.UserRequestDTO;
import com.project.tripmate.user.dto.UserResponseDTO;
import com.project.tripmate.user.repository.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;

    // 회원 가입
    public UserResponseDTO signUp(UserRequestDTO userRequestDTO) throws MessagingException {
        validateEmail(userRequestDTO.getEmail()); // 이메일 중복 체크
        String encodedPassword = encodePassword(userRequestDTO.getPassword());
        String verificationToken = UUID.randomUUID().toString();
        User user = createUser(userRequestDTO, encodedPassword, verificationToken);
        sendEmailForSignUp(user.getEmail(), verificationToken, "회원가입 이메일 인증");
        userRepository.save(user);
        return UserResponseDTO.from(user);
    }

    // 이메일 검증
    public UserResponseDTO verifyEmailForUser(String token) {
        User user = findUserByVerificationToken(token);
        user.enableAccount(); // 엔티티 메서드 사용
        userRepository.save(user);
        return UserResponseDTO.from(user);
    }

    // 회원 정보 조회
    public UserResponseDTO getUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("해당 ID를 가진 사용자가 존재하지 않습니다."));
        return UserResponseDTO.from(user);
    }

    // 회원 정보 수정
    public UserResponseDTO updateUser(Long userId, UserRequestDTO userRequestDTO) {
        return userRepository.findById(userId).map(user -> {
            if (!user.getEmail().equals(userRequestDTO.getEmail())) {
                validateEmail(userRequestDTO.getEmail());
            }
            String verificationToken = UUID.randomUUID().toString();
            user.updateUser(userRequestDTO, passwordEncoder,verificationToken);
            try {
                sendEmailForSignUp(user.getEmail(), verificationToken, "회원 정보 수정용 이메일 인증");
            } catch (MessagingException e) {
                e.printStackTrace();
            }

            userRepository.save(user);
            return UserResponseDTO.from(user);
        }).orElseThrow(() -> new IllegalStateException("사용자를 찾을 수 없습니다."));
    }

    // 회원 삭제
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }


    // 사용자 생성 메서드
    private User createUser(UserRequestDTO userRequestDTO, String encodedPassword, String verificationToken) {
        return User.builder()
                .username(userRequestDTO.getUsername())
                .nickname(userRequestDTO.getNickname())
                .password(encodedPassword)
                .email(userRequestDTO.getEmail())
                .accountNonExpired(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .accountEnabled(false)
                .mailVerificationToken(verificationToken)
                .build();
    }

    // 이메일 중복 체크 메서드
    private void validateEmail(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new DuplicateEmailException("이미 존재하는 이메일입니다.");
        }
    }

    // 이메일 중복 체크 메서드
    public boolean checkEmailDuplicate(String email) {
        return userRepository.existsByEmail(email);
    }

    // 이메일 중복 체크 메서드
    public boolean checkNicknameDuplicate(String nickname) {
        return userRepository.existsByNickname(nickname);
    }

    // 이메일 전송 메서드
    private void sendEmailForSignUp(String email, String verificationToken, String subject) throws MessagingException {
        mailService.sendEmailForSignUp(email, verificationToken, subject);
    }

    // 토큰을 사용하여 사용자 조회
    private User findUserByVerificationToken(String token) {
        return userRepository.findByMailVerificationToken(token)
                .orElseThrow(() -> new IllegalStateException("유효한 토큰이 없습니다."));
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("해당 이메일을 가진 사용자를 찾을 수 없습니다."));
    }

    // 비밀번호 암호화 메서드
    private String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }


    // 온라인 상태 업데이트
    public void setOnlineStatus(String email, boolean status) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("해당 이메일을 가진 사용자를 찾을 수 없습니다."));
        user.setOnlineStatus(status);
        userRepository.save(user);
    }

//    // 사용자가 참여하고 있는 채팅방 목록 조회
//    public Set<ChatRoom> getUserChatRooms(Long userId) {
//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new IllegalArgumentException("사용자가 존재하지 않습니다."));
//
//        return user.getChatRooms(); // User 엔티티에 있는 getChatRooms 메서드를 사용하여 채팅방 목록을 반환한다.
//    }
}
