package com.wali.live.watchsdk.videodetail.presenter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Pair;
import android.view.View;

import com.base.log.MyLog;
import com.mi.live.data.room.model.RoomBaseDataModel;
import com.wali.live.component.presenter.ComponentPresenter;
import com.wali.live.watchsdk.R;
import com.wali.live.watchsdk.videodetail.view.DetailCommentView;
import com.wali.live.watchsdk.videodetail.view.DetailReplayView;
import com.wali.live.watchsdk.videodetail.view.DetailTabView;

import java.util.ArrayList;
import java.util.List;

import static com.wali.live.component.ComponentController.MSG_COMMENT_TOTAL_CNT;
import static com.wali.live.component.ComponentController.MSG_FOLD_INFO_AREA;
import static com.wali.live.component.ComponentController.MSG_REPLAY_TOTAL_CNT;

/**
 * Created by yangli on 2017/06/02.
 * <p>
 * Generated using create_component_view.py
 *
 * @module 详情TAB表现
 */
public class DetailTabPresenter extends ComponentPresenter<DetailTabView.IView>
        implements DetailTabView.IPresenter {
    private static final String TAG = "DetailTabPresenter";

    private RoomBaseDataModel mMyRoomData;

    private final DetailCommentPresenter mCommentPresenter;
    private final DetailReplayPresenter mReplayPresenter;

    private DetailCommentView mCommentView;
    private DetailReplayView mReplayView;

    public DetailTabPresenter(
            @NonNull IComponentController componentController,
            @NonNull RoomBaseDataModel roomData) {
        super(componentController);
        mMyRoomData = roomData;
        mCommentPresenter = new DetailCommentPresenter(mComponentController, mMyRoomData);
        mReplayPresenter = new DetailReplayPresenter(mComponentController, mMyRoomData);
    }

    @Override
    public void startPresenter() {
        registerAction(MSG_COMMENT_TOTAL_CNT);
        registerAction(MSG_REPLAY_TOTAL_CNT);
        registerAction(MSG_FOLD_INFO_AREA);
        mCommentPresenter.startPresenter();
        mReplayPresenter.startPresenter();
    }

    @Override
    public void stopPresenter() {
        super.stopPresenter();
        mCommentPresenter.stopPresenter();
        mReplayPresenter.stopPresenter();
    }

    @Override
    public void destroy() {
        super.destroy();
        mCommentPresenter.destroy();
        mReplayPresenter.destroy();
    }

    @Nullable
    @Override
    protected IAction createAction() {
        return new Action();
    }

    @Override
    public void syncTabPageList(Context context) {
        List<Pair<String, ? extends View>> tabPageList = new ArrayList<>();
        if (mCommentView == null) {
            mCommentView = new DetailCommentView(context);
            mCommentPresenter.setComponentView(mCommentView.getViewProxy());
            mCommentView.setPresenter(mCommentPresenter);
        }
        tabPageList.add(Pair.create(String.format(context.getResources().getString(
                R.string.feeds_detail_label_comment), "0"), mCommentView));

        if (mReplayView == null) {
            mReplayView = new DetailReplayView(context);
            mReplayPresenter.setComponentView(mReplayView.getViewProxy());
            mReplayView.setMyRoomData(mMyRoomData);
            mReplayView.setPresenter(mReplayPresenter);
        }
        tabPageList.add(Pair.create(String.format(context.getResources().getString(
                R.string.feeds_detail_label_replay), "0"), mReplayView));

        mView.onTabPageList(tabPageList);
    }

    public class Action implements IAction {
        @Override
        public boolean onAction(int source, @Nullable Params params) {
            if (mView == null) {
                MyLog.e(TAG, "onAction but mView is null, source=" + source);
                return false;
            }
            switch (source) {
                case MSG_COMMENT_TOTAL_CNT:
                    mView.updateCommentTotalCnt((int) params.getItem(0));
                    break;
                case MSG_REPLAY_TOTAL_CNT:
                    mView.updateReplayTotalCnt((int) params.getItem(0));
                    break;
                case MSG_FOLD_INFO_AREA:
                    mView.onFoldInfoArea();
                    break;
                default:
                    break;
            }
            return false;
        }
    }
}
