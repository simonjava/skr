package com.module.home.game;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.common.base.BaseFragment;
import com.common.core.account.event.AccountEvent;
import com.common.core.avatar.AvatarUtils;

import com.common.core.myinfo.MyUserInfoManager;
import com.common.core.myinfo.event.MyUserInfoEvent;
import com.common.core.permission.SkrAudioPermission;
import com.common.floatwindow.FloatWindow;
import com.common.floatwindow.MoveType;
import com.common.floatwindow.Screen;
import com.common.floatwindow.ViewStateListenerAdapter;
import com.common.image.fresco.BaseImageView;
import com.common.log.MyLog;
import com.common.utils.HandlerTaskTimer;
import com.common.utils.U;
import com.common.view.AnimateClickListener;
import com.common.view.DebounceViewClickListener;
import com.common.view.ex.ExImageView;
import com.common.view.ex.ExTextView;
import com.common.view.titlebar.CommonTitleBar;
import com.component.busilib.friends.RecommendModel;
import com.component.busilib.friends.SpecialModel;
import com.component.busilib.view.BitmapTextView;
import com.facebook.drawee.view.SimpleDraweeView;
import com.module.RouterConstants;
import com.module.home.R;
import com.module.home.game.adapter.GameAdapter;
import com.module.home.game.model.BannerModel;
import com.module.home.game.model.QuickJoinRoomModel;
import com.module.home.game.model.RecommendRoomModel;
import com.module.home.model.GameKConfigModel;
import com.module.home.model.SlideShowModel;
import com.module.rank.IRankingModeService;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Iterator;
import java.util.List;

public class GameFragment2 extends BaseFragment implements IGameView {
    public final static String TAG = "Game2Fragment";

    RelativeLayout mBackground;
    SmartRefreshLayout mRefreshLayout;
    ClassicsHeader mClassicsHeader;
    CommonTitleBar mTitlebar;
    ExImageView mTaskIv;
    SimpleDraweeView mAvatarIv;
    ExTextView mNameTv;
    BitmapTextView mCoinNum;
    SmartRefreshLayout mRecyclerLayout;
    RecyclerView mRecyclerView;

    BaseImageView mIvRedPkg;

    GameAdapter mGameAdapter;

    GamePresenter mGamePresenter;

    SkrAudioPermission mSkrAudioPermission;

    int mRecommendInterval = 0;

    @Override
    public int initView() {
        return R.layout.game2_fragment_layout;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        mBackground = (RelativeLayout) mRootView.findViewById(R.id.background);
        mRefreshLayout = (SmartRefreshLayout) mRootView.findViewById(R.id.refreshLayout);
        mClassicsHeader = (ClassicsHeader) mRootView.findViewById(R.id.classics_header);
        mTitlebar = (CommonTitleBar) mRootView.findViewById(R.id.titlebar);
        mTaskIv = (ExImageView)mRootView.findViewById(R.id.task_iv);
        mAvatarIv = (SimpleDraweeView) mRootView.findViewById(R.id.avatar_iv);
        mNameTv = (ExTextView) mRootView.findViewById(R.id.name_tv);
        mCoinNum = (BitmapTextView) mRootView.findViewById(R.id.coin_num);
        mRecyclerLayout = (SmartRefreshLayout) mRootView.findViewById(R.id.recycler_layout);
        mRecyclerView = (RecyclerView) mRootView.findViewById(R.id.recycler_view);
        mIvRedPkg = (BaseImageView) mRootView.findViewById(R.id.iv_red_pkg);

        mSkrAudioPermission = new SkrAudioPermission();

        if (U.getDeviceUtils().hasNotch(getContext())) {
            mTitlebar.setVisibility(View.VISIBLE);
        } else {
            mTitlebar.setVisibility(View.GONE);
        }

        mRefreshLayout.setEnableRefresh(true);
        mRefreshLayout.setEnableLoadMore(false);
        mRefreshLayout.setEnableLoadMoreWhenContentNotFull(false);
        mRefreshLayout.setEnableOverScrollDrag(false);
        mRefreshLayout.setRefreshHeader(mClassicsHeader);
        mRefreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {

            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                refreshLayout.finishRefresh();
                mGamePresenter.initGameKConfig();
                mGamePresenter.initCoinNum(true);
                mGamePresenter.initOperationArea(true);
                mGamePresenter.initQuickRoom(true);
                mGamePresenter.initRecommendRoom(mRecommendInterval);

            }
        });

        mRecyclerLayout.setEnableRefresh(false);
        mRecyclerLayout.setEnableLoadMore(false);
        mRecyclerLayout.setEnableLoadMoreWhenContentNotFull(false);
        mRecyclerLayout.setEnableOverScrollDrag(true);

