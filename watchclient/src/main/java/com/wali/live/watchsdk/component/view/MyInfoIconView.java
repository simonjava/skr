package com.wali.live.watchsdk.component.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.base.image.fresco.BaseImageView;
import com.mi.live.data.account.MyUserInfoManager;
import com.mi.live.data.account.UserAccountManager;
import com.wali.live.utils.AvatarUtils;
import com.wali.live.watchsdk.R;

public class MyInfoIconView extends RelativeLayout {
    private static final String TAG = "MyInfoIconView";

    BaseImageView mMyAvatarIv;
    TextView mDescTv;
    ImageView mNewMsgPointIv;

    public MyInfoIconView(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        inflate(context, R.layout.my_info_icon_view, this);
        mMyAvatarIv = (BaseImageView) this.findViewById(R.id.my_avatar_iv);
        mDescTv = (TextView) this.findViewById(R.id.desc_tv);
        mNewMsgPointIv = (ImageView) this.findViewById(R.id.new_msg_point_iv);
        tryBindAvatar();
    }

    public void tryBindAvatar() {
        if (UserAccountManager.getInstance().hasAccount()) {
            AvatarUtils.loadAvatarByUidTs(mMyAvatarIv, MyUserInfoManager.getInstance().getUuid(), MyUserInfoManager.getInstance().getAvatar(), true);
        } else {
            AvatarUtils.loadAvatarByUidTs(mMyAvatarIv, 0, 0, true);
        }
    }

    public void setMsgUnreadCnt(int unReadCnt) {
        if (unReadCnt > 0) {
            mNewMsgPointIv.setVisibility(VISIBLE);
        } else {
            mNewMsgPointIv.setVisibility(GONE);
        }
    }
}
