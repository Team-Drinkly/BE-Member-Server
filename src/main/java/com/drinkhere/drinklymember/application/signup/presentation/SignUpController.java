package com.drinkhere.drinklymember.application.signup.presentation;

import com.drinkhere.drinklymember.application.signup.service.SignUpUseCase;
import com.drinkhere.drinklymember.common.response.ApplicationResponse;
import com.drinkhere.drinklymember.domain.member.dto.GetNiceApiResultResponse;
import com.drinkhere.drinklymember.domain.member.dto.MemberSignUpRequest;
import com.drinkhere.drinklymember.domain.member.dto.OwnerSignUpRequest;
import com.drinkhere.drinklymember.nice.dto.response.CreateNiceApiRequestDataDto;
import com.drinkhere.drinklymember.nice.service.InitializeNiceUseCase;
import com.drinkhere.drinklymember.nice.service.NiceCallBackUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/member")
public class SignUpController {
    private final InitializeNiceUseCase initializeNiceUseCase;
    private final NiceCallBackUseCase niceCallBackUseCase;
    private final SignUpUseCase<MemberSignUpRequest> memberSignUpService;
    private final SignUpUseCase<OwnerSignUpRequest> ownerSignUpService;

    @GetMapping("/nice/{memberId}")
    public ApplicationResponse<CreateNiceApiRequestDataDto> initNiceApi(
            @PathVariable("memberId") Long memberId
    ) {
        return ApplicationResponse.ok(initializeNiceUseCase.initializeNiceApi(memberId));
    }

    @GetMapping("/nice/call-back")
    public ApplicationResponse<GetNiceApiResultResponse> handleNiceCallBack(
            @RequestParam("mid") Long memberId,
            @RequestParam("token_version_id") String tokenVersionId,
            @RequestParam("enc_data") String encData,
            @RequestParam("integrity_value") String integrityValue
    ) {
        return ApplicationResponse.ok(niceCallBackUseCase.processCallback(memberId, encData), "NICE 본인인증에 성공했습니다.");
    }

    @PostMapping("/signup")
    public ApplicationResponse<String> signUpMember(@RequestBody MemberSignUpRequest memberSignUpRequest) {
        memberSignUpService.signUp(memberSignUpRequest);
        return ApplicationResponse.created("성공적으로 회원가입을 처리했습니다.");
    }

    @PostMapping("/signup/owner")
    public ApplicationResponse<String> signUpOwner(@RequestBody OwnerSignUpRequest ownerSignUpRequest) {
        ownerSignUpService.signUp(ownerSignUpRequest);
        return ApplicationResponse.created("성공적으로 회원가입을 처리했습니다.");
    }
}