//        mCreateRoom.setOnClickListener(new AnimateClickListener() {
//            @Override
//            public void click(View view) {
//                IRankingModeService iRankingModeService = (IRankingModeService) ARouter.getInstance().build(RouterConstants.SERVICE_RANKINGMODE).navigation();
//                if (iRankingModeService != null) {
//                    iRankingModeService.tryGoCreateRoom();
//                }
//            }
//        });

        mTaskIv.setOnClickListener(new AnimateClickListener() {
            @Override
            public void click(View view) {
                U.getToastUtil().showShort("点击了做任务");
            }
        });

        mGameAdapter = new GameAdapter(this, new GameAdapter.GameAdapterListener() {
            @Override
            public void createRoom() {
                MyLog.d(TAG, "createRoom");
                IRankingModeService iRankingModeService = (IRankingModeService) ARouter.getInstance().build(RouterConstants.SERVICE_RANKINGMODE).navigation();
                if (iRankingModeService != null) {
                    iRankingModeService.tryGoCreateRoom();
                }
            }

            @Override
            public void selectSpecial(SpecialModel specialModel) {
                MyLog.d(TAG, "selectSpecial" + " specialModel=" + specialModel);
                // TODO: 2019/3/29 选择专场，进入快速匹配
                if (specialModel != null) {
                    mSkrAudioPermission.ensurePermission(new Runnable() {
                        @Override
                        public void run() {
                            IRankingModeService iRankingModeService = (IRankingModeService) ARouter.getInstance().build(RouterConstants.SERVICE_RANKINGMODE).navigation();
                            if (iRankingModeService != null) {
                                iRankingModeService.tryGoGrabMatch(specialModel.getTagID());
                            }
                        }
                    }, true);
                } else {

                }
            }

            @Override
            public void enterRoom(RecommendModel friendRoomModel) {
                MyLog.d(TAG, "enterRoom" + " friendRoomModel=" + friendRoomModel);
                if (friendRoomModel != null) {
                    mSkrAudioPermission.ensurePermission(new Runnable() {
                        @Override
                        public void run() {
                            IRankingModeService iRankingModeService = (IRankingModeService) ARouter.getInstance().build(RouterConstants.SERVICE_RANKINGMODE).navigation();
                            if (iRankingModeService != null) {
                                iRankingModeService.tryGoGrabRoom(friendRoomModel.getRoomInfo().getRoomID());
                            }
                        }
                    }, true);

                } else {

                }
            }

            @Override
            public void moreRoom() {
                MyLog.d(TAG, "moreRoom");
                ARouter.getInstance()
                        .build(RouterConstants.ACTIVITY_FRIEND_ROOM)
                        .navigation();
            }
        });
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(mGameAdapter);

        mGamePresenter = new GamePresenter(this);
        addPresent(mGamePresenter);
        initBaseInfo();

