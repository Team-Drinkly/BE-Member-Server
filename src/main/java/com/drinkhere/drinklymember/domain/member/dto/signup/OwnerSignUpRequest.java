package com.drinkhere.drinklymember.domain.member.dto.signup;

import com.drinkhere.drinklymember.domain.member.entity.Owner;
import com.drinkhere.drinklymember.domain.member.enums.Gender;
import com.drinkhere.drinklymember.domain.member.enums.MobileCo;
import com.drinkhere.drinklymember.domain.member.enums.NationalInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "제휴 업체 사장님 회원 가입 요청 DTO")
public record OwnerSignUpRequest(

        @Schema(description = "사장님 ID", example = "1", required = false)
        Long ownerId,

        @Schema(description = "사장님 이름", example = "김사장", required = true)
        @NotBlank(message = "이름은 필수 입력값입니다.")
        String name,

        @Schema(description = "생년월일 (YYYYMMDD)", example = "19801212", required = true)
        @NotBlank(message = "생년월일은 필수 입력값입니다.")
        String birthDate,

        @Schema(description = "성별", example = "MALE", required = true)
        @NotNull(message = "성별은 필수 입력값입니다.")
        Gender gender,

        @Schema(description = "국적", example = "DOMESTIC", required = true)
        @NotNull(message = "국적 정보(내국인/외국인)는 필수 입력값입니다.")
        NationalInfo nationalInfo,

        @Schema(description = "통신사", example = "SK_TELECOM_MVNO", required = true)
        @NotNull(message = "통신사는 필수 입력값입니다.")
        MobileCo mobileCo,

        @Schema(description = "휴대폰 번호", example = "01056781234", required = true)
        @NotBlank(message = "휴대폰 번호는 필수 입력값입니다.")
        String mobileNo,

        @Schema(description = "DI (개인 식별 값)", example = "MC0GCCqGSIb3DQIJAyEAT47nGZUY85rgzAX/LWdJzbPiltuI7fTXL5ApXpYGkI8=", required = true)
        @NotBlank(message = "DI 값은 필수 입력값입니다.")
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
