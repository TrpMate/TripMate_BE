package com.project.tripmate.tourAPI.service;

import com.project.tripmate.tourAPI.domain.Course;
import com.project.tripmate.tourAPI.domain.CourseDay;
import com.project.tripmate.tourAPI.repository.CourseDayRepository;
import com.project.tripmate.tourAPI.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class CourseDayService {

    private final CourseDayRepository courseDayRepository;
    private final CourseRepository courseRepository;

    @Autowired
    public CourseDayService(CourseDayRepository courseDayRepository, CourseRepository courseRepository) {
        this.courseDayRepository = courseDayRepository;
        this.courseRepository = courseRepository;
    }

    public CourseDay createCourseDay(Long courseId, int dayNum, LocalDate dayDate) {
        Optional<Course> course = courseRepository.findById(courseId);
        if (course.isPresent()) {
            CourseDay courseDay = CourseDay.builder()
                    .course(course.get())
                    .dayNum(dayNum)
                    .dayDate(dayDate)
                    .build();
            return courseDayRepository.save(courseDay);
        }
        return null;
    }

    public Optional<CourseDay> getCourseDayById(Long id) {
        return courseDayRepository.findById(id);
    }

    public List<CourseDay> getAllCourseDays() {
        return courseDayRepository.findAll();
    }

    public CourseDay updateCourseDay(Long id, int dayNum, LocalDate dayDate) {
        Optional<CourseDay> existingCourseDay = courseDayRepository.findById(id);
        if (existingCourseDay.isPresent()) {
            CourseDay courseDay = existingCourseDay.get();
            courseDay.setDayNumAndDate(dayNum, dayDate);
            return courseDayRepository.save(courseDay);
        }
        return null;
    }

    public void deleteCourseDay(Long id) {
        courseDayRepository.deleteById(id);
    }
}