//        HandlerTaskTimer.newBuilder().interval(10000)
//                .start(new HandlerTaskTimer.ObserverW() {
//                    @Override
//                    public void onNext(Integer integer) {
//                        TextView textView = new TextView(getContext());
//                        textView.setText("阿斯顿健康垃圾堆里卡就睡了多久了 ");
//                        FloatWindow.with(U.app())
//                                .setView(textView)
//                                .setMoveType(MoveType.canRemove)
//                                .setWidth(Screen.width, 1f)                               //设置控件宽高
//                                .setHeight(Screen.height, 0.2f)
//                                .setDesktopShow(false)                        //桌面显示
//                                .setCancelIfExist(false)
//                                .setReqPermissionIfNeed(false)
//                                .build();
//                    }
//                });
    }

    private void initBaseInfo() {
        AvatarUtils.loadAvatarByUrl(mAvatarIv,
                AvatarUtils.newParamsBuilder(MyUserInfoManager.getInstance().getAvatar())
                        .setCircle(true)
                        .build());
        mNameTv.setText(MyUserInfoManager.getInstance().getNickName());
    }

    @Override
    public void onResume() {
        super.onResume();
        mSkrAudioPermission.onBackFromPermisionManagerMaybe();
    }

    @Override
    public boolean useEventBus() {
        return true;
    }


    @Override
    protected void onFragmentVisible() {
        super.onFragmentVisible();
        mGamePresenter.initOperationArea(false);
        mGamePresenter.initQuickRoom(false);
        mGamePresenter.initRecommendRoom(mRecommendInterval);
        mGamePresenter.initGameKConfig();
        mGamePresenter.initCoinNum(false);
    }

    @Override
    public boolean isInViewPager() {
        return true;
    }

    @Override
    protected void onFragmentInvisible() {
        super.onFragmentInvisible();
        mGamePresenter.stopTimer();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(MyUserInfoEvent.UserInfoChangeEvent userInfoChangeEvent) {
        initBaseInfo();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(AccountEvent.SetAccountEvent event) {
        mGamePresenter.initOperationArea(true);
        mGamePresenter.initQuickRoom(true);
        mGamePresenter.initRecommendRoom(mRecommendInterval);
        mGamePresenter.initGameKConfig();
        mGamePresenter.initCoinNum(true);
    }

    // TODO: 2019/4/3 这都是第一次拉
    @Override
    public void setQuickRoom(List<SpecialModel> list, int offset) {
        MyLog.d(TAG, "setQuickRoom" + " list=" + list + " offset=" + offset);
        // TODO: 2019/4/1 过滤一下空的背景
        if (list != null && list.size() > 0) {
            Iterator<SpecialModel> iterator = list.iterator();
            while (iterator.hasNext()) {
                SpecialModel specialModel = iterator.next();
                if (specialModel != null) {
                    if (TextUtils.isEmpty(specialModel.getBgImage2()) || TextUtils.isEmpty(specialModel.getBgImage1())) {
                        iterator.remove();
                    }
                }
            }
        }

        if (list == null || list.size() == 0) {
            // 快速加入专场空了，清空数据
            mGameAdapter.updateQuickJoinRoomInfo(null);
            return;
        }

        QuickJoinRoomModel quickJoinRoomModel = new QuickJoinRoomModel(list, offset);
        mGameAdapter.updateQuickJoinRoomInfo(quickJoinRoomModel);
    }

    @Override
    public void setGameConfig(GameKConfigModel gameKConfigModel) {
        mRecommendInterval = gameKConfigModel.getHomepagetickerinterval();
        mGamePresenter.initRecommendRoom(mRecommendInterval);
    }

    @Override
    public void setGrabCoinNum(int coinNum) {
        mCoinNum.setText("" + coinNum);
    }

    @Override
    public void showRedOperationView(GameKConfigModel.HomepagesitefirstBean homepagesitefirstBean) {
        AvatarUtils.loadAvatarByUrl(mIvRedPkg,
                AvatarUtils.newParamsBuilder(homepagesitefirstBean.getPic())
                        .setWidth(U.getDisplayUtils().dip2px(48f))
                        .setHeight(U.getDisplayUtils().dip2px(53f))
                        .build());
        mIvRedPkg.setVisibility(View.VISIBLE);
        mIvRedPkg.setOnClickListener(new DebounceViewClickListener() {
            @Override
            public void clickValid(View v) {
                ARouter.getInstance().build(RouterConstants.ACTIVITY_SCHEME)
                        .withString("uri", homepagesitefirstBean.getSchema())
                        .navigation();
            }
        });
    }

    @Override
    public void hideRedOperationView() {
        mIvRedPkg.setVisibility(View.GONE);
    }

    // TODO: 2019/4/3 这都是第一次拉数据
    @Override
    public void setRecommendInfo(List<RecommendModel> list, int offset, int totalNum) {
        if (list == null || list.size() == 0) {
            // 清空好友派对列表
            mGameAdapter.updateRecommendRoomInfo(null);
            return;
        }
        RecommendRoomModel recommendRoomModel = new RecommendRoomModel(list, offset, totalNum);
        mGameAdapter.updateRecommendRoomInfo(recommendRoomModel);
    }

    @Override
    public void setBannerImage(List<SlideShowModel> slideShowModelList) {
        if (slideShowModelList == null || slideShowModelList.size() == 0) {
            MyLog.w(TAG, "initOperationArea 为null");
            mGameAdapter.updateBanner(null);
            return;
        }
        BannerModel bannerModel = new BannerModel(slideShowModelList);
        mGameAdapter.updateBanner(bannerModel);
    }
}
