package com.project.tripmate.global.oauth.controller;

import com.project.tripmate.global.JsonResponse;
import com.project.tripmate.global.oauth.domain.OAuthRequestDto;
import com.project.tripmate.global.oauth.service.OAuth2LoginService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/oauth")
@RequiredArgsConstructor
public class OAuthController {

    private final OAuth2LoginService oAuth2LoginService;

    @Operation(summary = "OAuth2 로그인 API", description = "Authorization 코드와 소셜 타입을 넘겨받으면 DB에 유저 정보를 저장하고 JWT 토큰을 발행합니다. ")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "소셜 로그인 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = JsonResponse.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "statusCode": 200,
                                                "message": "소셜 로그인에 성공했습니다.",
                                                "data": null
                                            }
                                            """
                            )
                    )
            )
    })
    @PostMapping("/token")
    public ResponseEntity<JsonResponse<String>> oauthLogin(@RequestBody OAuthRequestDto oAuthRequestDto) {
        HttpHeaders headers = oAuth2LoginService.handleOAuth2Login(oAuthRequestDto.getAuthorizationCode(), oAuthRequestDto.getSocialType());
        JsonResponse<String> response = new JsonResponse<>(HttpStatus.OK.value(), "소셜 로그인 성공", null);
        return ResponseEntity.ok().headers(headers).body(response);
    }
}
