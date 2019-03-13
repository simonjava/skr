package com.module.playways.grab.room.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.common.log.MyLog;
import com.common.rx.RxRetryAssist;
import com.common.utils.HandlerTaskTimer;
import com.common.utils.SongResUtils;
import com.common.utils.U;
import com.common.view.ex.ExTextView;
import com.component.busilib.constans.GameModeType;
import com.component.busilib.manager.BgMusicManager;
import com.module.RouterConstants;
import com.module.playways.grab.room.GrabRoomData;
import com.module.playways.grab.room.model.GrabRoundInfoModel;
import com.module.playways.rank.room.view.ArcProgressBar;
import com.module.playways.rank.song.model.SongModel;
import com.module.rank.R;
import com.zq.lyrics.LyricsManager;
import com.zq.lyrics.LyricsReader;
import com.zq.lyrics.event.LyricEventLauncher;
import com.zq.lyrics.widget.AbstractLrcView;
import com.zq.lyrics.widget.ManyLyricsView;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okio.BufferedSource;
import okio.Okio;

import static com.zq.lyrics.widget.AbstractLrcView.LRCPLAYERSTATUS_PLAY;

/**
 * 你的主场景歌词
 */
public class SelfSingCardView2 extends RelativeLayout {
    public final static String TAG = "SelfSingCardView2";

    TextView mTvLyric;
    ArcProgressBar mCountDownProcess;
    ExTextView mCountDownTv;
    ImageView mCountIv;
    ManyLyricsView mManyLyricsView;

    Disposable mDisposable;
    HandlerTaskTimer mCounDownTask;

    GrabRoomData mRoomData;

    LyricEventLauncher mLyricEventLauncher = new LyricEventLauncher();

    public SelfSingCardView2(Context context) {
        super(context);
        init();
    }

