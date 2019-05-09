package com.module.home.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.fastjson.JSON;
import com.common.base.BaseFragment;
import com.common.core.myinfo.MyUserInfoManager;
import com.common.image.fresco.FrescoWorker;
import com.common.image.model.ImageFactory;
import com.common.rxretrofit.ApiManager;
import com.common.rxretrofit.ApiMethods;
import com.common.rxretrofit.ApiObserver;
import com.common.rxretrofit.ApiResult;
import com.common.utils.U;
import com.common.view.AnimateClickListener;
import com.component.busilib.friends.GrabSongApi;
import com.component.busilib.friends.SpecialModel;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.module.RouterConstants;
import com.module.home.R;
import com.module.playways.IPlaywaysModeService;

import java.util.List;

/**
 * 新手引导的首页
 */
public class GrabGuideHomePageFragment extends BaseFragment {

    SimpleDraweeView mSpecial1;
    SimpleDraweeView mSpecial2;
    SimpleDraweeView mSpecial3;
    SimpleDraweeView mSpecial4;
    ImageView mSkipIv;

    @Override
    public int initView() {
        return R.layout.grab_guide_home_page_layout;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {

        mSpecial1 = (SimpleDraweeView) mRootView.findViewById(R.id.special_1);
        mSpecial2 = (SimpleDraweeView) mRootView.findViewById(R.id.special_2);
        mSpecial3 = (SimpleDraweeView) mRootView.findViewById(R.id.special_3);
        mSpecial4 = (SimpleDraweeView) mRootView.findViewById(R.id.special_4);
        mSkipIv = (ImageView) mRootView.findViewById(R.id.skip_iv);

        GrabSongApi mGrabSongApi = ApiManager.getInstance().createService(GrabSongApi.class);
        ApiMethods.subscribe(mGrabSongApi.getSepcialList(0, 4), new ApiObserver<ApiResult>() {
            @Override
            public void process(ApiResult obj) {
                if (obj.getErrno() == 0) {
                    List<SpecialModel> list = JSON.parseArray(obj.getData().getString("tags"), SpecialModel.class);
                    showGuideSpecial(list);
                }
            }
        }, this);

        mSpecial1.setOnClickListener(new AnimateClickListener() {
            @Override
            public void click(View view) {
                goGuide();
            }
        });

        mSpecial2.setOnClickListener(new AnimateClickListener() {
            @Override
            public void click(View view) {
                goGuide();
            }
        });

        mSpecial3.setOnClickListener(new AnimateClickListener() {
            @Override
            public void click(View view) {
                goGuide();
            }
        });

        mSpecial4.setOnClickListener(new AnimateClickListener() {
            @Override
            public void click(View view) {
                goGuide();
            }
        });

        mSkipIv.setOnClickListener(new AnimateClickListener() {
            @Override
            public void click(View view) {
                // TODO: 2019/5/9  缺一个重新去拉一下数据
                MyUserInfoManager.getInstance().setNeedBeginnerGuide(false);
                U.getFragmentUtils().popFragment(GrabGuideHomePageFragment.this);
            }
        });
    }

    private void showGuideSpecial(List<SpecialModel> list) {
        if (list == null && list.size() == 0) {
            return;
        }
        if (list.size() > 0 && list.get(0) != null) {
            FrescoWorker.loadImage(mSpecial1, ImageFactory.newPathImage(list.get(0).getBgImage1())
                    .setLoadingDrawable(U.getDrawable(R.drawable.grab_img_btn_loading1))
                    .setLoadingScaleType(ScalingUtils.ScaleType.FIT_XY)
                    .setScaleType(ScalingUtils.ScaleType.FIT_XY)
                    .build());
        }

        if (list.size() > 1 && list.get(1) != null) {
            FrescoWorker.loadImage(mSpecial2, ImageFactory.newPathImage(list.get(1).getBgImage1())
                    .setLoadingDrawable(U.getDrawable(R.drawable.grab_img_btn_loading1))
                    .setLoadingScaleType(ScalingUtils.ScaleType.FIT_XY)
                    .setScaleType(ScalingUtils.ScaleType.FIT_XY)
                    .build());
        }

        if (list.size() > 2 && list.get(2) != null) {
            FrescoWorker.loadImage(mSpecial3, ImageFactory.newPathImage(list.get(2).getBgImage1())
                    .setLoadingDrawable(U.getDrawable(R.drawable.grab_img_btn_loading1))
                    .setLoadingScaleType(ScalingUtils.ScaleType.FIT_XY)
                    .setScaleType(ScalingUtils.ScaleType.FIT_XY)
                    .build());
        }

        if (list.size() > 3 && list.get(3) != null) {
            FrescoWorker.loadImage(mSpecial4, ImageFactory.newPathImage(list.get(3).getBgImage1())
                    .setLoadingDrawable(U.getDrawable(R.drawable.grab_img_btn_loading1))
                    .setLoadingScaleType(ScalingUtils.ScaleType.FIT_XY)
                    .setScaleType(ScalingUtils.ScaleType.FIT_XY)
                    .build());
        }
    }

    // 开始新手引导
    private void goGuide() {
        IPlaywaysModeService iRankingModeService = (IPlaywaysModeService) ARouter.getInstance().build(RouterConstants.SERVICE_RANKINGMODE).navigation();
        if (iRankingModeService != null) {
            iRankingModeService.tryGoGrabGuide();
        }

        MyUserInfoManager.getInstance().setNeedBeginnerGuide(false);
        U.getFragmentUtils().popFragment(GrabGuideHomePageFragment.this);
    }


    @Override
    public boolean useEventBus() {
        return false;
    }
}
