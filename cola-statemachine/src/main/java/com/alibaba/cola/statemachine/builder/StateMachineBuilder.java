package com.alibaba.cola.statemachine.builder;

import com.alibaba.cola.statemachine.StateMachine;

/**
 * StateMachineBuilder
 *
 * @author Frank Zhang
 * @date 2020-02-07 5:32 PM
 */
public interface StateMachineBuilder<S, E, C> {
    /**
     * Builder for one transition
     * @return External transition builder
     */
    ExternalTransitionBuilder<S, E, C> externalTransition();

    /**
     * Builder for multiple transitions
     * @return External transition builder
     */
    ExternalTransitionsBuilder<S, E, C> externalTransitions();

    /**
     * Start to build internal transition
     * @return Internal transition builder
     */
    InternalTransitionBuilder<S, E, C> internalTransition();

    /**
     * Start to build choice transition
     * @return choice transition builder
     */
    ChoiceTransitionBuilder<S, E, C> choiceTransition();

    StateMachine<S,E,C> build(String machineId);

}
