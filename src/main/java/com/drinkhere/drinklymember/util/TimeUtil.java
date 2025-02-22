package com.drinkhere.drinklymember.util;

import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RequiredArgsConstructor
public class TimeUtil {
    public static String refineToDateTime(LocalDateTime localDateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return localDateTime.format(formatter);
    }

    public static String refineToDate(LocalDateTime localDateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일");
        return localDateTime.format(formatter);
    }
}