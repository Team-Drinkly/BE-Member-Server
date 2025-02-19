package com.drinkhere.drinklymember.application.profile.presentation.docs;

import com.drinkhere.drinklymember.common.response.ApplicationResponse;
import com.drinkhere.drinklymember.domain.member.dto.profile.response.GetMemberProfileResponse;
import com.drinkhere.drinklymember.domain.member.dto.profile.response.MemberResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@Tag(name = "3. 회원 프로필 조회 API", description = "회원 프로필 관련 API 명세서입니다.")
public interface ProfileControllerDocs {

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = ApplicationResponse.class))),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST", content = @Content(schema = @Schema(implementation = ApplicationResponse.class))),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR - 서버 오류", content = @Content(schema = @Schema(implementation = ApplicationResponse.class)))
    })
    @Operation(
            summary = "회원 프로필 조회",
            description = "memberId를 통해 회원의 프로필 정보를 조회합니다. 마이페이지에서 사용됩니다."
    )
    ApplicationResponse<GetMemberProfileResponse> getMemberProfile(
            @Parameter(description = "조회할 회원의 ID", required = true, example = "1") @PathVariable Long memberId
    );

    @Operation(
            summary = "회원 닉네임 조회(프론트에서 작업 필요 X)",
            description = "특정 회원의 닉네임을 조회하는 API로, FeignClient에서 요청할 때 사용됩니다."
    )
    ApplicationResponse<MemberResponse> getMemberNickname(Long memberId);
}
