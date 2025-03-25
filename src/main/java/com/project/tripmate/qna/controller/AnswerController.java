package com.project.tripmate.qna.controller;

import com.project.tripmate.global.JsonResponse;
import com.project.tripmate.qna.dto.AnswerDTO;
import com.project.tripmate.qna.service.AnswerService;
import com.project.tripmate.user.domain.CustomUserDetails;
import com.project.tripmate.user.domain.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/qna/answers")
@RequiredArgsConstructor
@Tag(name = "Answer API", description = "답변 관련 API")
public class AnswerController {

    private final AnswerService answerService;

    @PostMapping("/{questionId}")
    @Operation(summary = "답변 생성", description = "질문에 대한 답변을 작성합니다.")
    public ResponseEntity<JsonResponse<AnswerDTO>> createAnswer(@PathVariable Long questionId, @RequestBody AnswerDTO dto, @AuthenticationPrincipal CustomUserDetails userDetails) {
        User admin = userDetails.getUser();
        AnswerDTO answerDTO = answerService.createAnswer(questionId, admin.getId(), dto.getContent());
        return ResponseEntity.ok(JsonResponse.success(answerDTO, "성공적으로 답변을 생성했습니다."));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<JsonResponse<String>> handleRuntimeException(RuntimeException e) {
        JsonResponse<String> response = JsonResponse.failure(HttpStatus.BAD_REQUEST, e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
}