package com.drinkhere.drinklymember.domain.member.repository;

import com.drinkhere.drinklymember.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

    boolean existsByDi(String di);
}
