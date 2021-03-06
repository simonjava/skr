package com.module.playways.audition;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.common.base.BaseActivity;
import com.common.utils.FragmentUtils;
import com.common.utils.U;
import com.module.RouterConstants;
import com.module.playways.room.song.fragment.SongSelectFragment;
import com.module.playways.R;
import com.module.playways.songmanager.SongManagerActivity;

/**
 * 调音间
 */
@Route(path = RouterConstants.ACTIVITY_AUDIOROOM)
public class AudioRoomActivity extends BaseActivity {

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.audio_room_activity_layout;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        boolean selectSong = getIntent().getBooleanExtra("selectSong", false);
        if (selectSong) {
            U.getFragmentUtils().addFragment(FragmentUtils.newAddParamsBuilder(this, SongSelectFragment.class)
                    .addDataBeforeAdd(0, SongManagerActivity.TYPE_FROM_AUDITION)
                    .setAddToBackStack(false)
                    .setHasAnimation(false)
                    .build());
        }
    }

    @Override
    public boolean canSlide() {
        return false;
    }

    @Override
    public boolean useEventBus() {
        return false;
    }

    @Override
    public boolean resizeLayoutSelfWhenKeyboardShow() {
        // 自己处理有键盘时的整体布局
        return true;
    }
}
