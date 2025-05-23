package com.project.tripmate.qna.dto;

import com.project.tripmate.qna.domain.Question;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuestionDTO {
    private Long id;
    private String category;
    private String title;
    private String content;
    private String createdDate;
    private boolean secret;

    public static QuestionDTO fromEntity(Question question) {
        return new QuestionDTO(
                question.getId(),
                question.getCategory(),
                question.getTitle(),
                question.getContent(),
                question.getCreatedAt().toString(),
                question.isSecret()
        );
    }
}
