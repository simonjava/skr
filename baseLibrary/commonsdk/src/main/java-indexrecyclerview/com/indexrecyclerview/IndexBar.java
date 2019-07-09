package com.indexrecyclerview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;

import com.common.base.R;
import com.indexrecyclerview.model.EntityWrapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by YoKey on 16/10/6.
 */
class IndexBar extends View {
    private int mTotalHeight;
    private float mTextSpace;

    // 所有索引的list 如 # A B C D 有序的
    private List<String> mIndexList = new ArrayList<>();
    // 首字母 到 mIndexList 的映射，存着 索引---> 满足该索引的第一个 实体类在mDatas中的position位置。
    // 如 H--->9
    private HashMap<String, Integer> mMapping = new HashMap<>();
    // mDatas 是排过序的 所有的参与索引的实体类
    private ArrayList<EntityWrapper> mDatas;

    private int mSelectionPosition;
    private float mIndexHeight;

    private Paint mPaint = new com.common.view.ExPaint(Paint.ANTI_ALIAS_FLAG);
    private Paint mFocusPaint = new com.common.view.ExPaint(Paint.ANTI_ALIAS_FLAG);

    public IndexBar(Context context) {
        super(context);
    }

    void init(Drawable barBg, int barTextColor, int barFocusTextColor, float barTextSize, float textSpace) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            setBackground(barBg);
        } else {
            setBackgroundDrawable(barBg);
        }

        this.mTextSpace = textSpace;

        mPaint.setColor(barTextColor);
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setTextSize(barTextSize);

        mFocusPaint.setTextAlign(Paint.Align.CENTER);
        mFocusPaint.setTextSize(barTextSize + (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, getResources().getDisplayMetrics()));
        mFocusPaint.setColor(barFocusTextColor);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int mode = MeasureSpec.getMode(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        if (mIndexList.size() > 0) {
            mTotalHeight = (int) (((mIndexList.size() - 1) * mPaint.getTextSize()
                    + mFocusPaint.getTextSize())
                    + (mIndexList.size() + 1) * mTextSpace);
        }

        if (mTotalHeight > height) {
            mTotalHeight = height;
        }

//        // TODO: 16/10/8  Measure AT_MOST
//        if (mode == MeasureSpec.AT_MOST) {
//            int maxWidth = (int) getResources().getDimension(R.dimen.default_indexBar_layout_width);
//            super.onMeasure(MeasureSpec.makeMeasureSpec(maxWidth, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(mTotalHeight, MeasureSpec.EXACTLY));
//            return;
//        }
        super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(mTotalHeight, MeasureSpec.EXACTLY));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mIndexList.size() == 0) return;

        mIndexHeight = ((float) getHeight()) / mIndexList.size();

        for (int i = 0; i < mIndexList.size(); i++) {
            if (mSelectionPosition == i) {
                canvas.drawText(mIndexList.get(i), getWidth() / 2, mIndexHeight * 0.85f + mIndexHeight * i, mFocusPaint);
            } else {
                canvas.drawText(mIndexList.get(i), getWidth() / 2, mIndexHeight * 0.85f + mIndexHeight * i, mPaint);
            }
        }
    }

    /**
     * 返回 屏幕上 y 轴的坐标 对应 mIndexList 里哪个 position
     */
    int getPositionForPointY(float y) {
        if (mIndexList.size() <= 0) {
            return -1;
        }

        int position = (int) (y / mIndexHeight);

        if (position < 0) {
            position = 0;
        } else if (position > mIndexList.size() - 1) {
            position = mIndexList.size() - 1;
        }

        return position;
    }


    int getSelectionPosition() {
        return mSelectionPosition;
    }

    void setSelectionPosition(int position) {
        this.mSelectionPosition = position;
        invalidate();
    }

    /**
     * 例如，返回 索引 H，对应的在 rv 中 第一个 H item 的postion
     * @return
     */
    int getFirstRecyclerViewPositionBySelection() {
        String index = mIndexList.get(mSelectionPosition);
        if (mMapping.containsKey(index)) {
            return mMapping.get(index);
        }
        return -1;
    }

    List<String> getIndexList() {
        return mIndexList;
    }

    /**
     *
     * @param showAllLetter 是否要显示 A B C 等字母索引
     * @param datas 已经排过序的数据
     */
    void setDatas(boolean showAllLetter, ArrayList<EntityWrapper> datas) {
        this.mDatas = datas;
        this.mIndexList.clear();
        this.mMapping.clear();

        ArrayList<String> tempHeaderList = null;
        if (showAllLetter) {
            mIndexList = Arrays.asList(getResources().getStringArray(R.array.indexable_letter));
            mIndexList = new ArrayList<>(mIndexList);
            tempHeaderList = new ArrayList<>();
        }
        for (int i = 0; i < datas.size(); i++) {
            EntityWrapper wrapper = datas.get(i);
            if (wrapper.getItemType() == EntityWrapper.TYPE_TITLE || wrapper.getIndexTitle() == null) {
                String index = wrapper.getIndex();
                if (!TextUtils.isEmpty(index)) {
                    if (!showAllLetter) {
                        mIndexList.add(index);
                    } else {
                        if (IndexableLayout.INDEX_SIGN.equals(index)) {
                            mIndexList.add(IndexableLayout.INDEX_SIGN);
                        } else if (mIndexList.indexOf(index) < 0) {
                            if (wrapper.getHeaderFooterType() == EntityWrapper.TYPE_HEADER && tempHeaderList.indexOf(index) < 0) {
                                // 加到头部
                                tempHeaderList.add(index);
                            } else if (wrapper.getHeaderFooterType() == EntityWrapper.TYPE_FOOTER) {
                                mIndexList.add(index);
                            }
                        }
                    }
                    if (!mMapping.containsKey(index)) {
                        mMapping.put(index, i);
                    }
                }
            }
        }
        if (showAllLetter) {
            mIndexList.addAll(0, tempHeaderList);
        }
        requestLayout();
    }

    void setSelection(int firstVisibleItemPosition) {
        if (mDatas == null || mDatas.size() <= firstVisibleItemPosition || firstVisibleItemPosition < 0) {
            return;
        }
        EntityWrapper wrapper = mDatas.get(firstVisibleItemPosition);
        int position = mIndexList.indexOf(wrapper.getIndex());

        if (mSelectionPosition != position && position >= 0) {
            mSelectionPosition = position;
            invalidate();
        }
    }
}
