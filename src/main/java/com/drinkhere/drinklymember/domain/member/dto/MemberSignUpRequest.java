package com.drinkhere.drinklymember.domain.member.dto;

import com.drinkhere.drinklymember.domain.member.entity.Member;
import com.drinkhere.drinklymember.domain.member.enums.Gender;
import com.drinkhere.drinklymember.domain.member.enums.MobileCo;
import com.drinkhere.drinklymember.domain.member.enums.NationalInfo;
import com.drinkhere.drinklymember.nice.dto.NiceDecryptedData;

public record MemberSignUpRequest(
        Long memberId,
        String name,
        String nickname,
        String birthDate,
        Gender gender,
        NationalInfo nationalInfo,
        MobileCo mobileCo,
        String mobileNo,
        String di
) {
    public Member toEntity() {
        return Member.builder()
                .id(memberId)
                .name(name)
                .nickname(nickname)
                .birthDate(birthDate)
                .gender(gender)
                .nationalInfo(nationalInfo)
                .mobileCo(mobileCo)
                .mobileNo(mobileNo)
                .di(di)
                .build();
    }
}




