package com.drinkhere.drinklymember.application.profile.presentation.docs;

import com.drinkhere.drinklymember.common.response.ApplicationResponse;
import com.drinkhere.drinklymember.domain.member.dto.profile.response.GetMemberProfileResponse;
import org.springframework.web.bind.annotation.RequestHeader;

public interface ProfileControllerDocs {
    ApplicationResponse<GetMemberProfileResponse> getMemberProfile(
            @RequestHeader(value = "member-id", required = false) Long memberId
    );
}
