package com.project.tripmate.user.controller;

import com.project.tripmate.global.exception.DuplicateEmailException;
import com.project.tripmate.global.JsonResponse;
import com.project.tripmate.user.dto.UserRequestDTO;
import com.project.tripmate.user.dto.UserResponseDTO;
import com.project.tripmate.user.service.UserService;
import com.project.tripmate.global.exception.UserNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
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
    @Operation(summary = "회원가입 API", description = "유효성 검증을 이용한 회원가입 로직을 진행합니다. 회원가입 후에는 이메일 인증을 마쳐야 합니다.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "회원가입 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = JsonResponse.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "statusCode": 201,
                                                "message": "회원 가입이 성공적으로 실행되었습니다. 이메일 인증을 완료해주세요.",
                                                "data": {
                                                    "id": 1,
                                                    "username": "john_doe",
                                                    "nickname": "존도",
                                                    "email": "john1231@naver.com"
                                                }
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "이미 등록된 이메일로 회원가입을 하려고 할 때",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = JsonResponse.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "statusCode": 400,
                                                "message": "이미 등록된 이메일입니다.",
                                                "data": null
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "422",
                    description = "유효성 검사 실패 - 실패한 부분 알림",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = JsonResponse.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "statusCode": 422,
                                                "message": "유효성 검사 실패",
                                                "data": {
                                                    "password": "비밀번호는 영문 대소문자, 숫자, 특수문자를 포함하여 8~16자여야 합니다",
                                                    "nickname": "닉네임을 입력해주세요"
                                                }
                                            }
                                            """
                            )
                    )
            )
    })
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

    // 회원 정보 수정
    @Operation(summary = "회원 정보 수정 API", description = "유효성 검증을 활용하여 회원 정보를 수정합니다. 수정 후에는 이메일 검증을 진행합니다.")
    @Parameter(description = "수정할 사용자의 ID", name = "userId", required = true, in = ParameterIn.PATH)
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "회원 정보 수정 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = JsonResponse.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "statusCode": 200,
                                                "message": "회원 정보를 성공적으로 수정했습니다. 이메일 인증을 완료해주세요.",
                                                "data": {
                                                    "id": 1,
                                                    "username": "john_doe",
                                                    "nickname": "존도",
                                                    "email": "john123@naver.com"
                                                }
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "이미 등록된 이메일로 회원 정보를 수정하려고 하려고 할 때",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = JsonResponse.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "statusCode": 400,
                                                "message": "이미 등록된 이메일입니다.",
                                                "data": null
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "422",
                    description = "유효성 검사 실패 - 실패한 부분 알림",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = JsonResponse.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "statusCode": 422,
                                                "message": "유효성 검사 실패",
                                                "data": {
                                                    "password": "비밀번호는 영문 대소문자, 숫자, 특수문자를 포함하여 8~16자여야 합니다",
                                                    "nickname": "닉네임을 입력해주세요"
                                                }
                                            }
                                            """
                            )
                    )
            )
    })
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

    // 회원 정보 조회
    @Operation(summary = "회원 정보 조회 API", description = "사용자의 Id(IDX)를 사용하여 회원 정보를 조회합니다.")
    @Parameter(description = "수정할 사용자의 ID", name = "userId", required = true, in = ParameterIn.PATH)
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "회원 정보 조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = JsonResponse.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "statusCode": 200,
                                                "message": "회원 정보를 성공적으로 조회했습니다.",
                                                "data": {
                                                    "id": 1,
                                                    "username": "john_doe",
                                                    "nickname": "존도",
                                                    "email": "john1231@naver.com"
                                                }
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "회원 정보 조회 실패: 해당 ID를 가진 사용자를 찾을 수 없음",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = JsonResponse.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "statusCode": 404,
                                                "message": "해당 ID를 가진 사용자를 찾을 수 없습니다.",
                                                "data": null
                                            }
                                            """
                            )
                    )
            )
    })
    @GetMapping("/{userId}")
    public ResponseEntity<JsonResponse<UserResponseDTO>> getUser(@PathVariable Long userId) {
        UserResponseDTO userResponseDTO = userService.getUserById(userId);
        JsonResponse<UserResponseDTO> response = new JsonResponse<>(
                HttpStatus.OK.value(),
                "회원 정보를 성공적으로 조회했습니다.",
                userResponseDTO);
        return ResponseEntity.ok().body(response);
    }

    // 회원 삭제
    @Operation(summary = "회원 삭제 API", description = "사용자의 Id(IDX)를 사용하여 회원을 데이터에서 삭제합니다.")
    @Parameter(description = "수정할 사용자의 ID", name = "userId", required = true, in = ParameterIn.PATH)
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "회원 삭제 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = JsonResponse.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "statusCode": 204,
                                                "message": "회원 정보를 성공적으로 삭제했습니다.",
                                                "data": null
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "회원 삭제 실패: 해당 ID를 가진 사용자를 찾을 수 없음",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = JsonResponse.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "statusCode": 404,
                                                "message": "해당 ID를 가진 사용자를 찾을 수 없습니다.",
                                                "data": null
                                            }
                                            """
                            )
                    )
            )
    })
    @DeleteMapping("/{userId}")
    public ResponseEntity<JsonResponse<Void>> deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        JsonResponse<Void> response = new JsonResponse<>(
                HttpStatus.NO_CONTENT.value(),
                "회원 정보를 성공적으로 삭제했습니다.",
                null);
        return ResponseEntity.ok().body(response);
    }

    @Operation(summary = "이메일 중복체크 API", description = "이메일이 중복되는 경우 true, 중복되지 않은경우 false가 리턴됩니다.")
    @Parameter(description = "중복체크할 이메일", name = "email", required = true, in = ParameterIn.PATH)
    @GetMapping("/check-duplicate/email")
    public ResponseEntity<Boolean> checkEmailDuplicate(@PathVariable String email) {
        return ResponseEntity.ok(userService.checkEmailDuplicate(email));
    }

    @Operation(summary = "닉네임 중복체크 API", description = "닉네임이 중복되는 경우 true, 중복되지 않은경우 false가 리턴됩니다.")
    @Parameter(description = "중복체크할 닉네임", name = "nickname", required = true, in = ParameterIn.PATH)
    @GetMapping("/check-duplicate/nickname")
    public ResponseEntity<Boolean> checkNicknameDuplicate(@PathVariable String nickname) {
        return ResponseEntity.ok(userService.checkNicknameDuplicate(nickname));
    }


    // 이미 존재하는 이메일로 회원가입을 시도할 때 발생하는 예외 처리
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
