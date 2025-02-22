package com.drinkhere.drinklymember.application.oauth.presentation.docs;

import com.drinkhere.drinklymember.common.response.ApplicationResponse;
import com.drinkhere.drinklymember.domain.auth.dto.OAuthResponse;
import com.drinkhere.drinklymember.domain.auth.enums.Authority;
import com.drinkhere.drinklymember.domain.auth.enums.Provider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "1. OAuth 로그인 API", description = "OAuth 로그인 및 회원 인증 관련 API 명세입니다.")
public interface OAuthControllerDocs {

        @ApiResponses(value = {
                @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = ApplicationResponse.class))),
                @ApiResponse(responseCode = "400", description = "BAD REQUEST - 잘못된 요청", content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = ApplicationResponse.class))),
                @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR - 서버 오류", content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = ApplicationResponse.class)))
        })
        @Operation(
                summary = "OAuth 로그인 API",
                description = "OAuth 로그인 API로, 로그인 시 `authority`와 `provider`를 각각 지정하여 요청합니다. \n\n" +
                        "`authority`: \n" +
                        "- `OWNER`: 사장님 앱 가입\n" +
                        "- `MEMBER`: 일반 앱 가입\n\n" +
                        "`provider`: \n" +
                        "- `KAKAO`: 카카오 소셜 로그인\n" +
                        "- `APPLE`: 애플 소셜 로그인\n\n" +
                        "또한, 헤더에는 Authorization 키를 사용하여 카카오 또는 애플에서 발급받은 Access Token을 값으로 포함해야 합니다."
        )
        ApplicationResponse<OAuthResponse> oAuthLogin(
                @PathVariable(value = "authority", required = true) @Schema(description = "어느 앱 가입자. 예: OWNER 또는 MEMBER") Authority authority,
                @PathVariable(value = "provider", required = true) @Schema(description = "OAuth Provider. 예: KAKAO 또는 APPLE") Provider provider,
                @RequestHeader(value = "Authorization", required = true) @Schema(description = "카카오 또는 애플에서 발급받은 Access Token") String accessToken
        );
}
