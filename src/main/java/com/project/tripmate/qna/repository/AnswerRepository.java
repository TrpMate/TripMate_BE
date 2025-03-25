package com.project.tripmate.qna.repository;

import com.project.tripmate.qna.domain.Answer;
import com.project.tripmate.qna.domain.Question;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
    Optional<Answer> findByQuestion(Question question);
}
