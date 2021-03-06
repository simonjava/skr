package com.component.lyrics.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.util.AttributeSet;
import android.view.View;

import com.common.log.MyLog;
import com.common.utils.U;
import com.component.busilib.R;
import com.component.lyrics.LyricsReader;
import com.component.lyrics.model.LyricsInfo;
import com.component.lyrics.model.LyricsLineInfo;
import com.component.lyrics.utils.LyricsUtils;

import java.lang.ref.WeakReference;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

/**
 * @Description: 歌词抽象视图
 * @param:
 * @return:
 * @throws
 * @author: zhangliangming
 * @date: 2018-04-21 9:06
 */
public abstract class AbstractLrcView extends View {
    public final String TAG = "AbstractLrcView";
    /**
     * 初始
     */
    public static final int LRCSTATUS_INIT = 0;
    /**
     * 加载中
     */
    public static final int LRCSTATUS_LOADING = 1;

    /**
     * 无歌词-去搜索（SearchLyricsListener不为空时）
     */
    public final int LRCSTATUS_NOLRC_GOTOSEARCH = 2;
    /**
     * 无歌词-显示默认文本
     */
    public final int LRCSTATUS_NOLRC_DEFTEXT = 3;

    /**
     * 绘画歌词
     */
    public static final int LRCSTATUS_LRC = 4;
    /**
     * 绘画歌词出错
     */
    public static final int LRCSTATUS_ERROR = 5;
    /**
     * 不支持格式
     */
    public static final int LRCSTATUS_NONSUPPORT = 6;

    /**
     * 没有的额外的歌词
     */
    public static final int EXTRALRCTYPE_NOLRC = 0;
    /**
     * 翻译歌词
     */
    public static final int EXTRALRCTYPE_TRANSLATELRC = 1;
    /**
     * 音译歌词
     */
    public static final int EXTRALRCTYPE_TRANSLITERATIONLRC = 2;
    /**
     * 翻译和音译歌词
     */
    public static final int EXTRALRCTYPE_BOTH = 3;

    /**
     * 初始
     */
    public static final int LRCPLAYERSTATUS_INIT = 0;
    /**
     * 播放
     */
    public static final int LRCPLAYERSTATUS_PLAY = 1;

    /**
     * seekto
     */
    public static final int LRCPLAYERSTATUS_SEEKTO = 2;


    /**
     * 默认歌词画笔
     */
    protected Paint mPaint;
    /**
     * 默认画笔颜色
     */
    protected int[] mPaintColors = new int[]{
            U.getColorUtils().parserColor("#555555"),
            U.getColorUtils().parserColor("#555555")
    };
    /**
     * 高亮歌词画笔
     */
    protected Paint mPaintHL;

    /**
     * 高亮歌词画笔
     */
    protected Paint mSubPaintHL;

    //高亮颜色
    protected int[] mPaintHLColors = new int[]{
            U.getColorUtils().parserColor("#0288d1"),
            U.getColorUtils().parserColor("#0288d1")
    };

    //高亮副颜色
    protected int mSubPaintHLColor = U.getColorUtils().parserColor("#555555");

    /**
     * 轮廓画笔
     */
    protected Paint mPaintOutline;

    /**
     * 额外歌词画笔
     */
    protected Paint mExtraLrcPaint;
    /**
     * 额外歌词高亮画笔
     */
    protected Paint mExtraLrcPaintHL;
    /**
     * 轮廓画笔
     */
    protected Paint mExtraLrcPaintOutline;

    /**
     * 默认提示文本
     */
    protected String mDefText;
    /**
     * 正在加载提示文本
     */
    protected String mLoadingText;
    /**
     * 加载歌词出错
     */
    protected String mLoadErrorText;
    /**
     * 不支持歌词格式文本
     */
    protected String mNonsupportText;
    /**
     * 搜索提示文本
     */
    protected String mGotoSearchText;

    /**
     * 搜索歌词区域
     */
    protected RectF mGotoSearchBtnRect;
    /**
     * 是否在去搜索歌词矩形区域内
     */
    protected boolean isInGotoSearchBtnRect = false;

    /**
     * 绘画去搜索歌词文字矩形画笔
     */
    protected Paint mGotoSearchRectPaint;

    /**
     * 去搜索歌词文字颜色
     */
    protected int mGotoSearchTextColor = U.getColorUtils().parserColor("#0288d1");

    /**
     * 绘画去搜索歌词文字画笔
     */
    protected Paint mGotoSearchTextPaint;
    /**
     * 按下搜索歌词文字颜色
     */
    protected int mGotoSearchTextPressedColor = U.getColorUtils().parserColor("#ffffff");

    /**
     * 歌词状态
     */
    protected int mLrcStatus = LRCSTATUS_INIT;
    /**
     * 搜索歌词回调
     */
    protected SearchLyricsListener mSearchLyricsListener;

    /**
     * 显示翻译歌词
     */
    public static final int EXTRALRCSTATUS_SHOWTRANSLATELRC = 0;
    /**
     * 显示音译歌词
     */
    public static final int EXTRALRCSTATUS_SHOWTRANSLITERATIONLRC = 1;
    /**
     * 只显示默认歌词
     */
    public static final int EXTRALRCSTATUS_NOSHOWEXTRALRC = 2;
    /**
     * 默认只显示默认歌词
     */
    public int mExtraLrcStatus = EXTRALRCSTATUS_NOSHOWEXTRALRC;

    /**
     * 空行高度
     */
    protected float mSpaceLineHeight = U.getDisplayUtils().dip2px(20);
    /**
     * 歌词字体大小
     */
    protected float mFontSize = 30;

    /**
     * 左右间隔距离
     */
    protected float mPaddingLeftOrRight = 15;

    /**
     * 歌词的最大宽度
     */
    protected float mTextMaxWidth = 0;
    /**
     * 当前歌词的所在行数
     */
    protected int mLyricsLineNum = 0;
    /**
     * 分割歌词的行索引
     */
    protected int mSplitLyricsLineNum = 0;
    /**
     * 当前歌词的第几个字
     */
    protected int mLyricsWordIndex = -1;
    /**
     * 分割歌词当前歌词的第几个字
     */
    protected int mSplitLyricsWordIndex = -1;

    /**
     * 当前歌词第几个字 已经播放的时间
     */
    protected float mLyricsWordHLTime = 0;

    /**
     * 额外的歌词类型
     */
    protected int mExtraLrcType = EXTRALRCTYPE_NOLRC;
    /**
     * 额外歌词监听事件
     */
    protected ExtraLyricsListener mExtraLyricsListener;
    /**
     * 额外歌词空行高度
     */
    protected float mExtraLrcSpaceLineHeight = 30;
    /**
     * 额外歌词字体大小
     */
    protected float mExtraLrcFontSize = 30;

