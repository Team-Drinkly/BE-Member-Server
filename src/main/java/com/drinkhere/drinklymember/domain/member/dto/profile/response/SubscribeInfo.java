package com.drinkhere.drinklymember.domain.member.dto.profile.response;

public record SubscribeInfo(
        int leftDays,
        String expiredDate,
        int usedCount
) {}
