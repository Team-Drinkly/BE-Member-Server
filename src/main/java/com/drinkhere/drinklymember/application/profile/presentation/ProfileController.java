package com.drinkhere.drinklymember.application.profile.presentation;

import com.drinkhere.drinklymember.application.profile.presentation.docs.ProfileControllerDocs;
import com.drinkhere.drinklymember.application.profile.service.GetMemberProfileUseCase;
import com.drinkhere.drinklymember.common.response.ApplicationResponse;
import com.drinkhere.drinklymember.domain.member.dto.profile.response.GetMemberProfileResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/member/profile")
public class ProfileController implements ProfileControllerDocs {

    private final GetMemberProfileUseCase getMemberProfileUseCase;

    @GetMapping
    public ApplicationResponse<GetMemberProfileResponse> getMemberProfile(
            @RequestHeader(value = "member-id", required = false) Long memberId
    ) {
        return ApplicationResponse.ok(getMemberProfileUseCase.getMemberProfile(memberId), "멤버 정보를 성공적으로 조회했습니다.");
    }


}
