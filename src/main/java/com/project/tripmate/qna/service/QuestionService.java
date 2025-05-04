package com.project.tripmate.qna.service;

import com.project.tripmate.qna.domain.Question;
import com.project.tripmate.qna.dto.QuestionDTO;
import com.project.tripmate.qna.repository.QuestionRepository;
import com.project.tripmate.user.domain.User;
import com.project.tripmate.user.repository.UserRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final UserRepository userRepository;

    // 문의 작성
    public QuestionDTO createQuestion(Long userId, String category, String title, String content, boolean secret) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        Question question = new Question();
        question.createQuestion(user, category, title, content, secret);

        Question savedQuestion = questionRepository.save(question);

        return QuestionDTO.fromEntity(savedQuestion);
    }

    // 내 문의 목록 조회
    public List<QuestionDTO> getMyQuestions(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        List<Question> questions = questionRepository.findByUser(user);
        return questions.stream()
                .map(QuestionDTO::fromEntity)
                .collect(Collectors.toList());
    }

    // 특정 문의 상세 조회
    public QuestionDTO getQuestion(Long questionId, Long userId) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new RuntimeException("문의가 존재하지 않습니다."));

        if (!question.getUser().getId().equals(userId)) {
            throw new RuntimeException("본인의 문의만 조회할 수 있습니다.");
        }

        return QuestionDTO.fromEntity(question);
    }
}
