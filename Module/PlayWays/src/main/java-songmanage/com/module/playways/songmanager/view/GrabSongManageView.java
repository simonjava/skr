package com.module.playways.songmanager.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.common.log.MyLog;
import com.common.utils.U;
import com.common.view.ex.ExTextView;
import com.component.busilib.friends.SpecialModel;
import com.module.playways.R;
import com.module.playways.songmanager.SongManageData;
import com.module.playways.songmanager.adapter.ManageSongAdapter;
import com.module.playways.songmanager.event.SongNumChangeEvent;
import com.module.playways.songmanager.model.ChangeTagSuccessEvent;
import com.module.playways.songmanager.model.GrabRoomSongModel;
import com.module.playways.songmanager.presenter.BaseSongManagePresenter;
import com.module.playways.songmanager.presenter.DoubleSongManagePresenter;
import com.module.playways.songmanager.presenter.GrabSongManagePresenter;
import com.module.playways.songmanager.adapter.GrabTagsAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * 房主已点
 */
public class GrabSongManageView extends FrameLayout implements IGrabSongManageView {
    public final static String TAG = "GrabSongManageView";

    SongManageData mSongManageData;

    SmartRefreshLayout mRefreshLayout;

    RecyclerView mRecyclerView;

    ExTextView mTvSelectedTag;

    FrameLayout mTopTagView;

    ImageView mIvArrow;

    ManageSongAdapter mManageSongAdapter;

    BaseSongManagePresenter mGrabSongManagePresenter;

    GrabSongTagsView mGrabSongTagsView;

    PopupWindow mPopupWindow;

    int mSpecialModelId;

    public GrabSongManageView(Context context, SongManageData grabRoomData) {
        super(context);
        mSongManageData = grabRoomData;
        initView();
    }

    public void initView() {
        inflate(getContext(), R.layout.grab_song_manage_view_layout, this);
        initData();
    }

