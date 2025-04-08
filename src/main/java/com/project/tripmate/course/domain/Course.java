package com.project.tripmate.course.domain;

import com.project.tripmate.user.domain.User;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String courseName;

    private boolean isPublic; // true면 검색 가능, false면 공유 링크로만 접근

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    private LocalDate startDate;

    private LocalDate endDate;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user; //작성자

    public void setCourse(String courseName, boolean isPublic, LocalDate startDate, LocalDate endDate) {
        this.courseName = courseName;
        this.isPublic = isPublic;
        this.startDate = startDate;
        this.endDate = endDate;
    };

    public void setPublic(boolean isPublic) {
        this.isPublic = isPublic;
    }

}
