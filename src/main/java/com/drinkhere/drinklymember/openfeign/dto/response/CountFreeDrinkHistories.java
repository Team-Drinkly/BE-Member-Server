package com.drinkhere.drinklymember.openfeign.dto.response;

import com.drinkhere.drinklymember.domain.member.dto.profile.response.DrinkHistory;

import java.util.List;

public record CountFreeDrinkHistories(
        Result result,
        List<DrinkHistory> payload
) {
    public record Result(
            int code,
            String message
    ) {}

    public List<DrinkHistory> getPayload() {
        return payload;
    }

    public int getCount() {
        return payload != null ? payload.size() : 0;
    }
}
