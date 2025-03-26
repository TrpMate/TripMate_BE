package com.project.tripmate.global.mail;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender javaMailSender;
    private final RedisTemplate<String, String> redisTemplate;

    private static final String SIGNUP_VERIFICATION_URL = "https://tripmate-be.shop/mail/signup/verify/";
    private static final String INVITE_VERIFICATION_URL = "https://tripmate-be.shop/mail/invite/verify/";

    private static final long TOKEN_EXPIRATION_TIME = 3600; // 초대 토큰 만료 시간 (1시간)

    public void sendEmailForSignUp(String to, String verificationToken, String subject) throws MessagingException {
        String verificationUrlWithToken = SIGNUP_VERIFICATION_URL + verificationToken;
        String htmlContent = generateHtmlContent("이메일 인증을 완료하려면 아래 링크를 클릭하세요:", verificationUrlWithToken);

        sendEmail(to, subject, htmlContent);
    }

    public void sendEmailForInviteMember(String to, String subject, Long courseId, Long userId) throws MessagingException {
        String inviteToken = generateInviteToken(courseId, userId);
        String verificationUrlWithToken = INVITE_VERIFICATION_URL + inviteToken + "?courseId=" + courseId + "&userId=" + userId;

        String htmlContent = generateHtmlContent("팀 초대에 참여하려면 아래 링크를 클릭하세요:", verificationUrlWithToken);

        sendEmail(to, subject, htmlContent);
    }

    // 공통 이메일 전송 메서드
    private void sendEmail(String to, String subject, String htmlContent) throws MessagingException {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true); // HTML 형식으로 전송
            javaMailSender.send(message);
        } catch (MessagingException ex) {
            throw new MessagingException("이메일 전송에 실패했습니다.", ex);
        }
    }

    // HTML 콘텐츠 생성 (메일 내용 템플릿화)
    private String generateHtmlContent(String message, String link) {
        return "<p>" + message + "</p>"
                + "<a href=\"" + link + "\">" + link + "</a>";
    }

    // 초대 토큰 생성 및 Redis에 저장
    private String generateInviteToken(Long courseId, Long userId) {
        String token = UUID.randomUUID().toString();  // 고유한 초대 토큰 생성
        String key = "invite_token:" + courseId + ":" + userId;  // 고유한 키를 사용하여 저장

        try {
            redisTemplate.opsForValue().set(key, token, TOKEN_EXPIRATION_TIME, TimeUnit.SECONDS);
        } catch (Exception ex) {
            throw new RuntimeException("Redis에 토큰 저장 중 오류가 발생했습니다.", ex);
        }

        return token;
    }

    // 초대 토큰 검증
    public boolean validateInviteToken(Long courseId, Long userId, String token) {
        String key = "invite_token:" + courseId + ":" + userId;  // 토큰이 저장된 Redis 키

        try {
            String storedToken = redisTemplate.opsForValue().get(key);
            // Redis에서 초대 토큰을 조회하고 유효 기간 체크
            if (storedToken == null) {
                throw new RuntimeException("초대 토큰이 만료되었거나 존재하지 않습니다.");
            }
            return storedToken.equals(token);
        } catch (Exception ex) {
            throw new RuntimeException("Redis에서 토큰을 조회하는 중 오류가 발생했습니다.", ex);
        }
    }

    // 인증 후 토큰 삭제
    public void deleteInviteToken(Long courseId, Long userId) {
        String key = "invite_token:" + courseId + ":" + userId;
        try {
            redisTemplate.delete(key);  // 사용한 토큰 삭제
        } catch (Exception ex) {
            throw new RuntimeException("Redis에서 토큰 삭제 중 오류가 발생했습니다.", ex);
        }
    }
}