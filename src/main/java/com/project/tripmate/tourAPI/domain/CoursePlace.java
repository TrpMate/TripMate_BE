package com.project.tripmate.tourAPI.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class CoursePlace {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "course_day_id")
    private CourseDay courseDay;

    private String contentId;

    private String contentTypeId;

    private LocalDateTime placeTime;

    private int courseOrder;

    public void updateCoursePlace(String contentId, String contentTypeId, LocalDateTime placeTime, int courseOrder) {
        this.contentId = contentId;
        this.contentTypeId = contentTypeId;
        this.placeTime = placeTime;
        this.courseOrder = courseOrder;
    };
}
