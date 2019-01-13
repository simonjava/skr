package com.module.home.fragment;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.fastjson.JSON;
import com.common.base.BaseFragment;
import com.common.core.myinfo.MyUserInfoManager;
import com.common.core.myinfo.event.MyUserInfoEvent;
import com.common.core.userinfo.UserInfoServerApi;
import com.common.core.userinfo.model.UserLevelModel;
import com.common.rxretrofit.ApiManager;
import com.common.rxretrofit.ApiMethods;
import com.common.rxretrofit.ApiObserver;
import com.common.rxretrofit.ApiResult;
import com.common.utils.U;
import com.common.view.ex.ExImageView;
import com.component.busilib.constans.GameModeType;
import com.jakewharton.rxbinding2.view.RxView;
import com.module.RouterConstants;
import com.module.home.R;
import com.module.home.widget.UserInfoTitleView;
import com.zq.level.view.NormalLevelView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.functions.Consumer;

public class GameFragment extends BaseFragment {

    public final static String TAG = "GameFragment";

    UserInfoTitleView userTitleView;
    NormalLevelView mLevelView;

    int rank = 0;           //当前父段位
    int subRank = 0;        //当前子段位
    int starNum = 0;        //当前星星
    int starLimit = 0;      //当前星星上限

    @Override
    public int initView() {
        return R.layout.game_fragment_layout;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        ExImageView ivAthleticsPk = (ExImageView) mRootView.findViewById(R.id.iv_athletics_pk);
        ExImageView ivNormalPk = (ExImageView) mRootView.findViewById(R.id.iv_yule_game);
        ExImageView mIvYulePk = (ExImageView) mRootView.findViewById(R.id.iv_singend_game);
        userTitleView = (UserInfoTitleView) mRootView.findViewById(R.id.user_title_view);
        mLevelView = (NormalLevelView) mRootView.findViewById(R.id.level_view);

        initLevel();

        RxView.clicks(ivAthleticsPk)
                .throttleFirst(500, TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) {
                        clickAnimation(ivAthleticsPk);
                    }
                });

        RxView.clicks(ivNormalPk)
                .throttleFirst(500, TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) {
                        clickAnimation(ivNormalPk);
                    }
                });

        RxView.clicks(mIvYulePk)
                .throttleFirst(500, TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) {
                        clickAnimation(mIvYulePk);
                    }
                });

        U.getSoundUtils().preLoad(TAG, R.raw.home_game);
    }

    private void initLevel() {
        UserInfoServerApi mUserInfoServerApi = ApiManager.getInstance().createService(UserInfoServerApi.class);
        ApiMethods.subscribe(mUserInfoServerApi.getScoreDetail((int) MyUserInfoManager.getInstance().getUid()), new ApiObserver<ApiResult>() {
            @Override
            public void process(ApiResult result) {
                if (result.getErrno() == 0) {
                    List<UserLevelModel> userLevelModels = JSON.parseArray(result.getData().getString("userScore"), UserLevelModel.class);
                    // 展示段位信息
                    for (UserLevelModel userLevelModel : userLevelModels) {
                        if (userLevelModel.getType() == UserLevelModel.RANKING_TYPE) {
                            rank = userLevelModel.getScore();
                        } else if (userLevelModel.getType() == UserLevelModel.SUB_RANKING_TYPE) {
                            subRank = userLevelModel.getScore();
                        } else if (userLevelModel.getType() == UserLevelModel.TOTAL_RANKING_STAR_TYPE) {
                            starNum = userLevelModel.getScore();
                        } else if (userLevelModel.getType() == UserLevelModel.REAL_RANKING_STAR_TYPE) {
                            starLimit = userLevelModel.getScore();
                        }
                    }
                    mLevelView.bindData(rank, subRank, starLimit, starNum, U.getDisplayUtils().dip2px(100));
                }
            }
        });
    }

    // 点击缩放动画
    public void clickAnimation(View view) {
        U.getSoundUtils().play(TAG, R.raw.home_game);

        ObjectAnimator a1 = ObjectAnimator.ofFloat(view, "scaleX", 1f, 0.95f);
        ObjectAnimator a2 = ObjectAnimator.ofFloat(view, "scaleY", 1f, 0.95f);
        ObjectAnimator a3 = ObjectAnimator.ofFloat(view, "scaleX", 0.95f, 1f);
        ObjectAnimator a4 = ObjectAnimator.ofFloat(view, "scaleY", 0.95f, 1f);

        AnimatorSet set = new AnimatorSet();
        set.setDuration(80);
        set.play(a1).with(a2);
        set.play(a3).with(a4).after(a1);
        set.start();

        set.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                if (view.getId() == R.id.iv_athletics_pk) {
                    ARouter.getInstance().build(RouterConstants.ACTIVITY_RANKINGMODE)
                            .withInt("key_game_type", GameModeType.GAME_MODE_CLASSIC_RANK)
                            .withBoolean("selectSong", true)
                            .navigation();
                } else if (view.getId() == R.id.iv_yule_game) {
                    ARouter.getInstance().build(RouterConstants.ACTIVITY_RANKINGMODE)
                            .withInt("key_game_type", GameModeType.GAME_MODE_FUNNY)
                            .withBoolean("selectSong", true)
                            .navigation();
                } else if (view.getId() == R.id.iv_singend_game) {
                    ARouter.getInstance().build(RouterConstants.ACTIVITY_SINGEND_ROOM)
                            .navigation();
                }
            }

            @Override
            public void onAnimationCancel(Animator animator) {
                onAnimationEnd(animator);
            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
    }

    @Override
    public void destroy() {
        super.destroy();
        U.getSoundUtils().release(TAG);
        userTitleView.destroy();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvnet(MyUserInfoEvent.UserInfoChangeEvent userInfoChangeEvent) {
        userTitleView.setData();
    }

    @Override
    public boolean useEventBus() {
        return true;
    }
}
