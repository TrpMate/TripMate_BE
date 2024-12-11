package com.project.tripmate.tourAPI.controller;

import com.project.tripmate.global.JsonResponse;
import com.project.tripmate.tourAPI.domain.Course;
import com.project.tripmate.tourAPI.dto.CourseDTO;
import com.project.tripmate.tourAPI.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<JsonResponse<CourseDTO>> createCourse(@RequestBody CourseDTO courseDTO) {
        courseService.createCourse(courseDTO);
        return buildResponse(HttpStatus.CREATED, "Course created successfully", courseDTO);
    }

    // 코스 조회
    @GetMapping("/{id}")
    public ResponseEntity<JsonResponse<CourseDTO>> getCourseById(@PathVariable Long id) {
        return courseService.getCourseById(id)
                .map(course -> buildResponse(HttpStatus.OK, "Course found", convertToDTO(course)))
                .orElseGet(() -> buildErrorResponse(HttpStatus.NOT_FOUND, "Course not found"));
    }

    // 코스 검색
    @GetMapping
    public ResponseEntity<JsonResponse<List<CourseDTO>>> getAllCourses() {
        List<CourseDTO> courseDTOs = courseService.getAllCourses().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return buildResponse(HttpStatus.OK, "Courses retrieved successfully", courseDTOs);
    }

    // 코스 업데이트
    @PutMapping("/{id}")
    public ResponseEntity<JsonResponse<CourseDTO>> updateCourse(@PathVariable Long id,
            @RequestBody CourseDTO courseDTO) {
        Course updatedCourse = courseService.updateCourse(id, courseDTO.getCourseName(), courseDTO.isPublic(), courseDTO.getStartDate(), courseDTO.getEndDate());
        return updatedCourse != null
                ? buildResponse(HttpStatus.OK, "Course updated successfully", convertToDTO(updatedCourse))
                : buildErrorResponse(HttpStatus.NOT_FOUND, "Course not found");
    }

    // 코스 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long id) {
        courseService.deleteCourse(id);
        return ResponseEntity.noContent().build();
    }

    // 공통 응답 빌더
    private <T> ResponseEntity<JsonResponse<T>> buildResponse(HttpStatus status, String message, T data) {
        JsonResponse<T> response = new JsonResponse<>(status.value(), message, data);
        return new ResponseEntity<>(response, status);
    }

    // 공통 오류 응답 빌더
    private <T> ResponseEntity<JsonResponse<T>> buildErrorResponse(HttpStatus status, String message) {
        JsonResponse<T> response = new JsonResponse<>(status.value(), message, null);
        return new ResponseEntity<>(response, status);
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
