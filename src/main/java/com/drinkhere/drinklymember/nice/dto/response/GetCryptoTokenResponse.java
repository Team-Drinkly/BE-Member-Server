package com.drinkhere.drinklymember.nice.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record GetCryptoTokenResponse(
        @JsonProperty("dataHeader") DataHeader dataHeader,
        @JsonProperty("dataBody") DataBody dataBody
) {
    public record DataHeader(
            @JsonProperty("GW_RSLT_CD") String gwRsltCd,
            @JsonProperty("GW_RSLT_MSG") String gwRsltMsg
    ) {
    }

    public record DataBody(
            @JsonProperty("rsp_cd") String rspCd,
            @JsonProperty("res_msg") String resMsg,
            @JsonProperty("result_cd") String resultCd,
            @JsonProperty("site_code") String siteCode,
            @JsonProperty("token_version_id") String tokenVersionId,
            @JsonProperty("token_val") String tokenVal,
            @JsonProperty("period") Integer period
    ) {
    }
}
