package com.project.tripmate.course.mapper;

import com.project.tripmate.course.domain.CoursePlace;
import com.project.tripmate.course.dto.CoursePlaceDTO;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class CoursePlaceMapper {
    public CoursePlaceDTO toDTO(CoursePlace place) {
        return new CoursePlaceDTO(
                place.getId(),
                place.getCourseDay().getId(),
                place.getPlaceName(),
                place.getContentTypeId(),
                place.getVisitStartTime(),
                place.getVisitEndTime(),
                place.getMapX(),
                place.getMapY(),
                place.getPhoneNumber(),
                place.getMemo()
        );
    }

    public List<CoursePlaceDTO> toDTOList(List<CoursePlace> places) {
        return places.stream().map(this::toDTO).toList();
    }
}

