package com.community.examplecode.rxjava.flatmap;

import android.util.Log;

import com.community.examplecode.rxjava.AbstractObserableWithUpStream;
import com.community.examplecode.rxjava.Function;
import com.community.examplecode.rxjava.ObservableSource;
import com.community.examplecode.rxjava.Observe;

public class ObservableFlatMap<T,U> extends AbstractObserableWithUpStream<T,U> {
    Function<T, ObservableSource<U>> function;

    public ObservableFlatMap(ObservableSource<T> source, Function<T, ObservableSource<U>> function) {
        super(source);
        this.function = function;
    }


    @Override
    protected void subscribeActul(Observe<U> observe) {
        source.subscribe(new MergeOberver<>(observe,function));//观察者是下游 被观察者是上游
    }

    static class MergeOberver<T,U> implements Observe<T> {

        final Observe<U> downStream;
        final Function<T, ObservableSource<U>> function;
        public MergeOberver(Observe<U> downStream, Function<T, ObservableSource<U>> function) {
            this.downStream = downStream;
            this.function = function;
        }


        @Override
        public void onSubscribe() {
            Log.d("TAG", "onSubscribe: 我是ObservableFlatMap中的方法");
            downStream.onSubscribe();
        }

        @Override
        public void next(T t) {
            Log.d("TAG", "flatmap执行onNext: ");
            ObservableSource<U> observableSource = function.apply(t);
            observableSource.subscribe(new Observe<U>() {//创建一个新的被观察者的目的 就是 能转化成你想要的任何模式。
                @Override
                public void onSubscribe() {
                    downStream.onSubscribe();
                }

                @Override
                public void next(U u) {

                    downStream.next(u);//最终还是由 下游处理
                }

                @Override
                public void complete()
                {
                    downStream.complete();
                }

                @Override
                public void error(Throwable throwable) {
                    downStream.error(throwable);
                }
            });

        }

        @Override
        public void complete() {
            Log.d("TAG", "flatmap执行complete: ");
            downStream.complete();
        }

        @Override
        public void error(Throwable throwable) {
            downStream.error(throwable);
        }
    }
}
