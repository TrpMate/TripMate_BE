package com.project.tripmate.course.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class CourseDTO {
    private final Long id;
    private final String courseName; // 코스 이름 추가
    private final boolean isPublic;   // 공개 여부 추가
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
}
