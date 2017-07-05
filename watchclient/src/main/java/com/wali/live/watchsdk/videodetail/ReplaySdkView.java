package com.wali.live.watchsdk.videodetail;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.RelativeLayout;

import com.base.activity.BaseActivity;
import com.base.log.MyLog;
import com.mi.live.data.account.UserAccountManager;
import com.mi.live.data.api.LiveManager;
import com.wali.live.common.barrage.manager.BarrageMessageManager;
import com.wali.live.common.flybarrage.view.FlyBarrageViewGroup;
import com.wali.live.common.gift.view.GiftAnimationView;
import com.wali.live.common.gift.view.GiftContinueViewGroup;
import com.wali.live.common.gift.view.GiftRoomEffectView;
import com.wali.live.component.BaseSdkView;
import com.wali.live.component.presenter.ComponentPresenter;
import com.wali.live.proto.LiveMessageProto;
import com.wali.live.utils.ReplayBarrageMessageManager;
import com.wali.live.watchsdk.R;
import com.wali.live.watchsdk.base.BaseComponentSdkActivity;
import com.wali.live.watchsdk.component.WatchComponentController;
import com.wali.live.watchsdk.component.presenter.LiveCommentPresenter;
import com.wali.live.watchsdk.component.presenter.TouchPresenter;
import com.wali.live.watchsdk.component.view.LiveCommentView;
import com.wali.live.watchsdk.component.view.VideoDetailPlayerView;
import com.wali.live.watchsdk.watch.presenter.push.GiftPresenter;
import com.wali.live.watchsdk.watch.presenter.push.RoomStatusPresenter;
import com.wali.live.watchsdk.watch.presenter.push.RoomTextMsgPresenter;
import com.wali.live.watchsdk.watch.presenter.push.RoomViewerPresenter;
import com.wali.live.watchsdk.watchtop.view.WatchTopInfoSingleView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static com.wali.live.component.ComponentController.MSG_BACKGROUND_CLICK;
import static com.wali.live.component.ComponentController.MSG_DISABLE_MOVE_VIEW;
import static com.wali.live.component.ComponentController.MSG_ENABLE_MOVE_VIEW;
import static com.wali.live.component.ComponentController.MSG_HIDE_GAME_INPUT;
import static com.wali.live.component.ComponentController.MSG_HIDE_INPUT_VIEW;
import static com.wali.live.component.ComponentController.MSG_INPUT_VIEW_SHOWED;
import static com.wali.live.component.ComponentController.MSG_ON_ORIENT_LANDSCAPE;
import static com.wali.live.component.ComponentController.MSG_ON_ORIENT_PORTRAIT;
import static com.wali.live.component.ComponentController.MSG_PLAYER_DETAIL_SCREEN;
import static com.wali.live.component.ComponentController.MSG_PLAYER_ROTATE_ORIENTATION;
import static com.wali.live.component.ComponentController.MSG_SHOW_GAME_INPUT;

/**
 * Created by yangli on 2017/6/19.
 *
 * @module 详情播放全屏
 */
public class ReplaySdkView extends BaseSdkView<VideoDetailController> implements View.OnClickListener {

    @NonNull
    protected final Action mAction = new Action();

    @NonNull
    protected ViewGroup mParentView;
    @NonNull
    protected View mContentView;

    private final List<View> mHorizontalMoveSet = new ArrayList();
    private final List<View> mVerticalMoveSet = new ArrayList(0);
    private final List<View> mGameHideSet = new ArrayList(0);

    protected GiftPresenter mGiftPresenter;
    protected RoomTextMsgPresenter mRoomTextMsgPresenter;
    protected RoomViewerPresenter mRoomViewerPresenter;
    protected RoomStatusPresenter mRoomStatusPresenter;

    @Nullable
    protected WatchTopInfoSingleView mTopInfoView;
    @Nullable
    protected View mLiveCommentView;
    @Nullable
    protected GiftContinueViewGroup mGiftContinueViewGroup;

    protected GiftAnimationView mGiftAnimationView;
    protected GiftRoomEffectView mGiftRoomEffectView;
    protected FlyBarrageViewGroup mFlyBarrageViewGroup;
    protected View mCloseBtn;
    protected View mRotateBtn;

