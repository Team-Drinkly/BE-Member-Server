package com.drinkhere.drinklymember.domain.auth.repository;

import com.drinkhere.drinklymember.domain.auth.entity.OAuth;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OAuthRepository extends JpaRepository<OAuth, Long> {

    boolean existsBySub(String sub);

    Optional<OAuth> findBySub(String sub);

    Optional<OAuth> findById(Long id);
}
