package com.drinkhere.drinklymember.domain.member.dto;

import com.drinkhere.drinklymember.domain.member.entity.Owner;
import com.drinkhere.drinklymember.domain.member.enums.Gender;
import com.drinkhere.drinklymember.domain.member.enums.MobileCo;
import com.drinkhere.drinklymember.domain.member.enums.NationalInfo;

public record OwnerSignUpRequest(
        Long ownerId,
        String name,
        String nickname,
        String birthDate,
        Gender gender,
        NationalInfo nationalInfo,
        MobileCo mobileCo,
        String mobileNo,
        String di,
        String businessRegistrationNumber,
        String storeName,          // 매장명 추가
        String storeTel,   // 매장 연락처 추가
        String storeAddress        // 매장 주소 추가
) {
    public Owner toOwnerEntity() {
        return Owner.builder()
                .id(ownerId)
                .name(name)
                .nickname(nickname)
                .birthDate(birthDate)
                .gender(gender)
                .nationalInfo(nationalInfo)
                .mobileCo(mobileCo)
                .mobileNo(mobileNo)
                .di(di)
                .businessRegistrationNumber(businessRegistrationNumber)
                .build();
    }
}




