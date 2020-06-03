package com.alibaba.cola.statemachine.builder;

import com.alibaba.cola.statemachine.Guard;

/**
 * On
 *
 * @author Frank Zhang
 * @date 2020-02-07 6:14 PM
 */
public interface On<S, E, C> extends When<S, E, C>{
    /**
     * Add guard for the transition
     * @param guard transition guard
     * @return When clause builder
     */
    When<S, E, C> when(Guard<C> guard);
}
