package com.project.tripmate.course.service;

import com.project.tripmate.course.domain.CourseDay;
import com.project.tripmate.course.domain.CoursePlace;
import com.project.tripmate.course.dto.CoursePlaceRequestDTO;
import com.project.tripmate.course.repository.CourseDayRepository;
import com.project.tripmate.course.repository.CoursePlaceRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class CoursePlaceService {

    private final CoursePlaceRepository coursePlaceRepository;
    private final CourseDayRepository courseDayRepository;

    @Autowired
    public CoursePlaceService(CoursePlaceRepository coursePlaceRepository, CourseDayRepository courseDayRepository) {
        this.coursePlaceRepository = coursePlaceRepository;
        this.courseDayRepository = courseDayRepository;
    }

    public CoursePlace createCoursePlace(CoursePlaceRequestDTO dto) {
        CourseDay courseDay = courseDayRepository.findById(dto.getCourseDayId())
                .orElseThrow(() -> new EntityNotFoundException("해당 코스 일자를 찾을 수 없습니다."));

        CoursePlace coursePlace = CoursePlace.builder()
                .courseDay(courseDay)
                .placeName(dto.getPlaceName())
                .contentTypeId(dto.getContentTypeId())
                .visitStartTime(dto.getVisitStartTime())
                .visitEndTime(dto.getVisitEndTime())
                .mapX(dto.getMapX())
                .mapY(dto.getMapY())
                .phoneNumber(dto.getPhoneNumber())
                .memo(dto.getMemo())
                .build();

        return coursePlaceRepository.save(coursePlace);
    }


    // 조회
    public Optional<CoursePlace> getCoursePlaceById(Long id) {
        return coursePlaceRepository.findById(id);
    }

    public List<CoursePlace> getAllCoursePlaces() {
        return coursePlaceRepository.findAll();
    }

    public CoursePlace updateCoursePlace(Long id, CoursePlaceRequestDTO dto) {
        CoursePlace coursePlace = coursePlaceRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("해당 코스 장소를 찾을 수 없습니다."));

        coursePlace.updateCoursePlace(
                dto.getPlaceName(),
                dto.getContentTypeId(),
                dto.getVisitStartTime(),
                dto.getVisitEndTime(),
                dto.getMapX(),
                dto.getMapY(),
                dto.getPhoneNumber(),
                dto.getMemo()
        );

        return coursePlaceRepository.save(coursePlace);
    }


    // 삭제
    public void deleteCoursePlace(Long id) {
        coursePlaceRepository.deleteById(id);
    }
}
