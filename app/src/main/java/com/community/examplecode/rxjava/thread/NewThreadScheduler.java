package com.community.examplecode.rxjava.thread;

import android.os.Handler;

import java.util.concurrent.ExecutorService;

public class NewThreadScheduler extends Scheduler{


    final ExecutorService executorService;

    public NewThreadScheduler(ExecutorService executorService) {
        this.executorService = executorService;
    }

    @Override
    public Worker createWorker() {
        return new HandlerWorker(executorService);
    }


    static class HandlerWorker implements Worker{
        final  ExecutorService exector;

        HandlerWorker(ExecutorService exector) {
            this.exector = exector;
        }

        @Override
        public void schedule(Runnable runnable) {
            exector.execute(runnable);
        }
    }
}
