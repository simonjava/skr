package com.wali.live.modulechannel.adapter.holder;

import android.view.View;
import android.widget.TextView;

import com.common.image.fresco.BaseImageView;
import com.common.utils.ImageUtils;
import com.facebook.drawee.drawable.ScalingUtils;
import com.wali.live.modulechannel.R;
import com.wali.live.modulechannel.model.viewmodel.ChannelLiveViewModel;

/**
 * Created by zhaomin on 17-3-10.
 *
 * @module 二级页顶部大图， 中间有文字，下方两行文字
 */
public class LargeCardHeadHolder extends FixedHolder {
    private BaseImageView mAvatarIv;
    private TextView mCenterTextUp;
    private TextView mCenterTextDown;
    private TextView mDisplayText;
    private TextView mDisplayTextDown;

    public LargeCardHeadHolder(View itemView) {
        super(itemView);
    }

    @Override
    protected void initContentView() {
        mAvatarIv = $(R.id.avatar_iv);
        mCenterTextUp = $(R.id.center_text_up);
        mCenterTextDown = $(R.id.center_text_down);
        mDisplayText = $(R.id.display_tv);
        mDisplayTextDown = $(R.id.display_tv_down);
    }

    @Override
    protected void bindLiveModel(ChannelLiveViewModel model) {
        final ChannelLiveViewModel.BaseItem item = model.getFirstItem();
        if (item == null) {
            return;
        }
        exposureItem(item);
        
        bindImageWithBorder(mAvatarIv, item.getImageUrl(ImageUtils.SIZE.SIZE_640), false, 640, 640, ScalingUtils.ScaleType.CENTER_CROP);
        mAvatarIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jumpItem(item);
            }
        });

        if (item.getMiddleInfo() != null) {
            bindText(mCenterTextUp, item.getMiddleInfo().getText1());
            bindText(mCenterTextDown, item.getMiddleInfo().getText2());
        }
        bindText(mDisplayText, item.getNameText());
        bindText(mDisplayTextDown, item.getDisplayText());
    }
}
