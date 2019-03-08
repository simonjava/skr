package com.module.playways.grab.room.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.OvershootInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.RelativeLayout;
import com.common.view.DebounceViewClickListener;
import com.common.view.ex.ExImageView;
import com.module.rank.R;

/**
 * 不唱了界面
 */
public class GrabPassView extends RelativeLayout {
    ExImageView mIvPass;

    Listener mListener;

    int mSeq = 0;

    Handler mUiHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            TranslateAnimation animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.0f,
                    Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0);
            animation.setDuration(200);
            animation.setRepeatMode(Animation.REVERSE);
            animation.setInterpolator(new OvershootInterpolator());
            animation.setFillAfter(true);
            startAnimation(animation);
        }
    };

    public GrabPassView(Context context) {
        super(context);
        init();
    }

    public GrabPassView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GrabPassView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void setListener(Listener listener) {
        mListener = listener;
    }

    private void init() {
        inflate(getContext(), R.layout.grab_pass_view_layout, this);
        mIvPass = (ExImageView) findViewById(R.id.iv_pass);
        mIvPass.setOnClickListener(new DebounceViewClickListener() {
            @Override
            public void clickValid(View v) {
                if (mListener != null) {
                    mListener.pass();
                }
            }
        });
    }

    public void delayShowPassView(int seq) {
        if(seq > mSeq){
            mSeq = seq;
            hideWithAnimation(false);
            mUiHandler.removeCallbacksAndMessages(null);
            mUiHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mUiHandler.sendMessage(null);
                }
            }, 5000);
        }
    }

    public void passSuccess(int seq) {
        if (mSeq == seq) {
            mUiHandler.removeCallbacksAndMessages(null);
            hideWithAnimation(true);
        }
    }

    public void hideWithAnimation(boolean needAnim) {
        TranslateAnimation animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 1.0f,
                Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0);
        animation.setDuration(needAnim ? 200 : 0);
        animation.setRepeatMode(Animation.REVERSE);
        animation.setInterpolator(new OvershootInterpolator());
        animation.setFillAfter(true);
        startAnimation(animation);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mUiHandler.removeCallbacksAndMessages(null);
    }

    public interface Listener {
        void pass();
    }
}
