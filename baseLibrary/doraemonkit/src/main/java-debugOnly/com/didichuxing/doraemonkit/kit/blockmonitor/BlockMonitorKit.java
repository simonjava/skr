package com.didichuxing.doraemonkit.kit.blockmonitor;

import android.content.Context;
import android.content.Intent;

import com.common.log.MyLog;
import com.didichuxing.doraemonkit.R;
import com.didichuxing.doraemonkit.constant.BundleKey;
import com.didichuxing.doraemonkit.constant.FragmentIndex;
import com.didichuxing.doraemonkit.kit.Category;
import com.didichuxing.doraemonkit.kit.IKit;
import com.didichuxing.doraemonkit.kit.blockmonitor.core.BlockMonitorManager;
import com.didichuxing.doraemonkit.ui.UniversalActivity;


/**
 * @desc: 卡顿检测kit
 */
public class BlockMonitorKit implements IKit {


    @Override
    public int getCategory() {
        return Category.PERFORMANCE;
    }

    @Override
    public int getName() {
        return R.string.dk_kit_block_monitor;
    }

    @Override
    public int getIcon() {
        return R.drawable.dk_block_monitor;
    }


    @Override
    public void onClick(Context context) {
        Intent intent = new Intent(context, UniversalActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(BundleKey.FRAGMENT_INDEX, FragmentIndex.FRAGMENT_BLOCK_MONITOR);
        context.startActivity(intent);
    }

    @Override
    public void onAppInit(Context context) {
//        if(MyLog.isDebugLogOpen()) {
//            BlockMonitorManager.getInstance().start(context);
//        }
    }
}
