package com.drinkhere.drinklymember.openfeign.dto.response;

public record CountFreeDrinkHistories(
        int subscribeId,
        int count
) {
    public String getNickname() {
        return payload.nickname();
    }

    public record Payload(String nickname) {}
}
