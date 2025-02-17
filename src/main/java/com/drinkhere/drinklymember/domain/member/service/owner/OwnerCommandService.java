package com.drinkhere.drinklymember.domain.member.service.owner;

import com.drinkhere.drinklymember.common.annotation.DomainService;
import com.drinkhere.drinklymember.domain.member.entity.Owner;
import com.drinkhere.drinklymember.domain.member.repository.OwnerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@DomainService
@Transactional
@RequiredArgsConstructor
public class OwnerCommandService {
    private final OwnerRepository ownerRepository;

    public void save(Owner owner) {
        ownerRepository.save(owner);
    }
}