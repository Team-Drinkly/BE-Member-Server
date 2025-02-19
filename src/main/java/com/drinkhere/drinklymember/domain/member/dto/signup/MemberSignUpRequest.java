package com.drinkhere.drinklymember.domain.member.dto.signup;

import com.drinkhere.drinklymember.domain.member.entity.Member;
import com.drinkhere.drinklymember.domain.member.enums.Gender;
import com.drinkhere.drinklymember.domain.member.enums.MobileCo;
import com.drinkhere.drinklymember.domain.member.enums.NationalInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Schema(description = "일반 회원 가입 요청 DTO")
public record MemberSignUpRequest(

        @Schema(description = "회원 ID", example = "1", required = false)
        Long memberId,

        @Schema(description = "회원 이름", example = "홍길동", required = true)
        @NotBlank(message = "이름은 필수 입력값입니다.")
        String name,

        @Schema(description = "닉네임", example = "길동이", required = true)
        @NotBlank(message = "닉네임은 필수 입력값입니다.")
        String nickname,

        @Schema(description = "생년월일 (YYYYMMDD)", example = "19900101", required = true)
        @NotBlank(message = "생년월일은 필수 입력값입니다.")
        String birthDate,

        @Schema(description = "성별", example = "MALE", required = true)
        @NotNull(message = "성별은 필수 입력값입니다.")
        Gender gender,

        @Schema(description = "국적", example = "DOMESTIC", required = true)
        @NotNull(message = "국적 정보(내국인/외국인)는 필수 입력값입니다.")
        NationalInfo nationalInfo,

        @Schema(description = "통신사", example = "SK_TELECOM", required = true)
        @NotNull(message = "통신사는 필수 입력값입니다.")
        MobileCo mobileCo,

        @Schema(description = "휴대폰 번호", example = "01012345678", required = true)
        @NotBlank(message = "휴대폰 번호는 필수 입력값입니다.")
        String mobileNo,

        @Schema(description = "DI (개인 식별 값)", example = "MC0GCCqGSIb3DQIJAyEAT47nGZUY85rgzAX/LWdJzbPiltuI7fTXL5ApXpYGkI8=", required = true)
        @NotBlank(message = "DI 값은 필수 입력값입니다.")
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
