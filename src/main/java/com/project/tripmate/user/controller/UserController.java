package com.project.tripmate.user.controller;

import com.project.tripmate.global.exception.DuplicateEmailException;
import com.project.tripmate.global.JsonResponse;
import com.project.tripmate.user.dto.UserRequestDTO;
import com.project.tripmate.user.dto.UserResponseDTO;
import com.project.tripmate.user.service.UserService;
import com.project.tripmate.global.exception.UserNotFoundException;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // 회원 가입
    @PostMapping("/signup")
    public ResponseEntity<JsonResponse<UserResponseDTO>> signUp(@Valid @RequestBody UserRequestDTO userRequestDTO)
            throws MessagingException {
        UserResponseDTO userResponseDTO = userService.signUp(userRequestDTO);
        JsonResponse<UserResponseDTO> response = new JsonResponse<>(
                HttpStatus.CREATED.value(),
                "회원 가입이 성공적으로 실행되었습니다. 이메일 인증을 완료해주세요.",
                userResponseDTO);
        return ResponseEntity.ok().body(response);
    }

    // 회원 정보 조회
    @GetMapping("/{userId}")
    public ResponseEntity<JsonResponse<UserResponseDTO>> getUser(@PathVariable Long userId) {
        UserResponseDTO userResponseDTO = userService.getUserById(userId);
        JsonResponse<UserResponseDTO> response = new JsonResponse<>(
                HttpStatus.OK.value(),
                "회원 정보를 성공적으로 조회했습니다.",
                userResponseDTO);
        return ResponseEntity.ok().body(response);
    }

    // 회원 정보 수정
    @PutMapping("/{userId}")
    public ResponseEntity<JsonResponse<UserResponseDTO>> updateUser(@PathVariable Long userId,
            @Valid @RequestBody UserRequestDTO userRequestDTO) {
        UserResponseDTO userResponseDTO = userService.updateUser(userId, userRequestDTO);
        JsonResponse<UserResponseDTO> response = new JsonResponse<>(
                HttpStatus.OK.value(),
                "회원 정보를 성공적으로 수정했습니다. 이메일 인증을 완료해주세요.",
                userResponseDTO);
        return ResponseEntity.ok().body(response);
    }

    // 회원 삭제
    @DeleteMapping("/{userId}")
    public ResponseEntity<JsonResponse<Void>> deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        JsonResponse<Void> response = new JsonResponse<>(
                HttpStatus.NO_CONTENT.value(),
                "회원 정보를 성공적으로 삭제했습니다.",
                null);
        return ResponseEntity.ok().body(response);
    }


    // 이미 존재하는 이메일로 회원가입을 시도할 때 발생하는 예외 처리
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "이미 등록된 이메일입니다.")
    })
    @ExceptionHandler(DuplicateEmailException.class)
    public ResponseEntity<JsonResponse<UserResponseDTO>> handleIllegalStateException(DuplicateEmailException ex) {
        JsonResponse<UserResponseDTO> response = new JsonResponse<>(
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage(),
                null
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    // 사용자를 찾을 수 없을 때 발생하는 예외 처리
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<JsonResponse<UserResponseDTO>> handleUserNotFoundException(UserNotFoundException ex) {
        JsonResponse<UserResponseDTO> response = new JsonResponse<>(
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage(),
                null);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }
}
