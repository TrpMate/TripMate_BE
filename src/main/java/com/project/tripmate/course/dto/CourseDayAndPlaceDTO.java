package com.project.tripmate.course.dto;

import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CourseDayAndPlaceDTO {

    private final Long id;
    private final Long courseId;
    private final int dayNum;
    private final LocalDate dayDate;
    private List<CoursePlaceDTO> coursePlaces; // 추가

}
