package com.alibaba.cola.statemachine.builder;

import com.alibaba.cola.statemachine.Action;
import com.alibaba.cola.statemachine.Guard;
import com.alibaba.cola.statemachine.State;
import com.alibaba.cola.statemachine.transition.Transition;
import com.alibaba.cola.statemachine.impl.StateHelper;
import com.alibaba.cola.statemachine.transition.TransitionType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * ExternalTransitionsBuilderImpl
 *
 * @author Frank Zhang
 * @date 2020-02-08 7:43 PM
 */
public class ExternalTransitionsBuilderImpl<S,E,C> extends ExternalTransitionBuilderImpl<S,E,C> implements ExternalTransitionsBuilder<S,E,C> {
    /**
     * This is for fromAmong where multiple sources can be configured to point to one target
     */
    private List<State<S, E, C>> sources = new ArrayList<>();

    private List<Transition<S, E, C>> transitions = new ArrayList<>();

    public ExternalTransitionsBuilderImpl(Map<S, State<S, E, C>> stateMap, TransitionType transitionType) {
        super(stateMap, transitionType);
    }

    @Override
    public From<S, E, C> fromAmong(S... stateIds) {
        for(S stateId : stateIds) {
            sources.add(StateHelper.getState(super.stateMap, stateId));
        }
        return this;
    }

    @Override
    public On<S, E, C> on(E event) {
        for(State source : sources) {
            Transition transition = source.addTransition(event, super.target, super.transitionType);
            transitions.add(transition);
        }
        return this;
    }

    @Override
    public When<S, E, C> when(Guard<C> guard) {
        for(Transition transition : transitions){
            transition.setGuard(guard);
        }
        return this;
    }

    @Override
    public void perform(Action<S, E, C> action) {
        for(Transition transition : transitions){
            transition.setAction(action);
        }
    }
}
