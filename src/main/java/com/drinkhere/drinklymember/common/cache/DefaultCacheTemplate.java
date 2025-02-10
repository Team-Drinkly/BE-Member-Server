package com.drinkhere.drinklymember.common.cache;

import com.drinkhere.drinklymember.common.cache.operators.SetOperator;
import com.drinkhere.drinklymember.common.cache.operators.ValueOperator;
import com.drinkhere.drinklymember.common.cache.operators.impl.DefaultSetOperator;
import com.drinkhere.drinklymember.common.cache.operators.impl.DefaultValueOperator;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class DefaultCacheTemplate<K, V> implements CacheTemplate<K, V> {

    private final SetOperator<K> SET_OPERATOR = new DefaultSetOperator<>();
    private final ValueOperator<K, V> VALUE_OPERATOR = new DefaultValueOperator<>();

    @Override
    public SetOperator<K> opsForSet() {
        return SET_OPERATOR;
    }

    @Override
    public ValueOperator<K, V> opsForValue() {
        return VALUE_OPERATOR;
    }
}
