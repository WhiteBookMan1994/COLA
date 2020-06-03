package com.alibaba.cola.statemachine.builder;

import com.alibaba.cola.statemachine.Action;
import com.alibaba.cola.statemachine.Guard;
import com.alibaba.cola.statemachine.transition.Transition;
/**
 * interface for build transition from a choice pseudo state
 *
 * @author dingchenchen
 * @since 2020-06-02
 */
public interface ChoiceTransitionBuilder<S, E, C> {

    /**
     * Specify a source state {@code S} for this {@link Transition}.
     *
     * @param source the source state {@code S}
     * @return configurer for chaining
     */
    ChoiceTransitionBuilder<S, E, C> source(S source);

    /**
     * Specify a target state {@code S} as a first choice.
     * This must be set.
     * <p>In normal if/else if/else this would represent if.</p>
     *
     * @param target the target state
     * @param guard the guard for this choice
     * @return configurer for chaining
     */
    ChoiceTransitionBuilder<S, E, C> first(S target, Guard<C> guard);

    /**
     * Specify a target state {@code S} as a then choice.
     * This is optional. Multiple thens will preserve order.
     * <p>In normal if/else if/else this would represent else if.</p>
     *
     * @param target the target state
     * @param guard the guard for this choice
     * @return configurer for chaining
     */
    ChoiceTransitionBuilder<S, E, C> then(S target, Guard<C> guard);

    /**
     * Specify a target state {@code S} as a last choice.
     * This must be set.
     * <p>In normal if/else if/else this would represent else.</p>
     *
     * @param target the target state
     * @return configurer for chaining
     */
    ChoiceTransitionBuilder<S, E, C> last(S target);

    /**
     * Build transition event
     * @param event transition event
     * @return On clause builder
     */
    ChoiceTransitionBuilder<S, E, C> on(E event);

    /**
     * Define action to be performed during transition
     *
     * @param action performed action
     */
    void perform(Action<S, E, C> action);
}
