package com.project.tripmate.global.mail;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class MailRequestDTO {

    @NotBlank(message = "이메일을 입력해주세요")
    @Email(message = "올바른 이메일 형식이 아닙니다")
    private final String to;

    private final String verificationUrl;
    private final String subject;

    @JsonCreator
    public MailRequestDTO(
            @JsonProperty("to") String to,
            @JsonProperty("verificationUrl") String verificationUrl,
            @JsonProperty("subject") String subject
    ) {
        this.to = to;
        this.verificationUrl = verificationUrl;
        this.subject = subject;
    }
}