    /**
     * 当前额外分割歌词的所在行数
     */
    protected int mExtraSplitLyricsLineNum = 0;
    /**
     * 当前额外歌词的第几个字
     */
    protected int mExtraLyricsWordIndex = -1;
    /**
     * 当前额外分割歌词的第几个字
     */
    protected int mExtraSplitLyricsWordIndex = -1;
    /**
     * 绘画类型：lrc类型
     */
    public static final int TRANSLATE_DRAW_TYPE_LRC = 0;
    /**
     * 绘画类型：动感歌词类型
     */
    public static final int TRANSLATE_DRAW_TYPE_DYNAMIC = 1;
    /**
     * 翻译歌词绘画类型
     */
    protected int mTranslateDrawType = TRANSLATE_DRAW_TYPE_LRC;

    /**
     * 翻译歌词的高亮宽度
     */
    protected float mTranslateLyricsWordHLTime = 0;

    ////////////////////////////////////////////////////////////////////

    /**
     * 歌词处理类
     */
    protected LyricsReader mLyricsReader;

    /**
     * 歌词列表
     */
    protected TreeMap<Integer, LyricsLineInfo> mLrcLineInfos;
    /**
     * 翻译行歌词列表
     */
    protected List<LyricsLineInfo> mTranslateLrcLineInfos;
    /**
     * 音译歌词行
     */
    protected List<LyricsLineInfo> mTransliterationLrcLineInfos;

    ///////////////////////////////歌词绘画播放器//////////////////////////////////

    protected byte[] lock = new byte[0];
    /**
     * 播放器类型
     */
    protected int mLrcPlayerStatus = LRCPLAYERSTATUS_INIT;

    /**
     * 播放器开始时间，用于计算歌曲播放的时长
     */
    protected long mPlayerStartTime = 0;
    /**
     * 播放器开始后，所经历的播放时长
     */
    protected long mPlayerSpendTime = 0;

    /**
     * 当前播放进度
     */
    protected long mCurPlayingTime = 0;
    /**
     * 刷新延时时间
     */
    protected long mRefreshTime = 20;

    /**
     * 已经结束的行数，一个行可能有多个分割行
     */
    protected int mCurEndLineNum = 0;


    /**
     * 已经结束的分割行行数
     */
    protected int mCurSplitLyricsEndLineNum = 0;

    /**
     * 子线程用于执行耗时任务
     */
    protected Handler mWorkerHandler;
    //创建异步HandlerThread
    protected HandlerThread mHandlerThread;

    /**
     * 要不要用逐字画
     */
    protected boolean mEnableVerbatim = true;

    /**
     * 是不是已经抛出最后一行结束事件
     */
    protected volatile boolean mHasPostLastLineEndEvent = false;

    /**
     * 需要倒计时的行
     */
    protected Set<Integer> mNeedCountDownLine = new HashSet<>();

    /**
     * 处理ui任务
     */
    protected Handler mUIHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Context context = mActivityWR.get();
            if (context != null) {
                synchronized (lock) {
                    if (mLrcPlayerStatus == LRCPLAYERSTATUS_PLAY && mLyricsReader != null) {
                        invalidateView();
                        long endTime = System.currentTimeMillis();
                        long updateTime = (endTime - mPlayerStartTime) - mPlayerSpendTime;
                        mPlayerSpendTime = (endTime - mPlayerStartTime);
                        long delayMs = mRefreshTime - updateTime;
                        mWorkerHandler.sendEmptyMessageDelayed(0, Math.max(0, delayMs));
                    }
                }
            }
        }
    };

    public Set<Integer> getNeedCountDownLine() {
        return mNeedCountDownLine;
    }

    /**
     * 需要展示倒计时的行
     * @param needCountDownLine
     */
    public void setNeedCountDownLine(Set<Integer> needCountDownLine) {
        mNeedCountDownLine = needCountDownLine;
    }

    public boolean isEnableVerbatim() {
        return mEnableVerbatim;
    }

    public void setEnableVerbatim(boolean enableVerbatim) {
        mEnableVerbatim = enableVerbatim;
    }

    private WeakReference<Context> mActivityWR;


    public AbstractLrcView(Context context) {
        super(context);
        init(context, null);
    }

    public AbstractLrcView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    /**
     * @throws
     * @Description: 初始
     * @param:
     * @return:
     * @author: zhangliangming
     * @date: 2018-04-21 9:08
     */
    private void init(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.lrc_view);
