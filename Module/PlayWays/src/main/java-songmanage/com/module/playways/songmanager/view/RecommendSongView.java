package com.module.playways.songmanager.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;

import com.alibaba.fastjson.JSONObject;
import com.common.log.MyLog;
import com.common.rxretrofit.ApiManager;
import com.common.rxretrofit.ApiMethods;
import com.common.rxretrofit.ApiObserver;
import com.common.rxretrofit.ApiResult;
import com.common.utils.U;
import com.common.view.ex.ExFrameLayout;
import com.common.view.ex.drawable.DrawableCreator;
import com.common.view.recyclerview.RecyclerOnItemClickListener;
import com.module.playways.R;
import com.module.playways.grab.room.GrabRoomServerApi;
import com.module.playways.songmanager.SongManagerActivity;
import com.module.playways.songmanager.adapter.RecommendSongAdapter;
import com.module.playways.songmanager.customgame.MakeGamePanelView;
import com.module.playways.songmanager.event.AddSongEvent;
import com.module.playways.songmanager.model.RecommendTagModel;
import com.module.playways.room.song.model.SongModel;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.zq.live.proto.Common.StandPlayType;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

/**
 * 推荐歌曲view
 */
public class RecommendSongView extends FrameLayout {
    public final static String TAG = "GrabSongManageView";

    ExFrameLayout mContainer;
    RecyclerView mRecyclerView;
    SmartRefreshLayout mRefreshLayout;

    int mType;
    boolean isOwner;
    int mGameID;

    private RecommendTagModel mRecommendTagModel;
    RecommendSongAdapter mRecommendSongAdapter;

    Disposable mDisposable;
    int mOffset = 0;
    int mLimit = 20;
    MakeGamePanelView mMakeGamePanelView;

    GrabRoomServerApi mGrabRoomServerApi;

    Drawable mDrawableBg = new DrawableCreator.Builder()
            .setSolidColor(U.getColor(R.color.white_trans_20))
            .setCornersRadius(0, 0, U.getDisplayUtils().dip2px(8f), U.getDisplayUtils().dip2px(8f))
            .build();

    public RecommendSongView(Context context, int type, boolean isOwner, int gameID, RecommendTagModel recommendTagModel) {
        super(context);
        this.mType = type;
        this.isOwner = isOwner;
        this.mGameID = gameID;
        this.mRecommendTagModel = recommendTagModel;
        initView();
    }

    public void initView() {
        inflate(getContext(), R.layout.recommend_song_view_layout, this);
        initData();
    }

    public void initData() {
        mContainer = findViewById(R.id.container);
        mRecyclerView = findViewById(R.id.recycler_view);
        mRefreshLayout = findViewById(R.id.refreshLayout);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        if (mType == SongManagerActivity.TYPE_FROM_GRAB) {
            mRecommendSongAdapter = new RecommendSongAdapter(isOwner, new RecyclerOnItemClickListener<SongModel>() {
                @Override
                public void onItemClicked(View view, int position, SongModel model) {
                    if (isOwner && model != null && model.getItemID() == SongModel.ID_CUSTOM_GAME) {
                        if (mMakeGamePanelView != null) {
                            mMakeGamePanelView.dismiss();
                        }
                        mMakeGamePanelView = new MakeGamePanelView(getContext());
                        mMakeGamePanelView.showByDialog(mGameID);
                    } else {
                        EventBus.getDefault().post(new AddSongEvent(model));
                    }
                }
            });
        } else {
            /**
             * 双人房默认是直接 点唱
             */
            mContainer.setBackground(mDrawableBg);
            mRecommendSongAdapter = new RecommendSongAdapter(true, new RecyclerOnItemClickListener<SongModel>() {
                @Override
                public void onItemClicked(View view, int position, SongModel model) {
                    EventBus.getDefault().post(new AddSongEvent(model));
                }
            });
        }

        mRecyclerView.setAdapter(mRecommendSongAdapter);
        mGrabRoomServerApi = ApiManager.getInstance().createService(GrabRoomServerApi.class);

        mRefreshLayout.setEnableRefresh(false);
        mRefreshLayout.setEnableLoadMore(true);
        mRefreshLayout.setEnableLoadMoreWhenContentNotFull(false);
        mRefreshLayout.setEnableOverScrollDrag(false);

        mRefreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                getSongList(mOffset);
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                getSongList(0);
            }
        });
        getSongList(0);
    }

    public void tryLoad() {
        if (mRecommendSongAdapter.getDataList().isEmpty()) {
            getSongList(0);
        }
    }

    private void getSongList(int offset) {
        if (mRecommendTagModel == null) {
            MyLog.e(TAG, "getSongList mRecommendTagModel is null");
            return;
        }

        if (mDisposable != null && !mDisposable.isDisposed()) {
            mDisposable.dispose();
        }
        mDisposable = ApiMethods.subscribe(getListStandBoardObservable(offset), new ApiObserver<ApiResult>() {
            @Override
            public void process(ApiResult result) {
                mRefreshLayout.finishLoadMore();

                if (result.getErrno() == 0) {
                    List<SongModel> recommendTagModelArrayList = JSONObject.parseArray(result.getData().getString("items"), SongModel.class);
                    mOffset = result.getData().getIntValue("offset");
                    if (recommendTagModelArrayList == null || recommendTagModelArrayList.size() == 0) {
                        mRefreshLayout.setEnableLoadMore(false);
                        return;
                    }
                    if (offset == 0) {
                        mRecommendSongAdapter.getDataList().clear();
                        if (mRecommendTagModel.getType() == 4 && isOwner) {
                            // 是双人游戏那一例
                            SongModel songModel = new SongModel();
                            songModel.setItemID(SongModel.ID_CUSTOM_GAME);
                            songModel.setPlayType(StandPlayType.PT_MINI_GAME_TYPE.getValue());
                            songModel.setItemName("自定义游戏");
                            mRecommendSongAdapter.getDataList().add(songModel);
                        }
                    }
                    mRecommendSongAdapter.getDataList().addAll(recommendTagModelArrayList);
                    mRecommendSongAdapter.notifyDataSetChanged();
                } else {
                    U.getToastUtil().showShort(result.getErrmsg() + "");
                }

            }
        });
    }

    private Observable<ApiResult> getListStandBoardObservable(int offset) {
        if (mType == SongManagerActivity.TYPE_FROM_GRAB) {
            return mGrabRoomServerApi.getListStandBoards(mRecommendTagModel.getType(), offset, mLimit);
        } else {
            return mGrabRoomServerApi.getDoubleListStandBoards(mRecommendTagModel.getType(), offset, mLimit);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        destroy();
    }

    public void destroy() {
        if (mDisposable != null && !mDisposable.isDisposed()) {
            mDisposable.dispose();
        }
    }
}
