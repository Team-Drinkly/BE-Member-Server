package com.drinkhere.drinklymember.domain.member.dto.profile.response;

public record DrinkHistory(
        Long freeDrinkHistoryId,
        String storeName,
        String providedDrink,
        String usageDate
) {}