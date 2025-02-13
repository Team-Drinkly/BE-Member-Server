package com.drinkhere.drinklymember.nice.dto;

public record NiceCryptoData(
        String key,
        String iv,
        String hmacKey
) {
    // 팩토리 메서드
    public static NiceCryptoData of(String key, String iv, String hmacKey) {
        return new NiceCryptoData(key, iv, hmacKey);
    }
}
