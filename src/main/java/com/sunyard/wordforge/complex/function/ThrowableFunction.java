package com.sunyard.wordforge.complex.function;

/**
 * @author Archer
 */
@FunctionalInterface
public interface ThrowableFunction<T, R> {
    R apply(T t) throws Exception;
}
