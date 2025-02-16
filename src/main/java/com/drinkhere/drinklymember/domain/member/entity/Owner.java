package com.drinkhere.drinklymember.domain.member.entity;

import com.drinkhere.drinklymember.domain.auditing.BaseTimeEntity;
import com.drinkhere.drinklymember.domain.member.enums.Gender;
import com.drinkhere.drinklymember.domain.member.enums.MobileCo;
import com.drinkhere.drinklymember.domain.member.enums.NationalInfo;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Owner extends BaseTimeEntity {
    @Id
    @Column(name = "owner_id", unique = true)
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

    @Column(name = "business_registration_number", nullable = false)
    private String businessRegistrationNumber;

    @Builder
    public Owner(Long id, String name, String nickname, String birthDate, Gender gender, NationalInfo nationalInfo, MobileCo mobileCo, String mobileNo, String di, String businessRegistrationNumber) {
        this.id = id;
        this.name = name;
        this.nickname = nickname;
        this.birthDate = birthDate;
        this.gender = gender;
        this.nationalInfo = nationalInfo;
        this.mobileCo = mobileCo;
        this.mobileNo = mobileNo;
        this.di = di;
        this.businessRegistrationNumber = businessRegistrationNumber;
    }
}