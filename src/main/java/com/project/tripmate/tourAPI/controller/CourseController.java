package com.project.tripmate.tourAPI.controller;

import com.project.tripmate.tourAPI.domain.Course;
import com.project.tripmate.tourAPI.dto.CourseDTO;
import com.project.tripmate.tourAPI.dto.CourseJsonResponse;
import com.project.tripmate.tourAPI.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/course")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;

    @PostMapping
    public ResponseEntity<CourseJsonResponse> createCourse(@RequestParam LocalDate startDate,
                                                           @RequestParam LocalDate endDate) {
        Course course = courseService.createCourse(startDate, endDate);
        CourseDTO courseDTO = convertToDTO(course);
        CourseJsonResponse response = new CourseJsonResponse(HttpStatus.CREATED.value(), "Course created successfully", courseDTO);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseJsonResponse> getCourseById(@PathVariable Long id) {
        Optional<Course> course = courseService.getCourseById(id);
        if (course.isPresent()) {
            CourseDTO courseDTO = convertToDTO(course.get());
            CourseJsonResponse response = new CourseJsonResponse(HttpStatus.OK.value(), "Course found", courseDTO);
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new CourseJsonResponse(HttpStatus.NOT_FOUND.value(), "Course not found", null));
        }
    }

    @GetMapping
    public ResponseEntity<CourseJsonResponse> getAllCourses() {
        List<Course> courses = courseService.getAllCourses();
        List<CourseDTO> courseDTOs = courses.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        CourseJsonResponse response = new CourseJsonResponse(HttpStatus.OK.value(), "Courses retrieved successfully", null);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CourseJsonResponse> updateCourse(@PathVariable Long id,
                                                           @RequestParam LocalDate startDate,
                                                           @RequestParam LocalDate endDate) {
        Course updatedCourse = courseService.updateCourse(id, startDate, endDate);
        if (updatedCourse != null) {
            CourseDTO courseDTO = convertToDTO(updatedCourse);
            CourseJsonResponse response = new CourseJsonResponse(HttpStatus.OK.value(), "Course updated successfully", courseDTO);
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new CourseJsonResponse(HttpStatus.NOT_FOUND.value(), "Course not found", null));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CourseJsonResponse> deleteCourse(@PathVariable Long id) {
        courseService.deleteCourse(id);
        CourseJsonResponse response = new CourseJsonResponse(HttpStatus.NO_CONTENT.value(), "Course deleted successfully", null);
        return ResponseEntity.noContent().build();
    }

    // DTO 변환 메서드
    private CourseDTO convertToDTO(Course course) {
        return new CourseDTO(
                course.getId(),
                course.getStartDate(),
                course.getEndDate(),
                course.getCreatedAt(),
                course.getUpdatedAt()
        );
    }
}
