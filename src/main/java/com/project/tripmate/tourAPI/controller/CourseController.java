package com.project.tripmate.tourAPI.controller;

import com.project.tripmate.tourAPI.domain.Course;
import com.project.tripmate.tourAPI.dto.CourseDTO;
import com.project.tripmate.global.jsonResponse.CourseJsonResponse;
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

    // 코스 생성
    @PostMapping
    public ResponseEntity<CourseJsonResponse> createCourse(@RequestBody CourseDTO courseDTO) {
        courseService.createCourse(courseDTO);
        CourseJsonResponse response = new CourseJsonResponse(HttpStatus.CREATED.value(), "Course created successfully", courseDTO);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // 코스 조회
    @GetMapping("/{id}")
    public ResponseEntity<CourseJsonResponse> getCourseById(@PathVariable Long id) {
        Optional<Course> course = courseService.getCourseById(id);
        if (course.isPresent()) {
            CourseDTO courseDTO = convertToDTO(course.get());
            CourseJsonResponse response = new CourseJsonResponse(HttpStatus.OK.value(), "Course found", courseDTO);
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new CourseJsonResponse(HttpStatus.NOT_FOUND.value(), "Course not found", null));
        }
    }

    // 코스 검색
    @GetMapping
    public ResponseEntity<CourseJsonResponse> getAllCourses() {
        List<Course> courses = courseService.getAllCourses();
        List<CourseDTO> courseDTOs = courses.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        CourseJsonResponse response = new CourseJsonResponse(HttpStatus.OK.value(), "Courses retrieved successfully", null);
        return ResponseEntity.ok(response);
    }

    // 코스 업데이트
    @PutMapping("/{id}")
    public ResponseEntity<CourseJsonResponse> updateCourse(@PathVariable Long id,
                                                           @RequestBody CourseDTO courseDTO) {
        Course updatedCourse = courseService.updateCourse(id, courseDTO.getCourseName(), courseDTO.isPublic(), courseDTO.getStartDate(), courseDTO.getEndDate());
        if (updatedCourse != null) {
            CourseDTO updatedCourseDTO = convertToDTO(updatedCourse);
            CourseJsonResponse response = new CourseJsonResponse(HttpStatus.OK.value(), "Course updated successfully", updatedCourseDTO);
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new CourseJsonResponse(HttpStatus.NOT_FOUND.value(), "Course not found", null));
        }
    }

    // 코스 삭제
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
                course.getCourseName(),
                course.isPublic(),
                course.getStartDate(),
                course.getEndDate(),
                course.getCreatedAt(),
                course.getUpdatedAt()
        );
    }
}

