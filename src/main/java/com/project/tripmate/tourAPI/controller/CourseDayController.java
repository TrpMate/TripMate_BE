package com.project.tripmate.tourAPI.controller;

import com.project.tripmate.tourAPI.domain.CourseDay;
import com.project.tripmate.tourAPI.dto.CourseDayDTO;
import com.project.tripmate.tourAPI.dto.CourseDayJsonResponse;
import com.project.tripmate.tourAPI.service.CourseDayService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/course-day")
@RequiredArgsConstructor
public class CourseDayController {

    private final CourseDayService courseDayService;

    @PostMapping
    public ResponseEntity<CourseDayJsonResponse> createCourseDay(@RequestParam Long courseId,
                                                                 @RequestParam int dayNum,
                                                                 @RequestParam LocalDate dayDate) {
        CourseDay courseDay = courseDayService.createCourseDay(courseId, dayNum, dayDate);
        if (courseDay != null) {
            CourseDayDTO courseDayDTO = convertToDTO(courseDay);
            CourseDayJsonResponse response = new CourseDayJsonResponse(HttpStatus.CREATED.value(), "Course day created successfully", courseDayDTO);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new CourseDayJsonResponse(HttpStatus.BAD_REQUEST.value(), "Course not found", null));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseDayJsonResponse> getCourseDayById(@PathVariable Long id) {
        Optional<CourseDay> courseDay = courseDayService.getCourseDayById(id);
        if (courseDay.isPresent()) {
            CourseDayDTO courseDayDTO = convertToDTO(courseDay.get());
            CourseDayJsonResponse response = new CourseDayJsonResponse(HttpStatus.OK.value(), "Course day found", courseDayDTO);
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new CourseDayJsonResponse(HttpStatus.NOT_FOUND.value(), "Course day not found", null));
        }
    }

    @GetMapping
    public ResponseEntity<CourseDayJsonResponse> getAllCourseDays() {
        List<CourseDay> courseDays = courseDayService.getAllCourseDays();
        List<CourseDayDTO> courseDayDTOs = courseDays.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        CourseDayJsonResponse response = new CourseDayJsonResponse(HttpStatus.OK.value(), "Course days retrieved successfully", null);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CourseDayJsonResponse> updateCourseDay(@PathVariable Long id,
                                                                 @RequestParam int dayNum,
                                                                 @RequestParam LocalDate dayDate) {
        CourseDay updatedCourseDay = courseDayService.updateCourseDay(id, dayNum, dayDate);
        if (updatedCourseDay != null) {
            CourseDayDTO courseDayDTO = convertToDTO(updatedCourseDay);
            CourseDayJsonResponse response = new CourseDayJsonResponse(HttpStatus.OK.value(), "Course day updated successfully", courseDayDTO);
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new CourseDayJsonResponse(HttpStatus.NOT_FOUND.value(), "Course day not found", null));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CourseDayJsonResponse> deleteCourseDay(@PathVariable Long id) {
        courseDayService.deleteCourseDay(id);
        CourseDayJsonResponse response = new CourseDayJsonResponse(HttpStatus.NO_CONTENT.value(), "Course day deleted successfully", null);
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
