package com.drinkhere.drinklymember.domain.auth.handler;

import com.drinkhere.drinklymember.domain.auth.entity.OAuth;
import com.drinkhere.drinklymember.domain.auth.handler.request.OAuthSuccessEvent;
import com.drinkhere.drinklymember.domain.auth.service.OAuthQueryService;
import com.drinkhere.drinklymember.domain.auth.service.OAuthSaveService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuthSuccessHandler {
    private final OAuthQueryService oAuthQueryService;
    private final OAuthSaveService oAuthSaveService;

    @EventListener(OAuthSuccessEvent.class)
    @Transactional
    public void handle(OAuthSuccessEvent oAuthSuccessEvent) {
        if (oAuthQueryService.existBySub(oAuthSuccessEvent.sub())) {
            final OAuth oAuth = oAuthQueryService.findBySub(oAuthSuccessEvent.sub());
        } else {
            saveOAuth(oAuthSuccessEvent);
        }
    }

    private void saveOAuth(OAuthSuccessEvent oAuthSuccessEvent) {
        OAuth oAuth = OAuth.of(
                oAuthSuccessEvent.provider(),
                oAuthSuccessEvent.sub()
        );
        oAuthSaveService.save(oAuth);
    }
}
