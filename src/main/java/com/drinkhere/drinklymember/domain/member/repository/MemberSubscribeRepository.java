package com.drinkhere.drinklymember.domain.member.repository;

import com.drinkhere.drinklymember.domain.member.entity.MemberSubscribe;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberSubscribeRepository extends JpaRepository<MemberSubscribe, Long> {

    Optional<MemberSubscribe> findByMemberId(Long memberId); // Optional로 변경
}
