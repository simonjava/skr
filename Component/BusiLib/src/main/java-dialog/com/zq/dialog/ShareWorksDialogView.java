package com.zq.dialog;

import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.common.view.AnimateClickListener;
import com.common.view.ex.ExTextView;
import com.component.busilib.R;

public class ShareWorksDialogView extends RelativeLayout {

    String mSongName;
    Listener mListener;

    ExTextView mTvTitle;
    ExTextView mTvText;
    TextView mTvQqShare;
    TextView mTvQzoneShare;
    TextView mTvWeixinShare;
    TextView mTvQuanShare;

    public ShareWorksDialogView(Context context, String songName, Listener listener) {
        super(context);
        this.mSongName = songName;
        this.mListener = listener;
        init(context);
    }

    private void init(Context context) {
        inflate(context, R.layout.share_works_panel, this);

        mTvTitle = (ExTextView) findViewById(R.id.tv_title);
        mTvText = (ExTextView) findViewById(R.id.tv_text);
        mTvQqShare = (TextView) findViewById(R.id.tv_qq_share);
        mTvQzoneShare = (TextView) findViewById(R.id.tv_qzone_share);
        mTvWeixinShare = (TextView) findViewById(R.id.tv_weixin_share);
        mTvQuanShare = (TextView) findViewById(R.id.tv_quan_share);

        mTvText.setText("分享《" + mSongName + "》");

        mTvQqShare.setOnClickListener(new AnimateClickListener() {
            @Override
            public void click(View view) {
                if (mListener != null) {
                    mListener.onClickQQShare();
                }
            }
        });


        mTvQzoneShare.setOnClickListener(new AnimateClickListener() {
            @Override
            public void click(View view) {
                if (mListener != null) {
                    mListener.onClickQZoneShare();
                }
            }
        });

        mTvWeixinShare.setOnClickListener(new AnimateClickListener() {
            @Override
            public void click(View view) {
                if (mListener != null) {
                    mListener.onClickWeixinShare();
                }
            }
        });

        mTvQuanShare.setOnClickListener(new AnimateClickListener() {
            @Override
            public void click(View view) {
                if (mListener != null) {
                    mListener.onClickQuanShare();
                }
            }
        });

    }

    public interface Listener {
        void onClickQQShare();

        void onClickQZoneShare();

        void onClickWeixinShare();

        void onClickQuanShare();
    }
}