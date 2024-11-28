package com.project.tripmate.tourAPI.controller;

import com.project.tripmate.tourAPI.domain.CourseUser;
import com.project.tripmate.tourAPI.dto.CourseUserDTO;
import com.project.tripmate.global.jsonResponse.CourseUserJsonResponse;
import com.project.tripmate.tourAPI.service.CourseUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/course-users")
public class CourseUserController {

    private final CourseUserService courseUserService;

    @Autowired
    public CourseUserController(CourseUserService courseUserService) {
        this.courseUserService = courseUserService;
    }

    @PostMapping
    public ResponseEntity<CourseUserJsonResponse> createCourseUser(@RequestParam Long courseId,
                                                                   @RequestParam Long userId) {
        CourseUser courseUser = courseUserService.createCourseUser(courseId, userId);
        if (courseUser != null) {
            CourseUserDTO courseUserDTO = convertToDTO(courseUser);
            CourseUserJsonResponse response = new CourseUserJsonResponse(HttpStatus.CREATED.value(), "Course user created successfully", courseUserDTO);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new CourseUserJsonResponse(HttpStatus.BAD_REQUEST.value(), "Course or User not found", null));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseUserJsonResponse> getCourseUserById(@PathVariable Long id) {
        Optional<CourseUser> courseUser = courseUserService.getCourseUserById(id);
        if (courseUser.isPresent()) {
            CourseUserDTO courseUserDTO = convertToDTO(courseUser.get());
            CourseUserJsonResponse response = new CourseUserJsonResponse(HttpStatus.OK.value(), "Course user found", courseUserDTO);
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new CourseUserJsonResponse(HttpStatus.NOT_FOUND.value(), "Course user not found", null));
        }
    }

    @GetMapping
    public ResponseEntity<CourseUserJsonResponse> getAllCourseUsers() {
        List<CourseUser> courseUsers = courseUserService.getAllCourseUsers();
        List<CourseUserDTO> courseUserDTOs = courseUsers.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        CourseUserJsonResponse response = new CourseUserJsonResponse(HttpStatus.OK.value(), "Course users retrieved successfully", null);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CourseUserJsonResponse> updateCourseUser(@PathVariable Long id,
                                                                   @RequestParam Long courseId,
                                                                   @RequestParam Long userId) {
        CourseUser updatedCourseUser = courseUserService.updateCourseUser(id, courseId, userId);
        if (updatedCourseUser != null) {
            CourseUserDTO courseUserDTO = convertToDTO(updatedCourseUser);
            CourseUserJsonResponse response = new CourseUserJsonResponse(HttpStatus.OK.value(), "Course user updated successfully", courseUserDTO);
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new CourseUserJsonResponse(HttpStatus.NOT_FOUND.value(), "Course user not found", null));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CourseUserJsonResponse> deleteCourseUser(@PathVariable Long id) {
        courseUserService.deleteCourseUser(id);
        CourseUserJsonResponse response = new CourseUserJsonResponse(HttpStatus.NO_CONTENT.value(), "Course user deleted successfully", null);
        return ResponseEntity.noContent().build();
    }

    // DTO 변환 메서드
    private CourseUserDTO convertToDTO(CourseUser courseUser) {
        return new CourseUserDTO(
                courseUser.getId(),
                courseUser.getCourse().getId(),
                courseUser.getUser().getId(),
                courseUser.getJoinedDate()
        );
    }
}