package com.drinkhere.drinklymember.domain.member.enums;

import lombok.RequiredArgsConstructor;

/**
 * 내국인/외국인
 */
@RequiredArgsConstructor
public enum NationalInfo {
    DOMESTIC(0, "내국인"),
    FOREIGN(1, "외국인");

    private final int value;
    private final String description;

    public int getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }

    public static NationalInfo fromValue(int value) {
        switch (value) {
            case 0: return DOMESTIC;
            case 1: return FOREIGN;
        }
        return DOMESTIC;
    }
}
