package com.drinkhere.drinklymember.domain.member.entity;

import com.drinkhere.drinklymember.domain.auditing.BaseTimeEntity;
import com.drinkhere.drinklymember.domain.member.enums.Gender;
import com.drinkhere.drinklymember.domain.member.enums.MobileCo;
import com.drinkhere.drinklymember.domain.member.enums.NationalInfo;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseTimeEntity {
    @Id
    @Column(name = "member_id", unique = true)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String nickname;

    @Column(name = "birth_date", nullable = false)
    private String birthDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender", nullable = false)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    @Column(name = "national_info", nullable = false)
    private NationalInfo nationalInfo;

    @Enumerated(EnumType.STRING)
    @Column(name = "mobile_co", nullable = false)
    private MobileCo mobileCo;

    @Column(name = "mobile_no", nullable = false)
    private String mobileNo;

    @Column(nullable = false)
    private String di;

    @Builder
    public Member(Long id, String name, String nickname, String birthDate, Gender gender, NationalInfo nationalInfo, MobileCo mobileCo, String mobileNo, String di) {
        this.id = id;
        this.name = name;
        this.nickname = nickname;
        this.birthDate = birthDate;
        this.gender = gender;
        this.nationalInfo = nationalInfo;
        this.mobileCo = mobileCo;
        this.mobileNo = mobileNo;
        this.di = di;
    }
}
