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
@RequestMapping("/api/v1/profile")
public class ProfileController implements ProfileControllerDocs {

    private final GetMemberProfileUseCase getMemberProfileUseCase;
    @GetMapping("/o")
    public ApplicationResponse<GetMemberProfileResponse> getMemberProfile(
            @RequestHeader(value = "member-id", required = false) Long memberId,
            @RequestHeader(value = "subscribe-id", required = false) Long subscribeId,
            @RequestHeader(value = "is-subscribe", required = false) String isSubscribe
    ) {
        return ApplicationResponse.ok(getMemberProfileUseCase.getMemberProfile(memberId, subscribeId, isSubscribe), "멤버 정보를 성공적으로 조회했습니다.");
    }
}
