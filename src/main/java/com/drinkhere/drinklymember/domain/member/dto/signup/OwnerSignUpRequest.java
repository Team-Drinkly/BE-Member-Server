package com.drinkhere.drinklymember.domain.member.dto.signup;

import com.drinkhere.drinklymember.domain.member.entity.Owner;
import com.drinkhere.drinklymember.domain.member.enums.Gender;
import com.drinkhere.drinklymember.domain.member.enums.MobileCo;
import com.drinkhere.drinklymember.domain.member.enums.NationalInfo;

public record OwnerSignUpRequest(
        Long ownerId,
        String name,
        String birthDate,
        Gender gender,
        NationalInfo nationalInfo,
        MobileCo mobileCo,
        String mobileNo,
        String di
) {
    public Owner toOwnerEntity() {
        return Owner.builder()
                .id(ownerId)
                .name(name)
                .birthDate(birthDate)
                .gender(gender)
                .nationalInfo(nationalInfo)
                .mobileCo(mobileCo)
                .mobileNo(mobileNo)
                .di(di)
                .build();
    }
}




