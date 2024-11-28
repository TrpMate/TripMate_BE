package com.project.tripmate.tourAPI.controller;

import com.project.tripmate.global.jsonResponse.CourseDayJsonResponse;
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
    public ResponseEntity<CourseDayJsonResponse> createCourseDay(@RequestBody CourseDayDTO courseDayDTO) {
        CourseDay courseDay = courseDayService
                .createCourseDay(courseDayDTO.getCourseId(), courseDayDTO.getDayNum(), courseDayDTO.getDayDate());

        if (courseDay != null) {
            CourseDayDTO createdCourseDayDTO = convertToDTO(courseDay);
            CourseDayJsonResponse response = new CourseDayJsonResponse(HttpStatus.CREATED.value(),
                    "Course day created successfully", createdCourseDayDTO);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new CourseDayJsonResponse(HttpStatus.BAD_REQUEST.value(), "Course not found", null));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseDayJsonResponse> getCourseDayById(@PathVariable Long id) {
        Optional<CourseDay> courseDay = courseDayService.getCourseDayById(id);

        if (courseDay.isPresent()) {
            CourseDayDTO courseDayDTO = convertToDTO(courseDay.get());
            CourseDayJsonResponse response = new CourseDayJsonResponse(HttpStatus.OK.value(), "Course day found",
                    courseDayDTO);
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new CourseDayJsonResponse(HttpStatus.NOT_FOUND.value(), "Course day not found", null));
        }
    }

    @GetMapping
    public ResponseEntity<CourseDayJsonResponse> getAllCourseDays() {
        List<CourseDay> courseDays = courseDayService.getAllCourseDays();
        List<CourseDayDTO> courseDayDTOs = courseDays.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        CourseDayJsonResponse response = new CourseDayJsonResponse(HttpStatus.OK.value(),
                "Course days retrieved successfully", (CourseDayDTO) courseDayDTOs);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CourseDayJsonResponse> updateCourseDay(@PathVariable Long id,
            @RequestBody CourseDayDTO courseDayDTO) {
        CourseDay updatedCourseDay = courseDayService
                .updateCourseDay(id, courseDayDTO.getDayNum(), courseDayDTO.getDayDate());

        if (updatedCourseDay != null) {
            CourseDayDTO updatedCourseDayDTO = convertToDTO(updatedCourseDay);
            CourseDayJsonResponse response = new CourseDayJsonResponse(HttpStatus.OK.value(),
                    "Course day updated successfully", updatedCourseDayDTO);
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new CourseDayJsonResponse(HttpStatus.NOT_FOUND.value(), "Course day not found", null));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CourseDayJsonResponse> deleteCourseDay(@PathVariable Long id) {
        courseDayService.deleteCourseDay(id);
        return ResponseEntity.noContent().build();
    }

    // DTO 변환 메서드
    private CourseDayDTO convertToDTO(CourseDay courseDay) {
        return new CourseDayDTO(
                courseDay.getId(),
                courseDay.getCourse().getId(),
                courseDay.getDayNum(),
                courseDay.getDayDate()
        );
    }
}
