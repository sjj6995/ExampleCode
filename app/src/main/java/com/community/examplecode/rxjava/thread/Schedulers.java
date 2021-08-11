package com.community.examplecode.rxjava.thread;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Schedulers {

    private static Scheduler   MAIN_THREAD = null;
    private static Scheduler   NEW_THREAD = null;




    static {
        MAIN_THREAD = new HandlerScheduler(new Handler(Looper.myLooper()));

        NEW_THREAD = new NewThreadScheduler(Executors.newScheduledThreadPool(2));
    }


    public static Scheduler mainThread(){
        return MAIN_THREAD;
    }

    public static Scheduler newThread(){
        return NEW_THREAD;
    }
}
