package com.community.examplecode.rxjava;

public interface ObservableSource<T> {

    public void  subscribe(Observe<T> observe);
}
