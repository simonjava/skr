package com.zq.notification;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.common.core.avatar.AvatarUtils;
import com.common.core.userinfo.model.UserInfoModel;
import com.common.utils.U;
import com.common.view.ex.ExTextView;
import com.component.busilib.R;
import com.facebook.drawee.view.SimpleDraweeView;
import com.zq.live.proto.Common.ESex;

/**
 * 一唱到底邀请ui
 */
public class GrabInviteNotifyView extends RelativeLayout {

    SimpleDraweeView mAvatarIv;
    ExTextView mNameTv;
    ExTextView mHintTv;
    ExTextView mAgreeTv;
    ExTextView mIgnoreTv;

    UserInfoModel mUserInfoModel;
    int mRoomID;

    public GrabInviteNotifyView(Context context) {
        super(context);
        init();
    }

    public GrabInviteNotifyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GrabInviteNotifyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.grab_invite_notification_view_layout, this);
        mAvatarIv = (SimpleDraweeView) this.findViewById(R.id.avatar_iv);
        mNameTv = (ExTextView) this.findViewById(R.id.name_tv);
        mHintTv = (ExTextView) this.findViewById(R.id.hint_tv);
        mAgreeTv = (ExTextView) this.findViewById(R.id.agree_tv);
        mIgnoreTv = (ExTextView) this.findViewById(R.id.ignore_tv);

        mAgreeTv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 2019/3/21 同意加入一场到底房间
            }
        });

        mIgnoreTv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 2019/3/21 忽略该消息
            }
        });
    }

    public void bindData(UserInfoModel userInfoModel, int roomID){
        this.mUserInfoModel = userInfoModel;
        this.mRoomID = roomID;

        AvatarUtils.loadAvatarByUrl(mAvatarIv,
                AvatarUtils.newParamsBuilder(mUserInfoModel.getAvatar())
                        .setCircle(true)
                        .setBorderColorBySex(mUserInfoModel.getSex() == ESex.SX_MALE.getValue())
                        .setBorderWidth(U.getDisplayUtils().dip2px(2))
                        .build());
        mNameTv.setText(mUserInfoModel.getNickname());
    }


}
