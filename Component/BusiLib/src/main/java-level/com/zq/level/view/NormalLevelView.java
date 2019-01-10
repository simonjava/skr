package com.zq.level.view;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.common.utils.U;
import com.component.busilib.R;
import com.opensource.svgaplayer.SVGACallback;
import com.opensource.svgaplayer.SVGADrawable;
import com.opensource.svgaplayer.SVGADynamicEntity;
import com.opensource.svgaplayer.SVGAImageView;
import com.opensource.svgaplayer.SVGAParser;
import com.opensource.svgaplayer.SVGAVideoEntity;
import com.zq.level.utils.LevelConfigUtils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

// 正常的段位 到铂金段位 星星是斜着排的
public class NormalLevelView extends RelativeLayout {

    int starTotalWidth = U.getDisplayUtils().dip2px(100);   // 星星的横向排列的长度
    int starTotalHeight;    //  星星的纵向排列的高度,每增加一颗星星就加6dp

    int widthStar = U.getDisplayUtils().dip2px(18); //普通星星的宽度
    int heightStar = U.getDisplayUtils().dip2px(18); //普通星星的长度

    int largeStarWidth = U.getDisplayUtils().dip2px(20); //星星的宽度
    int largeStarHeight = U.getDisplayUtils().dip2px(20); //星星的长度

    ImageView mLevelIv; // 大段位
    ImageView mSubLeveIv;  // 子段位

    List<ImageView> starts = new ArrayList<>(); // 星星数

    List<ImageView> rightStarts = new ArrayList<>(); // 右边的星星

    int level; //父段位
    int subLevel; //子段位
    int totalStats; //总星星数
    int selecStats; //亮着的星星

    public NormalLevelView(Context context) {
        super(context);
        init();
    }

