package com.drinkhere.drinklymember.domain.auth.entity;

import com.drinkhere.drinklymember.domain.auth.enums.Provider;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OAuthOwner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "oauth_owner_id")
    private Long id;

    @Enumerated(value = EnumType.STRING)
    private Provider provider;

    private String sub;

    @Column(name = "is_registered", nullable = false)
    boolean isRegistered;

    private OAuthOwner(Provider provider, String sub) {
        this.provider = provider;
        this.sub = sub;
        this.isRegistered = false;
    }

    public static OAuthOwner of(Provider provider, String sub) {
        return new OAuthOwner(provider, sub);
    }

    public void updateRegisterStatus() { // 가입 처리
        this.isRegistered = true;
    }
}

