package com.project.tripmate.tourAPI.service;

import com.project.tripmate.tourAPI.domain.Course;
import com.project.tripmate.tourAPI.dto.CourseDTO;
import com.project.tripmate.tourAPI.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;
    private final CourseDayService courseDayService;

    public void createCourse(CourseDTO courseDTO) {
        // 1. 코스 생성
        Course course = Course.builder()
                .courseName(courseDTO.getCourseName())
                .isPublic(courseDTO.isPublic())
                .startDate(courseDTO.getStartDate())
                .endDate(courseDTO.getEndDate())
                .build();

        // 2. 코스 저장 (ID 생성)
        course = courseRepository.save(course);

        // 3. 코스 일자 생성
        LocalDate startDate = courseDTO.getStartDate(); // 코스의 시작 날짜
        LocalDate endDate = courseDTO.getEndDate(); // 코스의 종료 날짜

        int dayNum = 1;  // 첫 번째 날
        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            courseDayService.createCourseDay(course.getId(), dayNum++, date);  // 자동으로 CourseDay 생성
        }
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
