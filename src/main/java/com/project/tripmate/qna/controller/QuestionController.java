package com.project.tripmate.qna.controller;

import com.project.tripmate.global.JsonResponse;
import com.project.tripmate.qna.dto.QuestionDTO;
import com.project.tripmate.qna.service.QuestionService;
import com.project.tripmate.user.domain.CustomUserDetails;
import com.project.tripmate.user.domain.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/qua/questions")
@RequiredArgsConstructor
@Tag(name = "Question API", description = "질문 관련 API")
public class QuestionController {

    private final QuestionService questionService;

    @PostMapping
    @Operation(summary = "질문 생성", description = "새로운 질문을 생성합니다.")
    public ResponseEntity<JsonResponse<QuestionDTO>> createQuestion(@RequestBody QuestionDTO dto, @AuthenticationPrincipal CustomUserDetails userDetails) {
        User user = userDetails.getUser();
        QuestionDTO questionDTO = questionService.createQuestion(user.getId(), dto.getCategory(), dto.getTitle(), dto.getContent(),
                dto.isSecret());
        return ResponseEntity.ok(JsonResponse.success(questionDTO, "성공적으로 문의 글을 생성했습니다."));
    }

    @GetMapping
    @Operation(summary = "내 질문 조회", description = "사용자가 작성한 질문 목록을 조회합니다.")
    public ResponseEntity<JsonResponse<List<QuestionDTO>>> getMyQuestions(@AuthenticationPrincipal CustomUserDetails userDetails) {
        User user = userDetails.getUser();
        return ResponseEntity.ok(JsonResponse.success(questionService.getMyQuestions(user.getId()), "성공적으로 내 질문을 조회했습니다."));
    }

    @GetMapping("/{id}")
    @Operation(summary = "질문 상세 조회", description = "특정 질문의 상세 정보를 조회합니다.")
    public ResponseEntity<JsonResponse<QuestionDTO>> getQuestion(@PathVariable Long id, @AuthenticationPrincipal CustomUserDetails userDetails) {
        User user = userDetails.getUser();
        return ResponseEntity.ok(JsonResponse.success(questionService.getQuestion(id, user.getId()), "성공적으로 질문의 상세 정보를 조회했습니다."));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<JsonResponse<String>> handleRuntimeException(RuntimeException e) {
        JsonResponse<String> response = JsonResponse.failure(HttpStatus.BAD_REQUEST, e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
}

