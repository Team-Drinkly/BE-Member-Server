package com.drinkhere.drinklymember.domain.member.enums;

import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

/**
 * 통신사 정보
 */
@RequiredArgsConstructor
public enum MobileCo {
    SK_TELECOM(1, "SK텔레콤"),
    KT(2, "KT"),
    LG_UPLUS(3, "LGU+"),
    SK_TELECOM_MVNO(5, "SK텔레콤 알뜰폰"),
    KT_MVNO(6, "KT 알뜰폰"),
    LG_UPLUS_MVNO(7, "LGU+ 알뜰폰");

    private final int value;
    private final String description;
    private static final Map<Integer, MobileCo> mobileCoMap = new HashMap<>();
    
    static {
        for (MobileCo mobileCo : values()) {
            mobileCoMap.put(mobileCo.value, mobileCo);
        }
    }
    
    public int value() {
        return value;
    }

    public String getDescription() {
        return description;
    }

    public static MobileCo fromValue(int value) {
        MobileCo mobileCo = mobileCoMap.get(value);
        return mobileCo;
    }
}
