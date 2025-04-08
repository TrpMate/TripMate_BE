package com.project.tripmate.course.controller;

import com.project.tripmate.global.JsonResponse;
import com.project.tripmate.course.domain.CoursePlace;
import com.project.tripmate.course.dto.CoursePlaceDTO;
import com.project.tripmate.course.service.CoursePlaceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/course-places")
@RequiredArgsConstructor
public class CoursePlaceController {

    private final CoursePlaceService coursePlaceService;

    @Operation(summary = "코스 장소 생성", description = "CourseDay에 장소를 추가합니다.")
    @PostMapping
    public ResponseEntity<JsonResponse<CoursePlaceDTO>> createCoursePlace(
            @Parameter(description = "코스 날짜 ID") @RequestParam(name = "courseDayId") Long courseDayId,
            @Parameter(description = "장소 이름") @RequestParam(name = "placeName") String placeName,
            @Parameter(description = "장소 타입") @RequestParam(name = "contentTypeId") String contentTypeId,
            @Parameter(description = "방문 시작 시간") @RequestParam(name = "visitStartTime") LocalTime visitStartTime,
            @Parameter(description = "방문 종료 시간") @RequestParam(name = "visitEndTime") LocalTime visitEndTime,
            @Parameter(description = "지도 X 좌표") @RequestParam(name = "mapX") double mapX,
            @Parameter(description = "지도 Y 좌표") @RequestParam(name = "mapY") double mapY,
            @Parameter(description = "전화번호") @RequestParam(name = "phoneNumber", required = false) String phoneNumber,
            @Parameter(description = "메모") @RequestParam(name = "memo", required = false) String memo
    ) {
        CoursePlace coursePlace = coursePlaceService.createCoursePlace(
                courseDayId, placeName, contentTypeId, visitStartTime, visitEndTime, mapX, mapY, phoneNumber, memo
        );

        return (coursePlace != null)
                ? ResponseEntity.status(HttpStatus.CREATED)
                .body(JsonResponse.success(convertToDTO(coursePlace), "코스 장소가 성공적으로 생성되었습니다."))
                : ResponseEntity.badRequest()
                        .body(JsonResponse.failure(HttpStatus.BAD_REQUEST, "코스 장소를 생성할 수 없습니다."));
    }

    @Operation(summary = "코스 장소 조회", description = "코스 장소 ID로 조회합니다.")
    @GetMapping("/{id}")
    public ResponseEntity<JsonResponse<CoursePlaceDTO>> getCoursePlaceById(
            @Parameter(description = "코스 장소 ID") @PathVariable Long id
    ) {
        Optional<CoursePlace> coursePlace = coursePlaceService.getCoursePlaceById(id);
        return coursePlace
                .map(place -> ResponseEntity.ok(
                        JsonResponse.success(convertToDTO(place), "코스 장소를 성공적으로 조회했습니다.")))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(JsonResponse.failure(HttpStatus.NOT_FOUND, "코스 장소를 찾을 수 없습니다.")));
    }

    @Operation(summary = "모든 코스 장소 조회", description = "전체 코스 장소 목록을 반환합니다.")
    @GetMapping
    public ResponseEntity<JsonResponse<List<CoursePlaceDTO>>> getAllCoursePlaces() {
        List<CoursePlace> coursePlaces = coursePlaceService.getAllCoursePlaces();
        List<CoursePlaceDTO> dtoList = coursePlaces.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(JsonResponse.success(dtoList, "전체 코스 장소를 성공적으로 조회했습니다."));
    }

    @Operation(summary = "코스 장소 수정", description = "코스 장소의 정보를 수정합니다.")
    @PutMapping("/{id}")
    public ResponseEntity<JsonResponse<CoursePlaceDTO>> updateCoursePlace(
            @Parameter(description = "코스 장소 ID") @PathVariable Long id,
            @Parameter(description = "장소 이름") @RequestParam String placeName,
            @Parameter(description = "장소 타입") @RequestParam String contentTypeId,
            @Parameter(description = "방문 시작 시간") @RequestParam LocalTime visitStartTime,
            @Parameter(description = "방문 종료 시간") @RequestParam LocalTime visitEndTime,
            @Parameter(description = "지도 X 좌표") @RequestParam double mapX,
            @Parameter(description = "지도 Y 좌표") @RequestParam double mapY,
            @Parameter(description = "전화번호") @RequestParam(required = false) String phoneNumber,
            @Parameter(description = "메모") @RequestParam(required = false) String memo
    ) {
        CoursePlace updated = coursePlaceService.updateCoursePlace(
                id, placeName, contentTypeId, visitStartTime, visitEndTime, mapX, mapY, phoneNumber, memo
        );

        return (updated != null)
                ? ResponseEntity.ok(JsonResponse.success(convertToDTO(updated), "코스 장소가 성공적으로 수정되었습니다."))
                : ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(JsonResponse.failure(HttpStatus.NOT_FOUND, "해당 코스 장소를 찾을 수 없습니다."));
    }

    @Operation(summary = "코스 장소 삭제", description = "코스 장소를 삭제합니다.")
    @DeleteMapping("/{id}")
    public ResponseEntity<JsonResponse<Void>> deleteCoursePlace(
            @Parameter(description = "코스 장소 ID") @PathVariable Long id
    ) {
        coursePlaceService.deleteCoursePlace(id);
        return ResponseEntity.noContent().build();
    }

    // DTO 변환
    private CoursePlaceDTO convertToDTO(CoursePlace coursePlace) {
        return new CoursePlaceDTO(
                coursePlace.getId(),
                coursePlace.getCourseDay().getId(),
                coursePlace.getPlaceName(),
                coursePlace.getContentTypeId(),
                coursePlace.getVisitStartTime(),
                coursePlace.getVisitEndTime(),
                coursePlace.getMapX(),
                coursePlace.getMapY(),
                coursePlace.getPhoneNumber(),
                coursePlace.getMemo()
        );
    }
}
