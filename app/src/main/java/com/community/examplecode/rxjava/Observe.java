package com.community.examplecode.rxjava;

public interface Observe<T> {

    public void onSubscribe();
    public void next(T t);
    public void complete();
    public void error(Throwable throwable);
}
