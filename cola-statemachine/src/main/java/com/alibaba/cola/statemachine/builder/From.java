package com.alibaba.cola.statemachine.builder;

/**
 * From
 *
 * @author Frank Zhang
 * @date 2020-02-07 6:13 PM
 */
public interface From<S, E, C> {
    /**
     * Build transition target state and return target clause builder
     * @param stateId id of state
     * @return To clause builder
     */
    To<S, E, C> target(S stateId);

}
