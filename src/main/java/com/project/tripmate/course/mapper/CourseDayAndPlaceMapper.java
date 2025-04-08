package com.project.tripmate.course.mapper;

import com.project.tripmate.course.domain.CourseDay;
import com.project.tripmate.course.domain.CoursePlace;
import com.project.tripmate.course.dto.CourseDayAndPlaceDTO;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CourseDayAndPlaceMapper {

    private final CoursePlaceMapper coursePlaceMapper;

    public CourseDayAndPlaceDTO toDTO(CourseDay courseDay, List<CoursePlace> places) {
        return new CourseDayAndPlaceDTO(
                courseDay.getId(),
                courseDay.getCourse().getId(),
                courseDay.getDayNum(),
                courseDay.getDayDate(),
                coursePlaceMapper.toDTOList(places)
        );
    }
}
