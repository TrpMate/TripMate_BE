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
    public QuestionDTO createQuestion(Long userId, String category, String title, String content) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        Question question = new Question();
        question.createQuestion(user, category, title, content);

        Question savedQuestion = questionRepository.save(question);

        // Question 엔티티를 QuestionDTO로 변환하여 반환
        return new QuestionDTO(savedQuestion.getId(), savedQuestion.getCategory(), savedQuestion.getTitle(), savedQuestion.getContent(), savedQuestion.getCreatedAt().toString());
    }

    // 내 문의 목록 조회
    public List<QuestionDTO> getMyQuestions(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        List<Question> questions = questionRepository.findByUser(user);

        // List<Question>을 List<QuestionDTO>로 변환
        return questions.stream()
                .map(question -> new QuestionDTO(question.getId(), question.getCategory(), question.getTitle(), question.getContent(), question.getCreatedAt().toString()))
                .collect(Collectors.toList());
    }

    // 특정 문의 상세 조회
    public QuestionDTO getQuestion(Long questionId, Long userId) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new RuntimeException("문의가 존재하지 않습니다."));

        if (!question.getUser().getId().equals(userId)) {
            throw new RuntimeException("본인의 문의만 조회할 수 있습니다.");
        }

        // Question 엔티티를 QuestionDTO로 변환하여 반환
        return new QuestionDTO(question.getId(), question.getCategory(), question.getTitle(), question.getContent(), question.getCreatedAt().toString());
    }
}