//        RegisterHelper.verify();

        //初始默认数据
        mDefText = context.getString(R.string.def_text);
        mLoadingText = context.getString(R.string.loading_text);
        mLoadErrorText = context.getString(R.string.load_error_text);
        mNonsupportText = context.getString(R.string.nonsupport_text);
        mGotoSearchText = context.getString(R.string.goto_search_text);

        if (typedArray.hasValue(R.styleable.lrc_view_ly_paint_color_from) && typedArray.hasValue(R.styleable.lrc_view_ly_paint_color_to)) {
            mPaintColors = new int[]{typedArray.getColor(R.styleable.lrc_view_ly_paint_color_from, Color.BLUE), typedArray.getColor(R.styleable.lrc_view_ly_paint_color_to, Color.BLUE)};
        }

        if (typedArray.hasValue(R.styleable.lrc_view_ly_high_light_paint_color_from) && typedArray.hasValue(R.styleable.lrc_view_ly_high_light_paint_color_to)) {
            mPaintHLColors = new int[]{typedArray.getColor(R.styleable.lrc_view_ly_high_light_paint_color_from, Color.BLUE), typedArray.getColor(R.styleable.lrc_view_ly_high_light_paint_color_to, Color.BLUE)};
        }

        if (typedArray.hasValue(R.styleable.lrc_view_ly_enable_verbatim)) {
            mEnableVerbatim = typedArray.getBoolean(R.styleable.lrc_view_ly_enable_verbatim, true);
        }
        //默认画笔
        mPaint = new com.common.view.ExPaint();
        mPaint.setDither(true);
        mPaint.setAntiAlias(true);
        mFontSize = U.getDisplayUtils().dip2px(typedArray.getFloat(R.styleable.lrc_view_ly_paint_text_size, 10));
        mPaint.setFakeBoldText(true);
        mPaint.setTextSize(mFontSize);

        //高亮画笔
        mPaintHL = new com.common.view.ExPaint();
        mPaintHL.setDither(true);
        mPaintHL.setAntiAlias(true);
        float hlSize = U.getDisplayUtils().dip2px(typedArray.getFloat(R.styleable.lrc_view_ly_high_light_paint_text_size, 10));
        mPaintHL.setFakeBoldText(true);
        mPaintHL.setTextSize(hlSize);

        //高亮画笔,画不高亮的部分
        mSubPaintHL = new com.common.view.ExPaint();
        mSubPaintHL.setDither(true);
        mSubPaintHL.setAntiAlias(true);
        mSubPaintHL.setFakeBoldText(true);
        mSubPaintHLColor = typedArray.getColor(R.styleable.lrc_view_ly_high_light_sub_paint_color, Color.BLACK);
        mSubPaintHL.setColor(mSubPaintHLColor);
        mSubPaintHL.setTextSize(hlSize);

        //轮廓画笔
        mPaintOutline = new com.common.view.ExPaint();
        mPaintOutline.setDither(true);
        mPaintOutline.setAntiAlias(true);
        int color = typedArray.getColor(R.styleable.lrc_view_ly_outline_paint_color, Color.BLACK);
        mPaintOutline.setColor(color);
        mPaintOutline.setTextSize(mFontSize);
        typedArray.recycle();
        //额外歌词画笔
        mExtraLrcPaint = new com.common.view.ExPaint();
        mExtraLrcPaint.setDither(true);
        mExtraLrcPaint.setAntiAlias(true);
        mExtraLrcPaint.setTextSize(mExtraLrcFontSize);

        //额外高亮歌词画笔
        mExtraLrcPaintHL = new com.common.view.ExPaint();
        mExtraLrcPaintHL.setDither(true);
        mExtraLrcPaintHL.setAntiAlias(true);
        mExtraLrcPaintHL.setTextSize(mExtraLrcFontSize);

        //额外画笔轮廓
        mExtraLrcPaintOutline = new com.common.view.ExPaint();
        mExtraLrcPaintOutline.setDither(true);
        mExtraLrcPaintOutline.setAntiAlias(true);
        mExtraLrcPaintOutline.setColor(Color.BLACK);
        mExtraLrcPaintOutline.setTextSize(mExtraLrcFontSize);

        //绘画去搜索歌词画笔
        mGotoSearchTextPaint = new com.common.view.ExPaint();
        mGotoSearchTextPaint.setDither(true);
        mGotoSearchTextPaint.setAntiAlias(true);
        mGotoSearchTextPaint.setTextSize(mFontSize);

        //绘画去搜索歌词矩形画笔
        mGotoSearchRectPaint = new com.common.view.ExPaint();
        mGotoSearchRectPaint.setDither(true);
        mGotoSearchRectPaint.setAntiAlias(true);
        mGotoSearchRectPaint.setStrokeWidth(2);
        mGotoSearchRectPaint.setTextSize(mFontSize);

        //
        mActivityWR = new WeakReference<Context>(context);
        //创建异步HandlerThread
        mHandlerThread = new HandlerThread("updateLrcData", Process.THREAD_PRIORITY_BACKGROUND);
        //必须先开启线程
        mHandlerThread.start();
        //子线程Handler
        mWorkerHandler = new Handler(mHandlerThread.getLooper(), new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                Context context = mActivityWR.get();
                if (context != null) {
                    synchronized (lock) {
                        if (mLyricsReader != null) {
                            updateView(mCurPlayingTime + mPlayerSpendTime);
                            if (mLrcPlayerStatus == LRCPLAYERSTATUS_PLAY) {
                                mUIHandler.sendEmptyMessage(0);
                            } else if (mLrcPlayerStatus == LRCPLAYERSTATUS_SEEKTO) {
                                invalidateView();
                            }
                        }
                    }
                }
                return false;
            }
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        onDrawView(canvas);
    }

    /**
     * 绘画视图
     *
     * @param canvas
     */
    private void onDrawView(Canvas canvas) {
        synchronized (lock) {
            mPaint.setAlpha(255);
            mPaintHL.setAlpha(255);
            mExtraLrcPaint.setAlpha(255);
            mExtraLrcPaintHL.setAlpha(255);
            if (mLrcStatus == LRCSTATUS_INIT || mLrcStatus == LRCSTATUS_NOLRC_DEFTEXT) {
                //绘画默认文本
                String defText = mDefText;
                float textWidth = LyricsUtils.getTextWidth(mPaint, defText);
                int textHeight = LyricsUtils.getTextHeight(mPaint);
                float hlWidth = textWidth / 2;
                float x = (getWidth() - textWidth) / 2;
                float y = (getHeight() + textHeight) / 2;
                // TODO: 2019/1/4 这个先注掉
//                LyricsUtils.drawOutline(canvas, mPaintOutline, defText, x, y);
                LyricsUtils.drawDynamicText(canvas, mPaint, mPaintHL, mPaintColors, mPaintHLColors, defText, hlWidth, x, y);
                MyLog.d("AbstractLrcView", "mLrcStatus == LRCSTATUS_INIT || mLrcStatus == LRCSTATUS_NOLRC_DEFTEXT");
            } else if (mLrcStatus == LRCSTATUS_LOADING || mLrcStatus == LRCSTATUS_ERROR || mLrcStatus == LRCSTATUS_NONSUPPORT) {
                //绘画加载中文本
                String text = mDefText;
                if (mLrcStatus == LRCSTATUS_LOADING) {
                    text = mLoadingText;
                } else if (mLrcStatus == LRCSTATUS_ERROR) {
                    text = mLoadingText;
                } else if (mLrcStatus == LRCSTATUS_NONSUPPORT) {
                    text = mNonsupportText;
                }
                float textWidth = LyricsUtils.getTextWidth(mPaint, text);
                int textHeight = LyricsUtils.getTextHeight(mPaint);
                float x = (getWidth() - textWidth) / 2;
                float y = (getHeight() + textHeight) / 2;
                LyricsUtils.drawOutline(canvas, mPaintOutline, text, x, y);
                LyricsUtils.drawText(canvas, mPaint, mPaintColors, text, x, y);
                MyLog.d("AbstractLrcView", "mLrcStatus == LRCSTATUS_LOADING || mLrcStatus == LRCSTATUS_ERROR || mLrcStatus == LRCSTATUS_NONSUPPORT");
            } else if (mLrcStatus == LRCSTATUS_NOLRC_GOTOSEARCH) {
                String btnText = mGotoSearchText;
                //绘画搜索歌词按钮
                drawGoToSearchBtn(canvas, mGotoSearchRectPaint, mGotoSearchTextPaint, btnText);
            } else if (mLrcStatus == LRCSTATUS_LRC) {
                onDrawLrcView(canvas);
                int lyricsLineNum = mLyricsLineNum;
                LyricsLineInfo currentLine = mLrcLineInfos.get(lyricsLineNum);
                int splitLyricsLineNum = mSplitLyricsLineNum;
                LyricsLineInfo realInfo = currentLine.getSplitLyricsLineInfos().get(splitLyricsLineNum);

//                MyLog.d("AbstractLrcViewAbstractLrcView", "realInfo lyric is " + realInfo.getLineLyrics() + ", lyricsLineNum is " + lyricsLineNum + ", splitLyricsLineNum " + splitLyricsLineNum
//                        + ", end time is " + endTime + ", lyricProgress " + lyricProgress);

                if ((mCurEndLineNum != lyricsLineNum || mCurSplitLyricsEndLineNum != splitLyricsLineNum)) {
//                    EventBus.getDefault().post(new LrcEvent.LineEndEvent(mCurEndLineNum));
                    MyLog.d("AbstractLrcViewAbstractLrcView", "结束 num is " + mCurEndLineNum);
                    mCurEndLineNum = lyricsLineNum;
                    mCurSplitLyricsEndLineNum = splitLyricsLineNum;
//                    U.getToastUtil().showShort("结束");
                }

                if (isLastLyricLine(lyricsLineNum, splitLyricsLineNum)) {
                    long endTime = realInfo.getEndTime();
                    long lyricProgress = mPlayerSpendTime + getCurPlayingTime();
                    if (endTime < lyricProgress && !mHasPostLastLineEndEvent) {
                        mHasPostLastLineEndEvent = true;
//                        EventBus.getDefault().post(new LrcEvent.LineEndEvent(lyricsLineNum));
                        mCurEndLineNum = lyricsLineNum;
                        mCurSplitLyricsEndLineNum = splitLyricsLineNum;
                        MyLog.d("AbstractLrcViewAbstractLrcView", "结束 num is " + lyricsLineNum);
//                        U.getToastUtil().showShort("结束");
                    }
                } else {
                    mHasPostLastLineEndEvent = false;
                }
            }
        }
    }

    //最后一行歌词
    private boolean isLastLyricLine(int lyricsLineNum, int splitLyricsLineNum) {
        if (lyricsLineNum == mLrcLineInfos.size() - 1) {
            LyricsLineInfo lineInfo = mLrcLineInfos.get(lyricsLineNum);
            if (lineInfo != null) {
                List<LyricsLineInfo> l = lineInfo.getSplitLyricsLineInfos();
                if (l != null) {
                    if (l.size() - 1 == splitLyricsLineNum) {
                        return true;
                    }
                }
            }
        }
        return false;
    }


    /**
     * 绘画去搜索歌词按钮
     *
     * @param canvas
     * @param paintRect 画矩形画笔
     * @param paintText 画文字画笔
     * @param btnText   按钮提示文字
     */
    public void drawGoToSearchBtn(Canvas canvas, Paint paintRect, Paint paintText, String btnText) {
        if (isInGotoSearchBtnRect) {
            paintRect.setStyle(Paint.Style.FILL);
            paintText.setColor(mGotoSearchTextPressedColor);
        } else {
            paintRect.setStyle(Paint.Style.STROKE);
            paintText.setColor(mGotoSearchTextColor);
        }
        paintRect.setColor(mGotoSearchTextColor);

        //
        int textY = (getHeight() + LyricsUtils.getTextHeight(paintText)) / 2;
        int textWidth = (int) LyricsUtils.getTextWidth(paintText, btnText);
        int textX = (getWidth() - textWidth) / 2;


        //初始化搜索
        if (mGotoSearchBtnRect == null) {
            int padding = LyricsUtils.getRealTextHeight(paintText) / 2;
            int rectTop = textY - LyricsUtils.getTextHeight(paintText) - padding;
            int rectLeft = textX - padding;
            int rectRight = rectLeft + textWidth + padding * 2;
            int rectBottom = textY + padding;
            mGotoSearchBtnRect = new RectF(rectLeft, rectTop, rectRight, rectBottom);
        }

        canvas.drawRoundRect(mGotoSearchBtnRect, 15, 15, paintRect);
        canvas.drawText(btnText, textX, textY, paintText);
    }

    /**
     * @throws
     * @Description: 刷新视图
     * @param:
     * @return:
     * @author: zhangliangming
     * @date: 2018-04-21 9:24
     */
    public synchronized void invalidateView() {
        if (Looper.getMainLooper() == Looper.myLooper()) {
            //  当前线程是主UI线程，直接刷新。
            invalidate();
        } else {
            //  当前线程是非UI线程，post刷新。
            postInvalidate();
        }
    }

    /**
     * view的draw歌词调用方法
     *
     * @param canvas
     * @return
     */
    protected abstract void onDrawLrcView(Canvas canvas);

    /**
     * 更新视图
     *
     * @param playProgress
     */
    protected abstract void updateView(long playProgress);

    /**
     * 获取额外歌词类型
     *
     * @return
     */
    public int getExtraLrcType() {
        return mExtraLrcType;
    }

    /**
     * 获取额外歌词的显示状态
     *
     * @return
     */
    public int getExtraLrcStatus() {
        return mExtraLrcStatus;
    }

    /**
     * 获取歌词状态
     *
     * @return
     */
    public int getLrcStatus() {
        return mLrcStatus;
    }

    /**
     * 初始歌词数据
     */
    public void initLrcData() {
        synchronized (lock) {
            mLyricsReader = null;
            mLrcStatus = LRCSTATUS_INIT;
            resetData();
            initExtraLrcTypeAndCallBack();
            invalidateView();
        }
    }

    public long getCurPlayingTime() {
        return mCurPlayingTime;
    }

    public Paint getSubPaintHL() {
        return mSubPaintHL;
    }

    public int getSubPaintHLColor() {
        return mSubPaintHLColor;
    }

    /**
     * 设置歌词状态
     *
     * @param lrcStatus
     */
    public void setLrcStatus(int lrcStatus) {
        this.mLrcStatus = lrcStatus;
        invalidateView();
    }

    /**
     * 设置默认颜色
     *
     * @param paintColor       至少两种颜色
     * @param isInvalidateView 是否更新视图
     */
    public void setPaintColor(int[] paintColor, boolean isInvalidateView) {
        this.mPaintColors = paintColor;
        if (isInvalidateView) {
            invalidateView();
        }
    }

    /**
     * 设置高亮颜色
     *
     * @param paintHLColor     至少两种颜色
     * @param isInvalidateView 是否更新视图
     */
    public void setPaintHLColor(int[] paintHLColor, boolean isInvalidateView) {
        this.mPaintHLColors = paintHLColor;
        if (isInvalidateView) {
            invalidateView();
        }
    }

    /**
     * 设置搜索文字颜色
     *
     * @param mGotoSearchTextColor
     */
    public void setGotoSearchTextColor(int mGotoSearchTextColor) {
        setGotoSearchTextColor(mGotoSearchTextColor, false);
    }

    /**
     * 设置搜索文字颜色
     *
     * @param mGotoSearchTextColor
     * @param isInvalidateView
     */
    public void setGotoSearchTextColor(int mGotoSearchTextColor, boolean isInvalidateView) {
        this.mGotoSearchTextColor = mGotoSearchTextColor;
        if (isInvalidateView) {
            invalidateView();
        }
    }

    /**
     * 设置搜索点击文字颜色
     *
     * @param mGotoSearchTextPressedColor
     */
    public void setGotoSearchTextPressedColor(int mGotoSearchTextPressedColor) {
        setGotoSearchTextPressedColor(mGotoSearchTextPressedColor, false);
    }

    /**
     * 设置搜索点击文字颜色
     *
     * @param mGotoSearchTextPressedColor
     * @param isInvalidateView
     */
    public void setGotoSearchTextPressedColor(int mGotoSearchTextPressedColor, boolean isInvalidateView) {
        this.mGotoSearchTextPressedColor = mGotoSearchTextPressedColor;
        if (isInvalidateView) {
            invalidateView();
        }
    }

    /**
     * 设置字体文件
     *
     * @param typeFace
     * @param isInvalidateView 是否更新视图
     */
    public void setTypeFace(Typeface typeFace, boolean isInvalidateView) {

        if (typeFace != null) {
            mPaint.setTypeface(typeFace);
            mPaintHL.setTypeface(typeFace);
            mPaintOutline.setTypeface(typeFace);
            mExtraLrcPaint.setTypeface(typeFace);
            mExtraLrcPaintHL.setTypeface(typeFace);
            mExtraLrcPaintOutline.setTypeface(typeFace);
        }

        if (isInvalidateView) {
            invalidateView();
        }
    }

    /**
     * 设置额外歌词事件
     *
     * @param extraLyricsListener
     */
    public void setExtraLyricsListener(ExtraLyricsListener extraLyricsListener) {
        this.mExtraLyricsListener = extraLyricsListener;
    }

    /**
     * 设置搜索歌词事件
     *
     * @param searchLyricsListener
     */
    public void setSearchLyricsListener(SearchLyricsListener searchLyricsListener) {
        this.mSearchLyricsListener = searchLyricsListener;
    }

    /**
     * 获取分隔歌词的开始时间
     *
     * @param playProgress
     * @return
     */
    public int getSplitLineLrcStartTime(int playProgress) {
        if (mLyricsReader == null || mLrcLineInfos == null || mLrcLineInfos.size() == 0)
            return -1;
        return LyricsUtils.getSplitLineLrcStartTime(mLyricsReader.getLyricsType(), mLrcLineInfos, playProgress, mLyricsReader.getPlayOffset());

    }

    /**
     * 获取行歌词的开始时间
     *
     * @param playProgress
     * @return
     */
    public int getLineLrcStartTime(int playProgress) {
        if (mLyricsReader == null || mLrcLineInfos == null || mLrcLineInfos.size() == 0)
            return -1;
        return LyricsUtils.getLineLrcStartTime(mLyricsReader.getLyricsType(), mLrcLineInfos, playProgress, mLyricsReader.getPlayOffset());
    }

    /**
     * 获取分隔行的歌词内容
     *
     * @param playProgress
     * @return
     */
    public String getSplitLineLrc(int playProgress) {
        if (mLyricsReader == null || mLrcLineInfos == null || mLrcLineInfos.size() == 0)
            return null;
        return LyricsUtils.getSplitLineLrc(mLyricsReader.getLyricsType(), mLrcLineInfos, playProgress, mLyricsReader.getPlayOffset());
    }

    /**
     * 获取行的歌词内容
     *
     * @param playProgress
     * @return
     */
    public String getLineLrc(int playProgress) {
        if (mLyricsReader == null || mLrcLineInfos == null || mLrcLineInfos.size() == 0)
            return null;
        return LyricsUtils.getLineLrc(mLyricsReader.getLyricsType(), mLrcLineInfos, playProgress, mLyricsReader.getPlayOffset());

    }

    /**
     * 设置空行高度
     *
     * @param spaceLineHeight
     * @param isInvalidateView 是否更新视图
     */
    public void setSpaceLineHeight(float spaceLineHeight, boolean isInvalidateView) {
        this.mSpaceLineHeight = spaceLineHeight;
        if (isInvalidateView) {
            invalidateView();
        }
    }

    /**
     * 设置额外空行高度
     *
     * @param extraLrcSpaceLineHeight
     * @param isInvalidateView        是否更新视图
     */
    public void setExtraLrcSpaceLineHeight(float extraLrcSpaceLineHeight, boolean isInvalidateView) {
        this.mExtraLrcSpaceLineHeight = extraLrcSpaceLineHeight;
        if (isInvalidateView) {
            invalidateView();
        }
    }

    /**
     * 设置额外歌词的显示状态
     *
     * @param extraLrcStatus
     */
    public void setExtraLrcStatus(int extraLrcStatus, boolean isReloadData) {
        this.mExtraLrcStatus = extraLrcStatus;
        if (isReloadData) {
            synchronized (lock) {
                removeCallbacksAndMessages();
                //更新行和索引等数据
                updateView(mCurPlayingTime + mPlayerSpendTime);
                invalidateView();
                if (mLrcPlayerStatus == LRCPLAYERSTATUS_PLAY) {
                    mWorkerHandler.sendEmptyMessageDelayed(0, mRefreshTime);
                }
            }
        }
    }

    /**
     * 设置额外歌词的显示状态
     *
     * @param extraLrcStatus
     */
    public void setExtraLrcStatus(int extraLrcStatus) {
        setExtraLrcStatus(extraLrcStatus, false);
    }

    /**
     * 歌词播放
     *
     * @param playProgress
     */
    public void play(int playProgress) {
        synchronized (lock) {
            if (mLrcPlayerStatus == LRCPLAYERSTATUS_PLAY) {
                removeCallbacksAndMessages();
            }
            mLrcPlayerStatus = LRCPLAYERSTATUS_PLAY;
            this.mCurPlayingTime = playProgress;
            mPlayerStartTime = System.currentTimeMillis();
            mPlayerSpendTime = 0;
            mWorkerHandler.sendEmptyMessageDelayed(0, 0);
        }
    }

    /**
     * 歌词暂停
     */
    public void pause() {
        synchronized (lock) {
            if (mLrcPlayerStatus == LRCPLAYERSTATUS_PLAY) {
                mLrcPlayerStatus = LRCPLAYERSTATUS_INIT;
                removeCallbacksAndMessages();
            }
            mCurPlayingTime += mPlayerSpendTime;
            mPlayerSpendTime = 0;
        }
    }

    /**
     * 快进
     *
     * @param playProgress
     */
    public void seekTo(int playProgress) {
        synchronized (lock) {
            if (mLyricsReader == null) {
                MyLog.w(TAG, "seekto" + " mLyricsReader=null");
                return;
            }
            mCurEndLineNum = mLyricsReader.getLineInfoIdByStartTs(playProgress);
            mCurSplitLyricsEndLineNum = 0;
            if (mLrcPlayerStatus == LRCPLAYERSTATUS_PLAY) {
                play(playProgress);
            } else {
                mLrcPlayerStatus = LRCPLAYERSTATUS_SEEKTO;

                this.mCurPlayingTime = playProgress;
                mPlayerStartTime = System.currentTimeMillis();
                mPlayerSpendTime = 0;
                mWorkerHandler.sendEmptyMessageDelayed(0, 0);
            }
        }
    }

    /**
     * 唤醒
     */
    public void resume() {
        MyLog.d("lycview", "resume " + mLrcPlayerStatus);
        synchronized (lock) {
            mLrcPlayerStatus = LRCPLAYERSTATUS_PLAY;
            mPlayerStartTime = System.currentTimeMillis();
            mPlayerSpendTime = 0;
            mWorkerHandler.sendEmptyMessageDelayed(0, 0);
        }
    }

    /**
     * 获取歌词播放器状态
     *
     * @return
     */
    public int getLrcPlayerStatus() {
        return mLrcPlayerStatus;
    }

    /**
     * 设置歌词解析器
     *
     * @param lyricsReader
     */
    public void setLyricsReader(LyricsReader lyricsReader) {
        synchronized (lock) {
            this.mLyricsReader = lyricsReader;
            resetData();
            if (!hasLrcLineInfos()) {
                MyLog.w(TAG, "setLyricsReader !hasLrcLineInfos()");
                if (mSearchLyricsListener != null) {
                    mLrcStatus = LRCSTATUS_NOLRC_GOTOSEARCH;
                } else {
                    mLrcStatus = LRCSTATUS_NOLRC_DEFTEXT;
                }

            } else {
                //是否有歌词数据
                mLrcStatus = LRCSTATUS_LRC;

                updateView(mCurPlayingTime);
            }
            initExtraLrcTypeAndCallBack();
            invalidateView();
        }
    }

    /**
     * 是否有歌词数据
     *
     * @return
     */
    public boolean hasLrcLineInfos() {
        boolean notNull = mLyricsReader != null;
        boolean lrcLineInfosNotNull = mLyricsReader != null && mLyricsReader.getLrcLineInfos() != null;
        boolean sizeNotZero = mLyricsReader != null && mLyricsReader.getLrcLineInfos() != null && mLyricsReader.getLrcLineInfos().size() > 0;

        MyLog.w(TAG, "hasLrcLineInfos notNull is " + notNull + ", lrcLineInfosNotNull is " + lrcLineInfosNotNull + ",sizeNotZero is " + sizeNotZero);

        if (mLyricsReader != null && mLyricsReader.getLrcLineInfos() != null && mLyricsReader.getLrcLineInfos().size() > 0) {
            //获取分割歌词集合
            if (mLyricsReader.getLyricsType() == LyricsInfo.LRC) {
                //lrc歌词
                mLrcLineInfos = LyricsUtils.getSplitLrcLyrics(mLyricsReader.getLrcLineInfos(), mTextMaxWidth, mPaint);
                if (mLrcLineInfos == null) {
                    MyLog.e(TAG, "mLrcLineInfos 1 为 null");
                }
                //翻译歌词
                mTranslateLrcLineInfos = LyricsUtils.getSplitLrcExtraLyrics(mLyricsReader.getTranslateLrcLineInfos(), mTextMaxWidth, mExtraLrcPaint);
                //该lrc歌词不支持音译歌词
            } else {
                //动感歌词
                //默认歌词
                mLrcLineInfos = LyricsUtils.getSplitDynamicLyrics(mLyricsReader.getLrcLineInfos(), mTextMaxWidth, mPaint);
                if (mLrcLineInfos == null) {
                    MyLog.e(TAG, "mLrcLineInfos 2 为 null");
                }
                //翻译歌词
                mTranslateLrcLineInfos = LyricsUtils.getSplitDynamicExtraLyrics(mLyricsReader.getTranslateLrcLineInfos(), mTextMaxWidth, mExtraLrcPaint);
                //音译歌词
                mTransliterationLrcLineInfos = LyricsUtils.getSplitDynamicExtraLyrics(mLyricsReader.getTransliterationLrcLineInfos(), mTextMaxWidth, mExtraLrcPaint);
            }

            return true;
        }

        return false;
    }

    /**
     * 初始化额外歌词类型
     */
    private void initExtraLrcTypeAndCallBack() {
        int extraLrcStatus = EXTRALRCSTATUS_NOSHOWEXTRALRC;
        //判断音译和翻译歌词
        if (mTranslateLrcLineInfos != null && mTranslateLrcLineInfos.size() > 0 && mTransliterationLrcLineInfos != null && mTransliterationLrcLineInfos.size() > 0) {
            //有翻译歌词和音译歌词
            mExtraLrcType = EXTRALRCTYPE_BOTH;
            extraLrcStatus = EXTRALRCSTATUS_SHOWTRANSLITERATIONLRC;
        } else if (mTranslateLrcLineInfos != null && mTranslateLrcLineInfos.size() > 0) {
            //有翻译歌词
            mExtraLrcType = EXTRALRCTYPE_TRANSLATELRC;
            extraLrcStatus = EXTRALRCSTATUS_SHOWTRANSLATELRC;
        } else if (mTransliterationLrcLineInfos != null && mTransliterationLrcLineInfos.size() > 0) {
            //音译歌词
            mExtraLrcType = EXTRALRCTYPE_TRANSLITERATIONLRC;
            extraLrcStatus = EXTRALRCSTATUS_SHOWTRANSLITERATIONLRC;
        } else {
            //无翻译歌词和音译歌词
            mExtraLrcType = EXTRALRCTYPE_NOLRC;
        }
        if (mExtraLyricsListener != null) {
            mExtraLyricsListener.extraLrcCallback();
        }
    }

    /**
     * 重置数据
     */
    public void resetData() {
        //
        mLrcPlayerStatus = LRCPLAYERSTATUS_INIT;
        removeCallbacksAndMessages();

        //player
        mCurPlayingTime = 0;
        mPlayerStartTime = 0;
        mPlayerSpendTime = 0;


        mExtraLrcStatus = EXTRALRCSTATUS_NOSHOWEXTRALRC;
        mLyricsLineNum = 0;
        mSplitLyricsLineNum = 0;
        mLyricsWordIndex = -1;
        mSplitLyricsWordIndex = -1;
        mLyricsWordHLTime = 0;

        //防止出现空指针，不至为null
//        mLrcLineInfos = null;
        mTranslateLrcLineInfos = null;
        mTransliterationLrcLineInfos = null;
        mExtraSplitLyricsLineNum = 0;
        mExtraLyricsWordIndex = -1;
        mExtraSplitLyricsWordIndex = -1;
        mTranslateLyricsWordHLTime = 0;

        mCurEndLineNum = 0;
        mCurSplitLyricsEndLineNum = 0;

        //无额外歌词回调
        if (mExtraLyricsListener != null) {
            mExtraLyricsListener.extraLrcCallback();
        }
    }

    /**
     * 设置字体大小
     *
     * @param fontSize
     * @param extraFontSize 额外歌词字体
     * @param isReloadData  是否重新加载数据及刷新界面
     */
    public void setSize(int fontSize, int extraFontSize, boolean isReloadData) {
        synchronized (lock) {
            if (isReloadData) {
                setFontSize(fontSize, false);
                setExtraLrcFontSize(extraFontSize, false);
                if (isReloadData) {
                    if (hasLrcLineInfos()) {
                        updateView(mCurPlayingTime + mPlayerSpendTime);
                    }
                    invalidateView();
                }
            } else {
                setFontSize(fontSize, false);
                setExtraLrcFontSize(extraFontSize, false);
            }
        }
    }

    /**
     * 设置字体大小
     *
     * @param fontSize
     * @param isReloadData 是否重新加载数据及刷新界面
     */
    public void setFontSize(float fontSize, boolean isReloadData) {
        synchronized (lock) {

            this.mFontSize = fontSize;

            //
            mPaint.setTextSize(mFontSize);
            mPaintHL.setTextSize(mFontSize);
            mPaintOutline.setTextSize(mFontSize);

            //搜索歌词回调不为空
            if (mSearchLyricsListener != null) {
                mGotoSearchRectPaint.setTextSize(mFontSize);
                mGotoSearchTextPaint.setTextSize(mFontSize);
                mGotoSearchBtnRect = null;
            }

            if (isReloadData) {
                //加载歌词数据
                if (hasLrcLineInfos()) {
                    updateView(mCurPlayingTime + mPlayerSpendTime);
                }
                invalidateView();
            }
        }
    }

    /**
     * 设置额外字体大小
     *
     * @param extraLrcFontSize
     * @param isReloadData     是否重新加载数据及刷新界面
     */
    public void setExtraLrcFontSize(float extraLrcFontSize, boolean isReloadData) {
        synchronized (lock) {
            this.mExtraLrcFontSize = extraLrcFontSize;

            //
            mExtraLrcPaint.setTextSize(mExtraLrcFontSize);
            mExtraLrcPaintHL.setTextSize(mExtraLrcFontSize);
            mExtraLrcPaintOutline.setTextSize(mExtraLrcFontSize);


            if (isReloadData) {
                if (hasLrcLineInfos()) {
                    updateView(mCurPlayingTime + mPlayerSpendTime);
                }
                invalidateView();
            }
        }
    }

    /**
     * 更新分隔后的行号，字索引，高亮时间
     *
     * @param playProgress
     */
    public void updateSplitData(long playProgress) {
        //动感歌词
        if (mLyricsReader.getLyricsType() == LyricsInfo.DYNAMIC) {
            //获取分割后的索引
            mSplitLyricsLineNum = LyricsUtils.getSplitDynamicLyricsLineNum(mLrcLineInfos, mLyricsLineNum, playProgress, mLyricsReader.getPlayOffset());
            //获取原始的歌词字索引
            mLyricsWordIndex = LyricsUtils.getLyricsWordIndex(mLrcLineInfos, mLyricsLineNum, playProgress, mLyricsReader.getPlayOffset());
            //获取分割后的歌词字索引
            mSplitLyricsWordIndex = LyricsUtils.getSplitLyricsWordIndex(mLrcLineInfos, mLyricsLineNum, playProgress, mLyricsReader.getPlayOffset());
            mLyricsWordHLTime = LyricsUtils.getDisWordsIndexLenTime(mLrcLineInfos, mLyricsLineNum, playProgress, mLyricsReader.getPlayOffset());
//            MyLog.v("LyricsView", " mLyricsWordHLTime is " + mLyricsWordHLTime);
        } else {
            //lrc歌词
            //获取分割后的索引
            mSplitLyricsLineNum = LyricsUtils.getSplitLrcLyricsLineNum(mLrcLineInfos, mLyricsLineNum, playProgress, mLyricsReader.getPlayOffset());
        }
        if (mLyricsReader.getLyricsType() == LyricsInfo.DYNAMIC && mExtraLrcStatus == EXTRALRCSTATUS_SHOWTRANSLATELRC && mTranslateDrawType == TRANSLATE_DRAW_TYPE_DYNAMIC) {
            //显示翻译歌词且歌词类型是动感歌词且以动感歌词的形式绘画翻译歌词
            if (mTranslateLrcLineInfos != null && mTranslateLrcLineInfos.size() > 0) {
                mExtraSplitLyricsLineNum = LyricsUtils.getSplitExtraLyricsLineNum(mTranslateLrcLineInfos, mLyricsLineNum, playProgress, mLyricsReader.getPlayOffset());

                mExtraLyricsWordIndex = LyricsUtils.getExtraLyricsWordIndex(mTranslateLrcLineInfos, mLyricsLineNum, playProgress, mLyricsReader.getPlayOffset());
                mExtraSplitLyricsWordIndex = LyricsUtils.getSplitExtraLyricsWordIndex(mTranslateLrcLineInfos, mLyricsLineNum, playProgress, mLyricsReader.getPlayOffset());
                mTranslateLyricsWordHLTime = LyricsUtils.getTranslateLrcDisWordsIndexLenTime(mTranslateLrcLineInfos, mLyricsLineNum, playProgress, mLyricsReader.getPlayOffset());
            }
        } else if (mExtraLrcStatus == EXTRALRCSTATUS_SHOWTRANSLITERATIONLRC) {
            //显示音译歌词
            if (mTransliterationLrcLineInfos != null && mTransliterationLrcLineInfos.size() > 0) {
                mExtraSplitLyricsLineNum = LyricsUtils.getSplitExtraLyricsLineNum(mTransliterationLrcLineInfos, mLyricsLineNum, playProgress, mLyricsReader.getPlayOffset());

                mExtraLyricsWordIndex = LyricsUtils.getExtraLyricsWordIndex(mTransliterationLrcLineInfos, mLyricsLineNum, playProgress, mLyricsReader.getPlayOffset());
                mExtraSplitLyricsWordIndex = LyricsUtils.getSplitExtraLyricsWordIndex(mTransliterationLrcLineInfos, mLyricsLineNum, playProgress, mLyricsReader.getPlayOffset());
            }
        }
    }

    /**
     * 绘画类型
     *
     * @param translateDrawType
     */
    public void setTranslateDrawType(int translateDrawType) {
        this.mTranslateDrawType = translateDrawType;
    }

    /**
     * 释放
     */
    public void release() {
        removeCallbacksAndMessages();
        //关闭线程
        if (mWorkerHandler != null) {
            mWorkerHandler.removeCallbacksAndMessages(null);
        }
        mHandlerThread.quit();
        mLrcPlayerStatus = LRCPLAYERSTATUS_INIT;
    }

    /**
     *
     */
    private void removeCallbacksAndMessages() {
        //移除队列任务
        if (mUIHandler != null) {
            mUIHandler.removeCallbacksAndMessages(null);
        }

        //移除队列任务
        if (mWorkerHandler != null) {
            mWorkerHandler.removeCallbacksAndMessages(null);
        }

    }

    /**
     * 搜索歌词接口
     */
    public interface SearchLyricsListener {
        /**
         * 搜索歌词回调
         */
        void goToSearchLrc();
    }

    /**
     * 额外歌词事件
     */
    public interface ExtraLyricsListener {
        /**
         * 额外歌词回调
         */
        void extraLrcCallback();
    }

    //    ///////////////////////////////////////////////
