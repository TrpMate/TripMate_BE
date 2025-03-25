package com.project.tripmate.global;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public class JsonResponse<T> {
    private final int statusCode;
    private final String message;
    private T data;  // 제네릭으로 데이터 타입을 변경

    public String toJson() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "{}";
        }
    }

    // 성공과 데이터를 포함하는 JsonResponse 생성
    public static <T> JsonResponse<T> success(T data, String message) {
        return new JsonResponse<>(200, message, data);
    }

    // 실패와 메시지를 포함하는 JsonResponse 생성
    public static <T> JsonResponse<T> failure(HttpStatus statusCode, String message) {
        return new JsonResponse<>(statusCode.value(), message, null);
    }
}
