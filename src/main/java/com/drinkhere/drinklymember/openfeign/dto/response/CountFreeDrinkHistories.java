package com.drinkhere.drinklymember.openfeign.dto.response;

public record CountFreeDrinkHistories(
        Payload payload
) {
    public int getCount() {
        return payload.count();
    }

    public Long getSubscribeId() {
        return payload().subscriberId;
    }

    public record Payload(
            Long subscriberId,
            int count
    ) {}
}