    protected boolean mIsGameMode = false;
    protected boolean mIsLandscape = false;

    protected Timer mTimer; // 每秒拉取弹幕的timer
    protected long mVideoStartTime;

    @Override
    protected final <V extends View> V $(@IdRes int id) {
        return (V) mContentView.findViewById(id);
    }

    @Override
    protected String getTAG() {
        return "ReplaySdkView";
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.close_btn) {
            mComponentController.onEvent(MSG_PLAYER_DETAIL_SCREEN);
        } else if (i == R.id.rotate_btn) {
            mComponentController.onEvent(MSG_PLAYER_ROTATE_ORIENTATION);
        }
    }

    public ReplaySdkView(
            @NonNull Activity activity,
            @NonNull VideoDetailController componentController) {
        super(activity, componentController);
        mIsGameMode = (componentController.mMyRoomData.getLiveType() == LiveManager.TYPE_LIVE_GAME);
    }

    private void startGetBarrageTimer() {
        if (mTimer == null) {
            syncSystemMessage();
            mTimer = new Timer();
            mTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    MyLog.d(TAG, "TickTimerTask mVideoStartTime=" + mVideoStartTime);
                    if (mComponentController.mPlayerPresenter == null) {
                        return;
                    }
                    ReplayBarrageMessageManager.getInstance().getBarrageMessageByReplayTime(
                            mComponentController.mMyRoomData.getRoomId(),
                            mVideoStartTime + mComponentController.mPlayerPresenter.getCurrentPosition(),
                            false);
                }
            }, 0, 1000);
        }
    }

    private void syncSystemMessage() {
        final String roomId = mComponentController.mMyRoomData.getRoomId();
        if (!TextUtils.isEmpty(roomId)) {
            long myId = UserAccountManager.getInstance().getUuidAsLong();
            LiveMessageProto.SyncSysMsgRequest syncSysMsgRequest =
                    LiveMessageProto.SyncSysMsgRequest.newBuilder()
                            .setCid(System.currentTimeMillis())
                            .setFromUser(myId)
                            .setRoomId(roomId)
                            .build();
            BarrageMessageManager.getInstance().sendSyncSystemMessage(syncSysMsgRequest);
        }
    }

    @Override
    public void setupSdkView() {
        MyLog.w(TAG, "setupSdkView");
        mParentView = (ViewGroup) mActivity.findViewById(android.R.id.content);
        mContentView = LayoutInflater.from(mActivity).inflate(R.layout.video_replay_layout,
                mParentView, false);

        mGiftPresenter = new GiftPresenter(mComponentController.mRoomChatMsgManager, false);
        mRoomTextMsgPresenter = new RoomTextMsgPresenter(mComponentController.mRoomChatMsgManager);
        mRoomViewerPresenter = new RoomViewerPresenter(mComponentController.mRoomChatMsgManager);
        mRoomStatusPresenter = new RoomStatusPresenter(mComponentController.mRoomChatMsgManager);

        // 顶部面板
        {
            WatchTopInfoSingleView view = $(mContentView, R.id.watch_top_info_view);
            if (view == null) {
                MyLog.e(TAG, "missing R.id.watch_top_info_view");
                return;
            }
            view.setMyRoomDataSet(mComponentController.mMyRoomData);
            view.initViewUseData();
            mTopInfoView = view;
            mTopInfoView.onActivityCreate();
        }

        // 大礼物动画
        {
            GiftAnimationView view = $(R.id.gift_animation_player_view);
            if (view == null) {
                MyLog.e(TAG, "missing R.id.gift_animation_player_view");
                return;
            }
            mGiftAnimationView = view;
            mGiftAnimationView.onActivityCreate();
        }

        // 房间特效动画
        {
            GiftRoomEffectView view = $(R.id.gift_room_effect_view);
            if (view == null) {
                MyLog.e(TAG, "missing R.id.gift_room_effect_view");
                return;
            }
            mGiftRoomEffectView = view;
            mGiftRoomEffectView.onActivityCreate();
        }

        // 礼物连送动画
        {
            GiftContinueViewGroup view = $(R.id.gift_continue_vg);
            if (view == null) {
                MyLog.e(TAG, "missing R.id.gift_continue_vg");
                return;
            }
            mGiftContinueViewGroup = view;
            mGiftContinueViewGroup.onActivityCreate();
        }

        // 飘屏弹幕
        {
            FlyBarrageViewGroup view = $(R.id.fly_barrage_viewgroup);
            if (view == null) {
                MyLog.e(TAG, "missing R.id.fly_barrage_viewgroup");
                return;
            }
            mFlyBarrageViewGroup = $(R.id.fly_barrage_viewgroup);
            mFlyBarrageViewGroup.onActivityCreate();
        }

        // 弹幕区
        {
            LiveCommentView view = $(R.id.live_comment_view);
            if (view == null) {
                return;
            }
            LiveCommentPresenter presenter = new LiveCommentPresenter(mComponentController);
            addComponentView(view, presenter);
            view.setToken(mComponentController.mRoomChatMsgManager.toString());
            mLiveCommentView = view;
        }

        mCloseBtn = $(R.id.close_btn);
        mRotateBtn = $(R.id.rotate_btn);
        $click(mRotateBtn, this);
        $click(mCloseBtn, this);
        orientCloseBtn(false);

        mVerticalMoveSet.add($(R.id.close_btn));
        addViewToSet(new int[]{
                R.id.watch_top_info_view,
                R.id.bottom_button_view,
                R.id.live_comment_view,
                R.id.gift_animation_player_view,
                R.id.gift_continue_vg,
                R.id.gift_room_effect_view,
                R.id.widget_view
        }, mHorizontalMoveSet, mVerticalMoveSet);

        if (mIsGameMode) {
            addViewToSet(new int[]{
                    R.id.watch_top_info_view,
                    R.id.bottom_button_view,
                    R.id.game_barrage_view,
                    R.id.game_input_view,
                    R.id.close_btn,
                    R.id.widget_view
            }, mGameHideSet);
        }

        // 滑动
        {
            View view = $(R.id.touch_view);
            if (view == null) {
                return;
            }
            TouchPresenter presenter = new TouchPresenter(mComponentController, view);
            addComponentView(presenter);
            presenter.setViewSet(mHorizontalMoveSet, mVerticalMoveSet, mIsGameMode);
        }
    }

    private void orientCloseBtn(boolean isLandscape) {
        if (mCloseBtn == null) {
            return;
        }
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams)
                mCloseBtn.getLayoutParams();
        if (!isLandscape && BaseActivity.isProfileMode()) {
            layoutParams.topMargin = layoutParams.rightMargin + BaseActivity.getStatusBarHeight();
        } else {
            layoutParams.topMargin = layoutParams.rightMargin;
        }
        mCloseBtn.setLayoutParams(layoutParams);
    }

    public void startSdkView(long videoStartTime) {
        mVideoStartTime = videoStartTime;
        startSdkView();
    }

    private Animation mShowAnimation;

    @Override
    public void startSdkView() {
        MyLog.w(TAG, "startSdkView");
        if (mParentView.indexOfChild(mContentView) == -1) {
            mParentView.addView(mContentView);
            if (mShowAnimation == null) {
                mShowAnimation = new AlphaAnimation(0, 1);
                mShowAnimation.setDuration(400);
            }
            mContentView.startAnimation(mShowAnimation);
        }
        ReplayBarrageMessageManager.getInstance().init(
                mComponentController.mRoomChatMsgManager.toString()); //回放弹幕管理
        startGetBarrageTimer();

        BaseComponentSdkActivity sdkActivity = (BaseComponentSdkActivity) mActivity;
        sdkActivity.addPushProcessor(mGiftPresenter);
        sdkActivity.addPushProcessor(mRoomTextMsgPresenter);
        sdkActivity.addPushProcessor(mRoomViewerPresenter);
        sdkActivity.addPushProcessor(mRoomStatusPresenter);

        for (ComponentPresenter presenter : mComponentPresenterSet) {
            presenter.startPresenter();
        }
        mAction.registerAction();

        // 添加播放器View
        VideoDetailPlayerView view = mComponentController.mPlayerView;
        if (view == null) {
            MyLog.e(TAG, "missing mComponentController.mPlayerView");
            return;
        }
        view.switchToFullScreen(true);
        view.showOrHideFullScreenBtn(false);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        addViewUnderAnchor(view, layoutParams, $(R.id.watch_top_info_view));
    }

    @Override
    public void stopSdkView() {
        MyLog.w(TAG, "stopSdkView");
        mContentView.clearAnimation();
        mParentView.removeView(mContentView);
        ReplayBarrageMessageManager.getInstance().destory();
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }

        BaseComponentSdkActivity sdkActivity = (BaseComponentSdkActivity) mActivity;
        sdkActivity.removePushProcessor(mGiftPresenter);
        sdkActivity.removePushProcessor(mRoomTextMsgPresenter);
        sdkActivity.removePushProcessor(mRoomViewerPresenter);
        sdkActivity.removePushProcessor(mRoomStatusPresenter);

        for (ComponentPresenter presenter : mComponentPresenterSet) {
            presenter.stopPresenter();
        }
        mAction.unregisterAction();

        // 将播放器View从其父View移出
        ViewGroup parentView = mComponentController.mPlayerView != null ?
                (ViewGroup) mComponentController.mPlayerView.getParent() : null;
        if (parentView != null && parentView.indexOfChild(mComponentController.mPlayerView) != -1) {
            parentView.removeView(mComponentController.mPlayerView);
        }
    }

    @Override
    public void releaseSdkView() {
        super.releaseSdkView();
        MyLog.w(TAG, "releaseSdkView");

        ReplayBarrageMessageManager.getInstance().destory();
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }

        mGiftPresenter.destroy();
        mRoomTextMsgPresenter.destroy();
        mRoomViewerPresenter.destroy();
        mRoomStatusPresenter.destroy();

        mTopInfoView.onActivityDestroy();
        mGiftAnimationView.onActivityDestroy();
        mGiftRoomEffectView.onActivityDestroy();
        mGiftContinueViewGroup.onActivityDestroy();
        mFlyBarrageViewGroup.onActivityDestroy();
    }

    public class Action extends BaseSdkView.Action {

        @Override
        protected void startInputAnimator(boolean inputShow) {
            if (mInputShow == inputShow) {
                return;
            }
            mInputShow = inputShow;
            ValueAnimator valueAnimator = deRef(mInputAnimatorRef);
            if (valueAnimator != null) {
                if (!valueAnimator.isStarted() && !valueAnimator.isRunning()) {
                    valueAnimator.start();
                }
                return;
            }
            valueAnimator = ValueAnimator.ofFloat(0.0f, 1.0f);
            valueAnimator.setDuration(300);
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float value = (float) animation.getAnimatedValue();
                    if (mInputShow) {
                        value = 1.0f - value;
                    }
                    setAlpha(mTopInfoView, value);
                }
            });
            valueAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animation) {
                    if (mInputShow) {
                        if (mIsLandscape && !mIsGameMode) {
                            setVisibility(mLiveCommentView, View.GONE);
                        }
                    } else {
                        setVisibility(mTopInfoView, View.VISIBLE);
                    }
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    if (mInputShow) {
                        setVisibility(mTopInfoView, View.GONE);
                    } else {
                        setAlpha(mTopInfoView, 1.0f);
                        if (mIsLandscape && !mIsGameMode) {
                            setVisibility(mLiveCommentView, View.VISIBLE);
                        }
                    }
                }
            });
            valueAnimator.start();
            mInputAnimatorRef = new WeakReference<>(valueAnimator);
        }

        private WeakReference<ValueAnimator> mGameAnimatorRef; // 游戏直播竖屏时，隐藏显示动画
        private boolean mGameHide = false;

        /**
         * 观看游戏直播横屏时，点击隐藏显示View
         */
        private void startGameAnimator() {
            mGameHide = !mGameHide;
            ValueAnimator valueAnimator = deRef(mGameAnimatorRef);
            if (valueAnimator != null) {
                if (!valueAnimator.isStarted() && !valueAnimator.isRunning()) {
                    valueAnimator.start();
                }
                return;
            }
            valueAnimator = ValueAnimator.ofFloat(0.0f, 1.0f);
            valueAnimator.setDuration(300);
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float value = (float) animation.getAnimatedValue();
                    if (mGameHide) {
                        value = 1.0f - value;
                    }
                    for (View view : mGameHideSet) {
                        if (view != null) {
                            setAlpha(view, value);
                        }
                    }
                }
            });
            valueAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animation) {
                    if (!mGameHide) {
                        for (View view : mGameHideSet) {
                            if (view != null) {
                                setAlpha(view, 0.0f);
                                setVisibility(view, View.VISIBLE);
                            }
                        }
                    }
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    if (mGameHide) {
                        for (View view : mGameHideSet) {
                            if (view != null) {
                                setAlpha(view, 1.0f);
                                setVisibility(view, View.GONE);
                            }
                        }
                    }
                }
            });
            valueAnimator.start();
            mGameAnimatorRef = new WeakReference<>(valueAnimator);
        }

        @Override
        protected void stopAllAnimator() {
            ValueAnimator valueAnimator = deRef(mInputAnimatorRef);
            if (valueAnimator != null) {
                valueAnimator.cancel();
            }
            valueAnimator = deRef(mGameAnimatorRef);
            if (valueAnimator != null) {
                valueAnimator.cancel();
            }
        }

        @Override
        public void clearAnimation() {
            stopAllAnimator();
            mInputAnimatorRef = null;
            mGameAnimatorRef = null;
        }

        @Override
        public boolean onAction(int source, @Nullable ComponentPresenter.Params params) {
            switch (source) {
                case MSG_ON_ORIENT_PORTRAIT:
                    mIsLandscape = false;
                    stopAllAnimator();
                    orientCloseBtn(false);
                    mTopInfoView.onScreenOrientationChanged(mIsLandscape);
                    if (mIsGameMode) {
                        mComponentController.onEvent(MSG_ENABLE_MOVE_VIEW);
                        if (mGameHide) { // 横屏转竖屏，恢复被隐藏的View，竖屏转横屏的逻辑在TouchPresenter中处理
                            mGameHide = false;
                            for (View view : mGameHideSet) {
                                if (view != null && view.getVisibility() != View.VISIBLE) {
                                    view.setVisibility(View.VISIBLE);
                                }
                            }
                        }
                        setVisibility(mLiveCommentView, View.VISIBLE);
                        mComponentController.onEvent(MSG_HIDE_GAME_INPUT);
                    }
                    return true;
                case MSG_ON_ORIENT_LANDSCAPE:
                    mIsLandscape = true;
                    stopAllAnimator();
                    orientCloseBtn(true);
                    mTopInfoView.onScreenOrientationChanged(mIsLandscape);
                    if (mIsGameMode) { // 游戏直播横屏不需左右滑
                        mComponentController.onEvent(MSG_DISABLE_MOVE_VIEW);
                        setVisibility(mLiveCommentView, View.INVISIBLE);
                        mComponentController.onEvent(MSG_SHOW_GAME_INPUT);
                    }
                    return true;
                case MSG_INPUT_VIEW_SHOWED:
                    if (!mIsGameMode || !mIsLandscape) {
                        mComponentController.onEvent(MSG_DISABLE_MOVE_VIEW);
                    }
                    if (mGiftContinueViewGroup != null) {
                        mGiftContinueViewGroup.onShowInputView();
                    }
                    startInputAnimator(true);
                    return true;
                case WatchComponentController.MSG_INPUT_VIEW_HIDDEN:
                    if (!mIsGameMode || !mIsLandscape) { // 游戏直播横屏不需左右滑
                        mComponentController.onEvent(MSG_ENABLE_MOVE_VIEW);
                    }
                    if (mGiftContinueViewGroup != null) {
                        mGiftContinueViewGroup.onHideInputView();
                    }
                    startInputAnimator(false);
                    return true;
                case MSG_BACKGROUND_CLICK:
                    if (mComponentController.onEvent(MSG_HIDE_INPUT_VIEW)) {
                        return true;
                    }
                    if (mComponentController.mPlayerView != null) {
                        mComponentController.mPlayerView.onSeekBarContainerClick();
                    }
                    if (mIsGameMode && mIsLandscape) {
                        startGameAnimator();
                        return true;
                    }
                    break;
                default:
                    break;
            }
            return false;
        }
    }
}
