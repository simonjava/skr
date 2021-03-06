package com.common.rxretrofit;

import com.common.base.BaseActivity;
import com.common.base.BaseFragment;
import com.common.log.MyLog;
import com.common.mvp.PresenterEvent;
import com.common.mvp.RxLifeCyclePresenter;
import com.common.view.ex.RxLifecycleView;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.android.FragmentEvent;

import java.util.HashMap;

import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class ApiMethods {

    static HashMap<String, Disposable> sReqMap = new HashMap<>();

    /**
     * 订阅数据，自己处理生命周期
     *
     * @param observable
     * @param apiObserver
     * @param <T>
     * @return
     */
    public static <T> Disposable subscribe(Observable<T> observable, ApiObserver<T> apiObserver) {
        return innerSubscribe(observable, apiObserver, null,null);
    }

    public static <T> Disposable subscribe(Observable<T> observable, ApiObserver<T> apiObserver,final RequestControl requestControl) {
        return innerSubscribe(observable, apiObserver, null,requestControl);
    }

    /**
     * 订阅数据，并绑定View的生命周期，在onDetach中取消事件
     *
     * @param observable
     * @param apiObserver
     * @param view
     * @param <T>
     */
    public static <T> Disposable subscribe(Observable<T> observable, ApiObserver<T> apiObserver, RxLifecycleView view) {
        return innerSubscribe(observable, apiObserver, view.bindDetachEvent(),null);
    }

    public static <T> Disposable subscribe(Observable<T> observable, ApiObserver<T> apiObserver, RxLifecycleView view,final RequestControl requestControl) {
        return innerSubscribe(observable, apiObserver, view.bindDetachEvent(),requestControl);
    }

    /**
     * 订阅数据，并绑定BaseFragment的生命周期
     *
     * @param observable
     * @param apiObserver
     * @param baseFragment
     * @param <T>
     */
    public static <T> Disposable subscribe(Observable<T> observable, ApiObserver<T> apiObserver, BaseFragment baseFragment) {
        return innerSubscribe(observable, apiObserver, baseFragment.bindUntilEvent(FragmentEvent.DESTROY),null);
    }

    public static <T> Disposable subscribe(Observable<T> observable, ApiObserver<T> apiObserver, BaseFragment baseFragment,final RequestControl requestControl) {
        return innerSubscribe(observable, apiObserver, baseFragment.bindUntilEvent(FragmentEvent.DESTROY),requestControl);
    }

    /**
     * 订阅数据，并绑定BaseFragment的生命周期
     *
     * @param observable
     * @param apiObserver
     * @param baseActivity
     * @param <T>
     */
    public static <T> Disposable subscribe(Observable<T> observable, ApiObserver<T> apiObserver, BaseActivity baseActivity) {
        return innerSubscribe(observable, apiObserver, baseActivity.bindUntilEvent(ActivityEvent.DESTROY),null);
    }

    public static <T> Disposable subscribe(Observable<T> observable, ApiObserver<T> apiObserver, BaseActivity baseActivity,final RequestControl requestControl) {
        return innerSubscribe(observable, apiObserver, baseActivity.bindUntilEvent(ActivityEvent.DESTROY),requestControl);
    }

    /**
     * 订阅数据，并绑定RxLifeCyclePresenter的声明周期
     *
     * @param observable
     * @param apiObserver
     * @param presenter
     * @param <T>
     */
    public static <T> Disposable subscribe(Observable<T> observable, ApiObserver<T> apiObserver, RxLifeCyclePresenter presenter) {
        return innerSubscribe(observable, apiObserver, presenter.bindUntilEvent(PresenterEvent.DESTROY),null);
    }

    public static <T> Disposable subscribe(Observable<T> observable, ApiObserver<T> apiObserver, RxLifeCyclePresenter presenter,final RequestControl requestControl) {
        return innerSubscribe(observable, apiObserver, presenter.bindUntilEvent(PresenterEvent.DESTROY),requestControl);
    }

    private static <T> Disposable innerSubscribe(final Observable<T> observable, final ApiObserver<T> apiObserver, ObservableTransformer transformer, final RequestControl requestControl) {
        if (requestControl != null) {
            if (requestControl.controlType == ControlType.CancelLast) {
                // 取消上一次,继续
                Disposable lastDisposeable = sReqMap.get(requestControl.key);
                if (lastDisposeable != null && !lastDisposeable.isDisposed()) {
                    lastDisposeable.dispose();
                }
            } else if (requestControl.controlType == ControlType.CancelThis) {
                // 取消上一次,继续
                Disposable lastDisposeable = sReqMap.get(requestControl.key);
                if (lastDisposeable != null && !lastDisposeable.isDisposed()) {
                    MyLog.e("ApiMethods", "请求key=" + requestControl.key + "还在执行，取消这一次");
                    return null;
                }
            }
        }
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

            Disposable disposable = ob1.subscribe(new Consumer<T>() {
                @Override
                public void accept(T t) throws Exception {
                    observer.onNext(t);
                }
            }, new Consumer<Throwable>() {
                @Override
                public void accept(Throwable throwable) throws Exception {
                    observer.onError(throwable);
                    if (requestControl != null) {
                        sReqMap.remove(requestControl.key);
                    }
                }
            }, new Action() {
                @Override
                public void run() throws Exception {
                    observer.onComplete();
                    if (requestControl != null) {
                        sReqMap.remove(requestControl.key);
                    }
                }
            }, new Consumer<Disposable>() {
                @Override
                public void accept(Disposable disposable) throws Exception {
                    observer.onSubscribe(disposable);
                }
            });
            if (requestControl != null ) {
                sReqMap.put(requestControl.key, disposable);
            }
            return disposable;

        } else {
            Disposable disposable = ob1.subscribe(new Consumer<T>() {
                @Override
                public void accept(T t) throws Exception {
                    apiObserver.onNext(t);
                }
            }, new Consumer<Throwable>() {
                @Override
                public void accept(Throwable throwable) throws Exception {
                    apiObserver.onError(throwable);
                    if (requestControl != null) {
                        sReqMap.remove(requestControl.key);
                    }
                }
            }, new Action() {
                @Override
                public void run() throws Exception {
                    apiObserver.onComplete();
                    if (requestControl != null) {
                        sReqMap.remove(requestControl.key);
                    }
                }
            }, new Consumer<Disposable>() {
                @Override
                public void accept(Disposable disposable) throws Exception {
                    apiObserver.onSubscribe(disposable);
                }
            });
            if (requestControl != null ) {
                sReqMap.put(requestControl.key, disposable);
            }
            return disposable;
        }
    }

}
