package com.module.rankingmode.prepare.sence;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.common.core.avatar.AvatarUtils;
import com.common.core.myinfo.MyUserInfoManager;
import com.common.image.fresco.BaseImageView;
import com.module.rankingmode.R;
import com.module.rankingmode.prepare.sence.controller.MatchSenceController;

public class FastMatchSuccessSence extends RelativeLayout implements ISence {
    MatchSenceController matchSenceController;

    BaseImageView mIvAArea;
    BaseImageView mIvBArea;
    BaseImageView mIvCArea;

    public FastMatchSuccessSence(Context context) {
        this(context, null);
    }

    public FastMatchSuccessSence(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FastMatchSuccessSence(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.match_success_layout, this);


        mIvAArea = findViewById(R.id.iv_a_area);
        mIvBArea = findViewById(R.id.iv_b_area);
        mIvCArea = findViewById(R.id.iv_c_area);

        AvatarUtils.loadAvatarByUrl(mIvAArea,
                AvatarUtils.newParamsBuilder(MyUserInfoManager.getInstance().getUid())
                        .setCircle(true)
                        .setTimestamp(MyUserInfoManager.getInstance().getAvatar())
                        .build());

        AvatarUtils.loadAvatarByUrl(mIvBArea,
                AvatarUtils.newParamsBuilder(MyUserInfoManager.getInstance().getUid())
                        .setCircle(true)
                        .setTimestamp(MyUserInfoManager.getInstance().getAvatar())
                        .build());

        AvatarUtils.loadAvatarByUrl(mIvCArea,
                AvatarUtils.newParamsBuilder(MyUserInfoManager.getInstance().getUid())
                        .setCircle(true)
                        .setTimestamp(MyUserInfoManager.getInstance().getAvatar())
                        .build());
    }

    @Override
    public void toShow(RelativeLayout parentViewGroup, Bundle bundle) {
        //这里可能有动画啥的
        setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        parentViewGroup.addView(this);
    }

    @Override
    public void toHide(RelativeLayout parentViewGroup) {
        //可能有动画
        setVisibility(GONE);
    }

    @Override
    public void toRemoveFromStack(RelativeLayout parentViewGroup) {
        parentViewGroup.removeView(this);
    }

    //每个场景有一个是不是可以往下跳的判断
    @Override
    public boolean isPrepareToNextSence() {
        return true;
    }

    @Override
    public void onResumeSence(RelativeLayout parentViewGroup) {
        setVisibility(VISIBLE);
    }

    @Override
    public boolean removeWhenPush() {
        return false;
    }

    @Override
    public void setSenceController(MatchSenceController matchSenceController) {
        this.matchSenceController = matchSenceController;
    }
}
