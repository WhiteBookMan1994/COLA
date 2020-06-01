package com.alibaba.cola.statemachine.builder;

import com.alibaba.cola.statemachine.Action;

/**
 * When
 *
 * @author Frank Zhang
 * @date 2020-02-07 9:33 PM
 */
public interface When<S, E, C> extends Builder<StateMachineBuilder<S, E, C>>{
    /**
     * Define action target be performed during transition
     *
     * @param action performed action
     */
    StateMachineBuilder<S, E, C> perform(Action<S, E, C> action);
}
