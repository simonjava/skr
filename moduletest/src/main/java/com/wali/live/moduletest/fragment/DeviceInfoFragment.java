package com.wali.live.moduletest.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.common.base.BaseFragment;
import com.common.base.BuildConfig;
import com.common.log.MyLog;
import com.common.statistics.TimeStatistics;
import com.common.utils.U;
import com.wali.live.moduletest.R;

import io.agora.rtc.RtcEngine;

public class DeviceInfoFragment extends BaseFragment {


    TextView mDescTv;


    @Override
    public int initView() {
        return R.layout.test_fragment_info_layout;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        mDescTv = (TextView) getRootView().findViewById(R.id.desc_tv);
        StringBuilder sb = new StringBuilder();
        sb.append("数据库调试地址:").append(U.getAppInfoUtils().getDebugDBAddressLog()).append("\n");
        sb.append("屏宽:").append(U.getDisplayUtils().getScreenWidth())
                .append(" 屏高:").append(U.getDisplayUtils().getScreenHeight())
                .append(" 手机高:").append(U.getDisplayUtils().getPhoneHeight())
                .append(" density:").append(U.getDisplayUtils().getDensity())
                .append(" densityDpi:").append(U.getDisplayUtils().getDensityDpi())
                .append("\n");
        sb.append("是否开启了虚拟导航键：").append(U.getDeviceUtils().hasNavigationBar()).append(" 虚拟导航键高度:")
                .append(U.getDeviceUtils().getVirtualNavBarHeight())
                .append("\n");
        sb.append("最小宽度为 px/(dpi/160)=").append((U.getDisplayUtils().getPhoneWidth() / (U.getDisplayUtils().getDensityDpi() / 160))).append("dp").append("\n");
        sb.append("当前手机适用的资源文件夹是").append(U.app().getResources().getString(R.string.values_from)).append("\n");
        sb.append("android.os.Build.VERSION.SDK_INT:").append(android.os.Build.VERSION.SDK_INT).append("\n");
        sb.append("手机型号:").append(U.getDeviceUtils().getProductModel()).append("\n");
        sb.append("手机厂商:").append(U.getDeviceUtils().getProductBrand()).append("\n");
        sb.append("渠道号:").append(U.getChannelUtils().getChannel()).append(" debug:").append(BuildConfig.DEBUG).append("\n");
        sb.append("deviceId(参考miui唯一设备号的方法):").append(U.getDeviceUtils().getDeviceID()).append("\n");
        sb.append("是否开启了打印方法执行时间: switch:").append(TimeStatistics.getSwitch()).append(" dt:").append(TimeStatistics.sDt).append("\n");
        sb.append("agora sdk version:").append(RtcEngine.getSdkVersion()).append("\n");
        sb.append("是否插着有线耳机:").append(U.getDeviceUtils().getWiredHeadsetPlugOn()).append("\n");
        sb.append("是否插着蓝牙耳机:").append(U.getDeviceUtils().getBlueToothHeadsetOn()).append("\n");
        MyLog.w(getTAG(), "deviceId:" + U.getDeviceUtils().getDeviceID());
        mDescTv.setText(sb.toString());
    }

    @Override
    public boolean useEventBus() {
        return false;
    }
}
