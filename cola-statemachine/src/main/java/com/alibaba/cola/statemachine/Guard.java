package com.alibaba.cola.statemachine;

/**
 * Guard
 *
 * @author Frank Zhang
 * @date 2020-02-07 2:50 PM
 */
public interface Guard<C> {

    /**
     * Evaluate a guard condition
     * @param context context object
     * @return whether the context satisfied current condition
     */
    boolean isSatisfied(C context);

    default String name(){
        return this.getClass().getSimpleName();
    }
}