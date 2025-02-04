package com.project.tripmate.tourAPI.controller;

import com.project.tripmate.global.JsonResponse;
import com.project.tripmate.tourAPI.domain.Course;
import com.project.tripmate.tourAPI.dto.CourseDTO;
import com.project.tripmate.tourAPI.service.CourseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(summary = "코스 생성 API", description = "새로운 여행 코스를 생성합니다.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "코스 생성 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = JsonResponse.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "statusCode": 201,
                                                "message": "코스가 성공적으로 생성되었습니다.",
                                                "data": {
                                                    "id": 1,
                                                    "courseName": "유럽 여행",
                                                    "isPublic": true,
                                                    "startDate": "2024-07-01",
                                                    "endDate": "2024-07-15",
                                                    "createdAt": "2024-02-04T10:15:30",
                                                    "updatedAt": "2024-02-04T10:15:30"
                                                }
                                            }
                                            """
                            )
                    )
            )
    })
    @PostMapping
    public ResponseEntity<JsonResponse<CourseDTO>> createCourse(@RequestBody CourseDTO courseDTO) {
        courseService.createCourse(courseDTO);
        return buildResponse(HttpStatus.CREATED, "코스가 성공적으로 생성되었습니다.", courseDTO);
    }

    @Operation(summary = "코스 조회 API", description = "주어진 ID의 여행 코스를 조회합니다.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "코스 조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = JsonResponse.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "statusCode": 200,
                                                "message": "코스 조회에 성공했습니다.",
                                                "data": {
                                                    "id": 1,
                                                    "courseName": "유럽 여행",
                                                    "isPublic": true,
                                                    "startDate": "2024-07-01",
                                                    "endDate": "2024-07-15",
                                                    "createdAt": "2024-02-04T10:15:30",
                                                    "updatedAt": "2024-02-04T10:15:30"
                                                }
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "코스가 존재하지 않습니다.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = JsonResponse.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "statusCode": 404,
                                                "message": "코스가 존재하지 않습니다.",
                                                "data": null
                                            }
                                            """
                            )
                    )
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<JsonResponse<CourseDTO>> getCourseById(@PathVariable Long id) {
        return courseService.getCourseById(id)
                .map(course -> buildResponse(HttpStatus.OK, "코스 조회에 성공했습니다.", convertToDTO(course)))
                .orElseGet(() -> buildErrorResponse(HttpStatus.NOT_FOUND, "코스가 존재하지 않습니다."));
    }

    @Operation(summary = "코스 목록 조회 API", description = "모든 여행 코스를 조회합니다.")
    @ApiResponse(
            responseCode = "200",
            description = "코스 목록 조회 성공",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = JsonResponse.class),
                    examples = @ExampleObject(
                            value = """
                                    {
                                        "statusCode": 200,
                                        "message": "코스 목록 조회에 성공했습니다.",
                                        "data": [
                                            {
                                                "id": 1,
                                                "courseName": "유럽 여행",
                                                "isPublic": true,
                                                "startDate": "2024-07-01",
                                                "endDate": "2024-07-15",
                                                "createdAt": "2024-02-04T10:15:30",
                                                "updatedAt": "2024-02-04T10:15:30"
                                            },
                                            {
                                                "id": 2,
                                                "courseName": "아시아 여행",
                                                "isPublic": false,
                                                "startDate": "2024-08-01",
                                                "endDate": "2024-08-15",
                                                "createdAt": "2024-02-04T10:15:30",
                                                "updatedAt": "2024-02-04T10:15:30"
                                            }
                                        ]
                                    }
                                    """
                    )
            )
    )
    @GetMapping
    public ResponseEntity<JsonResponse<List<CourseDTO>>> getAllCourses() {
        List<CourseDTO> courseDTOs = courseService.getAllCourses().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return buildResponse(HttpStatus.OK, "코스 목록 조회에 성공했습니다.", courseDTOs);
    }

    @Operation(summary = "코스 업데이트 API", description = "주어진 ID의 여행 코스를 업데이트합니다.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "코스 업데이트 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = JsonResponse.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "statusCode": 200,
                                                "message": "코스 업데이트에 성공했습니다.",
                                                "data": {
                                                    "id": 1,
                                                    "courseName": "유럽 여행 - 이탈리아",
                                                    "isPublic": true,
                                                    "startDate": "2024-07-01",
                                                    "endDate": "2024-07-15",
                                                    "createdAt": "2024-02-04T10:15:30",
                                                    "updatedAt": "2024-02-04T10:15:30"
                                                }
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "코스가 존재하지 않습니다.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = JsonResponse.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "statusCode": 404,
                                                "message": "코스가 존재하지 않습니다.",
                                                "data": null
                                            }
                                            """
                            )
                    )
            )
    })
    @PutMapping("/{id}")
    public ResponseEntity<JsonResponse<CourseDTO>> updateCourse(@PathVariable Long id,
            @RequestBody CourseDTO courseDTO) {
        Course updatedCourse = courseService.updateCourse(id, courseDTO.getCourseName(), courseDTO.isPublic(),
                courseDTO.getStartDate(), courseDTO.getEndDate());
        return updatedCourse != null
                ? buildResponse(HttpStatus.OK, "코스 업데이트에 성공했습니다.", convertToDTO(updatedCourse))
                : buildErrorResponse(HttpStatus.NOT_FOUND, "코스가 존재하지 않습니다.");
    }

    @Operation(summary = "코스 삭제 API", description = "주어진 ID의 여행 코스를 삭제합니다.")
    @ApiResponse(
            responseCode = "204",
            description = "코스 삭제에 성공했습니다."
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long id) {
        courseService.deleteCourse(id);
        return ResponseEntity.noContent().build();
    }

    private <T> ResponseEntity<JsonResponse<T>> buildResponse(HttpStatus status, String message, T data) {
        JsonResponse<T> response = new JsonResponse<>(status.value(), message, data);
        return new ResponseEntity<>(response, status);
    }

    private <T> ResponseEntity<JsonResponse<T>> buildErrorResponse(HttpStatus status, String message) {
        JsonResponse<T> response = new JsonResponse<>(status.value(), message, null);
        return new ResponseEntity<>(response, status);
    }

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
