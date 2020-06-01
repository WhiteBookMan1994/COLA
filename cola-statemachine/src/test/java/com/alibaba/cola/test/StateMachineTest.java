package com.alibaba.cola.test;

import com.alibaba.cola.statemachine.Action;
import com.alibaba.cola.statemachine.Condition;
import com.alibaba.cola.statemachine.StateMachine;
import com.alibaba.cola.statemachine.StateMachineFactory;
import com.alibaba.cola.statemachine.builder.StateMachineBuilder;
import com.alibaba.cola.statemachine.builder.StateMachineBuilderFactory;
import org.junit.Assert;
import org.junit.Test;

/**
 * StateMachineTest
 *
 * @author Frank Zhang
 * @date 2020-02-08 12:19 PM
 */
public class StateMachineTest {

    static String MACHINE_ID = "TestStateMachine";

    static enum States {
        STATE1, STATE2, STATE3, STATE4
    }

    static enum Events {
        EVENT1, EVENT2, EVENT3, EVENT4, INTERNAL_EVENT
    }

    static class Context {
        String operator = "frank";
        String entityId = "123465";
    }

    @Test
    public void testExternalNormal() {
        StateMachineBuilder<States, Events, Context> builder = StateMachineBuilderFactory.create();
        builder.externalTransition()
                .source(States.STATE1)
                .target(States.STATE2)
                .event(Events.EVENT1)
                .guard(checkCondition())
                .perform(doAction())
                .and()
                .externalTransition()
                .source(States.STATE1)
                .target(States.STATE3)
                .event(Events.EVENT2)
                .guard(checkCondition())
                .perform(doAction());

        StateMachine<States, Events, Context> stateMachine = builder.build(MACHINE_ID);
        States target = stateMachine.fireEvent(States.STATE1, Events.EVENT2, new Context());
        // Assert.assertEquals(States.STATE2, target);
        Assert.assertEquals(States.STATE3, target);
    }

    @Test
    public void testExternalTransitionsNormal() {
        StateMachineBuilder<States, Events, Context> builder = StateMachineBuilderFactory.create();
        builder.externalTransitions()
                .fromAmong(States.STATE1, States.STATE2, States.STATE3)
                .target(States.STATE4)
                .event(Events.EVENT1)
                .guard(checkCondition())
                .perform(doAction());

        StateMachine<States, Events, Context> stateMachine = builder.build(MACHINE_ID + "1");
        States target = stateMachine.fireEvent(States.STATE2, Events.EVENT1, new Context());
        Assert.assertEquals(States.STATE4, target);
    }

    @Test
    public void testInternalNormal() {
        StateMachineBuilder<States, Events, Context> builder = StateMachineBuilderFactory.create();
        builder.internalTransition()
                .within(States.STATE1)
                .event(Events.INTERNAL_EVENT)
                .guard(checkCondition())
                .perform(doAction());
        StateMachine<States, Events, Context> stateMachine = builder.build(MACHINE_ID + "2");

        stateMachine.fireEvent(States.STATE1, Events.EVENT1, new Context());
        States target = stateMachine.fireEvent(States.STATE1, Events.INTERNAL_EVENT, new Context());
        Assert.assertEquals(States.STATE1, target);
    }

    @Test
    public void testExternalInternalNormal() {
        StateMachine<States, Events, Context> stateMachine = buildStateMachine("testExternalInternalNormal");

        Context context = new Context();
        States target = stateMachine.fireEvent(States.STATE1, Events.EVENT1, context);
        Assert.assertEquals(States.STATE2, target);
        target = stateMachine.fireEvent(States.STATE2, Events.INTERNAL_EVENT, context);
        Assert.assertEquals(States.STATE2, target);
        target = stateMachine.fireEvent(States.STATE2, Events.EVENT2, context);
        Assert.assertEquals(States.STATE1, target);
        target = stateMachine.fireEvent(States.STATE1, Events.EVENT3, context);
        Assert.assertEquals(States.STATE3, target);
    }

    private StateMachine<States, Events, Context> buildStateMachine(String machineId) {
        StateMachineBuilder<States, Events, Context> builder = StateMachineBuilderFactory.create();
        builder.externalTransition()
                .source(States.STATE1)
                .target(States.STATE2)
                .event(Events.EVENT1)
                .guard(checkCondition())
                .perform(doAction());

        builder.internalTransition()
                .within(States.STATE2)
                .event(Events.INTERNAL_EVENT)
                .guard(checkCondition())
                .perform(doAction());

        builder.externalTransition()
                .source(States.STATE2)
                .target(States.STATE1)
                .event(Events.EVENT2)
                .guard(checkCondition())
                .perform(doAction());

        builder.externalTransition()
                .source(States.STATE1)
                .target(States.STATE3)
                .event(Events.EVENT3)
                .guard(checkCondition())
                .perform(doAction());

        builder.externalTransitions()
                .fromAmong(States.STATE1, States.STATE2, States.STATE3)
                .target(States.STATE4)
                .event(Events.EVENT4)
                .guard(checkCondition())
                .perform(doAction());

        builder.build(machineId);

        StateMachine<States, Events, Context> stateMachine = StateMachineFactory.get(machineId);
        stateMachine.showStateMachine();
        return stateMachine;
    }

    @Test
    public void testMultiThread() {
        buildStateMachine("testMultiThread");

        for (int i = 0; i < 10; i++) {
            Thread thread = new Thread(() -> {
                StateMachine<States, Events, Context> stateMachine = StateMachineFactory.get("testMultiThread");
                States target = stateMachine.fireEvent(States.STATE1, Events.EVENT1, new Context());
                Assert.assertEquals(States.STATE2, target);
            });
            thread.start();
        }


        for (int i = 0; i < 10; i++) {
            Thread thread = new Thread(() -> {
                StateMachine<States, Events, Context> stateMachine = StateMachineFactory.get("testMultiThread");
                States target = stateMachine.fireEvent(States.STATE1, Events.EVENT4, new Context());
                Assert.assertEquals(States.STATE4, target);
            });
            thread.start();
        }

        for (int i = 0; i < 10; i++) {
            Thread thread = new Thread(() -> {
                StateMachine<States, Events, Context> stateMachine = StateMachineFactory.get("testMultiThread");
                States target = stateMachine.fireEvent(States.STATE1, Events.EVENT3, new Context());
                Assert.assertEquals(States.STATE3, target);
            });
            thread.start();
        }

    }


    private Condition<Context> checkCondition() {
        return (ctx) -> {
            return true;
        };
    }

    private Action<States, Events, Context> doAction() {
        return (from, to, event, ctx) -> {
            System.out.println(ctx.operator + " is operating " + ctx.entityId + " source:" + from + " target:" + to + " event:" + event);
        };
    }

}
