package com.drinkhere.drinklymember.domain.member.dto;

import com.drinkhere.drinklymember.domain.member.enums.Gender;
import com.drinkhere.drinklymember.domain.member.enums.MobileCo;
import com.drinkhere.drinklymember.domain.member.enums.NationalInfo;
import com.drinkhere.drinklymember.nice.dto.NiceDecryptedData;

public record GetNiceApiResultResponse(
        Long id,
        String type,
        String name,
        String birthDate,
        Gender gender,
        NationalInfo nationalInfo,
        MobileCo mobileCo,
        String mobileNo,
        String di
) {
    public static GetNiceApiResultResponse from(Long id, String type, NiceDecryptedData niceDecryptedData, String decodedName) {
        return new GetNiceApiResultResponse(
                id,
                type,
                decodedName,
                niceDecryptedData.birthDate(),
                Gender.fromValue(Integer.parseInt(niceDecryptedData.gender())),
                NationalInfo.fromValue(Integer.parseInt(niceDecryptedData.nationalInfo())),
                MobileCo.fromValue(Integer.parseInt(niceDecryptedData.mobileCo())),
                niceDecryptedData.mobileNo(),
                niceDecryptedData.di()
        );
    }
}
