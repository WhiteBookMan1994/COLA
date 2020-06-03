package com.alibaba.cola.statemachine.builder;

import com.alibaba.cola.statemachine.Action;
import com.alibaba.cola.statemachine.Guard;
import com.alibaba.cola.statemachine.State;
import com.alibaba.cola.statemachine.impl.StateHelper;
import com.alibaba.cola.statemachine.transition.TransitionType;
import com.alibaba.cola.statemachine.transition.ExternalTransition;

import java.util.Map;

/**
 * ExternalTransitionBuilderImpl
 *
 * @author Frank Zhang
 * @date 2020-02-07 10:20 PM
 */
class ExternalTransitionBuilderImpl<S,E,C> implements ExternalTransitionBuilder<S,E,C>, InternalTransitionBuilder<S,E,C>, From<S,E,C>, On<S,E,C>, To<S,E,C> {

    final Map<S, State<S, E, C>> stateMap;

    private State<S, E, C> source;

    protected State<S, E, C> target;

    private ExternalTransition<S, E, C> externalTransition;

    final TransitionType transitionType;

    public ExternalTransitionBuilderImpl(Map<S, State<S, E, C>> stateMap, TransitionType transitionType) {
        this.stateMap = stateMap;
        this.transitionType = transitionType;
    }

    @Override
    public From<S, E, C> from(S stateId) {
        source = StateHelper.getState(stateMap, stateId);
        return this;
    }

    @Override
    public To<S, E, C> to(S stateId) {
        target = StateHelper.getState(stateMap, stateId);
        return this;
    }

    @Override
    public To<S, E, C> within(S stateId) {
        source = target = StateHelper.getState(stateMap, stateId);
        return this;
    }

    @Override
    public When<S, E, C> when(Guard<C> guard) {
        externalTransition.setGuard(guard);
        return this;
    }

    @Override
    public On<S, E, C> on(E event) {
        externalTransition = source.addTransition(event, target, transitionType);
        return this;
    }

    @Override
    public void perform(Action<S, E, C> action) {
        externalTransition.setAction(action);
    }


}
