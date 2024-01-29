package com.sunyard.wordforge.complex.function;

/**
 * 函数式接口
 * 抛出异常
 *
 * @author Archer
 */
@FunctionalInterface
public interface ThrowableFunction<T, R> {
    R apply(T t) throws Exception;
}
