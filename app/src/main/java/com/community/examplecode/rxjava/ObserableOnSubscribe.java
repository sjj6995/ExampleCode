package com.community.examplecode.rxjava;

public interface ObserableOnSubscribe<T> {//被观察者与 事件解耦

    void subscribe(Emitter<T> emitter);
}
