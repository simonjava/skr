package com.module.playways.grab.room.invite;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;

import com.common.base.BaseFragment;
import com.common.core.userinfo.UserInfoManager;
import com.kingja.loadsir.callback.Callback;
import com.kingja.loadsir.core.LoadService;
import com.kingja.loadsir.core.LoadSir;
import com.module.playways.R;
import com.module.playways.grab.room.inter.IGrabInviteView;
import com.module.playways.grab.room.model.GrabFriendModel;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.zq.relation.callback.FansEmptyCallback;
import com.zq.relation.callback.FriendsEmptyCallback;

import java.util.List;

public class InviteFriendView extends RelativeLayout implements IGrabInviteView {

    SmartRefreshLayout mRefreshLayout;
    RecyclerView mRecyclerView;
    InviteFirendAdapter mInviteFirendAdapter;

    private int mMode = UserInfoManager.RELATION_FRIENDS;
    private int mOffset = 0; // 偏移量
    private int DEFAULT_COUNT = 30; // 每次拉去最大值
    private boolean mHasMore = true;

    GrabInvitePresenter mGrabInvitePresenter;
    BaseFragment mBaseFragment;
    int mRoomID;
    LoadService mLoadService;

    public InviteFriendView(BaseFragment fragment, int roomID, int mode) {
        super(fragment.getContext());
        init(fragment, roomID, mode);
    }

    private void init(BaseFragment fragment, int roomID, int mode) {
        this.mBaseFragment = fragment;
        this.mRoomID = roomID;
        this.mMode = mode;
        inflate(fragment.getContext(), R.layout.invite_view_layout, this);

        mRefreshLayout = (SmartRefreshLayout) findViewById(R.id.refreshLayout);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        mInviteFirendAdapter = new InviteFirendAdapter(new InviteFirendAdapter.OnInviteClickListener() {
            @Override
            public void onClick(GrabFriendModel model) {
                mGrabInvitePresenter.inviteFriend(mRoomID, model);
            }
        });
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mInviteFirendAdapter);

        mGrabInvitePresenter = new GrabInvitePresenter(mBaseFragment, this);

        mRefreshLayout.setEnableRefresh(false);
        mRefreshLayout.setEnableLoadMore(true);
        mRefreshLayout.setEnableLoadMoreWhenContentNotFull(true);
        mRefreshLayout.setEnableOverScrollDrag(false);
        mRefreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                if (mMode == UserInfoManager.RELATION_FRIENDS) {
                    mGrabInvitePresenter.getFriendList(mOffset, DEFAULT_COUNT);
                } else if (mMode == UserInfoManager.RELATION_FANS) {
                    mGrabInvitePresenter.getFansList(mOffset, DEFAULT_COUNT);
                }
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mRefreshLayout.finishRefresh();
            }
        });

        LoadSir mLoadSir = new LoadSir.Builder()
                .addCallback(new FriendsEmptyCallback())
                .addCallback(new FansEmptyCallback())
                .build();
        mLoadService = mLoadSir.register(mRefreshLayout, new Callback.OnReloadListener() {
            @Override
            public void onReload(View v) {
                syncInviteMode();
            }
        });

        syncInviteMode();
    }

    private void syncInviteMode() {
        if (mMode == UserInfoManager.RELATION_FRIENDS) {
            mGrabInvitePresenter.getFriendList(0, DEFAULT_COUNT);
        } else if (mMode == UserInfoManager.RELATION_FANS) {
            mGrabInvitePresenter.getFansList(0, DEFAULT_COUNT);
        }
    }

    @Override
    public void addInviteModelList(List<GrabFriendModel> grabFriendModelList, int newOffset) {
        this.mOffset = newOffset;
        if (grabFriendModelList != null && grabFriendModelList.size() > 0) {
            mHasMore = true;
            mRefreshLayout.setEnableLoadMore(true);

            mLoadService.showSuccess();
            mInviteFirendAdapter.getDataList().addAll(grabFriendModelList);
            mInviteFirendAdapter.notifyDataSetChanged();
        } else {
            mHasMore = false;
            mRefreshLayout.setEnableLoadMore(false);

            if (mInviteFirendAdapter.getDataList() == null || mInviteFirendAdapter.getDataList().size() == 0) {
                // 没有数据
                if (mMode == UserInfoManager.RELATION_FRIENDS) {
                    mLoadService.showCallback(FriendsEmptyCallback.class);
                } else if (mMode == UserInfoManager.RELATION_FANS) {
                    mLoadService.showCallback(FansEmptyCallback.class);
                }
            } else {
                // 没有更多了
            }
        }
    }

    @Override
    public void updateInviteModel(GrabFriendModel grabFriendModel) {
        mInviteFirendAdapter.update(grabFriendModel);
    }


    @Override
    public void finishRefresh() {
        mRefreshLayout.finishLoadMore();
    }
}