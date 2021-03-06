package com.didichuxing.doraemonkit.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.didichuxing.doraemonkit.constant.BundleKey;
import com.didichuxing.doraemonkit.constant.FragmentIndex;
import com.didichuxing.doraemonkit.kit.crash.CrashCaptureMainFragment;
import com.didichuxing.doraemonkit.kit.fileexplorer.FileExplorerFragment;
import com.didichuxing.doraemonkit.kit.logInfo.LogInfoSettingFragment;
import com.didichuxing.doraemonkit.kit.sysinfo.SysInfoFragment;
import com.didichuxing.doraemonkit.kit.webdoor.WebDoorFragment;
import com.didichuxing.doraemonkit.ui.base.BaseActivity;
import com.didichuxing.doraemonkit.ui.base.BaseFragment;

/**
 * Created by wanglikun on 2018/10/26.
 */

public class UniversalActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
            finish();
            return;
        }
        int index = bundle.getInt(BundleKey.FRAGMENT_INDEX);
        if (index == 0) {
            finish();
            return;
        }
        Class<? extends BaseFragment> fragmentClass = null;
        switch (index) {
            case FragmentIndex.FRAGMENT_SYS_INFO: {
                fragmentClass = SysInfoFragment.class;
            }
            break;
            case FragmentIndex.FRAGMENT_FILE_EXPLORER: {
                fragmentClass = FileExplorerFragment.class;
            }
            break;
            case FragmentIndex.FRAGMENT_LOG_INFO_SETTING: {
                fragmentClass = LogInfoSettingFragment.class;
            }
            break;
            case FragmentIndex.FRAGMENT_WEB_DOOR: {
                fragmentClass = WebDoorFragment.class;
            }
            break;
            case FragmentIndex.FRAGMENT_CRASH:
                fragmentClass = CrashCaptureMainFragment.class;
                break;
            default:
                break;
        }
        if (fragmentClass == null) {
            finish();
            Toast.makeText(this, String.format("fragment index %s not found", index), Toast.LENGTH_SHORT).show();
            return;
        }
        showContent(fragmentClass, bundle);
    }
}
