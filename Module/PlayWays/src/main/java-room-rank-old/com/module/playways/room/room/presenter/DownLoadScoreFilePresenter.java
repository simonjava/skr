package com.module.playways.room.room.presenter;

import android.text.TextUtils;

import com.common.log.MyLog;
import com.common.mvp.RxLifeCyclePresenter;
import com.common.utils.HttpUtils;
import com.component.lyrics.utils.SongResUtils;
import com.common.utils.U;
import com.module.playways.room.prepare.model.ResourceInfoModel;
import com.module.playways.room.room.model.RankPlayerInfoModel;
import com.component.lyrics.model.UrlRes;
import com.component.lyrics.utils.ZipUrlResourceManager;

import org.greenrobot.greendao.annotation.NotNull;

import java.util.LinkedList;
import java.util.List;

public class DownLoadScoreFilePresenter extends RxLifeCyclePresenter {
    public final String TAG = "DownLoadScoreFilePresenter";
    ZipUrlResourceManager mZipUrlResourceManager;
    HttpUtils.OnDownloadProgress mOnDownloadProgress;

    List<RankPlayerInfoModel> mPlayerInfoModels;

    public DownLoadScoreFilePresenter(@NotNull HttpUtils.OnDownloadProgress onDownloadProgress, List<RankPlayerInfoModel> playerInfoList) {
        mOnDownloadProgress = onDownloadProgress;
        mPlayerInfoModels = playerInfoList;
    }

    public void prepareRes() {
        if (mPlayerInfoModels == null) {
            return;
        }
        LinkedList<UrlRes> songResList = new LinkedList<>();
        for (RankPlayerInfoModel playerInfo : mPlayerInfoModels) {
            if (playerInfo.isSkrer()) {
                List<ResourceInfoModel> resourceInfoModelList = playerInfo.getResourceInfoList();
                if (resourceInfoModelList.size() > 0) {
                    String midiUrl = resourceInfoModelList.get(0).getMidiURL();
                    if (!TextUtils.isEmpty(midiUrl)) {
                        UrlRes midi = new UrlRes(midiUrl, SongResUtils.getScoreDir(), U.getFileUtils().getSuffixFromUrl(midiUrl, SongResUtils.SUFF_JSON));
                        songResList.add(midi);
                    }
                }
            }
        }

        MyLog.d(TAG, "songResList size is " + songResList.size());
        mZipUrlResourceManager = new ZipUrlResourceManager(songResList, mOnDownloadProgress);
        mZipUrlResourceManager.go();
    }

    @Override
    public void destroy() {
        super.destroy();
        if (mZipUrlResourceManager != null) {
            mZipUrlResourceManager.cancelAllTask();
        }
    }
}
