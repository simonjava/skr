package com.module.playways.room.room.fragment;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.common.base.BaseFragment;
import com.common.core.avatar.AvatarUtils;
import com.common.core.myinfo.Location;
import com.common.core.myinfo.MyUserInfoManager;
import com.common.core.permission.SkrLocationPermission;
import com.common.core.userinfo.UserInfoManager;
import com.common.log.MyLog;
import com.common.utils.LbsUtils;
import com.component.level.utils.LevelConfigUtils;
import com.module.playways.room.room.model.RankInfoModel;
import com.component.person.model.UserRankModel;
import com.common.statistics.StatisticsAdapter;
import com.common.utils.NetworkUtils;
import com.common.utils.U;
import com.common.view.DebounceViewClickListener;
import com.common.view.ex.ExImageView;
import com.common.view.ex.ExTextView;
import com.facebook.drawee.view.SimpleDraweeView;
import com.module.RouterConstants;
import com.module.playways.R;
import com.module.playways.room.room.adapter.LeaderBoardAdapter;
import com.module.playways.room.room.presenter.LeaderboardPresenter;
import com.module.playways.room.room.view.ILeaderBoardView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.zq.live.proto.Common.ESex;
import com.component.person.utils.StringFromatUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

/**
 * 排行榜
 */
public class LeaderboardFragment extends BaseFragment implements ILeaderBoardView {

    public final String TAG = "LeaderboardFragment";

    RecyclerView mRecyclerView;

    LeaderBoardAdapter mLeaderBoardAdapter;

    LeaderboardPresenter mLeaderboardPresenter;

    SimpleDraweeView mSdvRightChampainIcon;
    ExTextView mTvRightChanpainName;
    ImageView mRightChanpainLevelView;
    ExTextView mRightChanpainLevelTv;

    SimpleDraweeView mSdvLeftChampainIcon;
    ExTextView mTvLeftChanpainName;
    ImageView mLeftChanpainLevelView;
    ExTextView mLeftChanpainLevelTv;

    SimpleDraweeView mSdvChampainIcon;
    ExTextView mTvChanpainName;
    ImageView mChanpainLevelView;
    ExTextView mChanpainLevelTv;

    TextView mTvArea;
    TextView mRefreshLocation;
    ExImageView mIvBack;

    SmartRefreshLayout mRefreshLayout;
    boolean mHasMore = true;

    ImageView mIvRankLeft;
    ImageView mIvRank;
    ImageView mIvRankRight;

    View mOwnInfoItem;

    SkrLocationPermission mSkrLocationPermission = new SkrLocationPermission();

    PopupWindow mPopupWindow;
    LinearLayout mLlAreaContainer;
    ExTextView mTvOtherArea;

    int mRankMode = UserRankModel.COUNTRY;

