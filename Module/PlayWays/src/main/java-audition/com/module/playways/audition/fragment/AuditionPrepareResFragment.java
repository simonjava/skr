package com.module.playways.audition.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.View;

import com.common.base.BaseFragment;
import com.common.base.FragmentDataListener;
import com.common.log.MyLog;
import com.common.utils.FragmentUtils;
import com.common.utils.HandlerTaskTimer;
import com.common.utils.HttpUtils;
import com.common.utils.U;
import com.common.view.DebounceViewClickListener;
import com.common.view.ex.ExImageView;
import com.common.view.ex.ExTextView;
import com.module.playways.room.prepare.model.PrepareData;
import com.module.playways.audition.presenter.PrepareAuditionResPresenter;
import com.module.playways.room.song.model.SongModel;
import com.module.playways.R;

public class AuditionPrepareResFragment extends BaseFragment {

    PrepareAuditionResPresenter mPrepareAuditionResPresenter;

    PrepareData mPrepareData = new PrepareData();

    ExImageView mIvBack;
    ExTextView mTvSongName;
    ExTextView mTvResProgress;

    Handler mHandler = new Handler();

    @Override
    public int initView() {
        return R.layout.audition_prepare_res_layout;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {

        mIvBack = (ExImageView) getRootView().findViewById(R.id.iv_back);
        mTvSongName = (ExTextView) getRootView().findViewById(R.id.tv_song_name);
        mTvResProgress = (ExTextView) getRootView().findViewById(R.id.tv_res_progress);

        mIvBack.setOnClickListener(new DebounceViewClickListener() {
            @Override
            public void clickValid(View v) {
                onBack();
            }
        });

        mTvSongName.setText("《" + mPrepareData.getSongModel().getItemName() + "》");

        mPrepareAuditionResPresenter = new PrepareAuditionResPresenter(new HttpUtils.OnDownloadProgress() {
            @Override
            public void onDownloaded(long downloaded, long totalLength) {
                long progress = (downloaded * 100) / totalLength;
                mHandler.post(() -> {
                    mTvResProgress.setText(progress + "%歌曲加载中...");
                });
            }

            @Override
            public void onCompleted(String localPath) {
                mHandler.post(() -> {
                    mTvResProgress.setText("100%歌曲加载中");
                    HandlerTaskTimer.newBuilder().delay(500).start(new HandlerTaskTimer.ObserverW() {
                        @Override
                        public void onNext(Integer integer) {
                            U.getFragmentUtils().popFragment(new FragmentUtils.PopParams.Builder()
                                    .setPopFragment(AuditionPrepareResFragment.this)
                                    .setPopAbove(false)
                                    .setHasAnimation(false)
                                    .build());
                        }
                    });

                    U.getFragmentUtils().addFragment(FragmentUtils.newAddParamsBuilder(getActivity(), AuditionFragment.class)
                            .setAddToBackStack(true)
                            .setHasAnimation(true)
                            .addDataBeforeAdd(0, mPrepareData)
                            .setFragmentDataListener(new FragmentDataListener() {
                                @Override
                                public void onFragmentResult(int requestCode, int resultCode, Bundle bundle, Object obj) {

                                }
                            })
                            .build());
                });
            }

            @Override
            public void onCanceled() {

            }

            @Override
            public void onFailed() {
                U.getToastUtil().showShort("下载资源失败，请退出重试");
                MyLog.w(getTAG(), "download song res failed");
            }
        }, mPrepareData.getSongModel());

        addPresent(mPrepareAuditionResPresenter);
        mPrepareAuditionResPresenter.prepareRes();
    }

    @Override
    public boolean onBackPressed() {
        onBack();
        return true;
    }

    private void onBack() {
        Activity activity = getActivity();
        if (activity != null) {
            activity.finish();
        }
    }

    @Override
    public void setData(int type, @Nullable Object data) {
        super.setData(type, data);
        if (type == 0) {
            mPrepareData.setSongModel((SongModel) data);
        }

        if (type == 1) {
            mPrepareData.setGameType((int) data);
        }
    }

    @Override
    public void destroy() {
        super.destroy();
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
    }

    @Override
    public boolean useEventBus() {
        return false;
    }
}
