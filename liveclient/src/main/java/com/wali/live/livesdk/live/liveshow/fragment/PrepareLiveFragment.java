package com.wali.live.livesdk.live.liveshow.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.base.dialog.MyAlertDialog;
import com.base.fragment.FragmentDataListener;
import com.base.fragment.utils.FragmentNaviUtils;
import com.base.global.GlobalData;
import com.base.keyboard.KeyboardUtils;
import com.base.log.MyLog;
import com.base.preference.PreferenceUtils;
import com.base.utils.language.LocaleUtil;
import com.wali.live.livesdk.R;
import com.wali.live.livesdk.live.api.RoomTagRequest;
import com.wali.live.livesdk.live.component.data.StreamerPresenter;
import com.wali.live.livesdk.live.fragment.BasePrepareLiveFragment;
import com.wali.live.livesdk.live.image.ClipImageActivity;
import com.wali.live.livesdk.live.manager.PrepareLiveCoverManager;
import com.wali.live.livesdk.live.view.BeautyView;
import com.wali.live.livesdk.live.view.SelectCoverView;
import com.wali.live.livesdk.live.viewmodel.RoomTag;
import com.wali.live.statistics.StatisticsKey;
import com.wali.live.statistics.StatisticsWorker;
import com.wali.live.watchsdk.auth.AccountAuthManager;
import com.wali.live.watchsdk.base.BaseComponentSdkActivity;

/**
 * Created by yangli on 2017/3/7.
 *
 * @module 秀场直播准备页
 */
public class PrepareLiveFragment extends BasePrepareLiveFragment {
    private static final String TAG = "PrepareShowLiveFragment";

    public static final int REQUEST_RECIPIENT_SELECT = 1000;
    final int mTopicLenMax = 28;
    private StreamerPresenter mStreamerPresenter;

    private MyAlertDialog.Builder builder;

    private ImageView mTurnOverIv;
    protected RelativeLayout mCoverArea;
    private RelativeLayout mAddTopicContainer;
    private BeautyView mBeautyView;
    private SelectCoverView mCoverView;

    private final void $click(View view, View.OnClickListener listener) {
        if (view != null) {
            view.setOnClickListener(listener);
        }
    }

    @Override
    public String getTAG() {
        return getClass().getSimpleName() + "#" + this.hashCode();
    }

    public void hideCloseBtn() {
        mCloseBtn.setVisibility(View.INVISIBLE);
    }

    @Override
    @LayoutRes
    protected int getLayoutResId() {
        return R.layout.prepare_live_layout;
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        int id = v.getId();
        if (id == R.id.turn_over) {
            StatisticsWorker.getsInstance().sendCommand(
                    StatisticsWorker.AC_APP, StatisticsKey.KEY_PRE_LIVE_CAMERA, 1);
            // TODO 切换前后置相机
            mStreamerPresenter.switchCamera();
        } else if (id == R.id.add_topic_container) {
            // TODO 跳转到添加话题页 暫時接的老版的話題方式
            mLiveTitleEt.requestFocus();
            String addStr = "##";
            String result = mLiveTitleEt.getText().toString() + addStr;
            if (result.length() > mTopicLenMax) {
                mTitleTextWatcher.formatInputString(mLiveTitleEt.getText().toString(), mLiveTitleEt.getSelectionStart());
            } else {
                mTitleTextWatcher.formatInputString(result, mLiveTitleEt.getText().length() + 1);
                KeyboardUtils.showKeyboard(getActivity(), mLiveTitleEt);
            }
        }
    }

