package com.drinkhere.drinklymember.domain.member.service.member;

import com.drinkhere.drinklymember.domain.member.entity.MemberSubscribe;
import com.drinkhere.drinklymember.domain.member.repository.MemberSubscribeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberSubscribeQueryService {

    private final MemberSubscribeRepository memberSubscribeRepository;

    public boolean isMemberSubscribed(Long memberId) {
        return memberSubscribeRepository.existsByMemberIdAndIsSubscribed(memberId, true);
    }

    /**
     * 멤버의 `subscribeId` 조회
     */
    public Long getSubscribeId(Long memberId) {
        return memberSubscribeRepository.findByMemberIdAndIsSubscribed(memberId, true)
                .map(MemberSubscribe::getSubscribeId)
                .orElse(null);
    }
}
