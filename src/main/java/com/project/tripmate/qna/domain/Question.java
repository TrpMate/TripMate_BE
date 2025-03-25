package com.project.tripmate.qna.domain;

import com.project.tripmate.user.domain.User;
import jakarta.persistence.CascadeType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.eclipse.jdt.internal.compiler.env.NameEnvironmentAnswer;
import org.hibernate.annotations.CreationTimestamp;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // 문의 작성자

    @Column(nullable = false)
    private String category; // 문의 유형 (예: 결제, 오류, 기타 등)

    @Column(nullable = false)
    private String title; // 문의 제목

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content; // 문의 내용

    @OneToOne(mappedBy = "question", cascade = CascadeType.ALL)
    private Answer answer; // 관리자 답변 (1:1 관계)

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public void createQuestion(User user, String category, String title, String content) {
        this.user = user;
        this.category = category;
        this.title = title;
        this.content = content;
    }
}