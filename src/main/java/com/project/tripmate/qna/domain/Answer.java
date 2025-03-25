package com.project.tripmate.qna.domain;

import com.project.tripmate.user.domain.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "question_id", nullable = false)
    private Question question; // 어떤 문의에 대한 답변인지

    @ManyToOne
    @JoinColumn(name = "admin_id", nullable = false)
    private User admin; // 답변을 작성한 관리자

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content; // 답변 내용

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public void createAnswer(Question question, User admin, String content) {
        this.question = question;
        this.admin = admin;
        this.content = content;
    }
}

