package com.drinkhere.drinklymember.domain.member.dto.profile.response;

import java.util.List;

public record SubscribeInfo(
        int leftDays,
        String expiredDate,
        int usedCount,
        List<DrinkHistory> drinksHistory
) {}