    public NormalLevelView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public NormalLevelView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.normal_level_view_layout, this);
        mLevelIv = (ImageView) this.findViewById(R.id.level_iv);
        mSubLeveIv = (ImageView) this.findViewById(R.id.sub_leve_iv);
    }

    public void bindData(int level, int subLevel, int totalStats, int selecStats) {
        this.level = level;
        this.subLevel = subLevel;
        this.totalStats = totalStats;
        this.selecStats = selecStats;
        starTotalHeight = totalStats * U.getDisplayUtils().dip2px(6);

        initStart();
    }

    private void initStart() {
        // 先清除所有的星星
        if (starts != null) {
            for (ImageView imageView : starts) {
                removeView(imageView);
            }
            starts.clear();
            rightStarts.clear();
        }

        float widDis = starTotalWidth / (totalStats + 1); //横向间距
        float highDis = starTotalHeight / (totalStats - 1); //纵向间距

        for (int i = 0; i < totalStats / 2 + 1; i++) {
            // 左边的星星
            ImageView imageView1 = new ImageView(getContext());
            RelativeLayout.LayoutParams rl1;
            int left = (int) (widDis * (i + 1) - widthStar / 2);
            int bottom = Math.abs(starTotalHeight / 2 - (int) (highDis * i));
            if (totalStats % 2 != 0 && i == totalStats / 2) {
                rl1 = new RelativeLayout.LayoutParams(largeStarWidth, largeStarHeight);
                left = left - (largeStarWidth - widthStar) / 2;
                rl1.setMargins(left, 0, 0, bottom);
            } else {
                rl1 = new RelativeLayout.LayoutParams(widthStar, heightStar);
                rl1.setMargins(left, 0, 0, bottom);
            }
            rl1.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            rl1.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            imageView1.setLayoutParams(rl1);
            if (i < selecStats) {
                imageView1.setBackground(ContextCompat.getDrawable(U.app(), R.drawable.zhanji_daxingxing_dianliang));
            } else {
                imageView1.setBackground(ContextCompat.getDrawable(U.app(), R.drawable.zhanji_xiaoxingxing_zhihui));
            }
            starts.add(imageView1);

            if (totalStats % 2 != 0 && i == totalStats / 2) {
                // 如果是放大的中间的星星，已处理，直接返回
                break;
            }

            // 与左边对称的星星
            ImageView imageView2 = new ImageView(getContext());
            RelativeLayout.LayoutParams rl2;
            rl2 = new RelativeLayout.LayoutParams(widthStar, heightStar);
            rl2.setMargins(0, 0, left, bottom);
            rl2.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            rl2.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            imageView2.setLayoutParams(rl2);
            if ((totalStats - 1 - i) < selecStats) {
                imageView2.setBackground(ContextCompat.getDrawable(U.app(), R.drawable.zhanji_daxingxing_dianliang));
            } else {
                imageView2.setBackground(ContextCompat.getDrawable(U.app(), R.drawable.zhanji_xiaoxingxing_zhihui));
            }
            rightStarts.add(0, imageView2);
        }

        starts.addAll(rightStarts);
        for (ImageView imageView : starts) {
            addView(imageView);
        }
    }

    /**
     * 段位变换动画
     *
     * @param viewGroup      承载动画的容器
     * @param levelBefore    之前的父段位
     * @param subLevelBefore 之前的子段位
     * @param levelNow       现在的父段位
     * @param sublevelNow    现在的子段位
     */
    public void levelChange(final ViewGroup viewGroup, final int levelBefore, final int subLevelBefore, final int levelNow, final int sublevelNow, final SVGAListener listener) {
        final SVGAImageView levelChange = new SVGAImageView(getContext());
        levelChange.setClearsAfterStop(false);   // 停在最后一帧
        levelChange.setLoops(1);  // 只播1次

        int[] location = new int[2];
        mLevelIv.getLocationOnScreen(location);

        RelativeLayout.LayoutParams rl = new RelativeLayout.LayoutParams(U.getDisplayUtils().dip2px(220), U.getDisplayUtils().dip2px(200));
        rl.setMargins(Math.abs(location[0]) - U.getDisplayUtils().dip2px((220 - 99) / 2),
                Math.abs(location[1]) - U.getDisplayUtils().dip2px((200 - 86) / 2), 0, 0);
        levelChange.setLayoutParams(rl);
        viewGroup.addView(levelChange);

        SVGAParser parser = new SVGAParser(getContext());
        try {
            parser.parse("duanwei_change.svga", new SVGAParser.ParseCompletion() {
                @Override
                public void onComplete(@NotNull SVGAVideoEntity videoItem) {
                    SVGADrawable drawable = new SVGADrawable(videoItem, requestDynamicBitmapItem(levelBefore, subLevelBefore, levelNow, sublevelNow));
                    levelChange.setImageDrawable(drawable);
                    levelChange.startAnimation();
                }

                @Override
                public void onError() {

                }
            });
        } catch (Exception e) {
            System.out.print(true);
        }

        levelChange.setCallback(new SVGACallback() {
            @Override
            public void onPause() {

            }

            @Override
            public void onFinished() {
                if (listener != null) {
                    listener.onFinish();
                }
            }

            @Override
            public void onRepeat() {

            }

            @Override
            public void onStep(int i, double v) {

            }
        });

    }

    private SVGADynamicEntity requestDynamicBitmapItem(int levelBefore, int subLevelBefore, int levelNow, int sublevelNow) {
        SVGADynamicEntity dynamicEntity = new SVGADynamicEntity();
        dynamicEntity.setDynamicImage(BitmapFactory.decodeResource(getResources(), LevelConfigUtils.getImageResoucesSubLevel(subLevelBefore)), "keyLevelBefore");
        dynamicEntity.setDynamicImage(BitmapFactory.decodeResource(getResources(), LevelConfigUtils.getImageResoucesLevel(levelBefore)), "keyMedalBefore");
        dynamicEntity.setDynamicImage(BitmapFactory.decodeResource(getResources(), LevelConfigUtils.getImageResoucesSubLevel(sublevelNow)), "keyLevelNew");
        dynamicEntity.setDynamicImage(BitmapFactory.decodeResource(getResources(), LevelConfigUtils.getImageResoucesLevel(levelNow)), "keyMedalNew");
        return dynamicEntity;
    }

    // 星星增加动画,从第几颗星增加到几个行
    // TODO: 2019/1/10 from 和 to都是从0开始计算
    public void starUp(final ViewGroup viewGroup, final int from, final int to, final SVGAListener listener) {
        final int dis = to - from;
        starUp(viewGroup, from, new SVGACallback() {
            @Override
            public void onPause() {

            }

            @Override
            public void onFinished() {
                if (dis > 0) {
                    starUp(viewGroup, from + 1, to, listener);
                } else {
                    if (listener != null) {
                        listener.onFinish();
                    }
                }
                ImageView imageView = starts.get(from);
                imageView.setBackground(ContextCompat.getDrawable(U.app(), R.drawable.zhanji_daxingxing_dianliang));
            }

            @Override
            public void onRepeat() {

            }

            @Override
            public void onStep(int i, double v) {

            }
        });
    }

    private void starUp(ViewGroup viewGroup, int index, SVGACallback callback) {
        if (index < 0 || index >= totalStats) {
            return;
        }
        final SVGAImageView starUp = new SVGAImageView(getContext());
        starUp.setLoops(1);  // 只播1次

        ImageView imageView = starts.get(index);
        int[] location = new int[2];
        imageView.getLocationOnScreen(location);

        if (totalStats % 2 != 0 && index == totalStats / 2) {
            RelativeLayout.LayoutParams rl = new RelativeLayout.LayoutParams(U.getDisplayUtils().dip2px(134), U.getDisplayUtils().dip2px(134));
            rl.setMargins(Math.abs(location[0]) - U.getDisplayUtils().dip2px((134 - 20) / 2),
                    Math.abs(location[1]) - U.getDisplayUtils().dip2px((134 - 20) / 2), 0, 0);
            starUp.setLayoutParams(rl);
        } else {
            RelativeLayout.LayoutParams rl = new RelativeLayout.LayoutParams((int) (U.getDisplayUtils().dip2px(134) * 0.9),
                    (int) (U.getDisplayUtils().dip2px(134) * 0.9));
            rl.setMargins(Math.abs(location[0]) - (int) (0.9 * U.getDisplayUtils().dip2px((int) ((134 - 20) / 2))),
                    Math.abs(location[1]) - (int) (0.9 * U.getDisplayUtils().dip2px((int) ((134 - 20) / 2))), 0, 0);
            starUp.setLayoutParams(rl);
        }

        viewGroup.addView(starUp);

        SVGAParser parser = new SVGAParser(getContext());
        try {
            parser.parse("star_up.svga", new SVGAParser.ParseCompletion() {
                @Override
                public void onComplete(@NotNull SVGAVideoEntity videoItem) {
                    SVGADrawable drawable = new SVGADrawable(videoItem);
                    starUp.setImageDrawable(drawable);
                    starUp.startAnimation();
                }

                @Override
                public void onError() {

                }
            });
        } catch (Exception e) {
            System.out.print(true);
        }
        starUp.setCallback(callback);
    }

    // 星星掉落动画 from必须大于to，表示从第几颗星星掉落
    // TODO: 2019/1/10 from 和 to都是从0开始计算
    public void starLoss(final ViewGroup viewGroup, final int from, final int to, final SVGAListener listener) {
        final int dis = from - to;
        starLoss(viewGroup, from, new SVGACallback() {
            @Override
            public void onPause() {

            }

            @Override
            public void onFinished() {
                if (dis > 0) {
                    starLoss(viewGroup, from - 1, to, listener);
                } else {
                    if (listener != null) {
                        listener.onFinish();
                    }
                }
            }

            @Override
            public void onRepeat() {

            }

            @Override
            public void onStep(int i, double v) {

            }
        });
    }

    private void starLoss(ViewGroup viewGroup, int index, SVGACallback callback) {
        if (index < 0 || index >= totalStats) {
            return;
        }
        final SVGAImageView starLoss = new SVGAImageView(getContext());
        starLoss.setClearsAfterStop(false);
        starLoss.setLoops(1);  // 只播1次

        final ImageView imageView = starts.get(index);
        int[] location = new int[2];
        imageView.getLocationOnScreen(location);

        if (totalStats % 2 != 0 && index == totalStats / 2) {
            RelativeLayout.LayoutParams rl = new RelativeLayout.LayoutParams(U.getDisplayUtils().dip2px(134), U.getDisplayUtils().dip2px(134));
            rl.setMargins(Math.abs(location[0]) - U.getDisplayUtils().dip2px((134 - 20) / 2),
                    Math.abs(location[1]) - U.getDisplayUtils().dip2px((134 - 20) / 2), 0, 0);
            starLoss.setLayoutParams(rl);
        } else {
            RelativeLayout.LayoutParams rl = new RelativeLayout.LayoutParams((int) (U.getDisplayUtils().dip2px(134) * 0.9),
                    (int) (U.getDisplayUtils().dip2px(134) * 0.9));
            rl.setMargins(Math.abs(location[0]) - (int) (0.9 * U.getDisplayUtils().dip2px((int) ((134 - 20) / 2))),
                    Math.abs(location[1]) - (int) (0.9 * U.getDisplayUtils().dip2px((int) ((134 - 20) / 2))), 0, 0);
            starLoss.setLayoutParams(rl);
        }

        viewGroup.addView(starLoss);

        SVGAParser parser = new SVGAParser(getContext());
        try {
            parser.parse("star_loss.svga", new SVGAParser.ParseCompletion() {
                @Override
                public void onComplete(@NotNull SVGAVideoEntity videoItem) {
                    SVGADrawable drawable = new SVGADrawable(videoItem);
                    imageView.setBackground(ContextCompat.getDrawable(U.app(), R.drawable.zhanji_xiaoxingxing_zhihui));
                    starLoss.setImageDrawable(drawable);
                    starLoss.startAnimation();
                }

                @Override
                public void onError() {

                }
            });
        } catch (Exception e) {
            System.out.print(true);
        }
        starLoss.setCallback(callback);
    }

    public interface SVGAListener {
        void onFinish();
    }
}
