package com.module.playways.grab.room.view;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.common.core.account.UserAccountManager;
import com.common.statistics.StatConstants;
import com.common.statistics.StatisticsAdapter;
import com.common.utils.U;
import com.module.playways.grab.room.fragment.GrabRoomFragment;
import com.module.playways.grab.room.listener.SVGAListener;
import com.module.rank.R;
import com.opensource.svgaplayer.SVGACallback;
import com.opensource.svgaplayer.SVGADrawable;
import com.opensource.svgaplayer.SVGADynamicEntity;
import com.opensource.svgaplayer.SVGAImageView;
import com.opensource.svgaplayer.SVGAParser;
import com.opensource.svgaplayer.SVGAVideoEntity;
import com.zq.live.proto.Room.EQRoundOverReason;

import org.greenrobot.greendao.annotation.NotNull;

/**
 * 轮次结束
 */
public class RoundOverCardView extends RelativeLayout {

    public final static int UNKNOW_END = 0;                // 未知原因
    public final static int NONE_SING_END = -1;             // 无人想唱
    public final static int SING_PERFECT_END = 1;          // 有种优秀叫一唱到底
    public final static int SING_MOMENT_END = 2;           // 有种结束叫刚刚开始
    public final static int SING_NO_PASS_END = 3;          // 有种悲伤叫都没及格
    public final static int SING_PASS_END = 4;             // 有种遗憾叫明明可以
    public final static int SING_ENOUGH_END = 5;           // 有种可惜叫觉得你行

    //0未知
    //1有种优秀叫一唱到底（全部唱完）
    //2有种结束叫刚刚开始（t<30%）
    //3有份悲伤叫都没及格(30%<=t <60%)
    //4有种遗憾叫明明可以（60%<=t<90%）
    //5有种可惜叫我觉得你行（90%<=t<=100%)

    SVGAImageView mNoneSingSvga;
    SVGAImageView mSingResultSvga;

    SVGAListener mSVGAListener;

    public RoundOverCardView(Context context) {
        super(context);
        init();
    }

