package com.community.examplecode.rxjava;

import com.community.examplecode.R;
import com.community.examplecode.rxjava.create.ObservableCreate;
import com.community.examplecode.rxjava.flatmap.ObservableFlatMap;
import com.community.examplecode.rxjava.map.ObserableMap;
import com.community.examplecode.rxjava.thread.ObservableSubscribeOn;
import com.community.examplecode.rxjava.thread.ObserveObservableOn;
import com.community.examplecode.rxjava.thread.Scheduler;

public abstract class Observable<T> implements ObservableSource<T> {


    @Override
    public void subscribe(Observe<T> observe) {

        subscribeActul(observe);
    }

    /**
     * 让实现类 去 具体实现 订阅的方法
     */
    protected abstract void subscribeActul(Observe<T> observe);


    public  static <T> Observable<T> create(ObserableOnSubscribe<T> subscribe){
        return new ObservableCreate<>(subscribe);
    }


    public <R> ObserableMap<T, R> map(Function<T,R> function){
        return new ObserableMap<>(this,function);
    }

    public <R> ObservableFlatMap<T, R> flatmap(Function<T,ObservableSource<R>> function){
        return new ObservableFlatMap<>(this,function);
    }

    public ObservableSubscribeOn<T> subscribeOn(Scheduler scheduler){
        return new ObservableSubscribeOn<>(scheduler,this);
    }
    public ObserveObservableOn<T> observeOn(Scheduler scheduler){
        return new ObserveObservableOn<>(scheduler,this);
    }
}
