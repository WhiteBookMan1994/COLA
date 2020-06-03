package com.alibaba.cola.statemachine.transition;

import com.alibaba.cola.statemachine.Guard;

/**
 * @author dingchenchen
 * @since 2020-06-02
 */
public interface ChoiceTransition<S,E,C> extends Transition<S,E,C>{
    E getEvent();

    void setEvent(E event);

    void first(S target, Guard<C> guard);

    void then(S target, Guard<C> guard);

    void last(S target);
}
