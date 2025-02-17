package com.drinkhere.drinklymember.domain.member.entity;

import com.drinkhere.drinklymember.domain.auditing.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberSubscribe extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_subscribe_id", unique = true)
    private Long id;

    @Column(nullable = false)
    private Long memberId;

    @Column(nullable = false)
    private Long subscribeId;

    @Column(nullable = false)
    private Boolean isSubscribed;

    @Column(nullable = false)
    private LocalDateTime expireDate;
}