//
//
//    public void setRefreshTime(long refreshTime) {
//        this.mRefreshTime = refreshTime;
//    }
//
//    public void setTextMaxWidth(float mTextMaxWidth) {
//        this.mTextMaxWidth = mTextMaxWidth;
//    }
//
//    public String getDefText() {
//        return mDefText;
//    }
//
//    public void setDefText(String mDefText) {
//        this.mDefText = mDefText;
//    }
//
//    public String getLoadingText() {
//        return mLoadingText;
//    }
//
//    public void setLoadingText(String mLoadingText) {
//        this.mLoadingText = mLoadingText;
//    }
//
//    public String getLoadErrorText() {
//        return mLoadErrorText;
//    }
//
//    public void setLoadErrorText(String mLoadErrorText) {
//        this.mLoadErrorText = mLoadErrorText;
//    }
//
//    public String getNonsupportText() {
//        return mNonsupportText;
//    }
//
//    public void setNonsupportText(String mNonsupportText) {
//        this.mNonsupportText = mNonsupportText;
//    }
//
//    public String getGotoSearchText() {
//        return mGotoSearchText;
//    }
//
//    public void setGotoSearchText(String mGotoSearchText) {
//        this.mGotoSearchText = mGotoSearchText;
//    }
//
//    public void setLyricsLineNum(int mLyricsLineNum) {
//        this.mLyricsLineNum = mLyricsLineNum;
//    }
//
//    public int getLyricsLineNum() {
//        return mLyricsLineNum;
//    }
//
//    public int getSplitLyricsLineNum() {
//        return mSplitLyricsLineNum;
//    }
//
//    public int getSplitLyricsWordIndex() {
//        return mSplitLyricsWordIndex;
//    }
//
//    public int getLyricsWordIndex() {
//        return mLyricsWordIndex;
//    }
//
//    public float getLyricsWordHLTime() {
//        return mLyricsWordHLTime;
//    }
//
    public long getPlayerSpendTime() {
        return mPlayerSpendTime;
    }

    //
