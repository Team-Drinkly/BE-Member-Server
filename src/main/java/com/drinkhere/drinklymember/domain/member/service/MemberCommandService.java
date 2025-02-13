package com.drinkhere.drinklymember.domain.member.service;

import com.drinkhere.drinklymember.common.annotation.DomainService;
import com.drinkhere.drinklymember.domain.member.entity.Member;
import com.drinkhere.drinklymember.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@DomainService
@Transactional
@RequiredArgsConstructor
public class MemberCommandService {
    private final MemberRepository memberRepository;
    public void save(final Member member) {memberRepository.save(member);}
}
