package com.module.playways.grab.room.songmanager.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.common.base.BaseFragment;
import com.common.utils.U;
import com.common.view.ex.ExTextView;
import com.component.busilib.friends.SpecialModel;
import com.module.playways.R;
import com.module.playways.grab.room.GrabRoomData;
import com.module.playways.grab.room.inter.IGrabSongManageView;
import com.module.playways.grab.room.songmanager.model.GrabRoomSongModel;
import com.module.playways.grab.room.songmanager.presenter.GrabSongManagePresenter;
import com.module.playways.grab.room.songmanager.adapter.ManageSongAdapter;
import com.module.playways.grab.room.songmanager.event.SongNumChangeEvent;
import com.module.playways.grab.room.songmanager.tags.GrabSongTagsView;
import com.module.playways.grab.room.songmanager.tags.GrabTagsAdapter;
import com.orhanobut.dialogplus.DialogPlus;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class GrabSongManageFragment extends BaseFragment implements IGrabSongManageView {
    public final static String TAG = "GrabSongManageFragment";

    GrabRoomData mRoomData;

    SmartRefreshLayout mRefreshLayout;

    RecyclerView mRecyclerView;

    ExTextView mTvSelectedTag;

    ImageView mIvArrow;

    ManageSongAdapter mManageSongAdapter;

    GrabSongManagePresenter mGrabSongManagePresenter;

    GrabSongTagsView mGrabSongTagsView;

    PopupWindow mPopupWindow;

    Handler mUiHandler = new Handler();

    int mSpecialModelId;

    @Override
    public int initView() {
        return R.layout.grab_song_manage_fragment_layout;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        mGrabSongManagePresenter = new GrabSongManagePresenter(this, mRoomData);
        addPresent(mGrabSongManagePresenter);

        mIvArrow = (ImageView) mRootView.findViewById(R.id.iv_arrow);
        mRefreshLayout = (SmartRefreshLayout) mRootView.findViewById(R.id.refreshLayout);
        mRecyclerView = (RecyclerView) mRootView.findViewById(R.id.recycler_view);
        mTvSelectedTag = mRootView.findViewById(R.id.selected_tag);

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

            }
        });

        DefaultItemAnimator defaultItemAnimator = new DefaultItemAnimator();
        defaultItemAnimator.setAddDuration(50);
        defaultItemAnimator.setRemoveDuration(50);
        mRecyclerView.setItemAnimator(defaultItemAnimator);

        initListener();

        if (mRoomData.getSpecialModel() != null) {
            setTagTv(mRoomData.getSpecialModel());
        }

        mUiHandler.postDelayed(() -> {
            TranslateAnimation animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.0f,
                    Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0);
            animation.setDuration(400);
            animation.setRepeatMode(Animation.REVERSE);
            animation.setFillAfter(true);
        }, 50);

        mGrabSongManagePresenter.getPlayBookList();
    }

    private void setTagTv(SpecialModel specialModel) {
        mRoomData.setSpecialModel(specialModel);
        mRoomData.setTagId(specialModel.getTagID());

        mSpecialModelId = specialModel.getTagID();
        mTvSelectedTag.setText(specialModel.getTagName());
        mTvSelectedTag.setTextColor(Color.parseColor(specialModel.getBgColor()));
    }

    @Override
    public void changeTagSuccess(SpecialModel specialModel) {
        setTagTv(specialModel);
        if (mPopupWindow != null) {
            mPopupWindow.dismiss();
        }
    }

    @Override
    public void showNum(int num) {
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

    private void initListener() {
        mTvSelectedTag.setOnClickListener(v -> {
            if (mGrabSongTagsView == null) {
                mGrabSongTagsView = new GrabSongTagsView(getContext());

                mGrabSongTagsView.setOnTagClickListener(new GrabTagsAdapter.OnTagClickListener() {
                    @Override
                    public void onClick(SpecialModel specialModel) {
                        mGrabSongManagePresenter.changeMusicTag(specialModel, mRoomData.getGameId());
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

                mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        mIvArrow.setBackground(U.getDrawable(R.drawable.fz_shuxing_xia));
                    }
                });
            }

            mGrabSongTagsView.setCurSpecialModel(mSpecialModelId);

            if (mPopupWindow.isShowing()) {
                mPopupWindow.dismiss();
            } else {
                mGrabSongManagePresenter.getTagList();
            }
        });

        mManageSongAdapter.setOnClickDeleteListener(grabRoomSongModel -> {
            mGrabSongManagePresenter.deleteSong(grabRoomSongModel);
        });

        mManageSongAdapter.setGrabRoomData(mRoomData);
    }

    @Override
    public void destroy() {
        super.destroy();
    }

    @Override
    public void setData(int type, @Nullable Object data) {
        super.setData(type, data);
        if (type == 0) {
            mRoomData = (GrabRoomData) data;
        }
    }

    @Override
    protected boolean onBackPressed() {
        return super.onBackPressed();
    }

    @Override
    public boolean useEventBus() {
        return false;
    }
}