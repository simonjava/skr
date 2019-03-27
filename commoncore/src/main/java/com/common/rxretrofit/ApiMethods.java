package com.common.rxretrofit;

import com.common.base.BaseActivity;
import com.common.base.BaseFragment;
import com.common.mvp.PresenterEvent;
import com.common.mvp.RxLifeCyclePresenter;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.android.FragmentEvent;

import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class ApiMethods {

    private static <T> void innerSubscribe(Observable<T> observable, ApiObserver<T> apiObserver, ObservableTransformer transformer) {
        Observable<T> ob1 = observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io());
        if (transformer != null) {
            ob1 = ob1.compose(transformer);
        }
        ob1 = ob1.observeOn(AndroidSchedulers.mainThread());
        if (apiObserver == null) {
            // 给个默认的实现，防止崩溃
            ob1.subscribe(new ApiObserver<T>() {
                @Override
                public void process(T obj) {

                }
            });
        } else {
            ob1.subscribe(apiObserver);
        }
    }

    /**
     * 订阅数据
     *
     * @param observable
     * @param apiObserver
     * @param <T>
     */
    public static <T> void subscribe(Observable<T> observable, ApiObserver<T> apiObserver) {
        innerSubscribe(observable, apiObserver, null);
    }

    /**
     * 订阅数据，并绑定BaseFragment的生命周期
     *
     * @param observable
     * @param apiObserver
     * @param baseFragment
     * @param <T>
     */
    public static <T> void subscribe(Observable<T> observable, ApiObserver<T> apiObserver, BaseFragment baseFragment) {
        innerSubscribe(observable, apiObserver, baseFragment.bindUntilEvent(FragmentEvent.DESTROY));
    }

    /**
     * 订阅数据，并绑定BaseFragment的生命周期
     *
     * @param observable
     * @param apiObserver
     * @param baseFragment
     * @param <T>
     */
    public static <T> Disposable subscribeWith(Observable<T> observable, ApiObserver<T> apiObserver, BaseFragment baseFragment) {
        return innerSubscribeWith(observable, apiObserver, baseFragment.bindUntilEvent(FragmentEvent.DESTROY));
    }

    /**
     * 订阅数据，自己处理生命周期
     * @param observable
     * @param apiObserver
     * @param <T>
     * @return
     */
    public static <T> Disposable subscribeWith(Observable<T> observable, ApiObserver<T> apiObserver) {
        return innerSubscribeWith(observable, apiObserver, null);
    }

    /**
     * 订阅数据，并绑定BaseFragment的生命周期
     *
     * @param observable
     * @param apiObserver
     * @param baseActivity
     * @param <T>
     */
    public static <T> void subscribe(Observable<T> observable, ApiObserver<T> apiObserver, BaseActivity baseActivity) {
        innerSubscribe(observable, apiObserver, baseActivity.bindUntilEvent(ActivityEvent.DESTROY));
    }

    /**
     * 订阅数据，并绑定BaseFragment的生命周期
     *
     * @param observable
     * @param apiObserver
     * @param baseActivity
     * @param <T>
     */
    public static <T> Disposable subscribeWith(Observable<T> observable, ApiObserver<T> apiObserver, BaseActivity baseActivity) {
        return innerSubscribeWith(observable, apiObserver, baseActivity.bindUntilEvent(ActivityEvent.DESTROY));
    }

    /**
     * 订阅数据，并绑定RxLifeCyclePresenter的声明周期
     *
     * @param observable
     * @param apiObserver
     * @param presenter
     * @param <T>
     */
    public static <T> void subscribe(Observable<T> observable, ApiObserver<T> apiObserver, RxLifeCyclePresenter presenter) {
        innerSubscribe(observable, apiObserver, presenter.bindUntilEvent(PresenterEvent.DESTROY));
    }

    /**
     * 订阅数据，并绑定RxLifeCyclePresenter的声明周期
     *
     * @param observable
     * @param apiObserver
     * @param presenter
     * @param <T>
     */
    public static <T> Disposable subscribeWith(Observable<T> observable, ApiObserver<T> apiObserver, RxLifeCyclePresenter presenter) {
        return innerSubscribeWith(observable, apiObserver, presenter.bindUntilEvent(PresenterEvent.DESTROY));
    }

    private static <T> Disposable innerSubscribeWith(Observable<T> observable, final ApiObserver<T> apiObserver, ObservableTransformer transformer) {
        Observable<T> ob1 = observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io());
        if (transformer != null) {
            ob1 = ob1.compose(transformer);
        }

        ob1 = ob1.observeOn(AndroidSchedulers.mainThread());
        if (apiObserver == null) {
            // 给个默认的实现，防止崩溃
            final ApiObserver observer = new ApiObserver() {
                @Override
                public void process(Object obj) {

                }
            };

            return ob1.subscribe(new Consumer<T>() {
                @Override
                public void accept(T t) throws Exception {
                    observer.onNext(t);
                }
            }, new Consumer<Throwable>() {
                @Override
                public void accept(Throwable throwable) throws Exception {
                    observer.onError(throwable);
                }
            });
        } else {
            return ob1.subscribe(new Consumer<T>() {
                @Override
                public void accept(T t) throws Exception {
                    apiObserver.onNext(t);
                }
            }, new Consumer<Throwable>() {
                @Override
                public void accept(Throwable throwable) throws Exception {
                    apiObserver.onError(throwable);
                }
            }, new Action() {
                @Override
                public void run() throws Exception {
                    apiObserver.onComplete();
                }
            }, new Consumer<Disposable>() {
                @Override
                public void accept(Disposable disposable) throws Exception {
                    apiObserver.onSubscribe(disposable);
                }
            });
        }
    }
}
