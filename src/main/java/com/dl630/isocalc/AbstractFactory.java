package com.dl630.isocalc;

public interface AbstractFactory<T> {
    T create(String objectType);
}
