package com.drinkhere.drinklymember.domain.member.service.member;

import com.drinkhere.drinklymember.domain.member.entity.MemberSubscribe;
import com.drinkhere.drinklymember.domain.member.repository.MemberSubscribeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberSubscribeQueryService {

    private final MemberSubscribeRepository memberSubscribeRepository;

    public boolean isMemberSubscribed(Long memberId) {
        return memberSubscribeRepository.findByMemberId(memberId)
                .map(MemberSubscribe::getIsSubscribed) // 존재하면 구독 여부 반환
                .orElse(false); // 없으면 기본값 false
    }
}

