package com.drinkhere.drinklymember.domain.member.repository;

import com.drinkhere.drinklymember.domain.member.entity.Owner;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OwnerRepository extends JpaRepository<Owner, Long> {
}
