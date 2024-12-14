package com.project.tripmate.global.mail;

import com.project.tripmate.global.JsonResponse;
import com.project.tripmate.user.dto.UserResponseDTO;
import com.project.tripmate.user.service.UserService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mail")
@RequiredArgsConstructor
public class MailController {

    private final MailService mailService;
    private final UserService userService;

    @PostMapping("/send")
    public ResponseEntity<JsonResponse<MailRequestDTO>> sendEmail(@RequestBody MailRequestDTO mailRequestDTO) throws MessagingException {
        mailService.sendEmail(mailRequestDTO.getTo(), mailRequestDTO.getVerificationUrl(), mailRequestDTO.getSubject());
        JsonResponse<MailRequestDTO> response = new JsonResponse<>(
                HttpStatus.OK.value(),
                "이메일 전송이 완료되었습니다.",
                mailRequestDTO);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/verify/{token}")
    public ResponseEntity<JsonResponse<UserResponseDTO>> verifyEmailForUser(@PathVariable("token") String token) {
        UserResponseDTO userResponseDTO = userService.verifyEmailForUser(token);
        JsonResponse<UserResponseDTO> response = new JsonResponse<>(
                HttpStatus.OK.value(),
                "이메일 인증이 완료되었습니다.",
                userResponseDTO);
        return ResponseEntity.ok(response);
    }

    // 이메일 전송 관련 예외 처리 (MessagingException)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "500", description = "이메일 전송에 실패했습니다.")
    })
    @ExceptionHandler(MessagingException.class)
    public ResponseEntity<JsonResponse> handleMessagingException(MessagingException ex) {
        JsonResponse response = new JsonResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                ex.getMessage(),
                null
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
