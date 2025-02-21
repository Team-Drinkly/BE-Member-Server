package com.drinkhere.drinklymember.domain.member.service.member;

import com.drinkhere.drinklymember.domain.member.entity.Member;
import com.drinkhere.drinklymember.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberQueryService {

    private final MemberRepository memberRepository;

    public boolean existsByDi(String di) {
        return memberRepository.existsByDi(di);
    }

    public boolean existsById(Long memberId) {
        return memberRepository.existsById(memberId);
    }

    public Member findById(Long memberId) { return memberRepository.findByIdOrThrow(memberId); }
}
