package com.alibaba.cola.statemachine.transition;

import com.alibaba.cola.statemachine.Action;
import com.alibaba.cola.statemachine.Guard;
import com.alibaba.cola.statemachine.State;
import com.alibaba.cola.statemachine.impl.Debugger;
import com.alibaba.cola.statemachine.impl.StateMachineException;
import com.alibaba.cola.statemachine.transition.ExternalTransition;
import com.alibaba.cola.statemachine.transition.TransitionType;

/**
 * ExternalTransitionImpl。
 *
 * This should be designed to be immutable, so that there is no thread-safe risk
 *
 * @author Frank Zhang
 * @date 2020-02-07 10:32 PM
 */
public class ExternalTransitionImpl<S,E,C> implements ExternalTransition<S,E,C> {

    private State<S, E, C> source;

    private State<S, E, C> target;

    private E event;

    private Guard<C> guard;

    private Action<S,E,C> action;

    private TransitionType type = TransitionType.EXTERNAL;

    @Override
    public State<S, E, C> getSource() {
        return source;
    }

    @Override
    public void setSource(State<S, E, C> state) {
        this.source = state;
    }

    @Override
    public E getEvent() {
        return this.event;
    }

    @Override
    public void setEvent(E event) {
        this.event = event;
    }

    @Override
    public void setType(TransitionType type) {
        this.type = type;
    }

    @Override
    public State<S, E, C> getTarget() {
        return this.target;
    }

    @Override
    public void setTarget(State<S, E, C> target) {
        this.target = target;
    }

    @Override
    public Guard<C> getGuard() {
        return this.guard;
    }

    @Override
    public void setGuard(Guard<C> guard) {
        this.guard = guard;
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
        Debugger.debug("Do transition: "+this);
        this.verify();
        if(guard == null || guard.isSatisfied(ctx)){
            if(action != null){
                action.execute(source.getId(), target.getId(), event, ctx);
            }
            return target;
        }

        Debugger.debug("Guard is not satisfied, stay at the "+source+" state ");
        return source;
    }

    @Override
    public final String toString() {
        return source + "-[" + event.toString() +", "+type+"]->" + target;
    }

    @Override
    public boolean equals(Object anObject){
        if(anObject instanceof ExternalTransition){
            ExternalTransition other = (ExternalTransition)anObject;
            if(this.event.equals(other.getEvent())
                    && this.source.equals(other.getSource())
                    && this.target.equals(other.getTarget())){
                return true;
            }
        }
        return false;
    }

    @Override
    public void verify() {
        if(type== TransitionType.INTERNAL && source != target) {
            throw new StateMachineException(String.format("Internal transition source state '%s' " +
                    "and target state '%s' must be same.", source, target));
        }
    }
}
