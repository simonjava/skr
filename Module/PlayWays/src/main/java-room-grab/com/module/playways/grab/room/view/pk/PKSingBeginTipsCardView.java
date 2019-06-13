package com.module.playways.grab.room.view.pk;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.common.anim.svga.SvgaParserAdapter;
import com.common.core.avatar.AvatarUtils;
import com.common.core.userinfo.model.UserInfoModel;
import com.common.image.fresco.FrescoWorker;
import com.common.image.model.HttpImage;
import com.common.log.MyLog;
import com.common.utils.U;
import com.glidebitmappool.BitmapFactoryAdapter;
import com.module.playways.grab.room.listener.SVGAListener;
import com.module.playways.R;
import com.opensource.svgaplayer.SVGACallback;
import com.opensource.svgaplayer.SVGADrawable;
import com.opensource.svgaplayer.SVGADynamicEntity;
import com.opensource.svgaplayer.SVGAImageView;
import com.opensource.svgaplayer.SVGAParser;
import com.opensource.svgaplayer.SVGAVideoEntity;

import java.io.File;

/**
 * PK开始的板子
 */
public class PKSingBeginTipsCardView {

    public final static String TAG = "PKSingBeginTipsCardView";


    public void bindData(SVGAImageView mPkSingBeginSvga, UserInfoModel left, UserInfoModel right, SVGAListener mSVGAListener) {
        if (left == null || right == null) {
            MyLog.w(TAG, "bindData" + " left=" + left + " right=" + right + " listener=" + mSVGAListener);
            return;
        }

        String assetsName = "grab_pk_sing_chance.svga";
        try {
            SvgaParserAdapter.parse(assetsName, new SVGAParser.ParseCompletion() {
                @Override
                public void onComplete(SVGAVideoEntity videoItem) {
                    SVGADrawable drawable = new SVGADrawable(videoItem, requestDynamic(left, right));
                    mPkSingBeginSvga.setImageDrawable(drawable);
                    mPkSingBeginSvga.startAnimation();
                }

                @Override
                public void onError() {

                }
            });
        } catch (Exception e) {
            MyLog.e(TAG, e);
        }

        mPkSingBeginSvga.setCallback(new SVGACallback() {
            @Override
            public void onPause() {

            }

            @Override
            public void onFinished() {
                if (mPkSingBeginSvga != null) {
                    mPkSingBeginSvga.setCallback(null);
                    mPkSingBeginSvga.stopAnimation(true);
                }
                if (mSVGAListener != null) {
                    mSVGAListener.onFinished();
                }
            }

            @Override
            public void onRepeat() {
                if (mPkSingBeginSvga != null && mPkSingBeginSvga.isAnimating()) {
                    mPkSingBeginSvga.stopAnimation(false);
                }
            }

            @Override
            public void onStep(int i, double v) {

            }
        });
    }

    private SVGADynamicEntity requestDynamic(UserInfoModel left, UserInfoModel right) {
        SVGADynamicEntity dynamicEntity = new SVGADynamicEntity();
        if (!TextUtils.isEmpty(left.getAvatar())) {
            // 填入左边头像
            HttpImage image = AvatarUtils.getAvatarUrl(AvatarUtils.newParamsBuilder(left.getAvatar())
                    .setCircle(true)
                    .build());
            File file = FrescoWorker.getCacheFileFromFrescoDiskCache(image.getUrl());
            if (file != null) {
                Bitmap bitmap = BitmapFactoryAdapter.decodeFile(file.getPath());
                //防止用户不给sd权限导致 bitmap为null
                if (bitmap != null) {
                    dynamicEntity.setDynamicImage(bitmap, "avatar_1081");
                } else {
                    dynamicEntity.setDynamicImage(image.getUrl(), "avatar_1081");
                }
            } else {
                dynamicEntity.setDynamicImage(image.getUrl(), "avatar_1081");
            }
        }

        if (!TextUtils.isEmpty(left.getNicknameRemark())) {
            TextPaint leftPaint = new TextPaint();
            leftPaint.setColor(U.getColor(R.color.black_trans_60));
            leftPaint.setAntiAlias(true);
            leftPaint.setTextSize(U.getDisplayUtils().dip2px(12));
            String text = left.getNicknameRemark();
            dynamicEntity.setDynamicText(text, leftPaint, "text_441");
        }

        if (!TextUtils.isEmpty(right.getAvatar())) {
            // 填入右边头像
            HttpImage image = AvatarUtils.getAvatarUrl(AvatarUtils.newParamsBuilder(right.getAvatar())
                    .setCircle(true)
                    .build());
            File file = FrescoWorker.getCacheFileFromFrescoDiskCache(image.getUrl());
            if (file != null) {
                Bitmap bitmap = BitmapFactoryAdapter.decodeFile(file.getPath());
                //防止用户不给sd权限导致 bitmap为null
                if (bitmap != null) {
                    dynamicEntity.setDynamicImage(bitmap, "avatar_1082");
                } else {
                    dynamicEntity.setDynamicImage(image.getUrl(), "avatar_1082");
                }
            } else {
                dynamicEntity.setDynamicImage(image.getUrl(), "avatar_1082");
            }
        }

        if (!TextUtils.isEmpty(right.getNicknameRemark())) {
            TextPaint rightPaint = new TextPaint();
            rightPaint.setColor(U.getColor(R.color.black_trans_60));
            rightPaint.setAntiAlias(true);
            rightPaint.setTextSize(U.getDisplayUtils().dip2px(12));
            String text = right.getNicknameRemark();
            dynamicEntity.setDynamicText(text, rightPaint, "text_442");
        }
        return dynamicEntity;
    }

}
