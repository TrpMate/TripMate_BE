package com.project.tripmate.course.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class CourseUserDTO {
    private final Long id;
    private final Long courseId;
    private final Long userId;
    private final LocalDateTime joinedDate;
}
