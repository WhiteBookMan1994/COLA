package com.alibaba.cola.statemachine.impl;

import com.alibaba.cola.statemachine.State;
import com.alibaba.cola.statemachine.StateMachine;
import com.alibaba.cola.statemachine.Transition;
import com.alibaba.cola.statemachine.Visitor;

import java.util.List;
import java.util.Map;

/**
 * For performance consideration,
 * The state machine is made "stateless" on purpose.
 * Once it's built, it can be shared by multi-thread
 *
 * One side effect is since the state machine is stateless, we can not get current state from State Machine.
 *
 * @author Frank Zhang
 * @date 2020-02-07 5:40 PM
 */
public class StateMachineImpl<S,E,C> implements StateMachine<S, E, C> {

    private String machineId;

    private final Map<S, State<S,E,C>> stateMap;

    private boolean ready;

    public StateMachineImpl(Map<S, State< S, E, C>> stateMap){
        this.stateMap = stateMap;
    }

    @Override
    public S fireEvent(S sourceStateId, E event, C ctx){
        isReady();
        State sourceState = getState(sourceStateId);
        return doTransition(sourceState, event, ctx).getId();
    }

    private State<S, E, C> doTransition(State sourceState, E event, C ctx) {
        List<Transition<S,E,C>> transitions = sourceState.getTransition(event);
        if (transitions != null && transitions.size() > 0) {
            if (transitions.size() == 1) {
                return transitions.get(0).transit(ctx);
            }
            //伪状态choice的情况,if-elseif-else
            else {
                Transition<S,E,C> elseTransition = null;
                for (Transition<S,E,C> transition: transitions) {
                    if (elseTransition == null && transition.getCondition() == null) {
                        elseTransition = transition;
                    }
                    else if (transition.getCondition().isSatisfied(ctx)) {
                        return transition.transit(ctx);
                    }
                }
                return elseTransition.transit(ctx);
            }
        }
        Debugger.debug("There is no Transition for " + event);
        return sourceState;
    }

    private State getState(S currentStateId) {
        State state = StateHelper.getState(stateMap, currentStateId);
        if(state == null){
            showStateMachine();
            throw new StateMachineException(currentStateId + " is not found, please check state machine");
        }
        return state;
    }

    private void isReady() {
        if(!ready){
            throw new StateMachineException("State machine is not built yet, can not work");
        }
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visitOnEntry(this);
        for(State state: stateMap.values()){
            state.accept(visitor);
        }
        visitor.visitOnExit(this);
    }

    @Override
    public void showStateMachine() {
        SysOutVisitor sysOutVisitor = new SysOutVisitor();
        accept(sysOutVisitor);
    }

    @Override
    public void generatePlantUML(){
        PlantUMLVisitor plantUMLVisitor = new PlantUMLVisitor();
        accept(plantUMLVisitor);
    }

    @Override
    public String getMachineId() {
        return machineId;
    }

    public void setMachineId(String machineId) {
        this.machineId = machineId;
    }

    public void setReady(boolean ready) {
        this.ready = ready;
    }
}
