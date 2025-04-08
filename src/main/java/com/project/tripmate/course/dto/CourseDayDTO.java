package com.project.tripmate.course.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class CourseDayDTO {

    private final Long id;
    private final Long courseId;
    private final int dayNum;
    private final LocalDate dayDate;
}
