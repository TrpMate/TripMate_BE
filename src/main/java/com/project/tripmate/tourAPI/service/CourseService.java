package com.project.tripmate.tourAPI.service;

import com.project.tripmate.tourAPI.domain.Course;
import com.project.tripmate.tourAPI.dto.CourseDTO;
import com.project.tripmate.tourAPI.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class CourseService {

    private final CourseRepository courseRepository;

    @Autowired
    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public Course createCourse(CourseDTO courseDTO) {
        Course course = Course.builder()
                .courseName(courseDTO.getCourseName())
                .isPublic(courseDTO.isPublic())
                .startDate(courseDTO.getStartDate())
                .endDate(courseDTO.getEndDate())
                .build();
        return courseRepository.save(course);
    }

    public Optional<Course> getCourseById(Long id) {
        return courseRepository.findById(id);
    }

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public Course updateCourse(Long id, String courseName, boolean isPublic, LocalDate startDate, LocalDate endDate) {
        Optional<Course> optionalCourse = courseRepository.findById(id);
        if (optionalCourse.isPresent()) {
            Course course = optionalCourse.get();
            course.setCourse(courseName, isPublic, startDate, endDate);
            return courseRepository.save(course);
        } else {
            return null; // or throw an exception
        }
    }

    public void deleteCourse(Long id) {
        courseRepository.deleteById(id);
    }


    // 코스 추천

}
