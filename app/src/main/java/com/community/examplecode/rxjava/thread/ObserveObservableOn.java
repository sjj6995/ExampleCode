package com.community.examplecode.rxjava.thread;

import android.util.Log;

import com.community.examplecode.rxjava.AbstractObserableWithUpStream;
import com.community.examplecode.rxjava.ObservableSource;
import com.community.examplecode.rxjava.Observe;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 *
 * @param <T>
 */
public class ObserveObservableOn<T> extends AbstractObserableWithUpStream<T,T> {

    final Scheduler scheduler;

    public ObserveObservableOn(Scheduler scheduler, ObservableSource<T> source) {
        super(source);
        this.scheduler = scheduler;
    }

    @Override
    protected void subscribeActul(Observe<T> observe) {
        Scheduler.Worker worker = scheduler.createWorker();
        source.subscribe(new ObserveOnObserver<>(observe,worker));//向上游订阅数据，切换线程是在
    }


    static class ObserveOnObserver<T> implements Observe<T>,Runnable{

        final Observe<T> downSource;
        final Scheduler.Worker worker;
        final Deque<T> queue;


        volatile boolean done;
        volatile Throwable error;
        volatile boolean over;

        ObserveOnObserver(Observe<T> downSource, Scheduler.Worker worker) {
            this.downSource = downSource;
            this.worker = worker;
            queue = new ArrayDeque<>();
        }

        @Override
        public void onSubscribe() {
            Log.d("TAG", "onSubscribe: 我是observeOnObservable中的方法");
            downSource.onSubscribe();
        }

        @Override
        public void next(T o) {
            Log.d("TAG", "ObserveObservableOn执行onNext: ");
            queue.offer(o);//加入队列
            schedule();
        }

        private void schedule() {
            worker.schedule(this);
        }

        @Override
        public void complete() {
            Log.d("TAG", "ObserveObservableOn执行complete: ");
            downSource.complete();
        }

        @Override
        public void error(Throwable throwable) {
            downSource.error(throwable);
        }

        @Override
        public void run() {

            drainNormal();

        }

        /**
         * 从队列并发处理
         */
        private void drainNormal() {
            final Observe<T> observe = downSource;
            while (true){
                boolean d = done;
                T t = queue.poll();
                boolean empty = t ==null;
                if (checkTerminated(d,empty,observe)){
                    return;
                }
                if (empty){
                    break;
                }
                observe.next(t);
            }
        }

        private boolean checkTerminated(boolean d, boolean empty, Observe<T> observe) {
            if (over){
                queue.clear();
                return true;
            }
            if (d){
                Throwable e = error;
                if (e!=null){
                    over = true;
                    observe.error(e);
                    return true;
                }else if (empty){
                    over =true;
                    observe.complete();
                    return true;
                }

            }
            return false;
        }
    }
}
