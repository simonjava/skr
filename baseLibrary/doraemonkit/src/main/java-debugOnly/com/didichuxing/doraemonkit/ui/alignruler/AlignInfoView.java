package com.didichuxing.doraemonkit.ui.alignruler;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.didichuxing.doraemonkit.R;

/**
 * Created by wanglikun on 2018/12/4.
 */

public class AlignInfoView extends View {
    private Paint mTextPaint;
    private Paint mLinePaint;

    private int mPosX = -1;
    private int mPosY = -1;

    public AlignInfoView(Context context) {
        super(context);
        init();
    }

    public AlignInfoView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AlignInfoView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mLinePaint = new com.common.view.ExPaint();
        mLinePaint.setAntiAlias(true);
        mLinePaint.setColor(getResources().getColor(R.color.dk_color_CC3A4B));

        mTextPaint = new com.common.view.ExPaint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextSize(getResources().getDimensionPixelSize(R.dimen.dk_font_size_14));
        mTextPaint.setColor(getResources().getColor(R.color.dk_color_333333));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawLine(canvas);
        drawText(canvas);
    }

    private void drawText(Canvas canvas) {
        if (mPosY == -1 && mPosX == -1) {
            return;
        }
        int left = mPosX;
        int right = getWidth() - mPosX;
        int top = mPosY;
        int bottom = getHeight() - mPosY;
        canvas.drawText(String.valueOf(left), left / 2, mPosY, mTextPaint);
        canvas.drawText(String.valueOf(right), (mPosX + getWidth()) / 2, mPosY, mTextPaint);
        canvas.drawText(String.valueOf(top), mPosX, top / 2, mTextPaint);
        canvas.drawText(String.valueOf(bottom), mPosX, (mPosY + getHeight()) / 2, mTextPaint);
    }

    private void drawLine(Canvas canvas) {
        if (mPosY == -1 && mPosX == -1) {
            return;
        }
        canvas.drawLine(0, mPosY, getWidth(), mPosY, mLinePaint);
        canvas.drawLine(mPosX, 0, mPosX, getHeight(), mLinePaint);
    }

    public void showInfo(int x, int y) {
        mPosX = x;
        mPosY = y;
        invalidate();
    }
}
