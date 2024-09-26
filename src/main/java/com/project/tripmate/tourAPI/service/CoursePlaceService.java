package com.project.tripmate.tourAPI.service;

import com.project.tripmate.tourAPI.domain.CourseDay;
import com.project.tripmate.tourAPI.domain.CoursePlace;
import com.project.tripmate.tourAPI.repository.CourseDayRepository;
import com.project.tripmate.tourAPI.repository.CoursePlaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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

    public CoursePlace createCoursePlace(Long courseDayId, String contentId, String contentTypeId, LocalDateTime placeTime, int courseOrder) {
        Optional<CourseDay> courseDay = courseDayRepository.findById(courseDayId);
        if (courseDay.isPresent()) {
            CoursePlace coursePlace = CoursePlace.builder()
                    .courseDay(courseDay.get())
                    .contentId(contentId)
                    .contentTypeId(contentTypeId)
                    .placeTime(placeTime)
                    .courseOrder(courseOrder)
                    .build();
            return coursePlaceRepository.save(coursePlace);
        }
        return null;
    }

    public Optional<CoursePlace> getCoursePlaceById(Long id) {
        return coursePlaceRepository.findById(id);
    }

    public List<CoursePlace> getAllCoursePlaces() {
        return coursePlaceRepository.findAll();
    }

    public CoursePlace updateCoursePlace(Long id, String contentId, String contentTypeId, LocalDateTime placeTime, int courseOrder) {
        Optional<CoursePlace> existingCoursePlace = coursePlaceRepository.findById(id);
        if (existingCoursePlace.isPresent()) {
            CoursePlace coursePlace = existingCoursePlace.get();
            coursePlace.updateCoursePlace(contentId, contentTypeId, placeTime, courseOrder);
            return coursePlaceRepository.save(coursePlace);
        }
        return null;
    }

    public void deleteCoursePlace(Long id) {
        coursePlaceRepository.deleteById(id);
    }
}
