package com.community.examplecode.rxjava.create;

import com.community.examplecode.rxjava.Emitter;
import com.community.examplecode.rxjava.ObserableOnSubscribe;
import com.community.examplecode.rxjava.Observable;
import com.community.examplecode.rxjava.Observe;

public class ObservableCreate<T> extends Observable<T> {


    ObserableOnSubscribe<T> obserableOnSubscribe;



    public ObservableCreate(ObserableOnSubscribe<T> obserableOnSubscribe) {
        this.obserableOnSubscribe = obserableOnSubscribe;
    }

    @Override
    protected void subscribeActul(Observe<T> observe) {//实现订阅
        observe.onSubscribe();
        CreateEmitter<T> emitter = new CreateEmitter<>(observe);
        obserableOnSubscribe.subscribe(emitter);

    }


    /**
     * 静态类创建一个发射器 与 观察者关联
     *
     * @param <T>
     */
    static class CreateEmitter<T> implements Emitter<T> {
        Observe<T> observe;
        private boolean done;
        public CreateEmitter(Observe<T> observe) {
            this.observe = observe;
        }

        @Override
        public void next(T t) {
            if (done) return;
            observe.next(t);
        }

        @Override
        public void complete() {
            if (done) return;
            observe.complete();
            done = true;
        }

        @Override
        public void error(Throwable throwable) {
            if (done) return;
            observe.error(throwable);
            done = true;
        }
    }
}