    @Override
    public int initView() {
        return R.layout.leader_board_fragment_layout;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        mRecyclerView = (RecyclerView) getRootView().findViewById(R.id.recycler_view);
        mLeaderBoardAdapter = new LeaderBoardAdapter();
        mLeaderboardPresenter = new LeaderboardPresenter(this);
        addPresent(mLeaderboardPresenter);
        mRecyclerView.setAdapter(mLeaderBoardAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mOwnInfoItem = getRootView().findViewById(R.id.own_info_item);
        mOwnInfoItem.setBackgroundColor(Color.parseColor("#6868A1"));

        mSdvRightChampainIcon = getRootView().findViewById(R.id.sdv_right_champain_icon);
        mTvRightChanpainName = getRootView().findViewById(R.id.tv_right_chanpain_name);
        mRightChanpainLevelView = getRootView().findViewById(R.id.right_chanpain_level_view);
        mRightChanpainLevelTv = getRootView().findViewById(R.id.right_chanpain_level_tv);

        mSdvLeftChampainIcon = getRootView().findViewById(R.id.sdv_left_champain_icon);
        mTvLeftChanpainName = getRootView().findViewById(R.id.tv_left_chanpain_name);
        mLeftChanpainLevelView = getRootView().findViewById(R.id.left_chanpain_level_view);
        mLeftChanpainLevelTv = getRootView().findViewById(R.id.left_chanpain_level_tv);

        mSdvChampainIcon = getRootView().findViewById(R.id.sdv_champain_icon);
        mTvChanpainName = getRootView().findViewById(R.id.tv_chanpain_name);
        mChanpainLevelView = getRootView().findViewById(R.id.chanpain_level_view);
        mChanpainLevelTv = getRootView().findViewById(R.id.chanpain_level_tv);

        mIvRankLeft = getRootView().findViewById(R.id.iv_rank_left);
        mIvRank = getRootView().findViewById(R.id.iv_rank);
        mIvRankRight = getRootView().findViewById(R.id.iv_rank_right);
        mRefreshLayout = getRootView().findViewById(R.id.refreshLayout);
        mTvArea = getRootView().findViewById(R.id.tv_area);
        mRefreshLocation = getRootView().findViewById(R.id.refresh_location);
        mIvBack = getRootView().findViewById(R.id.iv_back);

        mLlAreaContainer = (LinearLayout) getActivity().getLayoutInflater().inflate(R.layout.area_select_popup_window_layout, null);
        mTvOtherArea = (ExTextView) mLlAreaContainer.findViewById(R.id.tv_other_area);
        mPopupWindow = new PopupWindow(mLlAreaContainer);
        mPopupWindow.setOutsideTouchable(true);

        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                Drawable drawable = U.getDrawable(R.drawable.triangle_up_icon);
                drawable.setBounds(0, 0, U.getDisplayUtils().dip2px(11), U.getDisplayUtils().dip2px(8));
                mTvArea.setCompoundDrawables(null, null, drawable, null);
            }
        });

        U.getSoundUtils().preLoad(TAG, R.raw.normal_back);

        mRefreshLayout.setEnableRefresh(false);
        mRefreshLayout.setEnableLoadMore(true);
        mRefreshLayout.setEnableLoadMoreWhenContentNotFull(true);
        mRefreshLayout.setEnableOverScrollDrag(false);
        mRefreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                if (mHasMore) {
                    mLeaderboardPresenter.getLeaderBoardInfo(mRankMode);
                } else {
                    U.getToastUtil().showShort("没有更多数据了");
                }
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mRefreshLayout.finishRefresh();
            }
        });

        mOwnInfoItem.setOnClickListener(new DebounceViewClickListener() {
            @Override
            public void clickValid(View v) {
                gotoPersonFragment((int) MyUserInfoManager.INSTANCE.getUid());
            }
        });

        mIvBack.setOnClickListener(new DebounceViewClickListener() {
            @Override
            public void clickValid(View v) {
//                U.getSoundUtils().play(LeaderboardFragment.TAG, R.raw.normal_back, 500);
                finish();
            }
        });

        mTvArea.setOnClickListener(new DebounceViewClickListener() {
            @Override
            public void clickValid(View v) {
                showPopWindow();
            }
        });

        mTvOtherArea.setOnClickListener(new DebounceViewClickListener() {
            @Override
            public void clickValid(View v) {
                mPopupWindow.dismiss();
                changeRank();
            }
        });

        mRefreshLocation.setOnClickListener(new DebounceViewClickListener() {
            @Override
            public void clickValid(View v) {
                refreshLocation();
            }
        });

        if (MyUserInfoManager.INSTANCE.hasRealLocation()) {
            // 有地理位置，显示地理榜单
            mRankMode = UserRankModel.REGION;
            mRefreshLocation.setVisibility(View.VISIBLE);
            mTvArea.setText(getAreaFromLocation(MyUserInfoManager.INSTANCE.getRealLocation()));
            mLeaderboardPresenter.reset();
            refreshData();
        } else {
            // 无地理位置，显示全国榜单
            mRankMode = UserRankModel.COUNTRY;
            mRefreshLocation.setVisibility(View.GONE);
            mTvArea.setText("全国榜");
            mLeaderboardPresenter.reset();
            refreshData();
        }
        StatisticsAdapter.recordCountEvent("rank", "ranklist", null);
    }

    private void refreshData() {
        mLeaderboardPresenter.getOwnInfo(mRankMode);
        mLeaderboardPresenter.getLeaderBoardInfo(mRankMode);
    }

    private void changeRank() {
        // 切换榜单
        if (mRankMode == UserRankModel.COUNTRY) {
            // 切成地域榜
            if (MyUserInfoManager.INSTANCE.hasRealLocation()) {
                mRankMode = UserRankModel.REGION;
                mRefreshLocation.setVisibility(View.VISIBLE);
                mTvArea.setText(getAreaFromLocation(MyUserInfoManager.INSTANCE.getRealLocation()));
                mLeaderboardPresenter.reset();
                refreshData();
            } else {
                refreshLocation();
            }
        } else if (mRankMode == UserRankModel.REGION) {
            // 切成全国榜
            mRankMode = UserRankModel.COUNTRY;
            mRefreshLocation.setVisibility(View.GONE);
            mTvArea.setText("全国榜");
            mLeaderboardPresenter.reset();
            refreshData();
        }
    }

    private void refreshLocation() {
        mSkrLocationPermission.ensurePermission(new Runnable() {
            @Override
            public void run() {
                U.getLbsUtils().getLocation(false, new LbsUtils.Callback() {
                    @Override
                    public void onReceive(LbsUtils.Location location) {
                        MyLog.d(TAG, "onReceive" + " location=" + location);
                        if (location != null && location.isValid()) {
                            Location l = new Location();
                            l.setProvince(location.getProvince());
                            l.setCity(location.getCity());
                            l.setDistrict(location.getDistrict());

                            if (MyUserInfoManager.INSTANCE.hasRealLocation()
                                    && l.getProvince().equals(MyUserInfoManager.INSTANCE.getRealLocation().getProvince())
                                    && l.getCity().equals(MyUserInfoManager.INSTANCE.getRealLocation().getCity())
                                    && l.getDistrict().equals(MyUserInfoManager.INSTANCE.getRealLocation().getDistrict())) {
                                U.getToastUtil().showShort("定位位置与当前位置一致");
                            } else {
                                MyUserInfoManager.INSTANCE.updateInfo(MyUserInfoManager.INSTANCE
                                        .newMyInfoUpdateParamsBuilder()
                                        .setRealLocation(l)
                                        .build(), true, false, new MyUserInfoManager.ServerCallback() {
                                    @Override
                                    public void onSucess() {
                                        U.getToastUtil().showShort("位置更新成功");
                                        mRankMode = UserRankModel.REGION;
                                        mRefreshLocation.setVisibility(View.VISIBLE);
                                        mTvArea.setText(getAreaFromLocation(MyUserInfoManager.INSTANCE.getRealLocation()));
                                        mLeaderboardPresenter.reset();
                                        refreshData();
                                    }

                                    @Override
                                    public void onFail() {
                                        U.getToastUtil().showShort("位置更新失败");
                                    }
                                });
                            }
                        }
                    }
                });
            }
        }, true);
    }

    private void showPopWindow() {
        if (mPopupWindow.isShowing()) {
            mPopupWindow.dismiss();
        } else {
            mPopupWindow.setWidth(mTvArea.getMeasuredWidth());
            mPopupWindow.setHeight(U.getDisplayUtils().dip2px(32));
            Drawable drawable = U.getDrawable(R.drawable.triangle_down_icon);
            drawable.setBounds(0, 0, U.getDisplayUtils().dip2px(11), U.getDisplayUtils().dip2px(8));
            mTvArea.setCompoundDrawables(null, null, drawable, null);
            mPopupWindow.showAsDropDown(mTvArea);
        }
        if (mRankMode == UserRankModel.COUNTRY) {
            if (MyUserInfoManager.INSTANCE.hasRealLocation()) {
                mTvOtherArea.setText(getAreaFromLocation(MyUserInfoManager.INSTANCE.getRealLocation()));
            } else {
                mTvOtherArea.setText("地域榜");
            }
        } else if (mRankMode == UserRankModel.REGION) {
            mTvOtherArea.setText("全国榜");
        }
        mTvOtherArea.setSelected(false);
    }

    private String getAreaFromLocation(Location location) {
        if (location != null && !TextUtils.isEmpty(location.getDistrict())) {
            return location.getDistrict() + "榜";
        } else {
            return "未知位置";
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mSkrLocationPermission.onBackFromPermisionManagerMaybe(getActivity());
    }

    @Override
    public void showRankList(List<RankInfoModel> rankInfoModel, boolean hasMore) {
        mRefreshLayout.setEnableLoadMore(hasMore);
        mHasMore = hasMore;
        mRefreshLayout.finishLoadMore();
        mLeaderBoardAdapter.setDataList(rankInfoModel);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(NetworkUtils.NetworkChangeEvent event) {
        if (event.type != -1) {
            if (mLeaderBoardAdapter.getDataList() == null && mLeaderBoardAdapter.getDataList().size() == 0) {
                mLeaderboardPresenter.getLeaderBoardInfo(mRankMode);
            }
        }
    }

    @Override
    public void showOwnRankInfo(UserRankModel userRankModel) {
        ExTextView tvRank = mOwnInfoItem.findViewById(R.id.tv_rank);
        tvRank.setTextColor(U.getColor(R.color.white_trans_70));
        SimpleDraweeView sdvIcon = mOwnInfoItem.findViewById(R.id.sdv_icon);
        ExTextView tvName = mOwnInfoItem.findViewById(R.id.tv_name);
        ExTextView tvSegment = mOwnInfoItem.findViewById(R.id.tv_segment);
        ImageView normalLevelView = mOwnInfoItem.findViewById(R.id.level_view);

        if (userRankModel.getRankSeq() == 0) {
            tvRank.setVisibility(View.GONE);
        } else {
            tvRank.setVisibility(View.VISIBLE);
            tvRank.setText(StringFromatUtils.formatTenThousand(userRankModel.getRankSeq()));
        }
        if (LevelConfigUtils.getImageResoucesLevel(userRankModel.getMainRanking()) != 0) {
            normalLevelView.setBackground(U.getDrawable(LevelConfigUtils.getImageResoucesLevel(userRankModel.getMainRanking())));
        }

        tvName.setText(MyUserInfoManager.INSTANCE.getNickName());
        tvSegment.setText(userRankModel.getLevelDesc());
        AvatarUtils.loadAvatarByUrl(sdvIcon,
                AvatarUtils.newParamsBuilder(MyUserInfoManager.INSTANCE.getAvatar())
                        .setCircle(true)
                        .setBorderWidth(U.getDisplayUtils().dip2px(2))
                        .setBorderColorBySex(MyUserInfoManager.INSTANCE.getSex() == ESex.SX_MALE.getValue())
                        .build());
    }

    @Override
    public void showFirstThreeRankInfo(List<RankInfoModel> rankInfoModelList) {
        for (int i = 0; i < 3; i++) {
            if (rankInfoModelList.size() > i) {
                setTopThreeInfo(rankInfoModelList.get(i));
                continue;
            }

            setEmptyTopInfo(i);
        }
    }

    public void setEmptyTopInfo(int seq) {
        switch (seq) {
            case 1:
                mTvRightChanpainName.setText("虚位以待");
                mSdvRightChampainIcon.setImageResource(R.drawable.zanwu_dierming);
                mRightChanpainLevelView.setVisibility(View.GONE);
                mRightChanpainLevelTv.setText("");
                mSdvRightChampainIcon.setOnClickListener(null);
                break;
            case 2:
                mTvLeftChanpainName.setText("虚位以待");
                mSdvLeftChampainIcon.setImageResource(R.drawable.zanwu_disanming);
                mLeftChanpainLevelView.setVisibility(View.GONE);
                mLeftChanpainLevelTv.setText("");
                mSdvLeftChampainIcon.setOnClickListener(null);
                break;
        }
    }

    @Override
    public void noNetWork() {
        U.getToastUtil().showShort("网络异常");
        mRefreshLayout.finishLoadMore();
    }

    private void setTopThreeInfo(RankInfoModel rankInfoModel) {
        if (rankInfoModel.getRankSeq() == 1) {
            AvatarUtils.loadAvatarByUrl(mSdvChampainIcon,
                    AvatarUtils.newParamsBuilder(rankInfoModel.getAvatar())
                            .setCircle(true)
                            .setBorderWidth(U.getDisplayUtils().dip2px(3))
                            .setBorderColor(0xFFFFD958)
                            .build());

            mSdvChampainIcon.setOnClickListener(new DebounceViewClickListener() {
                @Override
                public void clickValid(View v) {
                    gotoPersonFragment(rankInfoModel.getUserID());
                }
            });
            mTvChanpainName.setText(UserInfoManager.getInstance().getRemarkName(rankInfoModel.getUserID(), rankInfoModel.getNickname()));
            if (LevelConfigUtils.getImageResoucesLevel(rankInfoModel.getMainRanking()) != 0) {
                mChanpainLevelView.setBackground(U.getDrawable(LevelConfigUtils.getImageResoucesLevel(rankInfoModel.getMainRanking())));
            }
            mChanpainLevelTv.setText(rankInfoModel.getLevelDesc());
        } else if (rankInfoModel.getRankSeq() == 2) {
            AvatarUtils.loadAvatarByUrl(mSdvRightChampainIcon,
                    AvatarUtils.newParamsBuilder(rankInfoModel.getAvatar())
                            .setCircle(true)
                            .setBorderWidth(U.getDisplayUtils().dip2px(3))
                            .setBorderColor(0xFFA2C9DA)
                            .build());
            mSdvRightChampainIcon.setOnClickListener(new DebounceViewClickListener() {
                @Override
                public void clickValid(View v) {
                    gotoPersonFragment(rankInfoModel.getUserID());
                }
            });
            mTvRightChanpainName.setText(UserInfoManager.getInstance().getRemarkName(rankInfoModel.getUserID(), rankInfoModel.getNickname()));
            mRightChanpainLevelView.setVisibility(View.VISIBLE);
            if (LevelConfigUtils.getImageResoucesLevel(rankInfoModel.getMainRanking()) != 0) {
                mRightChanpainLevelView.setBackground(U.getDrawable(LevelConfigUtils.getImageResoucesLevel(rankInfoModel.getMainRanking())));
            }
            mRightChanpainLevelTv.setText(rankInfoModel.getLevelDesc());
        } else if (rankInfoModel.getRankSeq() == 3) {
            AvatarUtils.loadAvatarByUrl(mSdvLeftChampainIcon,
                    AvatarUtils.newParamsBuilder(rankInfoModel.getAvatar())
                            .setCircle(true)
                            .setBorderWidth(U.getDisplayUtils().dip2px(3))
                            .setBorderColor(0xFFEEB874)
                            .build());

            mSdvLeftChampainIcon.setOnClickListener(new DebounceViewClickListener() {
                @Override
                public void clickValid(View v) {
                    gotoPersonFragment(rankInfoModel.getUserID());
                }
            });
            mTvLeftChanpainName.setText(UserInfoManager.getInstance().getRemarkName(rankInfoModel.getUserID(), rankInfoModel.getNickname()));
            mLeftChanpainLevelView.setVisibility(View.VISIBLE);
            if (LevelConfigUtils.getImageResoucesLevel(rankInfoModel.getMainRanking()) != 0) {
                mLeftChanpainLevelView.setBackground(U.getDrawable(LevelConfigUtils.getImageResoucesLevel(rankInfoModel.getMainRanking())));
            }
            mLeftChanpainLevelTv.setText(rankInfoModel.getLevelDesc());
        }
    }

    public void gotoPersonFragment(int uid) {
        Bundle bundle = new Bundle();
        bundle.putInt("bundle_user_id", uid);
        ARouter.getInstance()
                .build(RouterConstants.ACTIVITY_OTHER_PERSON)
                .with(bundle)
                .navigation();
    }

    @Override
    public void destroy() {
        super.destroy();
        mPopupWindow.dismiss();
        U.getSoundUtils().release(TAG);
    }

    @Override
    public boolean useEventBus() {
        return true;
    }
}
