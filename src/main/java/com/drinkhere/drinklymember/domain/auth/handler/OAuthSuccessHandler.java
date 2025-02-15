package com.drinkhere.drinklymember.domain.auth.handler;

import com.drinkhere.drinklymember.domain.auth.entity.OAuth;
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
    public void handle(OAuthSuccessEvent oAuthSuccessEvent) {
        // OAuth ID가 이미 포함된 경우 저장할 필요 없음
        if (oAuthSuccessEvent.userId() != null) {
            log.info("OAuth ID already exists: {}", oAuthSuccessEvent.userId());
            return;
        }

        // OAuth ID가 없을 경우만 저장
        OAuth oAuth = OAuth.of(
                oAuthSuccessEvent.provider(),
                oAuthSuccessEvent.sub()
        );

        OAuth savedOAuth = oAuthSaveService.save(oAuth);
        log.info("OAuth ID saved: {}", savedOAuth.getId());
    }

    private void saveOAuth(OAuthSuccessEvent oAuthSuccessEvent) {
        OAuth oAuth = OAuth.of(
                oAuthSuccessEvent.provider(),
                oAuthSuccessEvent.sub()
        );

        OAuth savedOAuth = oAuthSaveService.save(oAuth); // 저장 후 id 확인

        // 이벤트 다시 발행 (oauthId 포함)
        publisher.publishEvent(OAuthSuccessEvent.of(
                oAuthSuccessEvent.username(),
                oAuthSuccessEvent.email(),
                oAuthSuccessEvent.provider(),
                oAuthSuccessEvent.sub(),
                savedOAuth.getId()  // OAuth 엔티티의 ID 전달
        ));
    }

}
