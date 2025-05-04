package com.project.tripmate.course.controller;

import com.project.tripmate.course.dto.CoursePlaceRequestDTO;
import com.project.tripmate.course.mapper.CoursePlaceMapper;
import com.project.tripmate.global.JsonResponse;
import com.project.tripmate.course.domain.CoursePlace;
import com.project.tripmate.course.dto.CoursePlaceDTO;
import com.project.tripmate.course.service.CoursePlaceService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/course-places")
@RequiredArgsConstructor
public class CoursePlaceController {

    private final CoursePlaceService coursePlaceService;
    private final CoursePlaceMapper coursePlaceMapper;

    @PostMapping
    @Operation(summary = "코스 장소 생성")
    public ResponseEntity<JsonResponse<CoursePlaceDTO>> create(@RequestBody CoursePlaceRequestDTO dto) {
        CoursePlace created = coursePlaceService.createCoursePlace(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(JsonResponse.success(coursePlaceMapper.toDTO(created), "코스 장소가 성공적으로 생성되었습니다."));
    }

    @GetMapping("/{id}")
    @Operation(summary = "코스 장소 단건 조회")
    public ResponseEntity<JsonResponse<CoursePlaceDTO>> getById(@PathVariable Long id) {
        return coursePlaceService.getCoursePlaceById(id)
                .map(place -> JsonResponse.success(coursePlaceMapper.toDTO(place), "조회 성공"))
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(JsonResponse.failure(HttpStatus.NOT_FOUND, "존재하지 않는 코스 장소입니다.")));
    }

    @GetMapping
    @Operation(summary = "코스 장소 전체 조회")
    public ResponseEntity<JsonResponse<List<CoursePlaceDTO>>> getAll() {
        List<CoursePlace> allPlaces = coursePlaceService.getAllCoursePlaces();
        return ResponseEntity.ok(JsonResponse.success(
                coursePlaceMapper.toDTOList(allPlaces), "전체 코스 장소 조회 성공"));
    }

    @PutMapping("/{id}")
    @Operation(summary = "코스 장소 수정")
    public ResponseEntity<JsonResponse<CoursePlaceDTO>> update(
            @PathVariable Long id, @RequestBody CoursePlaceRequestDTO dto) {
        CoursePlace updated = coursePlaceService.updateCoursePlace(id, dto);
        return ResponseEntity.ok(JsonResponse.success(
                coursePlaceMapper.toDTO(updated), "수정 성공"));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "코스 장소 삭제")
    public ResponseEntity<JsonResponse<Void>> delete(@PathVariable Long id) {
        coursePlaceService.deleteCoursePlace(id);
        return ResponseEntity.noContent().build();
    }
}

