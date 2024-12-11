package com.project.tripmate.tourAPI.controller;

import com.project.tripmate.global.JsonResponse;
import com.project.tripmate.tourAPI.domain.CoursePlace;
import com.project.tripmate.tourAPI.dto.CoursePlaceDTO;
import com.project.tripmate.tourAPI.service.CoursePlaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/course-places")
public class CoursePlaceController {

    private final CoursePlaceService coursePlaceService;

    @Autowired
    public CoursePlaceController(CoursePlaceService coursePlaceService) {
        this.coursePlaceService = coursePlaceService;
    }

    @PostMapping
    public ResponseEntity<JsonResponse<CoursePlaceDTO>> createCoursePlace(@RequestParam Long courseDayId,
            @RequestParam String contentId,
            @RequestParam String contentTypeId,
            @RequestParam LocalDateTime placeTime,
            @RequestParam int courseOrder) {
        CoursePlace coursePlace = coursePlaceService
                .createCoursePlace(courseDayId, contentId, contentTypeId, placeTime, courseOrder);
        if (coursePlace != null) {
            CoursePlaceDTO coursePlaceDTO = convertToDTO(coursePlace);
            JsonResponse<CoursePlaceDTO> response = new JsonResponse<>(HttpStatus.CREATED.value(),
                    "Course place created successfully", coursePlaceDTO);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new JsonResponse<>(HttpStatus.BAD_REQUEST.value(), "Course day not found", null));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<JsonResponse<CoursePlaceDTO>> getCoursePlaceById(@PathVariable Long id) {
        Optional<CoursePlace> coursePlace = coursePlaceService.getCoursePlaceById(id);
        if (coursePlace.isPresent()) {
            CoursePlaceDTO coursePlaceDTO = convertToDTO(coursePlace.get());
            JsonResponse<CoursePlaceDTO> response = new JsonResponse<>(HttpStatus.OK.value(), "Course place found",
                    coursePlaceDTO);
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new JsonResponse<>(HttpStatus.NOT_FOUND.value(), "Course place not found", null));
        }
    }

    @GetMapping
    public ResponseEntity<JsonResponse<CoursePlaceDTO>> getAllCoursePlaces() {
        List<CoursePlace> coursePlaces = coursePlaceService.getAllCoursePlaces();
        List<CoursePlaceDTO> coursePlaceDTOs = coursePlaces.stream()
                .map(this::convertToDTO)
                .toList();
        JsonResponse<CoursePlaceDTO> response = new JsonResponse<>(HttpStatus.OK.value(),
                "Course places retrieved successfully", null);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<JsonResponse<CoursePlaceDTO>> updateCoursePlace(@PathVariable Long id,
            @RequestParam String contentId,
            @RequestParam String contentTypeId,
            @RequestParam LocalDateTime placeTime,
            @RequestParam int courseOrder) {
        CoursePlace updatedCoursePlace = coursePlaceService
                .updateCoursePlace(id, contentId, contentTypeId, placeTime, courseOrder);
        if (updatedCoursePlace != null) {
            CoursePlaceDTO coursePlaceDTO = convertToDTO(updatedCoursePlace);
            JsonResponse<CoursePlaceDTO> response = new JsonResponse<>(HttpStatus.OK.value(),
                    "Course place updated successfully", coursePlaceDTO);
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new JsonResponse<>(HttpStatus.NOT_FOUND.value(), "Course place not found", null));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<JsonResponse<CoursePlaceDTO>> deleteCoursePlace(@PathVariable Long id) {
        coursePlaceService.deleteCoursePlace(id);
        JsonResponse<CoursePlaceDTO> response = new JsonResponse<>(HttpStatus.NO_CONTENT.value(),
                "Course place deleted successfully", null);
        return ResponseEntity.noContent().build();
    }

    // DTO 변환 메서드
    private CoursePlaceDTO convertToDTO(CoursePlace coursePlace) {
        return new CoursePlaceDTO(
                coursePlace.getId(),
                coursePlace.getCourseDay().getId(),
                coursePlace.getContentId(),
                coursePlace.getContentTypeId(),
                coursePlace.getPlaceTime(),
                coursePlace.getCourseOrder()
        );
    }
}