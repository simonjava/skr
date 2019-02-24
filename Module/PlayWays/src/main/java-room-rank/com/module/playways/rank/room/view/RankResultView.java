package com.module.playways.rank.room.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.common.core.avatar.AvatarUtils;
import com.common.core.myinfo.MyUserInfoManager;
import com.common.utils.U;
import com.common.view.ex.ExImageView;
import com.common.view.ex.ExTextView;
import com.component.busilib.view.BitmapTextView;
import com.facebook.drawee.view.SimpleDraweeView;
import com.module.playways.RoomData;
import com.module.playways.RoomDataUtils;
import com.module.playways.rank.prepare.model.PlayerInfoModel;
import com.module.playways.rank.room.model.UserGameResultModel;
import com.module.rank.R;
import com.zq.live.proto.Room.EWinType;

import java.util.List;

/**
 * 某个人的战绩
 */
public class RankResultView extends RelativeLayout {

    RelativeLayout mAvatarArea;
    SimpleDraweeView mAvatarIv;
    ExImageView mResultIv;
    ExTextView mNameTv;
    ExTextView mSongTv;

    RelativeLayout mResultArea;
    SimpleDraweeView mAvatarIvFirst;
    ExTextView mFirstResultTv;
    SimpleDraweeView mAvatarIvSecond;
    ExTextView mSecondResultTv;
    SimpleDraweeView mAvatarIvThree;
    ExTextView mThirdResultTv;

    RelativeLayout mScoreArea;
    BitmapTextView mPkScore;

    public RankResultView(Context context) {
        super(context);
        init();
    }

    public RankResultView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RankResultView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.rank_result_view_layout, this);

        mAvatarArea = (RelativeLayout) findViewById(R.id.avatar_area);
        mAvatarIv = (SimpleDraweeView) findViewById(R.id.avatar_iv);
        mResultIv = (ExImageView) findViewById(R.id.result_iv);
        mNameTv = (ExTextView) findViewById(R.id.name_tv);
        mSongTv = (ExTextView) findViewById(R.id.song_tv);
        mResultArea = (RelativeLayout) findViewById(R.id.result_area);
        mAvatarIvFirst = (SimpleDraweeView) findViewById(R.id.avatar_iv_first);
        mFirstResultTv = (ExTextView) findViewById(R.id.first_result_tv);
        mAvatarIvSecond = (SimpleDraweeView) findViewById(R.id.avatar_iv_second);
        mSecondResultTv = (ExTextView) findViewById(R.id.second_result_tv);
        mAvatarIvThree = (SimpleDraweeView) findViewById(R.id.avatar_iv_three);
        mThirdResultTv = (ExTextView) findViewById(R.id.third_result_tv);
        mScoreArea = (RelativeLayout) findViewById(R.id.score_area);
        mPkScore = (BitmapTextView) findViewById(R.id.pk_score);
    }

    //绑定数据,表示是某个人的投票结果
    public void bindData(RoomData roomData, int useID) {
        if (useID == 0) {
            return;
        }
        UserGameResultModel userGameResultModel = roomData.getRecordData().getUserGameResultModel(useID);
        PlayerInfoModel playerInfoModel = RoomDataUtils.getPlayerInfoById(roomData, userGameResultModel.getUserID());
        if (playerInfoModel != null) {
            AvatarUtils.loadAvatarByUrl(mAvatarIv,
                    AvatarUtils.newParamsBuilder(MyUserInfoManager.getInstance().getAvatar())
                            .setCircle(true)
                            .setBorderColorBySex(playerInfoModel.getUserInfo().getIsMale())
                            .setBorderWidth(U.getDisplayUtils().dip2px(3))
                            .build());
            mNameTv.setText(playerInfoModel.getUserInfo().getNickname());
            mSongTv.setText(playerInfoModel.getSongList().get(0).getItemName());
            if (userGameResultModel.getWinType() == EWinType.Win.getValue()) {
                mResultIv.setBackground(getResources().getDrawable(R.drawable.ic_medal_win));
            } else if (userGameResultModel.getWinType() == EWinType.Draw.getValue()) {
                mResultIv.setBackground(getResources().getDrawable(R.drawable.ic_medal_draw));
            } else if (userGameResultModel.getWinType() == EWinType.Lose.getValue()) {
                mResultIv.setBackground(getResources().getDrawable(R.drawable.ic_medal_lose));
            }
        }

        if (userGameResultModel.getAudienceScores().size() == 3) {
            PlayerInfoModel playerInfoModel1 = RoomDataUtils.getPlayerInfoById(roomData, userGameResultModel.getAudienceScores().get(0).getUserID());
            if (playerInfoModel1 != null) {
                AvatarUtils.loadAvatarByUrl(mAvatarIvFirst,
                        AvatarUtils.newParamsBuilder(playerInfoModel1.getUserInfo().getAvatar())
                                .setCircle(true)
                                .setBorderColorBySex(playerInfoModel1.getUserInfo().getIsMale())
                                .setBorderWidth(U.getDisplayUtils().dip2px(2))
                                .build());
            }

            PlayerInfoModel playerInfoModel2 = RoomDataUtils.getPlayerInfoById(roomData, userGameResultModel.getAudienceScores().get(0).getUserID());
            if (playerInfoModel2 != null) {
                AvatarUtils.loadAvatarByUrl(mAvatarIvSecond,
                        AvatarUtils.newParamsBuilder(playerInfoModel2.getUserInfo().getAvatar())
                                .setCircle(true)
                                .setBorderColorBySex(playerInfoModel2.getUserInfo().getIsMale())
                                .setBorderWidth(U.getDisplayUtils().dip2px(2))
                                .build());
            }

            PlayerInfoModel playerInfoModel3 = RoomDataUtils.getPlayerInfoById(roomData, userGameResultModel.getAudienceScores().get(0).getUserID());
            if (playerInfoModel3 != null) {
                AvatarUtils.loadAvatarByUrl(mAvatarIvThree,
                        AvatarUtils.newParamsBuilder(playerInfoModel3.getUserInfo().getAvatar())
                                .setCircle(true)
                                .setBorderColorBySex(playerInfoModel3.getUserInfo().getIsMale())
                                .setBorderWidth(U.getDisplayUtils().dip2px(2))
                                .build());
            }
        }

        mPkScore.setText(userGameResultModel.getTotalScore() + "");
    }
}
