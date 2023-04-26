package com.dl630.isocalc.scene;

@FunctionalInterface
public interface ObjectConstructor<T> {
    T create(Object... args);
}
