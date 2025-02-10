package com.drinkhere.drinklymember.common.cache.operators.impl;

import net.jodah.expiringmap.ExpiringMap;

public abstract class ExpiredStorage<K,V> {
    protected final ExpiringMap<K,V> storage = ExpiringMap.builder()
        .variableExpiration()
        .build();
}
