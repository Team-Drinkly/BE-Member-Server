package com.drinkhere.drinklymember.domain.auth.handler;

import com.drinkhere.drinklymember.domain.auth.entity.OAuthMember;
import com.drinkhere.drinklymember.domain.auth.entity.OAuthOwner;
import com.drinkhere.drinklymember.domain.auth.enums.Authority;
import com.drinkhere.drinklymember.domain.auth.handler.request.OAuthSuccessEvent;
import com.drinkhere.drinklymember.domain.auth.service.OAuthQueryService;
import com.drinkhere.drinklymember.domain.auth.service.OAuthSaveService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuthSuccessHandler {

    private final OAuthQueryService oAuthQueryService;
    private final OAuthSaveService oAuthSaveService;
    private final ApplicationEventPublisher publisher;

    @EventListener(OAuthSuccessEvent.class)
    @Transactional
    public void handle(OAuthSuccessEvent event) {
        log.info("🚀 OAuthSuccessHandler - OAuth 로그인 성공 이벤트 수신: provider={}, sub={}, authority={}",
                event.provider(), event.sub(), event.authority());

        // 이미 존재하는 경우 처리 안 함
        if (event.userId() != null) {
            log.info("기존 OAuth ID 존재: {}", event.userId());
            return;
        }

        if (event.authority() == Authority.MEMBER) {
            log.info("🔍 [멤버] OAuth 저장 시작: sub={}", event.sub());
            OAuthMember savedOAuth = saveOAuthMember(event);
            publishOAuthEvent(event, savedOAuth.getId());
        } else if (event.authority() == Authority.OWNER) {
            log.info("🔍 [사장님] OAuth 저장 시작: sub={}", event.sub());
            OAuthOwner savedOAuth = saveOAuthOwner(event);
            publishOAuthEvent(event, savedOAuth.getId());
        }
    }

    /**
     * 멤버 OAuth 저장
     */
    private OAuthMember saveOAuthMember(OAuthSuccessEvent event) {
        OAuthMember oAuth = OAuthMember.of(event.provider(), event.sub());
        return oAuthSaveService.memberSave(oAuth);
    }

    /**
     * 사장님 OAuth 저장
     */
    private OAuthOwner saveOAuthOwner(OAuthSuccessEvent event) {
        OAuthOwner oAuth = OAuthOwner.of(event.provider(), event.sub());
        return oAuthSaveService.ownerSave(oAuth);
    }

    /**
     * OAuth 저장 후 이벤트 발행
     */
    private void publishOAuthEvent(OAuthSuccessEvent event, Long oAuthId) {
        log.info("🚀 OAuthSuccessHandler - OAuth 저장 완료, 이벤트 재발행: oauthId={}", oAuthId);

        publisher.publishEvent(OAuthSuccessEvent.of(
                event.username(),
                event.email(),
                event.provider(),
                event.sub(),
                oAuthId,
                event.authority()
        ));
    }
}
