package com.project.tripmate.global.mail;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender javaMailSender;
    private static final String VERIFICATION_URL = "https://tripmate-be.shop/mail/verify/";

    public void sendEmail(String to, String verificationToken, String subject) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(to);
        helper.setSubject(subject);

        String verificationUrlWithToken =  VERIFICATION_URL + verificationToken;

        // HTML 내용 작성
        String htmlContent = "<p>이메일 인증을 완료하려면 아래 링크를 클릭하세요:</p>"
                + "<a href=\"" + verificationUrlWithToken + "\">" + verificationUrlWithToken + "</a>";

        helper.setText(htmlContent, true); // HTML 형식으로 전송

        javaMailSender.send(message);
    }
}
