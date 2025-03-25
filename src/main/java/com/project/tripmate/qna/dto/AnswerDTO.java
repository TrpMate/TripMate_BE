package com.project.tripmate.qna.dto;

import com.project.tripmate.qna.domain.Question;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AnswerDTO {
    private Long id;
    private Question question;
    private String content;
    private LocalDateTime createdAt;
}
