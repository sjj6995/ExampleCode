package com.community.examplecode.rxjava.thread;

import android.util.Log;

import com.community.examplecode.rxjava.AbstractObserableWithUpStream;
import com.community.examplecode.rxjava.ObservableSource;
import com.community.examplecode.rxjava.Observe;

/***
 * 因为订阅时向上订阅的，所以 SubscribeOn 之后的都发生在第一个SubscribeOn的工作线程上
 * @param <T>
 */
public class ObservableSubscribeOn<T> extends AbstractObserableWithUpStream<T,T> {


    final Scheduler scheduler;

    public ObservableSubscribeOn(Scheduler scheduler, ObservableSource<T> source) {
        super(source);
        this.scheduler = scheduler;
    }

    @Override
    protected void subscribeActul(Observe observe) {

        Scheduler.Worker worker = scheduler.createWorker();//创建一个工作线程
        worker.schedule(new SubscribeTask(new SubscribeOnObserver<>(observe)));
    }


    class SubscribeOnObserver<T> implements Observe<T>{

        final Observe<T> downSource;

        SubscribeOnObserver(Observe<T> downSource) {
            this.downSource = downSource;
        }


        @Override
        public void onSubscribe() {
            Log.d("TAG", "onSubscribe: 我是ObservableSubscribeOn中的方法");
            downSource.onSubscribe();

        }

        @Override
        public void next(T o) {
            Log.d("TAG", "ObservableSubscribeOn执行onNext: ");
            downSource.next(o);
        }

        @Override
        public void complete() {
            Log.d("TAG", "ObservableSubscribeOn执行complete: ");
            downSource.complete();
        }

        @Override
        public void error(Throwable throwable) {
            downSource.error(throwable);
        }
    }

    final class SubscribeTask implements Runnable{

        final SubscribeOnObserver<T> mParent;

        SubscribeTask(SubscribeOnObserver<T> mParent) {
            this.mParent = mParent;
        }

        @Override
        public void run() {
            source.subscribe(mParent);//先切换线程 再去订阅 所以发射的事件 都是在 最近的SubscribeOn（从上往下数）线程执行的
        }
    }
}
