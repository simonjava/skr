package com.zq.notification;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.common.core.avatar.AvatarUtils;
import com.common.core.userinfo.UserInfoManager;
import com.common.core.userinfo.model.UserInfoModel;
import com.common.utils.U;
import com.common.view.DebounceViewClickListener;
import com.common.view.ex.ExTextView;
import com.component.busilib.R;
import com.facebook.drawee.view.SimpleDraweeView;
import com.zq.live.proto.Common.ESex;

/**
 * 关注弹窗通知
 */
public class RelationNotifyView extends RelativeLayout {

    SimpleDraweeView mAvatarIv;
    ExTextView mNameTv;
    ExTextView mHintTv;
    ExTextView mFollowTv;

    UserInfoModel mUserInfoModel;

    public RelationNotifyView(Context context) {
        super(context);
        init();
    }

    public RelationNotifyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RelationNotifyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.relation_notification_view_layout, this);
        mAvatarIv = (SimpleDraweeView) findViewById(R.id.avatar_iv);
        mNameTv = (ExTextView) findViewById(R.id.name_tv);
        mHintTv = (ExTextView) findViewById(R.id.hint_tv);
        mFollowTv = (ExTextView) findViewById(R.id.follow_tv);

        mFollowTv.setOnClickListener(new DebounceViewClickListener() {
            @Override
            public void clickValid(View v) {
                // TODO: 2019/3/21  处理关注请求
                if (mUserInfoModel.isFollow() || mUserInfoModel.isFriend()) {
                    UserInfoManager.getInstance().mateRelation(mUserInfoModel.getUserId(),
                            UserInfoManager.RA_UNBUILD, mUserInfoModel.isFriend());
                } else {
                    UserInfoManager.getInstance().mateRelation(mUserInfoModel.getUserId(),
                            UserInfoManager.RA_BUILD, mUserInfoModel.isFriend());
                }
            }
        });
    }

    public void bindData(UserInfoModel userInfoModel) {
        this.mUserInfoModel = userInfoModel;

        AvatarUtils.loadAvatarByUrl(mAvatarIv,
                AvatarUtils.newParamsBuilder(mUserInfoModel.getAvatar())
                        .setCircle(true)
                        .setBorderColorBySex(mUserInfoModel.getSex() == ESex.SX_MALE.getValue())
                        .setBorderWidth(U.getDisplayUtils().dip2px(2))
                        .build());
        mNameTv.setText(mUserInfoModel.getNickname());

        if (mUserInfoModel.isFriend()) {
            // 好友怎么展示
        } else if (mUserInfoModel.isFollow()) {
            // 只是关注消息
        } else {
            // 又不是好友，又没有关注
        }
    }
}
