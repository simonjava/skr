package com.module.playways.grab.room.production;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;

import com.common.core.myinfo.MyUserInfoManager;
import com.common.image.fresco.FrescoWorker;
import com.common.image.model.ImageFactory;
import com.common.image.model.oss.OssImgFactory;
import com.common.log.MyLog;
import com.common.utils.ImageUtils;
import com.common.utils.U;
import com.common.view.DebounceViewClickListener;
import com.common.view.ex.ExImageView;
import com.common.view.ex.ExRelativeLayout;
import com.common.view.ex.ExTextView;
import com.facebook.drawee.view.SimpleDraweeView;
import com.module.playways.R;
import com.module.playways.grab.room.model.WorksUploadModel;

public class ResultProducationViewHolder extends RecyclerView.ViewHolder {

    public final static String TAG = "ResultProducationViewHolder";

    WorksUploadModel mWonderfulMomentModel;

    int position;
    boolean isPlay;

    ExImageView mBlightTipsIv;
    ExRelativeLayout mCoverArea;
    SimpleDraweeView mCoverIv;
    ExImageView mPlayBackIv;
    RelativeLayout mSaveShareArea;
    ExImageView mSaveShareIv;
    ExTextView mSongNameTv;
    ExTextView mSongOwnerTv;

    ResultProducationAdapter.Listener mListener;

    public ResultProducationViewHolder(View itemView, ResultProducationAdapter.Listener listener) {
        super(itemView);
        this.mListener = listener;

        mBlightTipsIv = (ExImageView) itemView.findViewById(R.id.blight_tips_iv);
        mCoverArea = (ExRelativeLayout) itemView.findViewById(R.id.cover_area);
        mCoverIv = (SimpleDraweeView) itemView.findViewById(R.id.cover_iv);
        mPlayBackIv = (ExImageView) itemView.findViewById(R.id.play_back_iv);
        mSaveShareArea = (RelativeLayout) itemView.findViewById(R.id.save_share_area);
        mSaveShareIv = (ExImageView) itemView.findViewById(R.id.save_share_iv);
        mSongNameTv = (ExTextView) itemView.findViewById(R.id.song_name_tv);
        mSongOwnerTv = (ExTextView) itemView.findViewById(R.id.song_owner_tv);

        mPlayBackIv.setOnClickListener(new DebounceViewClickListener() {
            @Override
            public void clickValid(View v) {
                if (isPlay) {
                    if (mListener != null) {
                        mListener.onClickPause(position, mWonderfulMomentModel);
                    }
                } else {
                    if (mListener != null) {
                        mListener.onClickPlay(position, mWonderfulMomentModel);
                    }
                }
            }
        });

        mSaveShareArea.setOnClickListener(new DebounceViewClickListener() {
            @Override
            public void clickValid(View v) {
                if (mListener != null) {
                    mListener.onClickSaveAndShare(position, mWonderfulMomentModel);
                }
            }
        });
    }

    public void bindData(int position, WorksUploadModel momentModel, boolean isPlay) {
        this.position = position;
        this.mWonderfulMomentModel = momentModel;
        this.isPlay = isPlay;

        if (mWonderfulMomentModel != null && !TextUtils.isEmpty(mWonderfulMomentModel.getSongModel().getCover())) {
            mSongNameTv.setText(mWonderfulMomentModel.getSongModel().getDisplaySongName());
            if (mWonderfulMomentModel.isBlight()) {
                mBlightTipsIv.setVisibility(View.VISIBLE);
            } else {
                mBlightTipsIv.setVisibility(View.GONE);
            }
        } else {
            MyLog.w(TAG, "bindData" + " position=" + position + " momentModel=" + momentModel + " isPlay=" + isPlay);
        }

        mSongOwnerTv.setText(MyUserInfoManager.getInstance().getNickName());
        FrescoWorker.loadImage(mCoverIv,
                ImageFactory.newPathImage(MyUserInfoManager.getInstance().getAvatar())
                        .setCornerRadius(U.getDisplayUtils().dip2px(7))
                        .setBorderWidth(U.getDisplayUtils().dip2px(2))
                        .setBorderColor(U.getColor(R.color.white))
                        .addOssProcessors(OssImgFactory.newResizeBuilder().setW(ImageUtils.SIZE.SIZE_160.getW()).build())
                        .build());

        if (isPlay) {
            mPlayBackIv.setBackgroundResource(R.drawable.grab_works_pause);
        } else {
            mPlayBackIv.setBackgroundResource(R.drawable.grab_works_play);
        }

    }

}
