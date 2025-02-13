package com.drinkhere.drinklymember.nice.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public record GetCryptoTokenRequest(
        @JsonProperty("dataHeader") DataHeader dataHeader,
        @JsonProperty("dataBody") DataBody dataBody
) {
    public static record DataHeader(
            @JsonProperty("CNTY_CD") String countryCd
    ) {}

    public static record DataBody(
            @JsonProperty("req_dtim") String requestDateTime,
            @JsonProperty("req_no") String requestNo,
            @JsonProperty("enc_mode") String encryptionMode
    ) {}
}