    public RoundOverCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RoundOverCardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.grab_round_over_card_layout, this);
        mNoneSingSvga = (SVGAImageView) findViewById(R.id.none_sing_svga);
        mSingResultSvga = (SVGAImageView) findViewById(R.id.sing_result_svga);
    }

    public void bindData(int reason, int resultType, SVGAListener listener) {
        this.mSVGAListener = listener;
        setVisibility(VISIBLE);
        int mode = getRoundOver(reason, resultType);
        switch (mode) {
            case NONE_SING_END:
                startNoneSing();
                break;
            case SING_PERFECT_END:
                startPerfect();
                break;
            case SING_MOMENT_END:
            case SING_NO_PASS_END:
            case SING_PASS_END:
            case SING_ENOUGH_END:
                startFailed(mode);
                break;
            default:
                if (mSVGAListener != null) {
                    mSVGAListener.onFinished();
                }
                break;
        }
    }

    private int getRoundOver(int reason, int resultType) {
        if (reason == EQRoundOverReason.ROR_NO_ONE_SING.getValue()) {
            return NONE_SING_END;
        } else {
            return resultType;
        }
    }

    private void startNoneSing() {
        U.getSoundUtils().play(GrabRoomFragment.TAG, R.raw.nobodywants);
        StatisticsAdapter.recordCountEvent(UserAccountManager.getInstance().getGategory(StatConstants.CATEGORY_GRAB),
                StatConstants.KEY_SONG_NO_ONE, null);
        mNoneSingSvga.setVisibility(VISIBLE);
        mNoneSingSvga.setLoops(1);
        SVGAParser parser = new SVGAParser(getContext());
        try {
            parser.parse("grab_none_sing_end.svga", new SVGAParser.ParseCompletion() {
                @Override
                public void onComplete(@NotNull SVGAVideoEntity videoItem) {
                    SVGADrawable drawable = new SVGADrawable(videoItem);
                    mNoneSingSvga.setImageDrawable(drawable);
                    mNoneSingSvga.startAnimation();
                }

                @Override
                public void onError() {

                }
            });
        } catch (Exception e) {
            System.out.print(true);
        }

        mNoneSingSvga.setCallback(new SVGACallback() {
            @Override
            public void onPause() {

            }

            @Override
            public void onFinished() {
                if (mNoneSingSvga != null) {
                    mNoneSingSvga.stopAnimation(true);
                    mNoneSingSvga.setVisibility(GONE);
                }

                if (mSVGAListener != null) {
                    mSVGAListener.onFinished();
                }
            }

            @Override
            public void onRepeat() {
                if (mNoneSingSvga != null && mNoneSingSvga.isAnimating()) {
                    mNoneSingSvga.stopAnimation(false);
                }
            }

            @Override
            public void onStep(int i, double v) {

            }
        });
    }

    // 优秀, 目前缺动画
    private void startPerfect() {
        U.getSoundUtils().play(GrabRoomFragment.TAG, R.raw.success);
        StatisticsAdapter.recordCountEvent(UserAccountManager.getInstance().getGategory(StatConstants.CATEGORY_GRAB),
                StatConstants.KEY_SONG_SUCCESS, null);
        mSingResultSvga.setVisibility(VISIBLE);
        mSingResultSvga.setLoops(1);
        SVGAParser parser = new SVGAParser(getContext());
        try {
            parser.parse("grab_sing_sucess_end.svga", new SVGAParser.ParseCompletion() {
                @Override
                public void onComplete(@NotNull SVGAVideoEntity videoItem) {
                    SVGADrawable drawable = new SVGADrawable(videoItem);
                    mSingResultSvga.setImageDrawable(drawable);
                    mSingResultSvga.startAnimation();
                }

                @Override
                public void onError() {

                }
            });
        } catch (Exception e) {
            System.out.print(true);
        }

        mSingResultSvga.setCallback(new SVGACallback() {
            @Override
            public void onPause() {

            }

            @Override
            public void onFinished() {
                if (mSingResultSvga != null) {
                    mSingResultSvga.stopAnimation(true);
                    mSingResultSvga.setVisibility(GONE);
                }

                if (mSVGAListener != null) {
                    mSVGAListener.onFinished();
                }
            }

            @Override
            public void onRepeat() {
                if (mSingResultSvga != null && mSingResultSvga.isAnimating()) {
                    mSingResultSvga.stopAnimation(false);
                }
            }

            @Override
            public void onStep(int i, double v) {

            }
        });
    }

    // 不够优秀，换字即可，目前缺动画
    private void startFailed(int model) {
        U.getSoundUtils().play(GrabRoomFragment.TAG, R.raw.lose);
        StatisticsAdapter.recordCountEvent(UserAccountManager.getInstance().getGategory(StatConstants.CATEGORY_GRAB),
                StatConstants.KEY_SONG_FAIL, null);
        mSingResultSvga.setVisibility(VISIBLE);
        mSingResultSvga.setLoops(1);
        SVGAParser parser = new SVGAParser(getContext());
        try {
            parser.parse("grab_sing_fail_end.svga", new SVGAParser.ParseCompletion() {
                @Override
                public void onComplete(@NotNull SVGAVideoEntity videoItem) {
                    SVGADrawable drawable = new SVGADrawable(videoItem, requestDynamicItem(model));
                    mSingResultSvga.setImageDrawable(drawable);
                    mSingResultSvga.startAnimation();
                }

                @Override
                public void onError() {

                }
            });
        } catch (Exception e) {
            System.out.print(true);
        }

        mSingResultSvga.setCallback(new SVGACallback() {
            @Override
            public void onPause() {

            }

            @Override
            public void onFinished() {
                if (mSingResultSvga != null) {
                    mSingResultSvga.stopAnimation(true);
                    mSingResultSvga.setVisibility(GONE);
                }

                if (mSVGAListener != null) {
                    mSVGAListener.onFinished();
                }
            }

            @Override
            public void onRepeat() {
                if (mSingResultSvga != null && mSingResultSvga.isAnimating()) {
                    mSingResultSvga.stopAnimation(false);
                }
            }

            @Override
            public void onStep(int i, double v) {

            }
        });
    }

    private SVGADynamicEntity requestDynamicItem(int model) {
        SVGADynamicEntity dynamicEntity = new SVGADynamicEntity();
        if (model == SING_MOMENT_END) {
            dynamicEntity.setDynamicImage(BitmapFactory.decodeResource(getResources(), R.drawable.fankuizi_3), "text");
        } else if (model == SING_NO_PASS_END) {
            dynamicEntity.setDynamicImage(BitmapFactory.decodeResource(getResources(), R.drawable.fankuizi_1), "text");
        } else if (model == SING_PASS_END) {
            dynamicEntity.setDynamicImage(BitmapFactory.decodeResource(getResources(), R.drawable.fankuizi_2), "text");
        } else if (model == SING_ENOUGH_END) {
            dynamicEntity.setDynamicImage(BitmapFactory.decodeResource(getResources(), R.drawable.fankuizi_4), "text");
        }
        return dynamicEntity;
    }

    @Override
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
        if (visibility == GONE) {
            this.mSVGAListener = null;
            if (mNoneSingSvga != null) {
                mNoneSingSvga.stopAnimation(false);
            }
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.mSVGAListener = null;
        if (mNoneSingSvga != null) {
            mNoneSingSvga.stopAnimation(true);
        }
    }
}
