package com.zq.mediaengine.util;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;

/**
 * @hide
 */
public class TextGraphicUtils {
    /**
     * 获取给定文本的宽度
     * @param text 要计算的文本
     * @param textSize 文本大小
     * @return 给定文本的宽度
     */
    public static float getTextWidth(String text, float textSize){
        Paint paint = new com.common.view.ExPaint();
        paint.setTextSize(textSize);
        return paint.measureText(text);
    }

    /**
     * 获取当给定的文本使用给定的画笔绘制时的宽度
     * @param paint 指定的画笔
     * @param text 指定的文本
     * @return 当给定的文本使用给定的画笔绘制时的宽度
     */
    public static float getTextWidth(Paint paint, String text) {
        return paint.measureText(text);
    }

    /**
     * 获取给定尺寸的文本的高度
     * @param textSize 给定尺寸
     * @return 文本的高度
     */
    public static float getTextHeight(float textSize){
        Paint paint = new com.common.view.ExPaint();
        paint.setTextSize(textSize);
        Paint.FontMetrics fm = paint.getFontMetrics();
        return fm.descent - fm.ascent;
    }

    /**
     * 获取给定画笔的文本高度
     * @param paint 给定的画笔
     * @return 文本的高度
     */
    public static float getTextHeight(Paint paint){
        Paint.FontMetrics fm = paint.getFontMetrics();
        return fm.descent - fm.ascent;
    }

    /**
     * 获取给定文本的宽度
     * @param text 要计算的文本
     * @param textSize 文本大小
     * @return 文本的宽度
     */
    public static int getTextWidthByBounds(String text, float textSize){
        Paint paint = new com.common.view.ExPaint();
        Rect bounds = new Rect();
        paint.setTextSize(textSize);
        paint.getTextBounds(text, 0, text.length(), bounds);
        return bounds.width();
    }

    /**
     * 获取给定文本的高度
     * @param text 要计算的文本
     * @param textSize 文本大小
     * @return 文本的高度
     */
    public static int getTextHeightByBounds(String text, float textSize){
        Paint paint = new com.common.view.ExPaint();
        Rect bounds = new Rect();
        paint.setTextSize(textSize);
        paint.getTextBounds(text, 0, text.length(), bounds);
        return bounds.height();
    }

    /**
     * 获取指定画笔的文字离顶部的基准距离
     * @return 返回指定笔离文字顶部的基准距离
     */
    public static float getTextLeading(Paint paint)  {
        Paint.FontMetrics fm = paint.getFontMetrics();
        return fm.leading- fm.ascent;
    }

    /**
     * 获取一张文字位图
     * @param text 文字
     * @param textColor 文字颜色
     * @param textSize 文字大小
     * @param leftBitmap 可以在文字的左边放置一张图片
     * @return 文字位图
     */
    public static Bitmap getTextBitmap(String text, int textColor, float textSize, Bitmap leftBitmap){
        //创建并初始化画笔
        Paint paint = new com.common.view.ExPaint();
        paint.setTypeface(Typeface.create(Typeface.DEFAULT_BOLD, Typeface.BOLD));
        paint.setColor(textColor);
        paint.setTextSize(textSize);
        paint.setAntiAlias(true);//去除锯齿
        paint.setFilterBitmap(true);//对文字进行滤波处理，增强绘制效果

        //计算要绘制的文字的宽和高
        float textWidth = getTextWidth(paint, text);
        float textHeight = getTextHeight(paint);

        //计算图片的宽高
        int newBimapWidth = textWidth % 1==0?(int)textWidth:(int)textWidth + 1;
        int newBimapHeight = textHeight % 1==0?(int)textHeight:(int)textHeight + 1;

        if(leftBitmap != null){
            newBimapWidth += leftBitmap.getWidth();
            newBimapHeight = leftBitmap.getHeight() > newBimapHeight?leftBitmap.getHeight():newBimapHeight;
        }

        //先创建一张空白图片，然后在其上面绘制文字
        Bitmap bitmap = Bitmap.createBitmap(newBimapWidth, newBimapHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        if(leftBitmap != null){
            canvas.drawBitmap(leftBitmap, 0, (newBimapHeight - leftBitmap.getHeight())/2, paint);
            canvas.drawText(text, leftBitmap.getWidth(), (newBimapHeight - textHeight)/2 + getTextLeading(paint), paint);
        }else{
            canvas.drawText(text, 0, (newBimapHeight - textHeight)/2 + getTextLeading(paint), paint);
        }
        canvas.save();

        return bitmap;
    }

    /**
     * 获取一张文字位图
     * @param text 文字
     * @param textColor 文字颜色
     * @param textSize 文字大小
     * @return 文字位图
     */
    public static Bitmap getTextBitmap(String text, int textColor, float textSize){
        return getTextBitmap(text, textColor, textSize, null);
    }
}