//    public int getExtraSplitLyricsLineNum() {
//        return mExtraSplitLyricsLineNum;
//    }
//
//    public int getExtraSplitLyricsWordIndex() {
//        return mExtraSplitLyricsWordIndex;
//    }
//
//    public int getExtraLyricsWordIndex() {
//        return mExtraLyricsWordIndex;
//    }
//
//    public float getTranslateLyricsWordHLTime() {
//        return mTranslateLyricsWordHLTime;
//    }
//
    public float getSpaceLineHeight() {
        return mSpaceLineHeight;
    }

    //
//    public float getPaddingLeftOrRight() {
//        return mPaddingLeftOrRight;
//    }
//
//    public float getExtraLrcSpaceLineHeight() {
//        return mExtraLrcSpaceLineHeight;
//    }
//
//    public int getTranslateDrawType() {
//        return mTranslateDrawType;
//    }
//
    public LyricsReader getLyricsReader() {
        return mLyricsReader;
    }
//
//    public TreeMap<Integer, LyricsLineInfo> getLrcLineInfos() {
//        return mLrcLineInfos;
//    }
//
//    public List<LyricsLineInfo> getTranslateLrcLineInfos() {
//        return mTranslateLrcLineInfos;
//    }
//
//    public List<LyricsLineInfo> getTransliterationLrcLineInfos() {
//        return mTransliterationLrcLineInfos;
//    }
//
//    public Paint getPaint() {
//        return mPaint;
//    }
//
//    public int[] getPaintColors() {
//        return mPaintColors;
//    }
//
//    public Paint getPaintHL() {
//        return mPaintHL;
//    }
//
//    public int[] getPaintHLColors() {
//        return mPaintHLColors;
//    }
//
//    public Paint getPaintOutline() {
//        return mPaintOutline;
//    }
//
//    public Paint getExtraLrcPaint() {
//        return mExtraLrcPaint;
//    }
//
//    public Paint getExtraLrcPaintHL() {
//        return mExtraLrcPaintHL;
//    }
//
//    public Paint getExtraLrcPaintOutline() {
//        return mExtraLrcPaintOutline;
//    }
}
