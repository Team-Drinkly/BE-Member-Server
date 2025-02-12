package com.drinkhere.drinklymember.domain.member.service;

import com.drinkhere.drinklymember.domain.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberCommandService {
    private final com.drinkhere.drinklymember.domain.member.repository.MemberRepository memberRepository;
    public void save(final Member member) {memberRepository.save(member);}
}
