package com.wali.live;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
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
import com.mi.live.data.account.channel.HostChannelManager;
import com.mi.live.data.api.LiveManager;
import com.mi.live.data.milink.event.MiLinkEvent;
import com.mi.live.data.repository.GiftRepository;
import com.mi.liveassistant.R;
import com.trello.rxlifecycle.ActivityEvent;
import com.wali.live.livesdk.live.LiveSdkActivity;
import com.wali.live.watchsdk.auth.AccountAuthManager;
import com.wali.live.watchsdk.channel.adapter.ChannelRecyclerAdapter;
import com.wali.live.watchsdk.channel.presenter.ChannelPresenter;
import com.wali.live.watchsdk.channel.presenter.IChannelPresenter;
import com.wali.live.watchsdk.channel.presenter.IChannelView;
import com.wali.live.watchsdk.channel.viewmodel.BaseViewModel;
import com.wali.live.watchsdk.channel.viewmodel.ChannelLiveViewModel;
import com.wali.live.watchsdk.login.LoginPresenter;
import com.wali.live.watchsdk.scheme.SchemeConstants;
import com.wali.live.watchsdk.scheme.SchemeUtils;
import com.wali.live.watchsdk.watch.VideoDetailSdkActivity;
import com.wali.live.watchsdk.watch.WatchSdkActivity;
import com.wali.live.watchsdk.watch.model.RoomInfo;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

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
    protected long mChannelId = 20;
    protected LoginPresenter mLoginPresenter;

    private ArrayList<RoomInfo> mRoomInfoList;

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
                if (mLoginPresenter == null) {
                    mLoginPresenter = new LoginPresenter(MainActivity.this);
                }
                mLoginPresenter.miLogin(HostChannelManager.getInstance().getChannelId());
            }
        });
        ($(R.id.replay_tv)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RoomInfo roomInfo = RoomInfo.Builder.newInstance(101743, "101743_1471260348",
                        "http://playback.ks.zb.mi.com/record/live/101743_1471260348/hls/101743_1471260348.m3u8?playui=1")
                        .setLiveType(6)
                        .setEnableShare(true)
                        .build();
                VideoDetailSdkActivity.openActivity(MainActivity.this, roomInfo);
            }
        });

        ($(R.id.channel_tv)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getChannelById(201);
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
                String input = mInputEditText.getText().toString();
                if (TextUtils.isEmpty(input)) {
                    ToastUtils.showToast("主播id不能为空");
                    return;
                }
                RoomInfo roomInfo = RoomInfo.Builder.newInstance(Long.parseLong(input), null, null)
                        .setLiveType(0)
                        .build();
                WatchSdkActivity.openActivity(MainActivity.this, roomInfo);
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

        if (mRoomInfoList == null) {
            mRoomInfoList = new ArrayList<>();
        }
        mRoomInfoList.clear();
        int i = 0;
        for (BaseViewModel model : models) {
            if (model instanceof ChannelLiveViewModel) {
                List<ChannelLiveViewModel.BaseItem> items = ((ChannelLiveViewModel) model).getItemDatas();
                for (ChannelLiveViewModel.BaseItem item : items) {
                    if (item instanceof ChannelLiveViewModel.LiveItem) {
                        ChannelLiveViewModel.LiveItem liveItem = (ChannelLiveViewModel.LiveItem) item;
                        Uri uri = Uri.parse(liveItem.getSchemeUri());

                        long playerId = Long.parseLong(uri.getQueryParameter("playerid"));
                        String liveId = uri.getQueryParameter("liveid");
                        String videoUrl = uri.getQueryParameter("videourl");

                        int type = SchemeUtils.getInt(uri, SchemeConstants.PARAM_TYPE, 0);
                        int liveType = LiveManager.mapLiveTypeFromListToRoom(type);

                        RoomInfo roomInfo = RoomInfo.Builder.newInstance(playerId, liveId, videoUrl)
                                .setAvatar(liveItem.getUser().getAvatar())
                                .setCoverUrl(liveItem.getImageUrl())
                                .setLiveType(liveType)
                                .build();

                        if (i % 2 == 0) {
                            roomInfo.setGameId("47631");
                            roomInfo.setEnableShare(true);
                        }
                        mRoomInfoList.add(roomInfo);
                        i++;
                    }
                }
            }
        }
        MyLog.d(TAG, "size=" + mRoomInfoList.size());
        WatchSdkActivity.openActivity(this, mRoomInfoList, 0);
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

    public void getChannelById(int channelId) {
        String uri = "livesdk://channel?channel_id=" + channelId;
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        intent.putExtra("extra_channel_id", 50001);
        intent.putExtra("extra_package_name", "com.wali.live.sdk.manager.demo");
        startActivity(intent);
    }
}
