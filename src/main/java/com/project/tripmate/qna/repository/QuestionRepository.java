package com.project.tripmate.qna.repository;

import com.project.tripmate.qna.domain.Question;
import com.project.tripmate.user.domain.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findByUser(User user); // 유저가 작성한 문의만 조회
}
