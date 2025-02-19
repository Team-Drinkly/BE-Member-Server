package com.drinkhere.drinklymember.application.signup.presentation.docs;

import com.drinkhere.drinklymember.common.response.ApplicationResponse;
import com.drinkhere.drinklymember.domain.auth.dto.Token;
import com.drinkhere.drinklymember.domain.member.dto.signup.GetNiceApiResultResponse;
import com.drinkhere.drinklymember.domain.member.dto.signup.MemberSignUpRequest;
import com.drinkhere.drinklymember.domain.member.dto.signup.OwnerSignUpRequest;
import com.drinkhere.drinklymember.nice.dto.response.CreateNiceApiRequestDataDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "2. 회원 가입 처리 API", description = "아직 가입하지 않은 회원들을 처리하기 위한 API 명세입니다.")
public interface SignUpControllerDocs {

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApplicationResponse.class))),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST - 잘못된 요청", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApplicationResponse.class))),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR - 서버 오류", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApplicationResponse.class)))
    })
    @Operation(
            summary = "Nice API 초기화",
            description = "본인인증 절차를 시작하기 위해 `type`과 `oauthId`를 받아 Nice API를 초기화합니다."
    )
    ApplicationResponse<CreateNiceApiRequestDataDto> initNiceApi(
            @Parameter(description = "회원 유형 (`member`/`owner`)", example = "member") @PathVariable("type") String type,
            @Parameter(description = "회원의 OAuth ID", example = "1") @PathVariable("oauthId") Long oauthId
    );
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApplicationResponse.class))),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST - 잘못된 요청", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApplicationResponse.class))),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR - 서버 오류", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApplicationResponse.class)))
    })
    @Operation(summary = "Nice API CallBack 처리(자동)", description = "Nice 인증 API에서 콜백 데이터를 받아 처리합니다. 이 API는 인증 절차 완료 후 데이터를 반환합니다.")
    ApplicationResponse<GetNiceApiResultResponse> handleNiceCallBack(
            @RequestParam("id") Long id,
            @RequestParam("type") String type,
            @RequestParam("token_version_id") String tokenVersionId,
            @RequestParam("enc_data") String encData,
            @RequestParam("integrity_value") String integrityValue
    );


    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApplicationResponse.class))),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST - 잘못된 요청", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApplicationResponse.class))),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR - 서버 오류", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApplicationResponse.class)))
    })
    @Operation(
            summary = "일반 유저 회원가입 API",
            description = "일반 유저의 회원가입을 처리하는 API입니다. 이전에 NICE 본인인증 후 얻은 개인 정보에 nickname을 더해서 RequestBody로 넣어주세요."
    )
    ApplicationResponse<Token> signUpMember(
            @Valid @RequestBody MemberSignUpRequest memberSignUpRequest
    );

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApplicationResponse.class))),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST - 잘못된 요청", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApplicationResponse.class))),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR - 서버 오류", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApplicationResponse.class)))
    })
    @Operation(
            summary = "제휴 업체 사장님의 회원가입 API",
            description = "가맹점 회원가입을 처리하는 API입니다. 이전에 NICE 본인인증을 통해 얻은 개인 정보를 RequestBody에 포함하여 요청해야 합니다. 추가로 받은 매장 정보는 별도의 제휴 업체 등록 API를 이용해 등록해주세요."
    )
    ApplicationResponse<Token> signUpOwner(
            @Valid @RequestBody OwnerSignUpRequest ownerSignUpRequest
    );
}
