package com.alibaba.cola.statemachine.transition;

import com.alibaba.cola.statemachine.Action;
import com.alibaba.cola.statemachine.State;

/**
 * {@code transition} is something what a state machine associates with a state
 * changes.
 *
 * @author Frank Zhang
 *
 * @param <S> the type of state
 * @param <E> the type of event
 * @param <C> the type of user defined context, which is used to hold application data
 *
 * @date 2020-02-07 2:20 PM
 */
public interface Transition<S, E, C>{
    /**
     * Gets the source state of this transition.
     *
     * @return the source state
     */
    State<S,E,C> getSource();

    void setSource(State<S, E, C> state);

    void setType(TransitionType type);

    Action<S,E,C> getAction();

    void setAction(Action<S, E, C> action);

    /**
     * Do transition from source state to target state.
     *
     * @return the target state
     */

    State<S,E,C> transit(C ctx);
    /**
     * Verify transition correctness
     */
    void verify();
}
