package com.drinkhere.drinklymember.application.oauth.presentation;

import com.drinkhere.drinklymember.application.oauth.service.LogoutUseCase;
import com.drinkhere.drinklymember.application.oauth.service.OAuthUseCase;
import com.drinkhere.drinklymember.application.oauth.service.ReissueUseCase;
import com.drinkhere.drinklymember.domain.auth.consts.AuthConsts;
import com.drinkhere.drinklymember.domain.auth.dto.OAuthResponse;
import com.drinkhere.drinklymember.domain.auth.dto.TokenReissueResponse;
import com.drinkhere.drinklymember.domain.auth.enums.Provider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class OAuthController {

    private final OAuthUseCase oAuthUseCase;
    private final LogoutUseCase logoutUseCase;
    private final ReissueUseCase reissueUseCase;

    @GetMapping("/oauth/{provider}")
    public OAuthResponse oAuthLogin(@PathVariable Provider provider,
                                    @RequestHeader("Authorization") String accessToken
    ) {
        return oAuthUseCase.oAuthLogin(provider, accessToken);
    }

    @PostMapping("/reissue")
    public TokenReissueResponse reissue(@RequestHeader(AuthConsts.REFRESH_TOKEN_HEADER) String refreshToken) {
        return reissueUseCase.reissue(refreshToken);
    }


    @DeleteMapping("/logout")
    public void logout(@RequestHeader(AuthConsts.REFRESH_TOKEN_HEADER) String refreshToken) {
        logoutUseCase.logoutAccessUser(refreshToken);
    }

}
