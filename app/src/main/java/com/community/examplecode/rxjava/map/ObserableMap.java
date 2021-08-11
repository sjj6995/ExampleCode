package com.community.examplecode.rxjava.map;

import android.util.Log;

import com.community.examplecode.rxjava.AbstractObserableWithUpStream;
import com.community.examplecode.rxjava.Function;
import com.community.examplecode.rxjava.ObservableSource;
import com.community.examplecode.rxjava.Observe;

/**
 * 装饰类的具体实现
 * @param <T>
 * @param <U>
 */
public class ObserableMap<T, U> extends AbstractObserableWithUpStream<T, U> {
    Function<T, U> function;


    public ObserableMap(ObservableSource<T> source, Function<T, U> function) {//像套娃一样把自己的顶级类型传给自己
        super(source);
        this.function = function;
    }


    @Override
    protected void subscribeActul(Observe<U> observe) {
        source.subscribe(new MapObserver<>(observe, function));
    }

    static class MapObserver<T, U> implements Observe<T> {

        final Observe<U> downStream;

        final Function<T, U> function;

        public MapObserver(Observe<U> downStream, Function<T, U> function) {
            this.downStream = downStream;
            this.function = function;
        }

        @Override
        public void onSubscribe() {
            Log.d("TAG", "onSubscribe: 我是ObserableMap中的方法");
            downStream.onSubscribe();
        }

        @Override
        public void next(T t) {
            Log.d("TAG", "map执行onNext: ");
            Log.d("TAG", "map执行线程: "+Thread.currentThread().getName());
            U r = function.apply(t);
            downStream.next(r);
        }

        @Override
        public void complete() {
            Log.d("TAG", "map执行complete: ");
            downStream.complete();
        }

        @Override
        public void error(Throwable throwable) {
            downStream.error(throwable);
        }
    }
}
