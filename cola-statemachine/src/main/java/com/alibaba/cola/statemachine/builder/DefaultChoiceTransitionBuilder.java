package com.alibaba.cola.statemachine.builder;

import com.alibaba.cola.statemachine.Action;
import com.alibaba.cola.statemachine.Guard;
import com.alibaba.cola.statemachine.State;
import com.alibaba.cola.statemachine.transition.Transition;
import com.alibaba.cola.statemachine.impl.StateHelper;
import com.alibaba.cola.statemachine.transition.TransitionType;

import java.util.Map;

/**
 * @author dingchenchen
 * @since 2020-06-02
 */
public class DefaultChoiceTransitionBuilder<S,E,C> implements ChoiceTransitionBuilder<S,E,C> {

    final Map<S, State<S, E, C>> stateMap;

    private State<S, E, C> source;

    protected State<S, E, C> target;

    private Transition<S, E, C> transition;

    final TransitionType transitionType;

    public DefaultChoiceTransitionBuilder(Map<S, State<S, E, C>> stateMap, TransitionType transitionType) {
        this.stateMap = stateMap;
        this.transitionType = transitionType;
    }

    @Override
    public ChoiceTransitionBuilder<S, E, C> source(S source) {
        StateHelper.getState(stateMap, source);
        return this;
    }

    @Override
    public ChoiceTransitionBuilder<S, E, C> first(S target, Guard<C> guard) {
        return null;
    }

    @Override
    public ChoiceTransitionBuilder<S, E, C> then(S target, Guard<C> guard) {
        return null;
    }

    @Override
    public ChoiceTransitionBuilder<S, E, C> last(S target) {
        return null;
    }

    @Override
    public ChoiceTransitionBuilder<S, E, C> on(E event) {
        transition = source.addTransition(event, target, transitionType);
        return this;
    }

    @Override
    public void perform(Action<S, E, C> action) {
        transition.setAction(action);
    }
}
