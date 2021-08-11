package com.community.examplecode;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.community.examplecode.rxjava.Emitter;
import com.community.examplecode.rxjava.Function;
import com.community.examplecode.rxjava.ObserableOnSubscribe;
import com.community.examplecode.rxjava.Observable;
import com.community.examplecode.rxjava.ObservableSource;
import com.community.examplecode.rxjava.Observe;
import com.community.examplecode.rxjava.Test;
import com.community.examplecode.rxjava.create.ObservableCreate;
import com.community.examplecode.rxjava.thread.Schedulers;

public class MainActivity extends AppCompatActivity {


    private Test mTest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /**
         *
         * 为什么同时有多个SubscribeOn时 ，只有最上面的能有效切换线程，ObserveOn只能切换下游的线程呢？ 这个问题我想了有两天时间。才理解看下面的解释：
         * 观察者从下到上 ，事件从上到下
         * subscribeOn：先切换线程 再去订阅 所以发射的事件 都是在 最近的SubscribeOn（从上往下数）线程执行的
         * observeOn：先订阅观察者，然后再发送事件后 收到next 时 再执行切换线程，所以 最后一个observeOn 切换的线程未准。
         *
         *
         * 套娃。上游是最大的娃 下游是最小的娃  一个是在订阅之前就切换了线程（代表整个流程已经切换了线程） 所以如果没有再切换线程时 他都会在切换的线程中执行，另一个是 事件发射时  才去切换的线程，
         * 所以在observeOn之前代码都是在 那个线程中写的就是在哪个线程，之后的是切换的线程。
         */
        Observable.create(emitter -> {
            Log.d("TAG", "开始发送事件");
            Log.d("TAG", "开始发送事件-thread Name:"+Thread.currentThread().getName());
            emitter.next("333");
//            emitter.next("111");
//            emitter.next("222");
            emitter.complete();
//            emitter.error(new Throwable());

        })

//                .flatmap(o -> Observable.create(emitter -> emitter.next("flatmap处理后："+o)))
//                .flatmap(new Function<Object, ObservableSource<Object>>() {
//
//                    @Override
//                    public ObservableSource<Object> apply(Object o) {
//                        return Observable.create(new ObserableOnSubscribe<Object>() {
//
//                            @Override
//                            public void subscribe(Emitter<Object> emitter) {
//                                Log.d("TAG", "flatmap-thread Name:"+Thread.currentThread().getName());
//                                Log.d("TAG", "执行flatmap转化");
//
//                                emitter.next("flatmap处理后："+o);
//
//                            }
//                        });
//                    }
//                })

//                .subscribeOn(Schedulers.newThread())
                .flatmap(new Function<Object, ObservableSource<Object>>() {
                    @Override
                    public ObservableSource<Object> apply(Object o) {
                        return Observable.create(new ObserableOnSubscribe<Object>() {

                            @Override
                            public void subscribe(Emitter<Object> emitter) {
                                Log.d("TAG", "flatmap-thread Name:"+Thread.currentThread().getName());
                                Log.d("TAG", "执行flatmap转化");

                                emitter.next("flatmap处理后："+o);

                            }
                        });
                    }
                })
                .observeOn(Schedulers.newThread())
                .map(new Function<Object, Object>() {
                    @Override
                    public Object apply(Object o) {
                        Log.d("TAG", "map-thread: "+Thread.currentThread().getName());
                        Log.d("TAG", "执行map转化");
                        return "map拼接后的结果："+o;
                    }
                })
                .subscribe(new Observe<Object>() {//订阅时 先执行 map 这个对象中的 subscribe具体实现类 再执行 create subscribe具体实现类 然后再发射事件，执行

            @Override
            public void onSubscribe() {

                Log.d("TAG", "onSubscribe: 我是MainActivity匿名内部类中的方法");

            }

            @Override
            public void next(Object o) {
                Log.d("TAG", "next: "+o.toString());
                Log.d("TAG", "MainActivity匿名内部类中的方法-thread Name:"+Thread.currentThread().getName());
            }

            @Override
            public void complete() {

                Log.d("TAG", "MainActivity-complete: ");
            }

            @Override
            public void error(Throwable throwable) {

                Log.d("TAG", "error: ");
            }
        });

        mTest = new Test();
        mTest.setColloge("100");
        mTest.setName("小名");
        changeInt();
        System.out.println("mTest="+mTest.toString());
    }

    public void changeInt(){
        Test b = mTest;
//        b.setName("小李");
//        b.setColloge("99");
        System.out.println("test="+b);
    }
}