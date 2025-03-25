package com.project.tripmate.qna.service;

import com.project.tripmate.qna.domain.Answer;
import com.project.tripmate.qna.domain.Question;
import com.project.tripmate.qna.dto.AnswerDTO;
import com.project.tripmate.qna.repository.AnswerRepository;
import com.project.tripmate.qna.repository.QuestionRepository;
import com.project.tripmate.user.domain.Role;
import com.project.tripmate.user.domain.User;
import com.project.tripmate.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AnswerService {

    private final AnswerRepository answerRepository;
    private final QuestionRepository questionRepository;
    private final UserRepository userRepository;

    // 문의에 대한 답변 작성 (관리자만 가능)
    public AnswerDTO createAnswer(Long questionId, Long adminId, String content) {
        User admin = userRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("관리자를 찾을 수 없습니다."));

        if (admin.getUserRole() != Role.ADMIN) {
            throw new RuntimeException("관리자만 답변을 작성할 수 있습니다.");
        }

        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new RuntimeException("문의가 존재하지 않습니다."));

        if (question.getAnswer() != null) {
            throw new RuntimeException("이미 답변이 등록된 문의입니다.");
        }

        Answer answer = new Answer();
        answer.createAnswer(question, admin, content);
        answerRepository.save(answer);

        return new AnswerDTO(answer.getId(), answer.getQuestion(), answer.getContent(), answer.getCreatedAt());
    }
}

