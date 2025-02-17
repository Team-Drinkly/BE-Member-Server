package com.drinkhere.drinklymember.domain.auth.repository;

import com.drinkhere.drinklymember.domain.auth.entity.OAuthOwner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OAuthOwnerRepository extends JpaRepository<OAuthOwner, Long> {

    boolean existsBySub(String sub);

    Optional<OAuthOwner> findBySub(String sub);

    Optional<OAuthOwner> findById(Long id);
}
