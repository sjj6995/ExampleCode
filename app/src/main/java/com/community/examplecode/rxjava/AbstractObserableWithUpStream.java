package com.community.examplecode.rxjava;

/**
 *  此类作用相当于装饰类
 * @param <T>
 * @param <U>
 */
public abstract class AbstractObserableWithUpStream<T,U> extends Observable<U> {

    protected final ObservableSource<T> source;
    public AbstractObserableWithUpStream(ObservableSource<T> source) {
        this.source = source;
    }
}
