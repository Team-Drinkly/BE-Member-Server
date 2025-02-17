package com.drinkhere.drinklymember.application.oauth.service.oauth;

import com.drinkhere.drinklymember.common.exception.oauth.AuthErrorCode;
import com.drinkhere.drinklymember.common.exception.oauth.AuthException;
import com.drinkhere.drinklymember.domain.auth.dto.OAuthRequest;
import com.drinkhere.drinklymember.domain.auth.dto.OAuthResponse;
import com.drinkhere.drinklymember.domain.auth.dto.OAuthUserInfo;
import com.drinkhere.drinklymember.domain.auth.entity.OAuthMember;
import com.drinkhere.drinklymember.domain.auth.entity.OAuthOwner;
import com.drinkhere.drinklymember.domain.auth.enums.Authority;
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
        log.info("🚀 OAuthInvoker - OAuth 로그인 요청 시작: provider={}, sub={}, authority={}",
                request.getProvider(), request.getAccessToken(), request.getAuthority());

        final OAuthUserInfo oAuthUserInfo = attemptLogin(request);
        log.info("✅ OAuthInvoker - 로그인 성공: email={}, sub={}", oAuthUserInfo.getEmail(), oAuthUserInfo.getSub());

        if (request.getAuthority() == Authority.MEMBER) {
            return handleMemberOAuth(oAuthUserInfo, request);
        } else {
            return handleOwnerOAuth(oAuthUserInfo, request);
        }
    }

    private OAuthResponse handleMemberOAuth(OAuthUserInfo oAuthUserInfo, OAuthRequest request) {
        log.info("🔍 [START] OAuthInvoker - 멤버 OAuth 처리 시작: sub={}", oAuthUserInfo.getSub());

        OAuthMember oAuth = null;
        try {
            log.info("🔍 DB에서 멤버 sub={} 값으로 사용자 조회 시작", oAuthUserInfo.getSub());
            oAuth = oAuthQueryService.findMemberBySub(oAuthUserInfo.getSub());
        } catch (Exception e) {
            log.error("❌ 멤버 DB 조회 중 예외 발생: {}", e.getMessage(), e);
        }

        if (oAuth == null) {
            log.info("⚠ 기존 멤버 없음, 신규 OAuth 멤버 생성 필요");
            oAuth = saveOAuthMember(oAuthUserInfo, request);
        } else {
            log.info("✅ 기존 멤버 조회 성공: id={}, sub={}", oAuth.getId(), oAuth.getSub());
        }

        publishOAuthSuccessEvent(oAuthUserInfo, request, oAuth.getId());

        log.info("🔍 [END] OAuthInvoker - 멤버 처리 완료: id={}, sub={}", oAuth.getId(), oAuth.getSub());
        return new OAuthResponse(oAuth.getId(), oAuth.isRegistered());
    }

    private OAuthResponse handleOwnerOAuth(OAuthUserInfo oAuthUserInfo, OAuthRequest request) {
        log.info("🔍 [START] OAuthInvoker - 사장님 OAuth 처리 시작: sub={}", oAuthUserInfo.getSub());

        OAuthOwner oAuth = null;
        try {
            log.info("🔍 DB에서 사장님 sub={} 값으로 사용자 조회 시작", oAuthUserInfo.getSub());
            oAuth = oAuthQueryService.findOwnerBySub(oAuthUserInfo.getSub());
        } catch (Exception e) {
            log.error("❌ 사장님 DB 조회 중 예외 발생: {}", e.getMessage(), e);
        }

        if (oAuth == null) {
            log.info("⚠ 기존 사장님 없음, 신규 OAuth 사장님 생성 필요");
            oAuth = saveOAuthOwner(oAuthUserInfo, request);
        } else {
            log.info("✅ 기존 사장님 조회 성공: id={}, sub={}", oAuth.getId(), oAuth.getSub());
        }

        publishOAuthSuccessEvent(oAuthUserInfo, request, oAuth.getId());

        log.info("🔍 [END] OAuthInvoker - 사장님 처리 완료: id={}, sub={}", oAuth.getId(), oAuth.getSub());
        return new OAuthResponse(oAuth.getId(), oAuth.isRegistered());
    }

    private OAuthMember saveOAuthMember(OAuthUserInfo oAuthUserInfo, OAuthRequest request) {
        log.info("🔍 OAuthInvoker - OAuth 멤버 저장: sub={}, provider={}", oAuthUserInfo.getSub(), request.getProvider());

        OAuthMember oAuth = OAuthMember.of(request.getProvider(), oAuthUserInfo.getSub());
        OAuthMember savedOAuth = oAuthSaveService.memberSave(oAuth);

        log.info("✅ OAuthInvoker - OAuth 멤버 저장 완료: id={}", savedOAuth.getId());
        return savedOAuth;
    }

    private OAuthOwner saveOAuthOwner(OAuthUserInfo oAuthUserInfo, OAuthRequest request) {
        log.info("🔍 OAuthInvoker - OAuth 사장님 저장: sub={}, provider={}", oAuthUserInfo.getSub(), request.getProvider());

        OAuthOwner oAuth = OAuthOwner.of(request.getProvider(), oAuthUserInfo.getSub());
        OAuthOwner savedOAuth = oAuthSaveService.ownerSave(oAuth);

        log.info("✅ OAuthInvoker - OAuth 사장님 저장 완료: id={}", savedOAuth.getId());
        return savedOAuth;
    }

    private void publishOAuthSuccessEvent(OAuthUserInfo oAuthUserInfo, OAuthRequest request, Long id) {
        log.info("🚀 OAuthInvoker - 이벤트 발행 시작: nickname={}, email={}, provider={}, sub={}",
                oAuthUserInfo.getNickname(), oAuthUserInfo.getEmail(), request.getProvider(), oAuthUserInfo.getSub());

        try {
            publisher.publishEvent(OAuthSuccessEvent.of(
                    oAuthUserInfo.getNickname(),
                    oAuthUserInfo.getEmail(),
                    request.getProvider(),
                    oAuthUserInfo.getSub(),
                    id,
                    request.getAuthority()
            ));
            log.info("✅ OAuthInvoker - 이벤트 발행 완료");
        } catch (Exception e) {
            log.error("❌ OAuth 이벤트 발행 중 예외 발생: {}", e.getMessage(), e);
            throw e;
        }
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
