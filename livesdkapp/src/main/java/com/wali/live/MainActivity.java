package com.wali.live;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.base.activity.BaseSdkActivity;
import com.base.log.MyLog;
import com.base.utils.toast.ToastUtils;
import com.mi.live.data.account.XiaoMiOAuth;
import com.mi.live.data.data.LiveShow;
import com.mi.live.data.manager.UserInfoManager;
import com.mi.live.data.milink.event.MiLinkEvent;
import com.mi.live.data.repository.GiftRepository;
import com.mi.liveassistant.R;
import com.trello.rxlifecycle.ActivityEvent;
import com.wali.live.channel.adapter.ChannelRecyclerAdapter;
import com.wali.live.channel.presenter.ChannelPresenter;
import com.wali.live.channel.presenter.IChannelPresenter;
import com.wali.live.channel.presenter.IChannelView;
import com.wali.live.channel.viewmodel.BaseViewModel;
import com.wali.live.livesdk.live.LiveSdkActivity;
import com.wali.live.watchsdk.auth.AccountAuthManager;
import com.wali.live.watchsdk.login.LoginPresenter;
import com.wali.live.watchsdk.watch.VideoDetailSdkActivity;
import com.wali.live.watchsdk.watch.WatchSdkActivity;
import com.wali.live.watchsdk.watch.model.RoomInfo;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

//import com.wali.live.livesdk.live.LiveSdkActivity;

/**
 * Created by lan on 16/11/25.
 */
@Deprecated
public class MainActivity extends BaseSdkActivity implements IChannelView {
    protected SwipeRefreshLayout mRefreshLayout;
    protected RecyclerView mRecyclerView;
    protected LinearLayoutManager mLayoutManager;
    protected ChannelRecyclerAdapter mRecyclerAdapter;
    protected EditText mInputEditText;

    protected IChannelPresenter mPresenter;
    protected long mChannelId = 201;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        initPresenters();

        initData();
        getChannelFromServer();

        $(R.id.show_live_tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AccountAuthManager.triggerActionNeedAccount(MainActivity.this)) {
                    LiveSdkActivity.openActivity(MainActivity.this, null, false, false);
                }
            }
        });

        $(R.id.game_live_tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AccountAuthManager.triggerActionNeedAccount(MainActivity.this)) {
                    LiveSdkActivity.openActivity(MainActivity.this, null, false, true);
                }
            }
        });

        ($(R.id.login_tv)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Observable.just(0).map(new Func1<Integer, Boolean>() {
                    @Override
                    public Boolean call(Integer integer) {
                        String code = XiaoMiOAuth.getOAuthCode(MainActivity.this);
                        if (!TextUtils.isEmpty(code)) {
                            LoginPresenter loginPresenter = new LoginPresenter(MainActivity.this);
                            loginPresenter.miLoginByCode(code);
                            addPresent(loginPresenter);
                            return true;
                        }
                        return false;
                    }
                }).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Action1<Boolean>() {
                            @Override
                            public void call(Boolean b) {
                                MyLog.w(TAG, "result = " + b);
                            }
                        }, new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {
                                throwable.printStackTrace();
                                MyLog.w(TAG, "failed " + throwable);
                            }
                        });
            }
        });
        ($(R.id.replay_tv)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RoomInfo roomInfo = RoomInfo.Builder.newInstance(101743, "101743_1471260348", "http://playback.ks.zb.mi.com/record/live/101743_1471260348/hls/101743_1471260348.m3u8?playui=1")
                        .setLiveType(6)
                        .setEnableShare(true)
                        .build();
                VideoDetailSdkActivity.openActivity(MainActivity.this, roomInfo);
            }
        });

    }

    private void initData() {
        syncGiftList();
    }

    private void syncGiftList() {
        Observable
                .create(new Observable.OnSubscribe<Object>() {
                    @Override
                    public void call(Subscriber<? super Object> subscriber) {
                        GiftRepository.syncGiftList();
                        subscriber.onCompleted();
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(this.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(Object o) {
                    }
                });
    }

    private void initViews() {
        mRefreshLayout = $(R.id.swipe_refresh_layout);
        mRecyclerView = $(R.id.recycler_view);

        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                doRefresh();
            }
        });
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mRecyclerAdapter = new ChannelRecyclerAdapter(this, mChannelId);
        mRecyclerView.setAdapter(mRecyclerAdapter);

        mInputEditText = $(R.id.live_input_tv);

        $(R.id.watch_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(mInputEditText.getText())) {
                    ToastUtils.showToast("主播id不能为空");
                    return;
                }
                Observable.just(0).map(new Func1<Integer, Object>() {
                    @Override
                    public Object call(Integer integer) {
                        LiveShow liveShow = UserInfoManager.getLiveShowByUserId(Long.parseLong(mInputEditText.getText().toString()));
                        if (liveShow == null) {
                            return null;
                        }
                        RoomInfo roomInfo = RoomInfo.Builder.newInstance(liveShow.getUid(), liveShow.getLiveId(), liveShow.getUrl())
                                .setAvatar(liveShow.getAvatar())
                                .setCoverUrl(liveShow.getCoverUrl())
                                .setLiveType(0)
                                .build();
                        WatchSdkActivity.openActivity(MainActivity.this, roomInfo);
                        return null;
                    }
                }).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Action1<Object>() {
                            @Override
                            public void call(Object o) {

                            }
                        }, new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {
                                MyLog.e(TAG, throwable);
                            }
                        });

            }
        });
    }

    private void initPresenters() {
        mPresenter = new ChannelPresenter(this, this);
        mPresenter.setChannelId(mChannelId);
    }

    private void getChannelFromServer() {
        mPresenter.start();
    }

    @Override
    public void updateView(List<? extends BaseViewModel> models) {
        mRecyclerAdapter.setData(models);
    }

    @Override
    public void finishRefresh() {
        mRefreshLayout.setRefreshing(false);
    }

    @Override
    public void doRefresh() {
        getChannelFromServer();
        mRefreshLayout.setRefreshing(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.stop();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(MiLinkEvent.StatusConnected event) {
        MyLog.d(TAG, "milink is connected");
        if (event != null) {
            getChannelFromServer();
        }
    }

    public static void openActivity(@NonNull Activity activity) {
        Intent intent = new Intent(activity, MainActivity.class);
        activity.startActivity(intent);
    }
}
