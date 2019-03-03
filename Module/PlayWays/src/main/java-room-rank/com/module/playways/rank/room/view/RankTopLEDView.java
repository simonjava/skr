package com.module.playways.rank.room.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.common.log.MyLog;
import com.common.utils.U;
import com.module.rank.R;
import com.opensource.svgaplayer.SVGACallback;
import com.opensource.svgaplayer.SVGADrawable;
import com.opensource.svgaplayer.SVGAImageView;
import com.opensource.svgaplayer.SVGAParser;
import com.opensource.svgaplayer.SVGAVideoEntity;

import org.jetbrains.annotations.NotNull;

/**
 * 顶部动画
 */
public class RankTopLEDView extends RelativeLayout {

    public final static String TAG = "RankTopLEDView";

    SVGAImageView mDengSvga;
    int postion;             //view的位置，对应加载什么动画

    public RankTopLEDView(Context context) {
        this(context, null);
    }

    public RankTopLEDView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RankTopLEDView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.rankLED);
        postion = typedArray.getInt(R.styleable.rankLED_position, 0);
        typedArray.recycle();

        inflate(getContext(), R.layout.rank_top_led_view, this);
        mDengSvga = (SVGAImageView) findViewById(R.id.deng_svga);
        initSVGA();
    }

    // 初始状态
    public void initSVGA() {
        MyLog.d(TAG, " initSVGA " + " postion = " + postion);
        mDengSvga.setCallback(null);
        mDengSvga.stopAnimation(true);
        setVisibility(VISIBLE);
        mDengSvga.setVisibility(VISIBLE);

        String assetsName = "";
        switch (postion) {
            case 0:
                assetsName = "rank_love_left_beat.svga";
                break;
            case 1:
                assetsName = "rank_love_mid.svga";
                break;
            case 2:
                assetsName = "rank_love_right_beat.svga";
                break;
        }
        if (postion == 1) {
            mDengSvga.setLoops(1);
        } else {
            mDengSvga.setLoops(0);
        }

        SVGAParser parser = new SVGAParser(U.app());
        try {
            parser.parse(assetsName, new SVGAParser.ParseCompletion() {
                @Override
                public void onComplete(@NotNull SVGAVideoEntity svgaVideoEntity) {
                    SVGADrawable drawable = new SVGADrawable(svgaVideoEntity);
                    mDengSvga.setImageDrawable(drawable);
                    mDengSvga.startAnimation();
                }

                @Override
                public void onError() {

                }
            });
        } catch (Exception e) {
            System.out.print(true);
        }

        if (postion == 1) {
            mDengSvga.setCallback(new SVGACallback() {
                @Override
                public void onPause() {

                }

                @Override
                public void onFinished() {
                    if (mDengSvga != null) {
                        mDengSvga.stopAnimation(false);
                    }

                    playBaoDengAnimation();
                }

                @Override
                public void onRepeat() {
                    if (mDengSvga != null && mDengSvga.isAnimating()) {
                        mDengSvga.stopAnimation(false);
                    }
                }

                @Override
                public void onStep(int i, double v) {

                }
            });
        }
    }

    // 爆灯或者灭灯
    public void setSVGAMode(boolean isBao) {
        MyLog.d(TAG, "setSVGAMode" + " isBao=" + isBao + "postion" + postion);
        mDengSvga.setCallback(null);
        mDengSvga.stopAnimation(true);
        setVisibility(VISIBLE);
        String assetsName = isBao ? "rank_love_left.svga" : "rank_fork_left.svga";
        switch (postion) {
            case 0:
                assetsName = isBao ? "rank_love_left.svga" : "rank_fork_left.svga";
                break;
            case 1:
                assetsName = isBao ? "rank_love_mid.svga" : "rank_fork_mid.svga";
                break;
            case 2:
//                assetsName = isBao ? "rank_love_right.svga" : "rank_fork_right.svga";
                assetsName = isBao ? "rank_love_right.svga" : "rank_fork_end.svga";
                break;
        }
        mDengSvga.setVisibility(VISIBLE);
        mDengSvga.setLoops(1);
        SVGAParser parser = new SVGAParser(U.app());
        try {
            parser.parse(assetsName, new SVGAParser.ParseCompletion() {
                @Override
                public void onComplete(@NotNull SVGAVideoEntity svgaVideoEntity) {
                    SVGADrawable drawable = new SVGADrawable(svgaVideoEntity);
                    mDengSvga.setImageDrawable(drawable);
                    mDengSvga.startAnimation();
                }

                @Override
                public void onError() {

                }
            });
        } catch (Exception e) {
            System.out.print(true);
        }

        mDengSvga.setCallback(new SVGACallback() {
            @Override
            public void onPause() {

            }

            @Override
            public void onFinished() {
                if (mDengSvga != null) {
                    mDengSvga.setCallback(null);
                    mDengSvga.stopAnimation(false);
                }
                if (isBao) {
                    playBaoDengAnimation();
                }
            }

            @Override
            public void onRepeat() {
                if (mDengSvga != null && mDengSvga.isAnimating()) {
                    mDengSvga.stopAnimation(false);
                }
            }

            @Override
            public void onStep(int i, double v) {

            }
        });
    }

    private void playBaoDengAnimation() {
        String assetsName = "rank_love_left_beat.svga";
        switch (postion) {
            case 0:
                assetsName = "rank_love_left_beat.svga";
                break;
            case 1:
                assetsName = "rank_love_mid_beat.svga";
                break;
            case 2:
                assetsName = "rank_love_right_beat.svga";
                break;
        }
        mDengSvga.setVisibility(VISIBLE);
        mDengSvga.setCallback(null);
        mDengSvga.setLoops(0);
        SVGAParser parser = new SVGAParser(U.app());
        try {
            parser.parse(assetsName, new SVGAParser.ParseCompletion() {
                @Override
                public void onComplete(@NotNull SVGAVideoEntity svgaVideoEntity) {
                    SVGADrawable drawable = new SVGADrawable(svgaVideoEntity);
                    mDengSvga.setImageDrawable(drawable);
                    mDengSvga.startAnimation();
                }

                @Override
                public void onError() {

                }
            });
        } catch (Exception e) {
            System.out.print(true);
        }
    }

    @Override
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
        if (visibility == GONE) {
            if (mDengSvga != null) {
                mDengSvga.setCallback(null);
                mDengSvga.stopAnimation(true);
            }
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mDengSvga != null) {
            mDengSvga.setCallback(null);
            mDengSvga.stopAnimation(true);
        }
    }
}
