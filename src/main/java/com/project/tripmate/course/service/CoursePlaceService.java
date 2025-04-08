package com.project.tripmate.course.service;

import com.project.tripmate.course.domain.CourseDay;
import com.project.tripmate.course.domain.CoursePlace;
import com.project.tripmate.course.repository.CourseDayRepository;
import com.project.tripmate.course.repository.CoursePlaceRepository;
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

    // 생성
    public CoursePlace createCoursePlace(
            Long courseDayId,
            String placeName,
            String contentTypeId,
            LocalTime visitStartTime,
            LocalTime visitEndTime,
            double mapX,
            double mapY,
            String phoneNumber,
            String memo
    ) {
        Optional<CourseDay> courseDay = courseDayRepository.findById(courseDayId);
        if (courseDay.isPresent()) {
            CoursePlace coursePlace = CoursePlace.builder()
                    .courseDay(courseDay.get())
                    .placeName(placeName)
                    .contentTypeId(contentTypeId)
                    .visitStartTime(visitStartTime)
                    .visitEndTime(visitEndTime)
                    .mapX(mapX)
                    .mapY(mapY)
                    .phoneNumber(phoneNumber)
                    .memo(memo)
                    .build();
            return coursePlaceRepository.save(coursePlace);
        }
        return null;
    }

    // 조회
    public Optional<CoursePlace> getCoursePlaceById(Long id) {
        return coursePlaceRepository.findById(id);
    }

    public List<CoursePlace> getAllCoursePlaces() {
        return coursePlaceRepository.findAll();
    }

    // 수정
    public CoursePlace updateCoursePlace(
            Long id,
            String placeName,
            String contentTypeId,
            LocalTime visitStartTime,
            LocalTime visitEndTime,
            double mapX,
            double mapY,
            String phoneNumber,
            String memo
    ) {
        Optional<CoursePlace> existingCoursePlace = coursePlaceRepository.findById(id);
        if (existingCoursePlace.isPresent()) {
            CoursePlace coursePlace = existingCoursePlace.get();
            coursePlace.updateCoursePlace(
                    placeName,
                    contentTypeId,
                    visitStartTime,
                    visitEndTime,
                    mapX,
                    mapY,
                    phoneNumber,
                    memo
            );
            return coursePlaceRepository.save(coursePlace);
        }
        return null;
    }

    // 삭제
    public void deleteCoursePlace(Long id) {
        coursePlaceRepository.deleteById(id);
    }
}
