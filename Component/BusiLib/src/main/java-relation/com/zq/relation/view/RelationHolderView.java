package com.zq.relation.view;

import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;

import com.common.core.CoreConfiguration;
import com.common.core.avatar.AvatarUtils;
import com.common.core.myinfo.MyUserInfoManager;
import com.common.core.userinfo.UserInfoManager;
import com.common.core.userinfo.model.UserInfoModel;
import com.common.utils.U;
import com.common.view.ex.ExTextView;
import com.common.view.recyclerview.RecyclerOnItemClickListener;
import com.component.busilib.R;
import com.facebook.drawee.view.SimpleDraweeView;
import com.zq.live.proto.Common.ESex;

public class RelationHolderView extends RecyclerView.ViewHolder {
    RelativeLayout mContent;
    SimpleDraweeView mAvatarIv;
    ExTextView mNameTv;
    ExTextView mUseridTv;
    ExTextView mFollowTv;

    int mMode;
    int position;
    UserInfoModel userInfoModel;

    public RelationHolderView(View itemView, int mode, final RecyclerOnItemClickListener recyclerOnItemClickListener) {
        super(itemView);

        this.mMode = mode;
        mContent = (RelativeLayout) itemView.findViewById(R.id.content);
        mAvatarIv = (SimpleDraweeView) itemView.findViewById(R.id.avatar_iv);
        mNameTv = (ExTextView) itemView.findViewById(R.id.name_tv);
        mUseridTv = (ExTextView) itemView.findViewById(R.id.userid_tv);
        mFollowTv = (ExTextView) itemView.findViewById(R.id.follow_tv);

        mFollowTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (recyclerOnItemClickListener != null) {
                    recyclerOnItemClickListener.onItemClicked(mFollowTv, position, userInfoModel);
                }
            }
        });

        mContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (recyclerOnItemClickListener != null) {
                    recyclerOnItemClickListener.onItemClicked(mContent, position, userInfoModel);
                }
            }
        });
    }

    public void bind(int position, UserInfoModel userInfoModel) {
        this.position = position;
        this.userInfoModel = userInfoModel;

        AvatarUtils.loadAvatarByUrl(mAvatarIv,
                AvatarUtils.newParamsBuilder(userInfoModel.getAvatar())
                        .setCircle(true)
                        .setBorderWidth(U.getDisplayUtils().dip2px(2))
                        .setBorderColorBySex(userInfoModel.getIsMale())
                        .build());
        mNameTv.setText(userInfoModel.getNickname());
        mUseridTv.setText("ID: " + String.valueOf(userInfoModel.getUserId()));

        if (mMode == UserInfoManager.RELATION_BLACKLIST) {
            mFollowTv.setVisibility(View.VISIBLE);
            mFollowTv.setText("移出黑名单");
            mFollowTv.setTextColor(Color.parseColor("#0C2275"));
            mFollowTv.setBackground(ContextCompat.getDrawable(U.app(), R.drawable.unfollow_bg));
        } else {
            if (userInfoModel.getUserId() == MyUserInfoManager.getInstance().getUid()) {
                mFollowTv.setVisibility(View.GONE);
                return;
            } else {
                if (userInfoModel.isFriend()) {
                    mFollowTv.setVisibility(View.GONE);
                } else if (userInfoModel.isFollow()) {
                    mFollowTv.setVisibility(View.GONE);
                } else {
                    mFollowTv.setVisibility(View.VISIBLE);
                    if (mMode == UserInfoManager.RELATION_FANS) {
                        mFollowTv.setText("加为好友");
                    } else {
                        mFollowTv.setText("关注");
                    }

                    mFollowTv.setTextColor(Color.parseColor("#0C2275"));
                    mFollowTv.setBackground(ContextCompat.getDrawable(U.app(), R.drawable.unfollow_bg));
                }
            }


        }
    }
}