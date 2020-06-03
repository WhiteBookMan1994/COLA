package com.alibaba.cola.statemachine.impl;

import com.alibaba.cola.statemachine.State;
import com.alibaba.cola.statemachine.Transition;
import com.alibaba.cola.statemachine.Visitor;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Lists;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * StateImpl
 *
 * @author Frank Zhang
 * @date 2020-02-07 11:19 PM
 */
public class StateImpl<S, E, C> implements State<S, E, C> {
    protected final S stateId;
    /**
     * event和transition 改为 一对多，解决选择伪状态问题
     */
    private ListMultimap<E, Transition<S, E, C>> transitions = ArrayListMultimap.create();

    StateImpl(S stateId) {
        this.stateId = stateId;
    }

    @Override
    public Transition<S, E, C> addTransition(E event, State<S, E, C> target, TransitionType transitionType) {
        Transition<S, E, C> newTransition = new TransitionImpl<>();
        newTransition.setSource(this);
        newTransition.setTarget(target);
        newTransition.setEvent(event);
        newTransition.setType(transitionType);

        Debugger.debug("Begin to add new transition: " + newTransition);
        verify(event, newTransition);
        transitions.put(event, newTransition);
        return newTransition;
    }

    /**
     * Per one source and target state, there is only one transition is allowed
     *
     * @param event
     * @param newTransition
     */
    private void verify(E event, Transition<S, E, C> newTransition) {
        List<Transition<S, E, C>> existingTransitions = transitions.get(event);
        /*if (Co) {

        }*/
        for (Transition transition : existingTransitions) {
            if (transition.equals(newTransition)) {
                throw new StateMachineException(transition + " already Exist, you can not add another one");
            }
        }
    }

    @Override
    public List<Transition<S, E, C>> getTransition(E event) {
        return transitions.get(event);
    }

    @Override
    public Collection<Transition<S, E, C>> getTransitions() {
        return transitions.values();
    }

    @Override
    public S getId() {
        return stateId;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visitOnEntry(this);
        visitor.visitOnExit(this);
    }

    @Override
    public boolean equals(Object anObject) {
        if (anObject instanceof State) {
            State other = (State) anObject;
            if (this.stateId.equals(other.getId())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return stateId.toString();
    }
}
