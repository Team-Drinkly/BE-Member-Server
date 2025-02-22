package com.drinkhere.drinklymember.application.profile.presentation;

import com.drinkhere.drinklymember.application.profile.presentation.docs.ProfileControllerDocs;
import com.drinkhere.drinklymember.application.profile.service.GetMemberNicknameUseCase;
import com.drinkhere.drinklymember.application.profile.service.GetMemberProfileUseCase;
import com.drinkhere.drinklymember.common.response.ApplicationResponse;
import com.drinkhere.drinklymember.domain.member.dto.profile.response.GetMemberProfileResponse;
import com.drinkhere.drinklymember.domain.member.dto.profile.response.MemberResponse;
import com.drinkhere.drinklymember.domain.member.entity.Member;
import com.drinkhere.drinklymember.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/member/profile")
public class ProfileController implements ProfileControllerDocs {

    private final GetMemberProfileUseCase getMemberProfileUseCase;
    private final GetMemberNicknameUseCase getMemberNicknameUseCase;

    @GetMapping("/{memberId}")
    public ApplicationResponse<GetMemberProfileResponse> getMemberProfile(@PathVariable Long memberId) {
        return ApplicationResponse.ok(getMemberProfileUseCase.getMemberProfile(memberId), "멤버 정보를 성공적으로 조회했습니다.");
    }


    /**
     * Fromt Store Micro Service FeignClient Request
     */
    @GetMapping("/client/{memberId}")
    public ApplicationResponse<MemberResponse> getMemberNickname(@PathVariable Long memberId) {
        return ApplicationResponse.ok(getMemberNicknameUseCase.getMemberNickname(memberId), "요청하신 멤버의 닉네임입니다.(FeignClient Response)");
    }
}
