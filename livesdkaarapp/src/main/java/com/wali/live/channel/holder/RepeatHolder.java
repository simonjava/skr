package com.wali.live.channel.holder;

import android.app.Activity;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.base.image.fresco.BaseImageView;
import com.base.log.MyLog;
import com.facebook.drawee.drawable.ScalingUtils;
import com.wali.live.channel.viewmodel.ChannelLiveViewModel;
import com.wali.live.channel.viewmodel.ChannelLiveViewModel.BaseItem;
import com.wali.live.channel.viewmodel.ChannelLiveViewModel.BaseLiveItem;
import com.wali.live.livesdk.live.MiLiveSdkController;

import java.util.List;

/**
 * Created by lan on 16/6/28.
 *
 * @module 频道
 */
public abstract class RepeatHolder extends FixedHolder {
    protected int[] mParentIds;
    protected int[] mIvIds;
    protected int[] mTvIds;

    protected ViewGroup[] mParentViews;
    protected BaseImageView[] mImageViews;
    protected TextView[] mTextViews;

    protected int mViewSize;

    public RepeatHolder(View itemView) {
        super(itemView);

        if (isChangeImageSize()) {
            changeImageSize();
        }
    }

    protected void initView() {
        initContentView();
    }

    protected abstract void initContentViewId();

    protected void initContentView() {
        initContentViewId();

        mParentViews = new ViewGroup[mViewSize];
        mImageViews = new BaseImageView[mViewSize];
        mTextViews = new TextView[mViewSize];

        for (int i = 0; i < mViewSize; i++) {
            mParentViews[i] = $(mParentIds[i]);
            mImageViews[i] = $(mParentViews[i], mIvIds[i]);
            mTextViews[i] = $(mParentViews[i], mTvIds[i]);
        }
    }

    protected boolean isChangeImageSize() {
        return false;
    }

    protected void changeImageSize() {
        ViewGroup.MarginLayoutParams mlp;
        for (int i = 0; i < mViewSize; i++) {
            mlp = (ViewGroup.MarginLayoutParams) mImageViews[i].getLayoutParams();
            mlp.width = getImageWidth();
            mlp.height = getImageHeight();
        }
    }

    protected int getImageWidth() {
        return 0;
    }

    protected int getImageHeight() {
        return 0;
    }

    protected ScalingUtils.ScaleType getScaleType() {
        return ScalingUtils.ScaleType.CENTER_CROP;
    }

    protected abstract boolean isCircle();

    protected void setParentVisibility(int startIndex) {
        for (int i = startIndex; i < mViewSize; i++) {
            mParentViews[i].setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void bindLiveModel(ChannelLiveViewModel viewModel) {
        List<BaseItem> itemDatas = viewModel.getItemDatas();
        int minSize = Math.min(mViewSize, itemDatas.size());
        setParentVisibility(minSize);
        for (int i = 0; i < minSize; i++) {
            mParentViews[i].setVisibility(View.VISIBLE);
            BaseItem item = itemDatas.get(i);
            if (item != null) {
                MyLog.d(TAG, "bindLiveModel imageUrl : " + item.getImageUrl());
                if (viewModel.isFullColumn()) {
                    bindImage(mImageViews[i], item.getImageUrl(), isCircle(), 320, 320, getScaleType());
                } else {
                    bindImageWithBorder(mImageViews[i], item.getImageUrl(), isCircle(), 320, 320, getScaleType());
                }
                mTextViews[i].setText(item.getNameText());

                bindItemOnLiveModel(item, i);

                if (item instanceof BaseLiveItem) {
                    bindBaseLiveItem((BaseLiveItem) item, i);
                }
            }
        }
    }

    protected void bindItemOnLiveModel(BaseItem item, int i) {
    }

    protected void bindBaseLiveItem(final BaseLiveItem item, final int i) {
        mImageViews[i].setOnClickListener(new View.OnClickListener() {
                                              @Override
                                              public void onClick(View view) {
                                                  // todo
                                                  if (!TextUtils.isEmpty(item.getSchemeUri())) {
                                                      Uri uri = Uri.parse(item.getSchemeUri());

                                                      long playerId = Long.parseLong(uri.getQueryParameter("playerid"));
                                                      String liveId = uri.getQueryParameter("liveid");
                                                      String videoUrl = uri.getQueryParameter("videourl");

                                                      if (i == 0) {
                                                          MiLiveSdkController.getInstance().openReplay((Activity) itemView.getContext(), 22869193l, "22869193_1480938327",
                                                                  "http://playback.ks.zb.mi.com/record/live/22869193_1480938327/hls/22869193_1480938327.m3u8?playui=1", 0);
                                                          return;
                                                      }

                                                      MiLiveSdkController.getInstance().openWatch((Activity) itemView.getContext(), playerId, liveId,
                                                              videoUrl, 0);
                                                  }
                                              }
                                          }
        );
    }
}
