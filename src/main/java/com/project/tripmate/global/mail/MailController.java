package com.project.tripmate.global.mail;

import com.project.tripmate.global.JsonResponse;
import com.project.tripmate.user.domain.User;
import com.project.tripmate.user.dto.UserResponseDTO;
import com.project.tripmate.user.service.UserService;
import com.project.tripmate.tourAPI.service.CourseUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.mail.MessagingException;
import java.sql.SQLIntegrityConstraintViolationException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mail")
@RequiredArgsConstructor
public class MailController {

    private final MailService mailService;
    private final UserService userService;
    private final CourseUserService courseUserService;

    @Operation(summary = "이메일 전송", description = "사용자에게 가입 이메일을 전송합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "이메일 전송 완료",
                    content = @Content(schema = @Schema(implementation = JsonResponse.class),
                            examples = @ExampleObject(value = "{\"statusCode\": 200, \"message\": \"이메일 전송이 완료되었습니다.\", \"data\": {}}"))),
            @ApiResponse(responseCode = "500", description = "이메일 전송 실패",
                    content = @Content(schema = @Schema(implementation = JsonResponse.class),
                            examples = @ExampleObject(value = "{\"statusCode\": 500, \"message\": \"이메일 전송 실패\", \"data\": {}}")))
    })
    @PostMapping("/send")
    public ResponseEntity<JsonResponse<MailRequestDTO>> sendEmail(@RequestBody MailRequestDTO mailRequestDTO)
            throws MessagingException {
        mailService.sendEmailForSignUp(mailRequestDTO.getTo(), mailRequestDTO.getVerificationUrl(),
                mailRequestDTO.getSubject());
        return ResponseEntity.ok(JsonResponse.success(mailRequestDTO, "이메일 전송이 완료되었습니다."));
    }

    @Operation(summary = "이메일 인증", description = "사용자 이메일 인증을 수행합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "이메일 인증 완료",
                    content = @Content(schema = @Schema(implementation = JsonResponse.class),
                            examples = @ExampleObject(value = "{\"statusCode\": 200, \"message\": \"이메일 인증이 완료되었습니다.\", \"data\": {}}"))),
            @ApiResponse(responseCode = "400", description = "잘못된 인증 토큰",
                    content = @Content(schema = @Schema(implementation = JsonResponse.class),
                            examples = @ExampleObject(value = "{\"statusCode\": 400, \"message\": \"잘못된 인증 토큰\", \"data\": {}}")))
    })
    @GetMapping("/signup/verify/{token}")
    public ResponseEntity<JsonResponse<UserResponseDTO>> verifyEmailForUser(@PathVariable("token") String token) {
        UserResponseDTO userResponseDTO = userService.verifyEmailForUser(token);
        return ResponseEntity.ok(JsonResponse.success(userResponseDTO, "이메일 인증이 완료되었습니다."));
    }

    @Operation(summary = "초대 이메일 전송", description = "사용자에게 초대 이메일을 전송합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "초대 이메일 전송 완료",
                    content = @Content(schema = @Schema(implementation = JsonResponse.class),
                            examples = @ExampleObject(value = "{\"statusCode\": 200, \"message\": \"이메일 전송이 완료되었습니다.\", \"data\": {}}"))),
            @ApiResponse(responseCode = "500", description = "이메일 전송 실패",
                    content = @Content(schema = @Schema(implementation = JsonResponse.class),
                            examples = @ExampleObject(value = "{\"statusCode\": 500, \"message\": \"이메일 전송 실패\", \"data\": {}}")))
    })
    @PostMapping("/invite")
    public ResponseEntity<JsonResponse<MailForInviteRequestDTO>> sendEmailForInviteMember(
            @RequestBody MailForInviteRequestDTO mailForInviteRequestDTO) throws MessagingException {
        User user = userService.findByEmail(mailForInviteRequestDTO.getTo());
        mailService.sendEmailForInviteMember(user.getEmail(), mailForInviteRequestDTO.getSubject(),
                mailForInviteRequestDTO.getCourseId(), user.getId());
        return ResponseEntity.ok(JsonResponse.success(mailForInviteRequestDTO, "이메일 전송이 완료되었습니다."));
    }

    @Operation(summary = "초대 인증", description = "초대된 사용자가 초대 토큰을 인증하여 참여할 수 있도록 합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "초대 인증 완료",
                    content = @Content(schema = @Schema(implementation = JsonResponse.class),
                            examples = @ExampleObject(value = "{\"statusCode\": 200, \"message\": \"초대 인증이 완료되었습니다. 이제 팀의 멤버로 활동할 수 있습니다.\", \"data\": {}}"))),
            @ApiResponse(responseCode = "400", description = "초대 인증 실패",
                    content = @Content(schema = @Schema(implementation = JsonResponse.class),
                            examples = @ExampleObject(value = "{\"statusCode\": 400, \"message\": \"초대 인증 실패\", \"data\": {}}")))
    })
    @GetMapping("/invite/verify/{token}")
    public ResponseEntity<JsonResponse<String>> verifyInviteToken(@PathVariable("token") String token,
            @RequestParam("courseId") Long courseId,
            @RequestParam("userId") Long userId) {
        boolean isValid = mailService.validateInviteToken(courseId, userId, token);

        if (isValid) {
            courseUserService.createCourseUser(courseId, userId);
            mailService.deleteInviteToken(courseId, userId);
            return ResponseEntity.ok(JsonResponse.success(null, "초대 인증이 완료되었습니다. 이제 팀의 멤버로 활동할 수 있습니다."));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(JsonResponse.failure(HttpStatus.BAD_REQUEST, "초대 인증이 실패했습니다."));
        }
    }

    @Operation(summary = "이메일 전송 예외 처리", description = "이메일 전송 중 발생한 예외를 처리합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "500", description = "이메일 전송에 실패했습니다.",
                    content = @Content(schema = @Schema(implementation = JsonResponse.class),
                            examples = @ExampleObject(value = "{\"statusCode\": 500, \"message\": \"이메일 전송 실패\", \"data\": {}}")))
    })
    @ExceptionHandler(MessagingException.class)
    public ResponseEntity<JsonResponse<Object>> handleMessagingException(MessagingException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(JsonResponse.failure(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage()));
    }

    @Operation(summary = "중복 데이터 예외 처리", description = "중복된 데이터가 발생할 경우 처리합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "409", description = "이미 해당 코스에 참여한 사용자입니다.",
                    content = @Content(schema = @Schema(implementation = JsonResponse.class),
                            examples = @ExampleObject(value = "{\"statusCode\": 409, \"message\": \"이미 해당 코스에 참여한 사용자입니다.\", \"data\": {}}")))
    })
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ResponseEntity<JsonResponse<String>> handleDuplicateEntry(SQLIntegrityConstraintViolationException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(JsonResponse.failure(HttpStatus.CONFLICT, "이미 해당 코스에 참여한 사용자입니다."));
    }
}

