package com.zq.relation.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.common.base.BaseFragment;
import com.common.core.kouling.SkrKouLingUtils;
import com.common.core.myinfo.MyUserInfoManager;
import com.common.core.userinfo.UserInfoManager;
import com.common.core.userinfo.UserInfoServerApi;
import com.common.core.userinfo.event.RelationChangeEvent;
import com.common.log.MyLog;
import com.common.notification.NotificationManager;
import com.common.rxretrofit.ApiManager;
import com.common.rxretrofit.ApiMethods;
import com.common.rxretrofit.ApiObserver;
import com.common.rxretrofit.ApiResult;
import com.common.utils.FragmentUtils;
import com.common.utils.U;
import com.common.view.DebounceViewClickListener;
import com.common.view.ex.ExImageView;
import com.common.view.titlebar.CommonTitleBar;
import com.common.view.viewpager.NestViewPager;
import com.common.view.viewpager.SlidingTabLayout;
import com.component.busilib.R;
import com.component.busilib.constans.GrabRoomType;
import com.jakewharton.rxbinding2.view.RxView;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.zq.relation.view.RelationView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.functions.Consumer;
import model.RelationNumModel;

/**
 * 关系列表
 */
public class RelationFragment extends BaseFragment {

    public static final int FROM_FRIENDS = 0;
    public static final int FROM_FANS = 1;
    public static final int FROM_FOLLOW = 2;

    public static final String FROM_PAGE_KEY = "from_page_key";
    public static final String FRIEND_NUM_KEY = "friend_num_key";
    public static final String FANS_NUM_KEY = "fans_num_key";
    public static final String FOLLOW_NUM_KEY = "follow_num_key";

    CommonTitleBar mTitlebar;
    LinearLayout mContainer;
    SlidingTabLayout mRelationTab;
    View mSplitLine;
    NestViewPager mRelationVp;

    RelativeLayout mFriendArea;
    TextView mFriend;
    ExImageView mFriendRedDot;
    RelativeLayout mFansArea;
    TextView mFans;
    ExImageView mFansRedDot;
    RelativeLayout mFollowArea;
    TextView mFollow;

    PopupWindow mPopupWindow;  // 弹窗
    RelativeLayout mSearchArea;
    RelativeLayout mInviteArea;

    PagerAdapter mTabPagerAdapter;

    int mFriendNum = 0;  // 好友数
    int mFansNum = 0;    // 粉丝数
    int mFocusNum = 0;   // 关注数

    DialogPlus mShareDialog;
    TextView mTvWeixinShare;
    TextView mTvQqShare;

    HashMap<Integer, RelationView> mTitleAndViewMap = new HashMap<>();

