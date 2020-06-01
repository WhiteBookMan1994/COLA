package com.alibaba.cola.test;

import com.alibaba.cola.statemachine.Action;
import com.alibaba.cola.statemachine.Condition;
import com.alibaba.cola.statemachine.StateMachine;
import com.alibaba.cola.statemachine.builder.StateMachineBuilder;
import com.alibaba.cola.statemachine.builder.StateMachineBuilderFactory;
import com.alibaba.cola.statemachine.impl.StateMachineException;
import org.junit.Assert;
import org.junit.Test;

/**
 * StateMachineUnNormalTest
 *
 * @author Frank Zhang
 * @date 2020-02-08 5:52 PM
 */
public class StateMachineUnNormalTest {

    @Test
    public void testConditionNotMeet(){
        StateMachineBuilder<StateMachineTest.States, StateMachineTest.Events, StateMachineTest.Context> builder = StateMachineBuilderFactory.create();
        builder.externalTransition()
                .source(StateMachineTest.States.STATE1)
                .target(StateMachineTest.States.STATE2)
                .event(StateMachineTest.Events.EVENT1)
                .guard(checkConditionFalse())
                .perform(doAction());

        StateMachine<StateMachineTest.States, StateMachineTest.Events, StateMachineTest.Context> stateMachine = builder.build("NotMeetConditionMachine");
        StateMachineTest.States target = stateMachine.fireEvent(StateMachineTest.States.STATE1, StateMachineTest.Events.EVENT1, new StateMachineTest.Context());
        Assert.assertEquals(StateMachineTest.States.STATE1,target);
    }


    @Test(expected = StateMachineException.class)
    public void testDuplicatedTransition(){
        StateMachineBuilder<StateMachineTest.States, StateMachineTest.Events, StateMachineTest.Context> builder = StateMachineBuilderFactory.create();
        builder.externalTransition()
                .source(StateMachineTest.States.STATE1)
                .target(StateMachineTest.States.STATE2)
                .event(StateMachineTest.Events.EVENT1)
                .guard(checkCondition())
                .perform(doAction());

        builder.externalTransition()
                .source(StateMachineTest.States.STATE1)
                .target(StateMachineTest.States.STATE2)
                .event(StateMachineTest.Events.EVENT1)
                .guard(checkCondition())
                .perform(doAction());
    }

    @Test(expected = StateMachineException.class)
    public void testDuplicateMachine(){
        StateMachineBuilder<StateMachineTest.States, StateMachineTest.Events, StateMachineTest.Context> builder = StateMachineBuilderFactory.create();
        builder.externalTransition()
                .source(StateMachineTest.States.STATE1)
                .target(StateMachineTest.States.STATE2)
                .event(StateMachineTest.Events.EVENT1)
                .guard(checkCondition())
                .perform(doAction());

        builder.build("DuplicatedMachine");
        builder.build("DuplicatedMachine");
    }

    private Condition<StateMachineTest.Context> checkCondition() {
        return (ctx) -> {return true;};
    }

    private Condition<StateMachineTest.Context> checkConditionFalse() {
        return (ctx) -> {return false;};
    }

    private Action<StateMachineTest.States, StateMachineTest.Events, StateMachineTest.Context> doAction() {
        return (from, to, event, ctx)->{
            System.out.println(ctx.operator+" is operating "+ctx.entityId+"source:"+from+" target:"+to+" event:"+event);
        };
    }
}
