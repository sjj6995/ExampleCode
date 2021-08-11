package com.community.examplecode.rxjava.thread;

public abstract class Scheduler {

    public abstract Worker createWorker();
    public interface Worker{
        void schedule(Runnable runnable);
    }
}
