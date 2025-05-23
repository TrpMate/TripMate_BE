package com.project.tripmate.course.controller;

import com.project.tripmate.course.dto.CourseDayAndPlaceDTO;
import com.project.tripmate.global.JsonResponse;
import com.project.tripmate.course.domain.CourseDay;
import com.project.tripmate.course.dto.CourseDayDTO;
import com.project.tripmate.course.service.CourseDayService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/course-day")
@RequiredArgsConstructor
public class CourseDayController {

    private final CourseDayService courseDayService;

    @Operation(summary = "코스 일자 생성 API", description = "새로운 코스 일자를 생성합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "코스 일자 생성 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = JsonResponse.class), examples = @ExampleObject(value = """
                    {
                        "statusCode": 201,
                        "message": "코스 일자가 성공적으로 생성되었습니다.",
                        "data": {
                            "id": 1,
                            "courseId": 1,
                            "dayNum": 1,
                            "dayDate": "2024-07-01"
                        }
                    }
                    """))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 - 유효하지 않은 입력값", content = @Content(mediaType = "application/json", schema = @Schema(implementation = JsonResponse.class), examples = @ExampleObject(value = """
                    {
                        "statusCode": 400,
                        "message": "잘못된 요청입니다. 해당 코스가 존재하지 않습니다.",
                        "data": null
                    }
                    """)))})
    @PostMapping
    public ResponseEntity<JsonResponse<CourseDayDTO>> createCourseDay(@RequestBody CourseDayDTO courseDayDTO) {
        CourseDay courseDay = courseDayService.createCourseDay(courseDayDTO.getCourseId(), courseDayDTO.getDayNum(),
                courseDayDTO.getDayDate());
        return courseDay != null ? buildResponse(HttpStatus.CREATED, "코스 일자가 성공적으로 생성되었습니다.", convertToDTO(courseDay))
                : buildErrorResponse(HttpStatus.BAD_REQUEST, "잘못된 요청입니다. 해당 코스가 존재하지 않습니다.");
    }

    @Operation(summary = "코스 일자 조회 API", description = "ID로 특정 코스 일자를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "코스 일자 조회 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = JsonResponse.class), examples = @ExampleObject(value = """
                    {
                        "statusCode": 200,
                        "message": "코스 일자가 성공적으로 조회되었습니다.",
                        "data": {
                            "id": 1,
                            "courseId": 1,
                            "dayNum": 1,
                            "dayDate": "2024-07-01"
                        }
                    }
                    """))),
            @ApiResponse(responseCode = "404", description = "해당 코스 일자를 찾을 수 없습니다.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = JsonResponse.class), examples = @ExampleObject(value = """
                    {
                        "statusCode": 404,
                        "message": "해당 코스 일자를 찾을 수 없습니다.",
                        "data": null
                    }
                    """)))})
    @GetMapping("/{id}")
    public ResponseEntity<JsonResponse<CourseDayDTO>> getCourseDayById(@PathVariable Long id) {
        return courseDayService.getCourseDayById(id)
                .map(courseDay -> buildResponse(HttpStatus.OK, "코스 일자가 성공적으로 조회되었습니다.", convertToDTO(courseDay)))
                .orElseGet(() -> buildErrorResponse(HttpStatus.NOT_FOUND, "해당 코스 일자를 찾을 수 없습니다."));
    }

    @Operation(summary = "코스 일자 + 장소 조회 API", description = "ID로 특정 코스 일자와 해당 장소들을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "코스 일자 + 장소 조회 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = JsonResponse.class), examples = @ExampleObject(value = """
                    {
                       "statusCode": 200,
                       "message": "코스 일자와 장소들이 성공적으로 조회되었습니다.",
                       "data": {
                         "id": 1,
                         "courseId": 1,
                         "dayNum": 1,
                         "dayDate": "2024-07-01",
                         "coursePlaces": [
                           {
                             "id": 1,
                             "courseDayId": 1,
                             "placeName": "쿠키를 파는 카페",
                             "contentTypeId": "39",
                             "visitStartTime": "10:00:00",
                             "visitEndTime": "11:00:00",
                             "mapX": 127.123456,
                             "mapY": 37.123456,
                             "phoneNumber": "010-1234-5678",
                             "memo": "아침 커피 마시는 곳"
                           },
                           {
                             "id": 2,
                             "courseDayId": 1,
                             "placeName": "단풍 공원",
                             "contentTypeId": "12",
                             "visitStartTime": "11:30:00",
                             "visitEndTime": "13:00:00",
                             "mapX": 127.789123,
                             "mapY": 37.456789,
                             "phoneNumber": "",
                             "memo": "산책 코스"
                           }
                         ]
                       }
                     }
                """))),
            @ApiResponse(responseCode = "404", description = "해당 코스 일자를 찾을 수 없습니다.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = JsonResponse.class), examples = @ExampleObject(value = """
                {
                    "statusCode": 404,
                    "message": "해당 코스 일자를 찾을 수 없습니다.",
                    "data": null
                }
                """)))
    })
    @GetMapping("/{id}/with-places")
    public ResponseEntity<JsonResponse<CourseDayAndPlaceDTO>> getCourseDayWithPlaces(@PathVariable Long id) {
        return courseDayService.getCourseDayDTOById(id)
                .map(dto -> buildResponse(HttpStatus.OK, "코스 일자와 장소들이 성공적으로 조회되었습니다.", dto))
                .orElseGet(() -> buildErrorResponse(HttpStatus.NOT_FOUND, "해당 코스 일자를 찾을 수 없습니다."));
    }


    @Operation(summary = "모든 코스 일자 조회 API", description = "모든 코스 일자를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "코스 일자 목록 조회 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = JsonResponse.class), examples = @ExampleObject(value = """
            {
                "statusCode": 200,
                "message": "코스 일자 목록을 성공적으로 조회했습니다.",
                "data": [
                    {
                        "id": 1,
                        "courseId": 1,
                        "dayNum": 1,
                        "dayDate": "2024-07-01"
                    },
                    {
                        "id": 2,
                        "courseId": 1,
                        "dayNum": 2,
                        "dayDate": "2024-07-02"
                    }
                ]
            }
            """)))
    @GetMapping
    public ResponseEntity<JsonResponse<List<CourseDayDTO>>> getAllCourseDays() {
        List<CourseDayDTO> courseDayDTOs = courseDayService.getAllCourseDays().stream().map(this::convertToDTO)
                .collect(Collectors.toList());
        return buildResponse(HttpStatus.OK, "코스 일자 목록을 성공적으로 조회했습니다.", courseDayDTOs);
    }

    @GetMapping("/by-course-id/{courseId}")
    @Operation(
            summary = "특정 Course의 CourseDay 목록 조회",
            description = "courseId를 기준으로 CourseDay 목록을 조회합니다.",
            parameters = {
                    @Parameter(name = "courseId", description = "조회할 코스의 ID", required = true)
            }
    )
    @ApiResponse(
            responseCode = "200",
            description = "코스 일자 목록 조회 성공",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = JsonResponse.class),
                    examples = @ExampleObject(value = """
            {
                "statusCode": 200,
                "message": "코스 일자 목록을 성공적으로 조회했습니다.",
                "data": [
                    {
                        "id": 1,
                        "courseId": 1,
                        "dayNum": 1,
                        "dayDate": "2024-07-01"
                    },
                    {
                        "id": 2,
                        "courseId": 1,
                        "dayNum": 2,
                        "dayDate": "2024-07-02"
                    }
                ]
            }
            """)
            )
    )
    public ResponseEntity<JsonResponse<List<CourseDayDTO>>> getCourseDaysByCourse(@PathVariable Long courseId) {
        List<CourseDayDTO> courseDays = courseDayService.getCourseDaysByCourseId(courseId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return buildResponse(HttpStatus.OK, "해당 코스의 코스 일자 목록을 성공적으로 조회했습니다.", courseDays);
    }



    @Operation(summary = "코스 일자 수정 API", description = "ID로 특정 코스 일자를 수정합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "코스 일자 수정 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = JsonResponse.class), examples = @ExampleObject(value = """
                    {
                        "statusCode": 200,
                        "message": "코스 일자가 성공적으로 수정되었습니다.",
                        "data": {
                            "id": 1,
                            "courseId": 1,
                            "dayNum": 1,
                            "dayDate": "2024-07-01"
                        }
                    }
                    """))),
            @ApiResponse(responseCode = "404", description = "해당 코스 일자를 찾을 수 없음", content = @Content(mediaType = "application/json", schema = @Schema(implementation = JsonResponse.class), examples = @ExampleObject(value = """
                    {
                        "statusCode": 404,
                        "message": "해당 코스 일자를 찾을 수 없습니다.",
                        "data": null
                    }
                    """)))})
    @PutMapping("/{id}")
    public ResponseEntity<JsonResponse<CourseDayDTO>> updateCourseDay(@PathVariable Long id,
            @RequestBody CourseDayDTO courseDayDTO) {
        CourseDay updatedCourseDay = courseDayService.updateCourseDay(id, courseDayDTO.getDayNum(),
                courseDayDTO.getDayDate());
        return updatedCourseDay != null ? buildResponse(HttpStatus.OK, "코스 일자가 성공적으로 수정되었습니다.",
                convertToDTO(updatedCourseDay)) : buildErrorResponse(HttpStatus.NOT_FOUND, "해당 코스 일자를 찾을 수 없습니다.");
    }

    @Operation(summary = "코스 일자 삭제 API", description = "ID로 특정 코스 일자를 삭제합니다.")
    @ApiResponse(responseCode = "204", description = "코스 일자 삭제 성공", content = @Content(mediaType = "application/json"))
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
        return new CourseDayDTO(courseDay.getId(), courseDay.getCourse().getId(), courseDay.getDayNum(),
                courseDay.getDayDate());
    }

}