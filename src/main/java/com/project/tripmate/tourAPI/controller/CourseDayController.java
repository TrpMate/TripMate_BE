package com.project.tripmate.tourAPI.controller;

import com.project.tripmate.global.JsonResponse;
import com.project.tripmate.tourAPI.domain.CourseDay;
import com.project.tripmate.tourAPI.dto.CourseDayDTO;
import com.project.tripmate.tourAPI.service.CourseDayService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/course-day")
@RequiredArgsConstructor
public class CourseDayController {

    private final CourseDayService courseDayService;

    @PostMapping
    public ResponseEntity<JsonResponse<CourseDayDTO>> createCourseDay(@RequestBody CourseDayDTO courseDayDTO) {
        CourseDay courseDay = courseDayService.createCourseDay(courseDayDTO.getCourseId(), courseDayDTO.getDayNum(), courseDayDTO.getDayDate());
        return courseDay != null ? buildResponse(HttpStatus.CREATED, "Course day created successfully", convertToDTO(courseDay))
                : buildErrorResponse(HttpStatus.BAD_REQUEST, "Course not found");
    }

    @GetMapping("/{id}")
    public ResponseEntity<JsonResponse<CourseDayDTO>> getCourseDayById(@PathVariable Long id) {
        return courseDayService.getCourseDayById(id)
                .map(courseDay -> buildResponse(HttpStatus.OK, "Course day found", convertToDTO(courseDay)))
                .orElseGet(() -> buildErrorResponse(HttpStatus.NOT_FOUND, "Course day not found"));
    }

    @GetMapping
    public ResponseEntity<JsonResponse<List<CourseDayDTO>>> getAllCourseDays() {
        List<CourseDayDTO> courseDayDTOs = courseDayService.getAllCourseDays().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return buildResponse(HttpStatus.OK, "Course days retrieved successfully", courseDayDTOs);
    }

    @PutMapping("/{id}")
    public ResponseEntity<JsonResponse<CourseDayDTO>> updateCourseDay(@PathVariable Long id, @RequestBody CourseDayDTO courseDayDTO) {
        CourseDay updatedCourseDay = courseDayService.updateCourseDay(id, courseDayDTO.getDayNum(), courseDayDTO.getDayDate());
        return updatedCourseDay != null ? buildResponse(HttpStatus.OK, "Course day updated successfully", convertToDTO(updatedCourseDay))
                : buildErrorResponse(HttpStatus.NOT_FOUND, "Course day not found");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourseDay(@PathVariable Long id) {
        courseDayService.deleteCourseDay(id);
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
    private CourseDayDTO convertToDTO(CourseDay courseDay) {
        return new CourseDayDTO(courseDay.getId(), courseDay.getCourse().getId(), courseDay.getDayNum(), courseDay.getDayDate());
    }
}