    @Override
    protected void initContentView() {
        super.initContentView();
        mRootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KeyboardUtils.hideKeyboardImmediately(getActivity());
            }
        });
        mTurnOverIv = $(R.id.turn_over);
        mCoverArea = $(R.id.cover_layout);
        mAddTopicContainer = $(R.id.add_topic_container);
        mBeautyView = $(R.id.beauty_view);
        mBeautyView.setStreamerPresenter(mStreamerPresenter);
        mBeautyView.setBeautyCallBack(new BeautyView.BeautyCallBack() {
            @Override
            public void showMultiBeautyAnim() {
                //隐藏相机和地理位置
                mLocationTv.setVisibility(View.INVISIBLE);
                mTurnOverIv.setVisibility(View.INVISIBLE);
            }

            @Override
            public void hideMultiBeautyAnim() {
                //显示相机和地理位置
                mLocationTv.setVisibility(View.VISIBLE);
                mTurnOverIv.setVisibility(View.VISIBLE);
            }
        });
        mCoverView = $(R.id.cover_layout);
        mCoverView.setFragment(this);

        $click(mTurnOverIv, this);
        $click(mCoverArea, this);
        $click(mAddTopicContainer, this);
    }

    @Override
    protected void getTagFromServer() {
        mBeginBtn.setEnabled(true);
        mRoomTagPresenter.start(RoomTagRequest.TAG_TYPE_NORMAL);
    }

    protected void prepareTagFromServer() {
        mRoomTagPresenter.prepare(RoomTagRequest.TAG_TYPE_NORMAL);
    }

    @Override
    public boolean onBackPressed() {
        super.onBackPressed();
        getActivity().finish();
        return true;
    }

    @Override
    protected void adjustTitleEtPosByCover(boolean isTitleEtFocus, int coverState) {
        if (!isTitleEtFocus) {
            return;
        }
        // 应产品需求去掉margin
        // RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mLiveTitleEt.getLayoutParams();
        // params.rightMargin = params.leftMargin = DisplayUtils.dip2px(58f);
        // mLiveTitleEt.setLayoutParams(params);


//        mLiveTitleEt.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
//        mLiveTitleEt.setHint("");
    }

    @Override
    protected void initTagName() {
        if (!LocaleUtil.isChineseLocal()) {
            hideTag();
            return;
        }
        String jsonString = PreferenceUtils.getSettingString(GlobalData.app(), PreferenceUtils.PREF_KEY_LIVE_NORMAL_TAG, "");
        if (!TextUtils.isEmpty(jsonString)) {
            try {
                mRoomTag = new RoomTag(jsonString);
            } catch (Exception e) {
            }
        }
        if (mRoomTag != null) {
            mTagNameTv.setText(getString(R.string.quanzi_name, mRoomTag.getTagName()));
            mTagNameTv.setSelected(true);
            mBeginBtn.setEnabled(true);
        } else {
            mBeginBtn.setEnabled(false);
        }
        prepareTagFromServer();
    }

    @Override
    protected void updateTagName() {
        if (mRoomTag != null) {
            mTagNameTv.setText(getString(R.string.quanzi_name, mRoomTag.getTagName()));
            mTagNameTv.setSelected(true);
        }
    }

    @Override
    public void hideTag() {
        super.hideTag();
        mBeginBtn.setEnabled(true);
    }

    @Override
    protected void putCommonData(Bundle bundle) {
        super.putCommonData(bundle);
        bundle.putString(EXTRA_LIVE_COVER_URL, mCoverView.getCoverUrl());
    }

    @Override
    protected void openLive() {
        if (!AccountAuthManager.triggerActionNeedAccount(getActivity())) {
            return;
        }
        if (mRoomTag != null) {
            PreferenceUtils.setSettingString(GlobalData.app(), PreferenceUtils.PREF_KEY_LIVE_NORMAL_TAG, mRoomTag.toJsonString());
        }
        openPublicLive();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        MyLog.w(TAG, "onActivityResult requestCode : " + requestCode);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case PrepareLiveCoverManager.REQUEST_CODE_TAKE_PHOTO:
            case ClipImageActivity.REQUEST_CODE_CROP:
                if (mCoverView != null)
                    mCoverView.onActivityResult(requestCode, resultCode, data);
                break;
            default:
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mCoverView != null) {
            mCoverView.onDestroy();
        }
    }

    public void setStreamerPresenter(StreamerPresenter streamerPresenter) {
        mStreamerPresenter = streamerPresenter;
    }

    public static void openFragment(
            BaseComponentSdkActivity fragmentActivity,
            int requestCode,
            FragmentDataListener listener,
            StreamerPresenter streamerPresenter) {
        PrepareLiveFragment fragment = (PrepareLiveFragment) FragmentNaviUtils.addFragment(fragmentActivity, R.id.main_act_container,
                PrepareLiveFragment.class, null, true, false, true);
        fragment.setStreamerPresenter(streamerPresenter);
        if (listener != null) {
            fragment.initDataResult(requestCode, listener);
        }
    }
}
