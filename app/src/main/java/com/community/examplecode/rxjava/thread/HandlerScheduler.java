package com.community.examplecode.rxjava.thread;

import android.os.Handler;

public class HandlerScheduler extends Scheduler{


    final Handler handler;

    public HandlerScheduler(Handler handler) {
        this.handler = handler;
    }

    @Override
    public Worker createWorker() {
        return new HandlerWorker(handler);
    }


    static class HandlerWorker implements Worker{
        final  Handler wraper;

        HandlerWorker(Handler wraper) {
            this.wraper = wraper;
        }

        @Override
        public void schedule(Runnable runnable) {
            wraper.post(runnable);
        }
    }
}
