package com.drinkhere.drinklymember.nice.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CreateNiceApiRequestDataDto(
        @JsonProperty("token_version_id") String tokenVersionId,
        @JsonProperty("enc_data") String encData,
        @JsonProperty("integrity_value") String integrityValue
) {
}
