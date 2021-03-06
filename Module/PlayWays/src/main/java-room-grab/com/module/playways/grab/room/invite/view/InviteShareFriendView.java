package com.module.playways.grab.room.invite.view;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.common.view.recyclerview.RecyclerOnItemClickListener;
import com.component.busilib.constans.GameModeType;
import com.module.playways.R;
import com.module.playways.grab.room.invite.fragment.InviteFriendFragment2;
import com.module.playways.grab.room.invite.model.ShareModel;
import com.module.playways.grab.room.invite.adapter.InviteShareAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 邀请分享的view
 */
public class InviteShareFriendView extends RelativeLayout {

    TextView mTextTitle;
    RecyclerView mShareRecycle;
    InviteShareAdapter mAdapter;

    RecyclerOnItemClickListener<ShareModel> mItemClickListener;

    List<ShareModel> list = new ArrayList<>();

    public InviteShareFriendView(Context context) {
        super(context);
        init();
    }

    public InviteShareFriendView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public InviteShareFriendView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.invite_share_view_layout, this);
        mTextTitle = (TextView) findViewById(R.id.text_title);
        mShareRecycle = (RecyclerView) findViewById(R.id.share_recycle);
    }

    public void setData(int from) {
        initSharModel(from);
        mAdapter = new InviteShareAdapter(new RecyclerOnItemClickListener<ShareModel>() {
            @Override
            public void onItemClicked(View view, int position, ShareModel model) {
                if (mItemClickListener != null) {
                    mItemClickListener.onItemClicked(view, position, model);
                }
            }
        });
        mShareRecycle.setLayoutManager(new GridLayoutManager(getContext(), list.size()));
        mShareRecycle.setAdapter(mAdapter);
        mAdapter.setDataList(list);
        mAdapter.notifyDataSetChanged();
    }

    public void setListener(RecyclerOnItemClickListener<ShareModel> onItemClickListener) {
        this.mItemClickListener = onItemClickListener;
    }

    private void initSharModel(int from) {
        if (from == GameModeType.GAME_MODE_DOUBLE || from == GameModeType.GAME_MODE_RELAY) {
            list.add(new ShareModel(ShareModel.SHARE_TYPE_CIPHER, R.drawable.share_anhao_icon, "暗号邀请"));
            list.add(new ShareModel(ShareModel.SHARE_TYPE_QQ, R.drawable.share_qq_icon, "QQ好友"));
            list.add(new ShareModel(ShareModel.SHARE_TYPE_WECHAT, R.drawable.share_weixin_icon, "微信好友"));
        } else {
            list.add(new ShareModel(ShareModel.SHARE_TYPE_CIPHER, R.drawable.share_anhao_icon, "暗号邀请"));
            list.add(new ShareModel(ShareModel.SHARE_TYPE_QQ, R.drawable.share_qq_icon, "QQ好友"));
            list.add(new ShareModel(ShareModel.SHARE_TYPE_QQ_QZON, R.drawable.share_qzone_icon, "QQ空间"));
            list.add(new ShareModel(ShareModel.SHARE_TYPE_WECHAT, R.drawable.share_weixin_icon, "微信好友"));
            list.add(new ShareModel(ShareModel.SHARE_TYPE_WECHAT_FRIEND, R.drawable.share_quan_icon, "朋友圈"));
        }

    }
}
