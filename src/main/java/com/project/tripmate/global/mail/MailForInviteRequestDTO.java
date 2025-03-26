package com.project.tripmate.global.mail;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class MailForInviteRequestDTO {

    @NotBlank(message = "이메일을 입력해주세요")
    @Email(message = "올바른 이메일 형식이 아닙니다")
    private final String to;
    private final String subject;

    private final Long courseId;

    @JsonCreator
    public MailForInviteRequestDTO(
            @JsonProperty("to") String to,
            @JsonProperty("subject") String subject,
            @JsonProperty("courseId") Long courseId
    ) {
        this.to = to;
        this.subject = subject;
        this.courseId = courseId;
    }
}
