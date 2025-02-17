package com.drinkhere.drinklymember.nice.webclient;

import com.drinkhere.drinklymember.common.exception.nice.NiceException;
import com.drinkhere.drinklymember.nice.dto.request.GetCryptoTokenRequest;
import com.drinkhere.drinklymember.nice.dto.response.GetCryptoTokenResponse;
import com.drinkhere.drinklymember.nice.webclient.config.NiceProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import static com.drinkhere.drinklymember.common.exception.nice.NiceErrorCode.CRYPTO_TOKEN_REQUEST_FAILED;


@Component
@RequiredArgsConstructor
public class NiceClient {
    private final WebClient niceWebClient;
    private final NiceProperties niceProperties;

    private static final String NICE_API_URL = "/digital/niceid/api/v1.0/common/crypto/token";
    private static final String BEARER_PREFIX = "bearer ";
    private static final String VALID_RESULT_CODE = "1200";

    public GetCryptoTokenResponse requestCryptoToken(String reqDtim, String reqNo) {

        String authorization = generateAuthorizationHeader();

        GetCryptoTokenRequest request = new GetCryptoTokenRequest(
                new GetCryptoTokenRequest.DataHeader("ko"),
                new GetCryptoTokenRequest.DataBody(reqDtim, reqNo, "1")
        );

        GetCryptoTokenResponse response
                 = niceWebClient.post()
                                .uri(NICE_API_URL)
                                .header("Authorization", authorization)
                                .header("ProductID", niceProperties.getProductId())
                                .bodyValue(request)
                                .retrieve()
                                .bodyToMono(GetCryptoTokenResponse.class)
                                .block();

        validateNiceClientResponse(response.dataHeader().gwRsltCd());

        return response;
    }

    /**----------------------------------------------------------------------------------------------------**/
    // Authorization 헤더 생성
    private String generateAuthorizationHeader() {
        long currentTimestamp = System.currentTimeMillis() / 1000;
        String rawAuth = String.format("%s:%d:%s", niceProperties.getOrganizationToken(), currentTimestamp, niceProperties.getClientId());
        return BEARER_PREFIX + Base64.getEncoder().encodeToString(rawAuth.getBytes(StandardCharsets.UTF_8));
    }

    private static void validateNiceClientResponse(String resultCode) {
        if (!VALID_RESULT_CODE.equals(resultCode)) {
            throw new NiceException(CRYPTO_TOKEN_REQUEST_FAILED);
        }
    }
}
