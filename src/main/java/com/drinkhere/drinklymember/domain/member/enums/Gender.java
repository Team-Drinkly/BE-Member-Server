package com.drinkhere.drinklymember.domain.member.enums;

import lombok.RequiredArgsConstructor;

/**
 * 성별
 */
@RequiredArgsConstructor
public enum Gender {
    FEMALE(0, "여성"),  // 여성(0)
    MALE(1, "남성");    // 남성(1)

    private final int value;
    private final String description;

    public int getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }

    // 주어진 정수값에 맞는 Gender를 반환하는 정적 메소드
    public static Gender fromValue(int value) {
        // 정수값이 0이면 MALE, 1이면 FEMALE을 반환
        switch (value) {
            case 0: return FEMALE;
            case 1: return MALE;
        }
        return MALE;
    }

}
