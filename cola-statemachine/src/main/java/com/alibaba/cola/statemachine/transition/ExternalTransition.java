package com.alibaba.cola.statemachine.transition;

import com.alibaba.cola.statemachine.Guard;
import com.alibaba.cola.statemachine.State;

/**
 * @author dingchenchen
 * @since 2020-06-02
 */
public interface ExternalTransition<S,E,C> extends Transition<S,E,C> {
    E getEvent();

    void setEvent(E event);

    /**
     * Gets the target state of this transition.
     *
     * @return the target state
     */
    State<S,E,C> getTarget();

    void setTarget(State<S, E, C> state);

    /**
     * Gets the guard of this transition.
     *
     * @return the guard
     */
    Guard<C> getGuard();

    void setGuard(Guard<C> guard);
}
