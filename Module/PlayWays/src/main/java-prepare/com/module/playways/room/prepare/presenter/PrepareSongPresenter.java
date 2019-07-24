package com.module.playways.room.prepare.presenter;

import android.text.TextUtils;

import com.common.log.MyLog;
import com.common.mvp.PresenterEvent;
import com.common.mvp.RxLifeCyclePresenter;
import com.common.utils.HttpUtils;
import com.common.utils.U;
import com.module.playways.room.prepare.view.IPrepareResView;
import com.module.playways.room.song.model.SongModel;
import com.component.lyrics.LyricsManager;
import com.component.lyrics.LyricsReader;
import com.component.lyrics.model.LyricsLineInfo;
import com.component.lyrics.model.UrlRes;
import com.component.lyrics.utils.SongResUtils;
import com.component.lyrics.utils.ZipUrlResourceManager;

import org.greenrobot.greendao.annotation.NotNull;

import java.util.LinkedList;
import java.util.TreeMap;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class PrepareSongPresenter extends RxLifeCyclePresenter {
    HttpUtils.OnDownloadProgress mOnDownloadProgress;
    SongModel mSongModel;

    ZipUrlResourceManager mSongResourceZhang;

    IPrepareResView mIPrepareResView;

    public PrepareSongPresenter(@NotNull HttpUtils.OnDownloadProgress onDownloadProgress, IPrepareResView iPrepareResView, @NotNull SongModel songModel) {
        MyLog.d(TAG, "PrepareSongPresenter" + " mOnDownloadProgress=" + onDownloadProgress + " mSongModel=" + songModel);
        this.mOnDownloadProgress = onDownloadProgress;
        this.mSongModel = songModel;
        this.mIPrepareResView = iPrepareResView;
    }

    public void prepareRes() {
        LinkedList<UrlRes> songResList = new LinkedList<>();
//        String lyricUrl = mSongModel.getLyric();
//        if (!TextUtils.isEmpty(lyricUrl)) {
//            UrlRes lyric = new UrlRes(lyricUrl, SongResUtils.getLyricDir(), SongResUtils.SUFF_ZRCE);
//            songResList.add(lyric);
//        }

        //伴奏
        String accUrl = mSongModel.getAcc();
        if (!TextUtils.isEmpty(accUrl)) {
            UrlRes acc = new UrlRes(accUrl, SongResUtils.getACCDir(), U.getFileUtils().getSuffixFromUrl(accUrl, SongResUtils.SUFF_ACC));
            songResList.add(acc);
        }

        //原唱
//        String oriUrl = mSongModel.getOri();
//        if (!TextUtils.isEmpty(oriUrl)) {
//            UrlRes acc = new UrlRes(oriUrl, SongResUtils.getORIDir(),U.getFileUtils().getSuffixFromUrl(oriUrl,SongResUtils.SUFF_ORI));
//            songResList.add(acc);
//        }

        //评分文件
        String midiUrl = mSongModel.getMidi();
        if (!TextUtils.isEmpty(midiUrl)) {
            UrlRes midi = new UrlRes(midiUrl, SongResUtils.getMIDIDir(), U.getFileUtils().getSuffixFromUrl(midiUrl, SongResUtils.SUFF_MIDI));
            songResList.add(midi);
        }

        mSongResourceZhang = new ZipUrlResourceManager(songResList, mOnDownloadProgress);
        mSongResourceZhang.go();
        showLyric();
    }

    private void showLyric() {
        LyricsManager.getLyricsManager(U.app())
                .loadStandardLyric(mSongModel.getLyric())
                .compose(bindUntilEvent(PresenterEvent.DESTROY))
                .subscribe(new Consumer<LyricsReader>() {
                    @Override
                    public void accept(LyricsReader lyricsReader) throws Exception {
                        TreeMap<Integer, LyricsLineInfo> lyricsLineInfos = lyricsReader.getLrcLineInfos();

                        for (int i = 0; i < lyricsLineInfos.size(); i++) {
                            LyricsLineInfo lyricsLineInfo = lyricsLineInfos.get(i);
                            if (lyricsLineInfo.getStartTime() >= mSongModel.getRankLrcBeginT()) {
                                String l = "";
                                int count = lyricsLineInfos.size() - i > 6 ? 6 : lyricsLineInfos.size() - i;
                                for (int j = 0; j < count; j++) {
                                    l = l + lyricsLineInfos.get(i + j).getLineLyrics() + "\n";
                                }

                                mIPrepareResView.onLyricReady(l);
                                break;
                            }
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        MyLog.e(TAG, throwable);
                        mIPrepareResView.lyricReadyFailed();
                    }
                });
    }

    public void cancelTask() {
        mSongResourceZhang.cancelAllTask();
    }
}
