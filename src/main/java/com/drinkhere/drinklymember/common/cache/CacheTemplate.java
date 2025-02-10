package com.drinkhere.drinklymember.common.cache;

import com.drinkhere.drinklymember.common.cache.operators.SetOperator;
import com.drinkhere.drinklymember.common.cache.operators.ValueOperator;

public interface CacheTemplate<K, V> {

    SetOperator<K> opsForSet();

    ValueOperator<K, V> opsForValue();
}