    @Override
    public int initView() {
        return R.layout.relation_fragment_layout;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        mTitlebar = (CommonTitleBar) mRootView.findViewById(R.id.titlebar);
        mContainer = (LinearLayout) mRootView.findViewById(R.id.container);
        mRelationTab = (SlidingTabLayout) mRootView.findViewById(R.id.relation_tab);
        mSplitLine = (View) mRootView.findViewById(R.id.split_line);
        mRelationVp = (NestViewPager) mRootView.findViewById(R.id.relation_vp);

        mFriendArea = (RelativeLayout) mRootView.findViewById(R.id.friend_area);
        mFriend = (TextView) mRootView.findViewById(R.id.friend);
        mFriendRedDot = (ExImageView) mRootView.findViewById(R.id.friend_red_dot);
        mFansArea = (RelativeLayout) mRootView.findViewById(R.id.fans_area);
        mFans = (TextView) mRootView.findViewById(R.id.fans);
        mFansRedDot = (ExImageView) mRootView.findViewById(R.id.fans_red_dot);
        mFollowArea = (RelativeLayout) mRootView.findViewById(R.id.follow_area);
        mFollow = (TextView) mRootView.findViewById(R.id.follow);

        LinearLayout linearLayout = (LinearLayout) getActivity().getLayoutInflater().inflate(R.layout.add_friend_pop_window_layout, null);
        mSearchArea = (RelativeLayout) linearLayout.findViewById(R.id.search_area);
        mInviteArea = (RelativeLayout) linearLayout.findViewById(R.id.invite_area);
        mPopupWindow = new PopupWindow(linearLayout);
        mPopupWindow.setOutsideTouchable(true);

        mTitlebar.getLeftTextView().setOnClickListener(new DebounceViewClickListener() {
            @Override
            public void clickValid(View v) {
                U.getFragmentUtils().popFragment(RelationFragment.this);
            }
        });

        mTitlebar.getRightImageButton().setOnClickListener(new DebounceViewClickListener() {
            @Override
            public void clickValid(View v) {
                if (mPopupWindow != null && mPopupWindow.isShowing()) {
                    mPopupWindow.dismiss();
                }
                mPopupWindow.setWidth(U.getDisplayUtils().dip2px(118));
                mPopupWindow.setHeight(U.getDisplayUtils().dip2px(115));
                mPopupWindow.showAsDropDown(mTitlebar.getRightImageButton(), -U.getDisplayUtils().dip2px(80), -U.getDisplayUtils().dip2px(5));
            }
        });

        mSearchArea.setOnClickListener(new DebounceViewClickListener() {
            @Override
            public void clickValid(View v) {
                if (mPopupWindow != null && mPopupWindow.isShowing()) {
                    mPopupWindow.dismiss();
                }
                U.getFragmentUtils().addFragment(
                        FragmentUtils.newAddParamsBuilder(getActivity(), SearchFriendFragment.class)
                                .setAddToBackStack(true)
                                .setHasAnimation(true)
                                .build());
            }
        });

        mInviteArea.setOnClickListener(new DebounceViewClickListener() {
            @Override
            public void clickValid(View v) {
                if (mPopupWindow != null && mPopupWindow.isShowing()) {
                    mPopupWindow.dismiss();
                }
                showShareDialog();
            }
        });

        mTitleAndViewMap.put(0, new RelationView(getContext(), UserInfoManager.RELATION_FRIENDS));
        mTitleAndViewMap.put(1, new RelationView(getContext(), UserInfoManager.RELATION_FANS));
        mTitleAndViewMap.put(2, new RelationView(getContext(), UserInfoManager.RELATION_FOLLOW));

        mRelationTab.setCustomTabView(R.layout.relation_tab_view, R.id.tab_tv);
        mRelationTab.setSelectedIndicatorColors(Color.parseColor("#FE8400"));
        mRelationTab.setDistributeMode(SlidingTabLayout.DISTRIBUTE_MODE_TAB_IN_SECTION_CENTER);
        mRelationTab.setIndicatorWidth(U.getDisplayUtils().dip2px(27));
        mRelationTab.setIndicatorBottomMargin(U.getDisplayUtils().dip2px(5));
        mRelationTab.setSelectedIndicatorThickness(U.getDisplayUtils().dip2px(4));
        mRelationTab.setIndicatorCornorRadius(U.getDisplayUtils().dip2px(2));

        mTabPagerAdapter = new PagerAdapter() {

            @Override
            public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
                MyLog.d(TAG, "destroyItem" + " container=" + container + " position=" + position + " object=" + object);
                container.removeView((View) object);
            }

            @NonNull
            @Override
            public Object instantiateItem(@NonNull ViewGroup container, int position) {
                MyLog.d(TAG, "instantiateItem" + " container=" + container + " position=" + position);
                View view = mTitleAndViewMap.get(position);
                if (container.indexOfChild(view) == -1) {
                    container.addView(view);
                }
                return view;
            }

            @Override
            public int getItemPosition(@NonNull Object object) {
                return POSITION_NONE;
            }

            @Override
            public int getCount() {
                return mTitleAndViewMap.size();
            }

            @Override
            public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
                return view == (object);
            }
        };

        mRelationTab.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    mFriendRedDot.setVisibility(View.GONE);
                    if (U.getPreferenceUtils().getSettingInt(NotificationManager.SP_KEY_NEW_FRIEND, 0) >= 1) {
                        U.getPreferenceUtils().setSettingInt(NotificationManager.SP_KEY_NEW_FRIEND, 0);
                    }
                } else if (position == 1) {
                    mFansRedDot.setVisibility(View.GONE);
                    if (U.getPreferenceUtils().getSettingInt(NotificationManager.SP_KEY_NEW_FANS, 0) >= 1) {
                        U.getPreferenceUtils().setSettingInt(NotificationManager.SP_KEY_NEW_FANS, 0);
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mRelationVp.setAdapter(mTabPagerAdapter);
        mRelationTab.setViewPager(mRelationVp);
        mTabPagerAdapter.notifyDataSetChanged();

        Bundle bundle = getArguments();
        if (bundle != null) {
            int from = bundle.getInt(FROM_PAGE_KEY);
            mFriendNum = bundle.getInt(FRIEND_NUM_KEY);
            mFansNum = bundle.getInt(FANS_NUM_KEY);
            mFocusNum = bundle.getInt(FOLLOW_NUM_KEY);
            mRelationVp.setCurrentItem(from);
            refreshRelationNums();
        } else {
            getRelationNums();
        }

        U.getSoundUtils().preLoad(TAG, R.raw.normal_back);
    }


    private void showShareDialog() {
        if (mShareDialog == null) {
            mShareDialog = DialogPlus.newDialog(getContext())
                    .setContentHolder(new ViewHolder(R.layout.invite_friend_panel))
                    .setContentBackgroundResource(R.color.transparent)
                    .setOverlayBackgroundResource(R.color.black_trans_50)
                    .setExpanded(false)
                    .setGravity(Gravity.BOTTOM)
                    .create();

            mTvWeixinShare = (TextView) mShareDialog.findViewById(R.id.tv_weixin_share);
            mTvQqShare = (TextView) mShareDialog.findViewById(R.id.tv_qq_share);
            mTvWeixinShare.setOnClickListener(new DebounceViewClickListener() {
                @Override
                public void clickValid(View v) {
                    // TODO: 2019/3/24 邀请好友
                }
            });

            mTvQqShare.setOnClickListener(new DebounceViewClickListener() {
                @Override
                public void clickValid(View v) {
                    // TODO: 2019/3/24 邀请好友
                }
            });
        }

        if (!mShareDialog.isShowing()) {
            mShareDialog.show();
        }
    }

    private void getRelationNums() {
        UserInfoServerApi userInfoServerApi = ApiManager.getInstance().createService(UserInfoServerApi.class);
        ApiMethods.subscribe(userInfoServerApi.getRelationNum((int) MyUserInfoManager.getInstance().getUid()), new ApiObserver<ApiResult>() {
            @Override
            public void process(ApiResult result) {
                if (result.getErrno() == 0) {
                    List<RelationNumModel> relationNumModels = JSON.parseArray(result.getData().getString("cnt"), RelationNumModel.class);
                    if (relationNumModels != null && relationNumModels.size() > 0) {
                        for (RelationNumModel mode : relationNumModels) {
                            if (mode.getRelation() == UserInfoManager.RELATION_FRIENDS) {
                                mFriendNum = mode.getCnt();
                            } else if (mode.getRelation() == UserInfoManager.RELATION_FANS) {
                                mFansNum = mode.getCnt();
                            } else if (mode.getRelation() == UserInfoManager.RELATION_FOLLOW) {
                                mFocusNum = mode.getCnt();
                            }
                        }
                    }
                    refreshRelationNums();
                }
            }
        });

    }

    public void refreshRelationNums() {
        if (U.getPreferenceUtils().getSettingInt(NotificationManager.SP_KEY_NEW_FRIEND, 0) >= 1) {
            // 好友红点
            mFriendRedDot.setVisibility(View.VISIBLE);
        }
        if (U.getPreferenceUtils().getSettingInt(NotificationManager.SP_KEY_NEW_FANS, 0) >= 1) {
            // 粉丝红点
            mFansRedDot.setVisibility(View.VISIBLE);
        }
        mFriend.setText(String.format(getString(R.string.friends_num), mFriendNum));
        mFollow.setText(String.format(getString(R.string.follows_num), mFocusNum));
        mFans.setText(String.format(getString(R.string.fans_num), mFansNum));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(RelationChangeEvent event) {
        if (event.type == RelationChangeEvent.FOLLOW_TYPE) {
            if (event.isFriend) {
                // 新增好友,好友数加1
                mFriendNum = mFriendNum + 1;
                mFocusNum = mFocusNum + 1;
            } else if (event.isFollow) {
                // 新增关注,关注数加1
                mFocusNum = mFocusNum + 1;
            }
        } else if (event.type == RelationChangeEvent.UNFOLLOW_TYPE) {
            // 关注数减1
            mFocusNum = mFocusNum - 1;
            // TODO: 2019/1/17 怎么判断之前也是好友
            if (event.isOldFriend) {
                mFriendNum = mFriendNum - 1;
            }
        }

        refreshRelationNums();
    }

    @Override
    public void destroy() {
        super.destroy();
        if (mTitleAndViewMap != null) {
            for (RelationView view : mTitleAndViewMap.values()) {
                view.destroy();
            }
        }

        if (mPopupWindow != null && mPopupWindow.isShowing()) {
            mPopupWindow.dismiss();
        }

        U.getSoundUtils().release(TAG);
    }
}
