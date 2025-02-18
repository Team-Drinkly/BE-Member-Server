package com.drinkhere.drinklymember.domain.member.service.owner;

import com.drinkhere.drinklymember.domain.member.repository.OwnerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OwnerQueryService {

    private final OwnerRepository ownerRepository;

    public boolean existsByDi(String di) {
        return ownerRepository.existsByDi(di);
    }

    public boolean existsById(Long ownerId) {
        return ownerRepository.existsById(ownerId);
    }
}
