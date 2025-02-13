package com.drinkhere.drinklymember.nice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record NiceDecryptedData(

        @JsonProperty("resultcode")
        String resultCode,

        @JsonProperty("requestno")
        String requestNo,

        @JsonProperty("enctime")
        String encTime,

        @JsonProperty("sitecode")
        String siteCode,

        @JsonProperty("responseno")
        String responseNo,

        @JsonProperty("authtype")
        String authType,

        @JsonProperty("name")
        String name,

        @JsonProperty("utf8_name")
        String utf8Name,

        @JsonProperty("birthdate")
        String birthDate,

        @JsonProperty("gender")
        String gender,

        @JsonProperty("nationalinfo")
        String nationalInfo,

        @JsonProperty("mobileco")
        String mobileCo,

        @JsonProperty("mobileno")
        String mobileNo,

        @JsonProperty("ci")
        String ci,

        @JsonProperty("di")
        String di,

        @JsonProperty("businessno")
        String businessNo,

        @JsonProperty("receivedata")
        String receiveData

) {
}
