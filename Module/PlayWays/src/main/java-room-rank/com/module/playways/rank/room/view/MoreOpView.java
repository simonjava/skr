package com.module.playways.rank.room.view;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.common.core.account.UserAccountManager;
import com.common.statistics.StatConstants;
import com.common.statistics.StatisticsAdapter;
import com.common.utils.U;
import com.common.view.DebounceViewClickListener;
import com.common.view.ex.ExTextView;
import com.component.busilib.constans.GameModeType;
import com.module.playways.BaseRoomData;
import com.module.playways.RoomDataUtils;
import com.module.rank.R;

public class MoreOpView extends RelativeLayout {

    LinearLayout mMenuContainer;
    RelativeLayout mVoiceAudition;// 调音板
    ExTextView mVoiceAuditionBt;
    RelativeLayout mQuitBtnContainer;
    ExTextView mQuitBtn;
    RelativeLayout mVoiceControlBtnContainer;
    ExTextView mVoiceControlBtn;
    PopupWindow mPopupWindow;

    RelativeLayout mGameGuideRl;
    ExTextView mGameGuideBtn;
    Listener mListener;

    BaseRoomData mRoomData;

    boolean mVoiceOpen = true;

    public MoreOpView(Context context) {
        super(context);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.more_op_view_layout, this);
        setBackgroundResource(R.drawable.tuichufangjian);

        mMenuContainer = (LinearLayout) this.findViewById(R.id.menu_container);
        mVoiceAudition = (RelativeLayout) this.findViewById(R.id.voice_audition);
        mVoiceAuditionBt = (ExTextView) this.findViewById(R.id.voice_audition_bt);
        mVoiceControlBtnContainer = (RelativeLayout) this.findViewById(R.id.voice_control_btn_container);
        mVoiceControlBtn = (ExTextView) this.findViewById(R.id.voice_control_btn);
        mGameGuideRl = (RelativeLayout) findViewById(R.id.game_guide_rl);
        mGameGuideBtn = (ExTextView) findViewById(R.id.game_guide_btn);
        mQuitBtnContainer = (RelativeLayout) this.findViewById(R.id.quit_btn_container);
        mQuitBtn = (ExTextView) this.findViewById(R.id.quit_btn);

        mQuitBtnContainer.setOnClickListener(new DebounceViewClickListener() {
            @Override
            public void clickValid(View v) {
                if (mListener != null) {
                    mListener.onClostBtnClick();
                }
                mPopupWindow.dismiss();
            }
        });

        mVoiceControlBtnContainer.setOnClickListener(new DebounceViewClickListener() {
            @Override
            public void clickValid(View v) {
                if (mListener != null) {
                    mVoiceOpen = !mVoiceOpen;
                    mListener.onVoiceChange(mVoiceOpen);
                    if (mVoiceOpen) {
                        mVoiceControlBtn.setText("关闭声音");
                        Drawable drawableLeft = getResources().getDrawable(
                                R.drawable.soundoff);
                        mVoiceControlBtn.setCompoundDrawablesWithIntrinsicBounds(drawableLeft,
                                null, null, null);
                    } else {
                        StatisticsAdapter.recordCountEvent(UserAccountManager.getInstance().getGategory(StatConstants.CATEGORY_RANK), "game_muteon", null);
                        mVoiceControlBtn.setText("打开声音");
                        Drawable drawableLeft = getResources().getDrawable(
                                R.drawable.soundon);
                        mVoiceControlBtn.setCompoundDrawablesWithIntrinsicBounds(drawableLeft,
                                null, null, null);
                    }
                }
                mPopupWindow.dismiss();
            }
        });

        mVoiceAudition.setOnClickListener(new DebounceViewClickListener() {
            @Override
            public void clickValid(View v) {
                if (mListener != null) {
                    mListener.onClickVoiceAudition();
                }
                mPopupWindow.dismiss();
            }
        });
    }

    public void showAt(View view) {
        if (mPopupWindow == null) {
            mPopupWindow = new PopupWindow(this, U.getDisplayUtils().dip2px(118), ViewGroup.LayoutParams.WRAP_CONTENT);
            mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
            mPopupWindow.setOutsideTouchable(true);
        }
        if (!mPopupWindow.isShowing()) {
            mPopupWindow.showAsDropDown(view, -U.getDisplayUtils().dip2px(2), U.getDisplayUtils().dip2px(5));
        }
        if (RoomDataUtils.isMyRound(mRoomData.getRealRoundInfo())) {
            mVoiceControlBtnContainer.setVisibility(GONE);
        } else {
            mVoiceControlBtnContainer.setVisibility(VISIBLE);
        }
        if (mRoomData.getGameType() == GameModeType.GAME_MODE_GRAB) {
            mVoiceAudition.setVisibility(VISIBLE);
        } else {
            mVoiceAudition.setVisibility(GONE);
        }
    }

    public void dismiss() {
        if (mPopupWindow != null) {
            mPopupWindow.dismiss();
        }
    }

    public void setListener(Listener listener) {
        mListener = listener;
    }

    public void setRoomData(BaseRoomData roomData) {
        mRoomData = roomData;
        if (roomData.getGameType() == GameModeType.GAME_MODE_GRAB) {
            mGameGuideRl.setVisibility(VISIBLE);
            mGameGuideBtn.setOnClickListener(new DebounceViewClickListener() {
                @Override
                public void clickValid(View v) {
                    if (mListener != null) {
                        mListener.onClickGameRule();
                    }
                    mPopupWindow.dismiss();
                }
            });
        } else {
            mGameGuideRl.setVisibility(GONE);
        }
    }

    public interface Listener {
        void onClostBtnClick();

        void onVoiceChange(boolean voiceOpen);

        void onClickGameRule();

        void onClickVoiceAudition();
    }
}