    public SelfSingCardView2(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SelfSingCardView2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.grab_self_sing_card_layout_two, this);
        mTvLyric = findViewById(R.id.tv_lyric);
        mManyLyricsView = (ManyLyricsView) findViewById(R.id.many_lyrics_view);
        mCountDownProcess = (ArcProgressBar) findViewById(R.id.count_down_process);
        mCountDownTv = (ExTextView) findViewById(R.id.count_down_tv);
        mCountIv = (ImageView) findViewById(R.id.count_iv);
    }


    public void playLyric(SongModel songModel, boolean hasAcc) {
        mTvLyric.setText("歌词加载中...");
        mTvLyric.setVisibility(VISIBLE);
        mManyLyricsView.setVisibility(GONE);
        mManyLyricsView.initLrcData();
        if (songModel == null) {
            MyLog.d(TAG, "songModel 是空的");
            return;
        }

        if (!hasAcc) {
            playWithNoAcc(songModel);
        } else {
            playWithAcc(songModel);
        }

        starCounDown(songModel);
    }

    private void playWithNoAcc(SongModel songModel) {
        mTvLyric.setVisibility(VISIBLE);
        if (mDisposable != null && !mDisposable.isDisposed()) {
            mDisposable.dispose();
        }
        File file = SongResUtils.getGrabLyricFileByUrl(songModel.getStandLrc());
        if (file == null || !file.exists()) {
            MyLog.w(TAG, "playLyric is not in local file");
            fetchLyricTask(songModel);
        } else {
            MyLog.w(TAG, "playLyric is exist");
            drawLyric(file);
        }
    }

    private void playWithAcc(SongModel songModel) {
        if (songModel == null || TextUtils.isEmpty(songModel.getAcc())) {
            MyLog.d(TAG, "playWithAcc" + " songModel data is error, " + songModel);
            return;
        }
        mDisposable = LyricsManager.getLyricsManager(U.app())
                .fetchAndLoadLyrics(songModel.getLyric())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<LyricsReader>() {
                    @Override
                    public void accept(LyricsReader lyricsReader) throws Exception {
                        MyLog.w(TAG, "onEventMainThread " + "play");
                        mTvLyric.setVisibility(GONE);
                        mManyLyricsView.setVisibility(VISIBLE);
                        mManyLyricsView.initLrcData();
                        int lrcBeginTs = songModel.getStandLrcBeginT();
                        int totalMs = songModel.getTotalMs();
                        int preAccTs = 5000;// 播放歌词前5秒的伴奏
                        lyricsReader.cut(lrcBeginTs,lrcBeginTs+totalMs-preAccTs);
                        mManyLyricsView.setLyricsReader(lyricsReader);

                        Set<Integer> set = new HashSet<>();
                        set.add(lyricsReader.getLineInfoIdByStartTs(lrcBeginTs));
                        mManyLyricsView.setNeedCountDownLine(set);

                        if (mManyLyricsView.getLrcStatus() == AbstractLrcView.LRCSTATUS_LRC
                                && mManyLyricsView.getLrcPlayerStatus() != LRCPLAYERSTATUS_PLAY) {
                            mManyLyricsView.play(lrcBeginTs-preAccTs);
                            mManyLyricsView.seekto(lrcBeginTs-preAccTs);
                            int lineNum = mLyricEventLauncher.postLyricEvent(lyricsReader,lrcBeginTs-preAccTs, lrcBeginTs+totalMs-preAccTs, null);
                            mRoomData.setSongLineNum(lineNum);
                        }
                    }
                }, throwable -> MyLog.e(TAG, throwable));
    }

    private void starCounDown(SongModel songModel) {
        mCountIv.setVisibility(GONE);
        mCountDownTv.setVisibility(VISIBLE);
        mCountDownProcess.startCountDown(0, songModel.getTotalMs());
        int counDown = songModel.getTotalMs() / 1000;
        mCounDownTask = HandlerTaskTimer.newBuilder()
                .interval(1000)
                .take(counDown)
                .start(new HandlerTaskTimer.ObserverW() {
                    @Override
                    public void onNext(Integer integer) {
                        mCountDownTv.setText((counDown - integer) + "");
                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();
                        if (mListener != null) {
                            mListener.onSelfSingOver();
                        }
                        stopCounDown();
                        mCountIv.setVisibility(VISIBLE);
                        mCountDownTv.setVisibility(GONE);
                    }
                });
    }

    private void stopCounDown() {
        if (mCounDownTask != null) {
            mCounDownTask.dispose();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopCounDown();
        if (mDisposable != null && !mDisposable.isDisposed()) {
            mDisposable.dispose();
        }
        if (mLyricEventLauncher != null) {
            mLyricEventLauncher.destroy();
        }
    }

    @Override
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
        if (visibility == GONE) {
            stopCounDown();
        }
    }

    /**
     * 拉取一唱到底的歌词字符串
     *
     * @param songModel
     */
    private void fetchLyricTask(SongModel songModel) {
        MyLog.w(TAG, "fetchLyricTask" + " songModel=" + songModel);
        mDisposable = Observable.create(new ObservableOnSubscribe<File>() {
            @Override
            public void subscribe(ObservableEmitter<File> emitter) {
                File tempFile = new File(SongResUtils.createStandLyricTempFileName(songModel.getStandLrc()));
                boolean isSuccess = U.getHttpUtils().downloadFileSync(songModel.getStandLrc(), tempFile, null);
                File oldName = new File(SongResUtils.createStandLyricTempFileName(songModel.getStandLrc()));
                File newName = new File(SongResUtils.createStandLyricFileName(songModel.getStandLrc()));
                if (isSuccess) {
                    if (oldName != null && oldName.renameTo(newName)) {
                        MyLog.w(TAG, "已重命名");
                        emitter.onNext(newName);
                        emitter.onComplete();
                    } else {
                        MyLog.w(TAG, "Error");
                        emitter.onError(new Throwable("重命名错误"));
                    }
                } else {
                    emitter.onError(new Throwable("下载失败, 文件地址是" + songModel.getStandLrc()));
                }
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .retryWhen(new RxRetryAssist(5, 1, false))
//                .compose(bindUntilEvent(FragmentEvent.DESTROY))
                .subscribe(file -> {
                    final File fileName = SongResUtils.getGrabLyricFileByUrl(songModel.getStandLrc());
                    drawLyric(fileName);
                }, throwable -> {
                    MyLog.e(TAG, throwable);
                });
    }

    public void destroy() {
        if (mManyLyricsView != null) {
            mManyLyricsView.release();
        }
    }

    /**
     * 画出一唱到底歌词字符串
     *
     * @param file
     */
    private void drawLyric(final File file) {
        MyLog.w(TAG, "file is " + file);
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) {
                if (file != null && file.exists() && file.isFile()) {
                    try (BufferedSource source = Okio.buffer(Okio.source(file))) {
                        emitter.onNext(source.readUtf8());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                emitter.onComplete();
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new Consumer<String>() {
            @Override
            public void accept(String o) {
                mTvLyric.setText(o);
            }
        }, throwable -> MyLog.e(TAG, throwable));
    }

    public void setRoomData(GrabRoomData roomData) {
        mRoomData = roomData;
    }

    Listener mListener;

    public void setListener(Listener l) {
        mListener = l;
    }

    public interface Listener {
        void onSelfSingOver();
    }

}
