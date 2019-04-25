package com.module.playways.grab.room.bottom;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.common.utils.U;
import com.common.view.DebounceViewClickListener;
import com.common.view.ex.ExImageView;
import com.module.playways.BaseRoomData;
import com.module.playways.grab.room.GrabRoomData;
import com.module.playways.grab.room.event.GrabRoundChangeEvent;
import com.module.playways.grab.room.event.GrabRoundStatusChangeEvent;
import com.module.playways.grab.room.event.GrabSpeakingControlEvent;
import com.module.playways.grab.room.dynamicmsg.DynamicMsgView;
import com.module.playways.grab.room.model.GrabRoundInfoModel;
import com.module.playways.room.room.view.BottomContainerView;
import com.module.playways.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class GrabBottomContainerView extends BottomContainerView {

    View mIvRoomManage;

    ExImageView mQuickBtn;

    View mSpeakingDotAnimationView;

    PopupWindow mDynamicMsgPopWindow;    //动态表情弹出面板

    DynamicMsgView mDynamicMsgView;

    GrabRoomData mGrabRoomData;

    public GrabBottomContainerView(Context context) {
        super(context);
    }

    public GrabBottomContainerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected int getLayout() {
        return R.layout.grab_bottom_container_view_layout;
    }

    protected void init() {
        super.init();
        mIvRoomManage = this.findViewById(R.id.iv_room_manage);
        mIvRoomManage.setOnClickListener(new DebounceViewClickListener() {
            @Override
            public void clickValid(View v) {
                if (mBottomContainerListener != null) {
                    mBottomContainerListener.clickRoomManagerBtn();
                }
            }
        });
        mSpeakingDotAnimationView = this.findViewById(R.id.speaking_dot_animation_view);
        mQuickBtn = (ExImageView) super.mQuickBtn;

        mEmoji1Btn.setOnClickListener(new DebounceViewClickListener() {
            @Override
            public void clickValid(View v) {
                // 动态表情按钮
                int w = U.getDisplayUtils().getScreenWidth() - U.getDisplayUtils().dip2px(32);
                int h = U.getDisplayUtils().dip2px(72);
                if (mDynamicMsgView == null) {
                    mDynamicMsgView = new DynamicMsgView(getContext());
                    mDynamicMsgView.setData(mRoomData);
                    mDynamicMsgView.setListener(new DynamicMsgView.Listener() {
                        @Override
                        public void onSendMsgOver() {
                            if (mDynamicMsgPopWindow != null) {
                                mDynamicMsgPopWindow.dismiss();
                            }
                        }
                    });
                } else {
                    mDynamicMsgView.loadEmoji();
                }
                if (mDynamicMsgPopWindow == null) {
                    mDynamicMsgPopWindow = new PopupWindow(mDynamicMsgView, w, h);
                    mDynamicMsgPopWindow.setFocusable(true);
                    mDynamicMsgPopWindow.setAnimationStyle(R.style.MyPopupWindow_anim_style);
                    // 去除动画
//                      mDynamicMsgPopWindow.setAnimationStyle(R.style.anim_quickmsg_dialog);
                    mDynamicMsgPopWindow.setBackgroundDrawable(new BitmapDrawable());
                    mDynamicMsgPopWindow.setOutsideTouchable(true);
                }
                if (!mDynamicMsgPopWindow.isShowing()) {
                    int l[] = new int[2];
                    mQuickBtn.getLocationInWindow(l);
                    mDynamicMsgPopWindow.showAtLocation(mQuickBtn, Gravity.START | Gravity.TOP, l[0], l[1] - h - U.getDisplayUtils().dip2px(5));
                } else {
                    mDynamicMsgPopWindow.dismiss();
                }
//                if (mBottomContainerListener != null) {
//                    mBottomContainerListener.showGiftPanel();
//                }
            }
        });
    }

    protected void onQuickMsgDialogShow(boolean show) {
        if (show) {
            mQuickBtn.setImageResource(R.drawable.ycdd_kuaijie_anxia);
        } else {
            mQuickBtn.setImageResource(R.drawable.ycdd_kuaijie);
        }
    }

    public void setRoomData(BaseRoomData roomData) {
        super.setRoomData(roomData);
        if (mRoomData instanceof GrabRoomData) {
            mGrabRoomData = (GrabRoomData) mRoomData;
            if (mGrabRoomData.isOwner()) {
                //是房主
                adjustUi(true);
            } else {
                //不是一唱到底房主
                adjustUi(false);
            }
        }
    }

    void adjustUi(boolean grabOwner) {
        if (grabOwner) {
            mIvRoomManage.setVisibility(VISIBLE);
            LayoutParams lp = (LayoutParams) mEmoji2Btn.getLayoutParams();
            lp.addRule(RelativeLayout.LEFT_OF, mIvRoomManage.getId());
            lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 0);
            mEmoji2Btn.setLayoutParams(lp);
            mQuickBtn.setImageResource(R.drawable.fz_anzhushuohua);
            mQuickBtn.setEnabled(true);
            mQuickBtn.setOnClickListener(null);

            mShowInputContainerBtn.setTextSize(TypedValue.COMPLEX_UNIT_PX, U.getDimension(R.dimen.textsize_13_dp));

            mQuickBtn.setOnTouchListener(new OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (!mQuickBtn.isEnabled()) {
                        if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
                            U.getToastUtil().showShort("演唱阶段不能说话哦");
                        }
                        return true;
                    }
                    switch (event.getActionMasked()) {
                        case MotionEvent.ACTION_DOWN: {
                            mQuickBtn.setImageResource(R.drawable.fz_shuohuazhong);
                            mSpeakingDotAnimationView.setVisibility(VISIBLE);
                            mShowInputContainerBtn.setText("");
                            EventBus.getDefault().post(new GrabSpeakingControlEvent(true));
                        }
                        break;
                        case MotionEvent.ACTION_CANCEL:
                        case MotionEvent.ACTION_UP: {
                            mQuickBtn.setImageResource(R.drawable.fz_anzhushuohua);
                            mSpeakingDotAnimationView.setVisibility(GONE);
                            mShowInputContainerBtn.setText("夸赞是一种美德");
                            EventBus.getDefault().post(new GrabSpeakingControlEvent(false));
                        }
                        break;
                    }
                    return true;
                }
            });
        } else {
            mIvRoomManage.setVisibility(GONE);
            LayoutParams lp = (LayoutParams) mEmoji2Btn.getLayoutParams();
            lp.addRule(RelativeLayout.LEFT_OF, 0);
            lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            mEmoji2Btn.setLayoutParams(lp);
            mQuickBtn.setImageResource(R.drawable.ycdd_kuaijie);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(GrabRoundStatusChangeEvent event) {
        //MyLog.d("GrabBottomContainerView","onEvent" + " event=" + event);
        GrabRoundInfoModel now = event.roundInfo;
        if (now != null && now.isSingStatus() && mGrabRoomData.isOwner()) {
            if (mGrabRoomData.isSpeaking() && !now.singBySelf()) {
                U.getToastUtil().showShort("有人上麦了,暂时不能说话哦", 0, Gravity.CENTER);
            }
            mQuickBtn.setImageResource(R.drawable.fz_anzhushuohua_b);
            mQuickBtn.setEnabled(false);
            mSpeakingDotAnimationView.setVisibility(GONE);
            mShowInputContainerBtn.setText("夸赞是一种美德");
            EventBus.getDefault().post(new GrabSpeakingControlEvent(false));

        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(GrabRoundChangeEvent event) {
        if (mGrabRoomData.isOwner()) {
            mQuickBtn.setEnabled(true);
            if (mGrabRoomData.isSpeaking()) {
                // 正在说话，就算了
            } else {
                mQuickBtn.setImageResource(R.drawable.fz_anzhushuohua);
            }
        }
    }

    @Override
    public void dismissPopWindow() {
        super.dismissPopWindow();
        if (mDynamicMsgPopWindow != null) {
            mDynamicMsgPopWindow.dismiss();
        }
    }
}
