package com.project.tripmate.global;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.mail.MessagingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 유효성 검사 실패 시 발생하는 예외 처리 (MethodArgumentNotValidException)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "422", description = "유효성 검사 실패")
    })
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<JsonResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errorMessages = new HashMap<>();

        // 모든 유효성 검사 오류를 필드와 메시지로 매핑하여 반환
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String message = error.getDefaultMessage();
            errorMessages.put(fieldName, message);
        });

        // ValidateJsonResponse로 응답 생성
        JsonResponse response = new JsonResponse(
                HttpStatus.UNPROCESSABLE_ENTITY.value(),  // 422
                "유효성 검사 실패",
                errorMessages
        );

        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(response);
    }

    // 모든 예외 처리 (예상하지 못한 예외 처리)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "500", description = "서버 오류가 발생했습니다. 잠시 후 다시 시도해주세요.")
    })
    @ExceptionHandler(Exception.class)
    public ResponseEntity<JsonResponse> handleGenericException(Exception ex) {
        JsonResponse response = new JsonResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage(), null);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

}
