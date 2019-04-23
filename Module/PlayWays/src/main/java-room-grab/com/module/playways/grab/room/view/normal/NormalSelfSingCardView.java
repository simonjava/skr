package com.module.playways.grab.room.view.normal;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.common.log.MyLog;
import com.common.utils.U;
import com.engine.EngineManager;
import com.engine.arccloud.ArcRecognizeListener;
import com.engine.arccloud.SongInfo;
import com.module.playways.RoomDataUtils;
import com.module.playways.grab.room.GrabRoomData;
import com.module.playways.grab.room.model.GrabRoundInfoModel;
import com.module.playways.grab.room.view.common.SingCountDownView;
import com.module.playways.grab.room.view.control.SelfSingCardView;
import com.module.playways.others.LyricAndAccMatchManager;
import com.module.playways.room.song.model.SongModel;
import com.module.playways.R;
import com.zq.live.proto.Room.EQRoundStatus;
import com.zq.live.proto.Room.EWantSingType;
import com.zq.lyrics.LyricsManager;
import com.zq.lyrics.widget.ManyLyricsView;
import com.zq.lyrics.widget.VoiceScaleView;

import java.util.List;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * 你的主场景歌词
 */
public class NormalSelfSingCardView extends RelativeLayout {
    public final static String TAG = "SelfSingCardView2";

    TextView mTvLyric;
    ManyLyricsView mManyLyricsView;
    Disposable mDisposable;
    GrabRoomData mRoomData;
    SongModel mSongModel;
    ImageView mIvChallengeIcon;
    VoiceScaleView mVoiceScaleView;
    SingCountDownView mSingCountDownView;

    LyricAndAccMatchManager mLyricAndAccMatchManager = new LyricAndAccMatchManager();


    public NormalSelfSingCardView(Context context) {
        super(context);
        init();
    }

    public NormalSelfSingCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public NormalSelfSingCardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.grab_normal_self_sing_card_layout, this);
        mTvLyric = findViewById(R.id.tv_lyric);
        mManyLyricsView = (ManyLyricsView) findViewById(R.id.many_lyrics_view);
        mSingCountDownView = (SingCountDownView) findViewById(R.id.sing_count_down_view);
        mVoiceScaleView = (VoiceScaleView) findViewById(R.id.voice_scale_view);
        mIvChallengeIcon = (ImageView) findViewById(R.id.iv_challenge_icon);
    }

    public void playLyric() {
        GrabRoundInfoModel infoModel = mRoomData.getRealRoundInfo();
        if (infoModel == null) {
            MyLog.d(TAG, "infoModel 是空的");
            return;
        }
        if (infoModel.getWantSingType() == EWantSingType.EWST_COMMON_OVER_TIME.getValue()
                || infoModel.getWantSingType() == EWantSingType.EWST_ACCOMPANY_OVER_TIME.getValue()) {
            mIvChallengeIcon.setVisibility(VISIBLE);
        } else {
            mIvChallengeIcon.setVisibility(INVISIBLE);
        }
        mSongModel = infoModel.getMusic();
        mTvLyric.setText("歌词加载中...");
        mTvLyric.setVisibility(VISIBLE);
        mManyLyricsView.setVisibility(GONE);
        mManyLyricsView.initLrcData();
        mVoiceScaleView.setVisibility(View.GONE);
        if (mSongModel == null) {
            MyLog.d(TAG, "songModel 是空的");
            return;
        }

        int totalTs = infoModel.getSingTotalMs();
        boolean withAcc = false;
        if (infoModel.isAccRound() && mRoomData != null && mRoomData.isAccEnable()) {
            withAcc = true;
        }
        if (RoomDataUtils.isPKRound(mRoomData)) {
            // pk模式
            withAcc = true;
        }
        if (!withAcc) {
            playWithNoAcc(mSongModel);
            mSingCountDownView.setTagImgRes(R.drawable.ycdd_daojishi_qingchang);
            mLyricAndAccMatchManager.stop();
        } else {
            mSingCountDownView.setTagImgRes(R.drawable.ycdd_daojishi_banzou);
            SongModel curSong = mSongModel;
            if (infoModel.getStatus() == EQRoundStatus.QRS_SPK_SECOND_PEER_SING.getValue()) {
                if (mSongModel.getPkMusic() != null) {
                    curSong = mSongModel.getPkMusic();
                }
            }
            mLyricAndAccMatchManager.setArgs(mManyLyricsView, mVoiceScaleView,
                    curSong.getLyric(),
                    curSong.getStandLrcBeginT(), curSong.getStandLrcBeginT() + totalTs,
                    curSong.getBeginMs(), curSong.getBeginMs() + totalTs);

            SongModel finalCurSong = curSong;
            mLyricAndAccMatchManager.start(new LyricAndAccMatchManager.Listener() {
                @Override
                public void onLyricParseSuccess() {
                    mTvLyric.setVisibility(GONE);
                }

                @Override
                public void onLyricParseFailed() {
                    playWithNoAcc(finalCurSong);
                }

                @Override
                public void onLyricEventPost(int lineNum) {
                    mRoomData.setSongLineNum(lineNum);
                }

            });
            EngineManager.getInstance().setRecognizeListener(new ArcRecognizeListener() {
                @Override
                public void onResult(String result, List<SongInfo> list, SongInfo targetSongInfo, int lineNo) {
                    mLyricAndAccMatchManager.onAcrResult(result, list, targetSongInfo, lineNo);
                }
            });
        }
        starCounDown(totalTs);
    }

    private void playWithNoAcc(SongModel songModel) {
        if (songModel == null) {
            return;
        }
        mManyLyricsView.setVisibility(GONE);
        mTvLyric.setVisibility(VISIBLE);
        if (mDisposable != null && !mDisposable.isDisposed()) {
            mDisposable.dispose();
        }
        mDisposable = LyricsManager.getLyricsManager(U.app())
                .loadGrabPlainLyric(songModel.getStandLrc())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        mTvLyric.setText(s);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        MyLog.d(TAG, "accept" + " throwable=" + throwable);
                    }
                });
    }

    private void starCounDown(int totalMs) {
        mSingCountDownView.startPlay(0, totalMs, true);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mDisposable != null && !mDisposable.isDisposed()) {
            mDisposable.dispose();
        }
        if (mLyricAndAccMatchManager != null) {
            mLyricAndAccMatchManager.stop();
        }
    }

    @Override
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
        if (visibility == GONE) {
            mSingCountDownView.reset();
            if (mManyLyricsView != null) {
                mManyLyricsView.setLyricsReader(null);
            }
            if (mLyricAndAccMatchManager != null) {
                mLyricAndAccMatchManager.stop();
            }
        }
    }

    public void destroy() {
        if (mManyLyricsView != null) {
            mManyLyricsView.release();
        }
    }

    public void setRoomData(GrabRoomData roomData) {
        mRoomData = roomData;
    }

    SelfSingCardView.Listener mListener;

    public void setListener(SelfSingCardView.Listener l) {
        mListener = l;
    }


}
