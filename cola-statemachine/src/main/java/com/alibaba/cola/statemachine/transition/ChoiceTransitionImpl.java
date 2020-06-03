package com.alibaba.cola.statemachine.transition;

import com.alibaba.cola.statemachine.Action;
import com.alibaba.cola.statemachine.Guard;
import com.alibaba.cola.statemachine.State;

import java.util.HashMap;
import java.util.Map;

/**
 * @author dingchenchen
 * @since 2020-06-02
 */
public class ChoiceTransitionImpl<S,E,C> implements ChoiceTransition<S,E,C>{

    private State<S, E, C> source;

    private Action<S,E,C> action;

    private TransitionType type = TransitionType.CHOICE;

    private E event;

    private Map<S, Guard<C>> targetStateMap = new HashMap<>();

    @Override
    public E getEvent() {
        return event;
    }

    @Override
    public void setEvent(E event) {
        this.event = event;
    }

    @Override
    public void first(S target, Guard<C> guard) {
        targetStateMap.put(target, guard);
    }

    @Override
    public void then(S target, Guard<C> guard) {
        targetStateMap.put(target, guard);
    }

    @Override
    public void last(S target) {
        targetStateMap.put(target, null);
    }

    @Override
    public State<S, E, C> getSource() {
        return source;
    }

    @Override
    public void setSource(State<S, E, C> state) {
        this.source = state;
    }

    @Override
    public void setType(TransitionType type) {
        this.type = type;
    }

    @Override
    public Action<S, E, C> getAction() {
        return this.action;
    }

    @Override
    public void setAction(Action<S, E, C> action) {
        this.action = action;
    }

    @Override
    public State<S, E, C> transit(C ctx) {
        return null;
    }

    @Override
    public void verify() {

    }
}
