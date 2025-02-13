package com.drinkhere.drinklymember.nice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record NiceRequestData(
        @JsonProperty("requestno") String requestNo,
        @JsonProperty("returnurl") String returnUrl,
        @JsonProperty("sitecode") String siteCode,
        @JsonProperty("popupyn") String popupYn
) {
}