    public void initData() {
        if (mSongManageData.isGrabRoom()) {
            mGrabSongManagePresenter = new GrabSongManagePresenter(this, mSongManageData.getGrabRoomData());
        } else {
            mGrabSongManagePresenter = new DoubleSongManagePresenter(this, mSongManageData);
        }

        mIvArrow = (ImageView) findViewById(R.id.iv_arrow);
        mRefreshLayout = (SmartRefreshLayout) findViewById(R.id.refreshLayout);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mTvSelectedTag = findViewById(R.id.selected_tag);
        mTopTagView = (FrameLayout) findViewById(R.id.top_tag_view);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mManageSongAdapter = new ManageSongAdapter();
        mRecyclerView.setAdapter(mManageSongAdapter);

        mRefreshLayout.setEnableRefresh(false);
        mRefreshLayout.setEnableLoadMore(true);
        mRefreshLayout.setEnableLoadMoreWhenContentNotFull(true);
        mRefreshLayout.setEnableOverScrollDrag(false);
        mRefreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                mGrabSongManagePresenter.getPlayBookList();
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mGrabSongManagePresenter.getPlayBookList();
            }
        });

        DefaultItemAnimator defaultItemAnimator = new DefaultItemAnimator();
        defaultItemAnimator.setAddDuration(50);
        defaultItemAnimator.setRemoveDuration(50);
        mRecyclerView.setItemAnimator(defaultItemAnimator);

        initListener();

        if (mSongManageData.isGrabRoom()) {
            if (mSongManageData.getSpecialModel() != null) {
                setTagTv(mSongManageData.getSpecialModel());
            }
        } else {
            mTopTagView.setVisibility(GONE);
        }

        mGrabSongManagePresenter.getPlayBookList();
    }

    public void tryLoad() {
        if (mManageSongAdapter.getDataList().isEmpty()) {
            mGrabSongManagePresenter.getPlayBookList();
        }
    }

    private void initListener() {
        if (mSongManageData.isGrabRoom()) {
            mTvSelectedTag.setOnClickListener(v -> {
                if (mGrabSongTagsView == null) {
                    mGrabSongTagsView = new GrabSongTagsView(getContext());

                    mGrabSongTagsView.setOnTagClickListener(new GrabTagsAdapter.OnTagClickListener() {
                        @Override
                        public void onClick(SpecialModel specialModel) {
                            mGrabSongManagePresenter.changeMusicTag(specialModel, mSongManageData.getGameId());
                        }

                        @Override
                        public void dismissDialog() {
                            if (mPopupWindow != null) {
                                mPopupWindow.dismiss();
                            }
                        }
                    });
                    mPopupWindow = new PopupWindow(mGrabSongTagsView);
                    mPopupWindow.setWidth(mTvSelectedTag.getWidth());
                    mPopupWindow.setOutsideTouchable(true);
                    mPopupWindow.setFocusable(true);

                    MyLog.d(TAG, "initListener Build.VERSION.SDK_INT " + Build.VERSION.SDK_INT);
                    if (Build.VERSION.SDK_INT < 23) {
                        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
                    }

                    mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                        @Override
                        public void onDismiss() {
                            mIvArrow.setBackground(U.getDrawable(R.drawable.fz_shuxing_xia));
                        }
                    });
                }

                mGrabSongTagsView.setCurSpecialModel(mSpecialModelId);

                if (mPopupWindow != null && mPopupWindow.isShowing()) {
                    mPopupWindow.dismiss();
                } else {
                    mGrabSongManagePresenter.getTagList();
                }
            });
        }

        mManageSongAdapter.setOnClickDeleteListener(grabRoomSongModel -> {
            mGrabSongManagePresenter.deleteSong(grabRoomSongModel);
        });

        mManageSongAdapter.setGrabRoomData(mSongManageData);
    }

    private void setTagTv(SpecialModel specialModel) {
        mSongManageData.setSpecialModel(specialModel);
        mSongManageData.setTagId(specialModel.getTagID());

        mSpecialModelId = specialModel.getTagID();
        mTvSelectedTag.setText(specialModel.getTagName());
        if (!TextUtils.isEmpty(specialModel.getBgColor())) {
            mTvSelectedTag.setTextColor(Color.parseColor(specialModel.getBgColor()));
        }
    }

    @Override
    public void changeTagSuccess(SpecialModel specialModel) {
        setTagTv(specialModel);
        EventBus.getDefault().post(new ChangeTagSuccessEvent(specialModel));
        if (mPopupWindow != null) {
            mPopupWindow.dismiss();
        }
    }

    @Override
    public void showNum(int num) {
        if (num < 0) {
            mGrabSongManagePresenter.getPlayBookList();
            return;
        }

        EventBus.getDefault().post(new SongNumChangeEvent(num));

    }

    @Override
    public void deleteSong(GrabRoomSongModel grabRoomSongModel) {
        mManageSongAdapter.deleteSong(grabRoomSongModel);
    }

    @Override
    public void hasMoreSongList(boolean hasMore) {
        mRefreshLayout.setEnableLoadMore(hasMore);
        mRefreshLayout.finishLoadMore();
    }

    @Override
    public void showTagList(List<SpecialModel> specialModelList) {
        int height = U.getDisplayUtils().dip2px(specialModelList.size() > 4 ? 190 : (55 * (specialModelList.size() - 1)));
        if (mGrabSongTagsView != null) {
            mGrabSongTagsView.setSpecialModelList(specialModelList);
            mPopupWindow.setHeight(height);
        }

        int[] location = new int[2];
        mTvSelectedTag.getLocationOnScreen(location);
//        mPopupWindow.showAtLocation(mTvSelectedTag, Gravity.NO_GRAVITY, location[0], location[1] - height - U.getDisplayUtils().dip2px(5));
        mPopupWindow.showAsDropDown(mTvSelectedTag);
        mIvArrow.setBackground(U.getDrawable(R.drawable.fz_shuxing_shang));
    }

    @Override
    public void updateSongList(List<GrabRoomSongModel> grabRoomSongModelsList) {
        mManageSongAdapter.setDataList(grabRoomSongModelsList);
    }

    public void destroy() {
        mGrabSongManagePresenter.destroy();
    }
}
