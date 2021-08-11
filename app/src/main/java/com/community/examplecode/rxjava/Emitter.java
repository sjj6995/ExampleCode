package com.community.examplecode.rxjava;

public interface Emitter<T> {

    public void next(T t);
    public void complete();
    public void error(Throwable throwable);
}
