package com.drinkhere.drinklymember.application.oauth.service.oauth;

import com.drinkhere.drinklymember.common.exception.oauth.AuthErrorCode;
import com.drinkhere.drinklymember.common.exception.oauth.AuthException;
import com.drinkhere.drinklymember.domain.auth.dto.OAuthRequest;
import com.drinkhere.drinklymember.domain.auth.dto.OAuthResponse;
import com.drinkhere.drinklymember.domain.auth.dto.OAuthUserInfo;
import com.drinkhere.drinklymember.domain.auth.entity.OAuthMember;
import com.drinkhere.drinklymember.domain.auth.handler.request.OAuthSuccessEvent;
import com.drinkhere.drinklymember.domain.auth.service.OAuthQueryService;
import com.drinkhere.drinklymember.domain.auth.service.OAuthSaveService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class OAuthInvoker {

    private final List<AuthHandler> authHandlerList;
    private final OAuthQueryService oAuthQueryService;
    private final OAuthSaveService oAuthSaveService;
    private final ApplicationEventPublisher publisher;

    public OAuthResponse execute(OAuthRequest request) {
        log.info("🚀 OAuthInvoker - OAuth 로그인 요청 시작: provider={}, sub={}", request.getProvider(), request.getAccessToken());

        final OAuthUserInfo oAuthUserInfo = attemptLogin(request);
        log.info("✅ OAuthInvoker - 로그인 성공: email={}, sub={}", oAuthUserInfo.getEmail(), oAuthUserInfo.getSub());

        OAuthMember oAuth = publishEventAndRetrieveOAuth(oAuthUserInfo, request);
        log.info("✅ OAuthInvoker - OAuth 저장 완료: id={}, registered={}", oAuth.getId(), oAuth.isRegistered());

        return new OAuthResponse(oAuth.getId(), oAuth.isRegistered());
    }

    private OAuthMember publishEventAndRetrieveOAuth(OAuthUserInfo oAuthUserInfo, OAuthRequest request) {
        log.info("🔍 [START] OAuthInvoker - 사용자 정보 조회 시작: sub={}", oAuthUserInfo.getSub());

        OAuthMember oAuth = null;
        try {
            log.info("🔍 DB에서 sub={} 값으로 사용자 조회 시작", oAuthUserInfo.getSub());
            oAuth = oAuthQueryService.findBySub(oAuthUserInfo.getSub());
            log.info("✅ DB 조회 완료: 결과={}", oAuth != null ? "기존 사용자 존재" : "사용자 없음 (신규 가입 필요)");
        } catch (Exception e) {
            log.error("❌ DB 조회 중 예외 발생: {}", e.getMessage(), e);
            throw e;
        }

        if (oAuth == null) {
            log.info("⚠ OAuthInvoker - 기존 사용자 없음, 신규 OAuth 생성 필요");

            try {
                oAuth = saveOAuth(oAuthUserInfo, request);
                log.info("✅ OAuth 저장 성공: id={}", oAuth.getId());
            } catch (Exception e) {
                log.error("❌ OAuth 저장 중 예외 발생: {}", e.getMessage(), e);
                throw e;
            }
        } else {
            log.info("✅ OAuthInvoker - 기존 사용자 조회 성공: id={}, sub={}", oAuth.getId(), oAuth.getSub());
        }

        log.info("🚀 OAuthInvoker - 이벤트 발행 시작: nickname={}, email={}, provider={}, sub={}",
                oAuthUserInfo.getNickname(), oAuthUserInfo.getEmail(), request.getProvider(), oAuthUserInfo.getSub());

        try {
            publisher.publishEvent(OAuthSuccessEvent.of(
                    oAuthUserInfo.getNickname(),
                    oAuthUserInfo.getEmail(),
                    request.getProvider(),
                    oAuthUserInfo.getSub(),
                    oAuth.getId()
            ));
            log.info("✅ OAuthInvoker - 이벤트 발행 완료");
        } catch (Exception e) {
            log.error("❌ OAuth 이벤트 발행 중 예외 발생: {}", e.getMessage(), e);
            throw e;
        }

        log.info("🔍 [END] OAuthInvoker - 사용자 처리 완료: id={}, sub={}", oAuth.getId(), oAuth.getSub());
        return oAuth;
    }

    private OAuthMember saveOAuth(OAuthUserInfo oAuthUserInfo, OAuthRequest request) {
        log.info("🔍 OAuthInvoker - OAuth 신규 저장: sub={}, provider={}", oAuthUserInfo.getSub(), request.getProvider());

        OAuthMember oAuth = OAuthMember.of(
                request.getProvider(),
                oAuthUserInfo.getSub()
        );

        OAuthMember savedOAuth = oAuthSaveService.save(oAuth);
        log.info("✅ OAuthInvoker - OAuth 저장 완료: id={}", savedOAuth.getId());

        return savedOAuth;
    }

    private OAuthUserInfo attemptLogin(OAuthRequest request) {
        for (AuthHandler authHandler : authHandlerList) {
            if (authHandler.isAccessible(request)) {
                return authHandler.handle(request);
            }
        }
        throw new AuthException(AuthErrorCode.OAUTH_FAIL);
    }
}
