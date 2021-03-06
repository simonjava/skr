package com.component.relation.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.common.base.BaseFragment;
import com.common.core.kouling.SkrKouLingUtils;
import com.common.core.kouling.api.KouLingServerApi;
import com.common.core.myinfo.MyUserInfoManager;
import com.common.core.userinfo.UserInfoManager;
import com.common.core.userinfo.event.RelationChangeEvent;
import com.common.core.userinfo.event.RemarkChangeEvent;
import com.common.log.MyLog;
import com.common.notification.event.FollowNotifyEvent;
import com.common.rxretrofit.ApiManager;
import com.common.rxretrofit.ApiResult;
import com.common.utils.FragmentUtils;
import com.common.utils.U;
import com.common.view.DebounceViewClickListener;
import com.common.view.ex.ExImageView;
import com.common.view.ex.ExTextView;
import com.common.view.viewpager.NestViewPager;
import com.common.view.viewpager.SlidingTabLayout;
import com.component.busilib.R;
import com.component.dialog.InviteFriendDialog;
import com.component.relation.activity.RelationActivity;
import com.component.relation.view.RelationView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;

import io.reactivex.Observable;

/**
 * 关系列表
 */
public class RelationFragment extends BaseFragment {

    LinearLayout mContainer;
    ExTextView mIvBack;
    ExImageView mAddFriendIv;
    SlidingTabLayout mRelationTab;
    NestViewPager mRelationVp;

    RelativeLayout mFriendArea;
    ExTextView mFriend;
    ExImageView mFriendRedDot;
    RelativeLayout mFansArea;
    ExTextView mFans;
    ExImageView mFansRedDot;
    RelativeLayout mFollowArea;
    ExTextView mFollow;

    PopupWindow mPopupWindow;  // 弹窗
    RelativeLayout mSearchArea;
    RelativeLayout mInviteArea;
    LinearLayout mTabLl;

    PagerAdapter mTabPagerAdapter;

    int mFriendNum = 0;  // 好友数
    int mFansNum = 0;    // 粉丝数
    int mFocusNum = 0;   // 关注数

    InviteFriendDialog mInviteFriendDialog;

    HashMap<Integer, RelationView> mTitleAndViewMap = new HashMap<>();

    @Override
    public int initView() {
        return R.layout.relation_fragment_layout;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        mIvBack = (ExTextView) getRootView().findViewById(R.id.iv_back);
        mAddFriendIv = (ExImageView) getRootView().findViewById(R.id.add_friend_iv);
        mContainer = (LinearLayout) getRootView().findViewById(R.id.container);
        mRelationTab = (SlidingTabLayout) getRootView().findViewById(R.id.relation_tab);
        mRelationVp = (NestViewPager) getRootView().findViewById(R.id.relation_vp);

        mFriendArea = (RelativeLayout) getRootView().findViewById(R.id.friend_area);
        mFriend = (ExTextView) getRootView().findViewById(R.id.friend);
        mFriendRedDot = (ExImageView) getRootView().findViewById(R.id.friend_red_dot);
        mFansArea = (RelativeLayout) getRootView().findViewById(R.id.fans_area);
        mFans = (ExTextView) getRootView().findViewById(R.id.fans);
        mFansRedDot = (ExImageView) getRootView().findViewById(R.id.fans_red_dot);
        mFollowArea = (RelativeLayout) getRootView().findViewById(R.id.follow_area);
        mFollow = (ExTextView) getRootView().findViewById(R.id.follow);
        mTabLl = (LinearLayout) getRootView().findViewById(R.id.tab_ll);

        LinearLayout linearLayout = (LinearLayout) getActivity().getLayoutInflater().inflate(R.layout.add_friend_pop_window_layout, null);
        mSearchArea = (RelativeLayout) linearLayout.findViewById(R.id.search_area);
        mInviteArea = (RelativeLayout) linearLayout.findViewById(R.id.invite_area);
        mPopupWindow = new PopupWindow(linearLayout);
        mPopupWindow.setOutsideTouchable(true);

        mIvBack.setOnClickListener(new DebounceViewClickListener() {
            @Override
            public void clickValid(View v) {
//                U.getFragmentUtils().popFragment(RelationFragment.this);
                if (getActivity() != null) {
                    getActivity().finish();
                }
            }
        });

        mAddFriendIv.setOnClickListener(new DebounceViewClickListener() {
            @Override
            public void clickValid(View v) {
                if (mPopupWindow != null && mPopupWindow.isShowing()) {
                    mPopupWindow.dismiss();
                }
                mPopupWindow.setWidth(U.getDisplayUtils().dip2px(118));
                mPopupWindow.setHeight(U.getDisplayUtils().dip2px(115));
                mPopupWindow.showAsDropDown(mAddFriendIv, -U.getDisplayUtils().dip2px(80), -U.getDisplayUtils().dip2px(5));
            }
        });

        mSearchArea.setOnClickListener(new DebounceViewClickListener() {
            @Override
            public void clickValid(View v) {
                if (mPopupWindow != null && mPopupWindow.isShowing()) {
                    mPopupWindow.dismiss();
                }
                U.getFragmentUtils().addFragment(
                        FragmentUtils.newAddParamsBuilder(getActivity(), SearchUserFragment.class)
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

        mTitleAndViewMap.put(0, new RelationView(getContext(), UserInfoManager.RELATION.FRIENDS.getValue()));
        mTitleAndViewMap.put(1, new RelationView(getContext(), UserInfoManager.RELATION.FOLLOW.getValue()));
        mTitleAndViewMap.put(2, new RelationView(getContext(), UserInfoManager.RELATION.FANS.getValue()));

        mRelationTab.setCustomTabView(R.layout.relation_tab_view, R.id.tab_tv);
        mRelationTab.setSelectedIndicatorColors(U.getColor(R.color.black_trans_20));
        mRelationTab.setDistributeMode(SlidingTabLayout.DISTRIBUTE_MODE_TAB_IN_SECTION_CENTER);
        mRelationTab.setIndicatorAnimationMode(SlidingTabLayout.ANI_MODE_NONE);
        mRelationTab.setIndicatorWidth(U.getDisplayUtils().dip2px(67));
        mRelationTab.setIndicatorBottomMargin(U.getDisplayUtils().dip2px(12));
        mRelationTab.setSelectedIndicatorThickness(U.getDisplayUtils().dip2px(28));
        mRelationTab.setIndicatorCornorRadius(U.getDisplayUtils().dip2px(14));

        mTabPagerAdapter = new PagerAdapter() {

            @Override
            public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
                MyLog.d(getTAG(), "destroyItem" + " container=" + container + " position=" + position + " object=" + object);
                container.removeView((View) object);
            }

            @NonNull
            @Override
            public Object instantiateItem(@NonNull ViewGroup container, int position) {
                MyLog.d(getTAG(), "instantiateItem" + " container=" + container + " position=" + position);
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
                } else if (position == 1) {
                    mFansRedDot.setVisibility(View.GONE);
                }
                selectPosition(position);
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
            int relation = bundle.getInt(RelationActivity.FROM_PAGE_KEY);
            mFriendNum = bundle.getInt(RelationActivity.FRIEND_NUM_KEY);
            mFansNum = bundle.getInt(RelationActivity.FANS_NUM_KEY);
            mFocusNum = bundle.getInt(RelationActivity.FOLLOW_NUM_KEY);
            if (relation != UserInfoManager.RA_UNKNOWN) {
                if (relation == UserInfoManager.RELATION.FRIENDS.getValue()) {
                    mRelationVp.setCurrentItem(0);
                } else if (relation == UserInfoManager.RELATION.FOLLOW.getValue()) {
                    mRelationVp.setCurrentItem(1);
                } else if (relation == UserInfoManager.RELATION.FANS.getValue()) {
                    mRelationVp.setCurrentItem(2);
                }
            }
            selectPosition(mRelationVp.getCurrentItem());
        } else {
            MyLog.w(getTAG(), "initData" + " savedInstanceState=" + savedInstanceState);
        }

        U.getSoundUtils().preLoad(getTAG(), R.raw.normal_back);
    }

    private void selectPosition(int position) {
        if (position == 0) {
            mFriend.setSelected(true);
            mFollow.setSelected(false);
            mFans.setSelected(false);
        } else if (position == 1) {
            mFriend.setSelected(false);
            mFollow.setSelected(true);
            mFans.setSelected(false);
        } else if (position == 2) {
            mFriend.setSelected(false);
            mFollow.setSelected(false);
            mFans.setSelected(true);
        }

        RelationView relationView = mTitleAndViewMap.get(position);
        if (relationView != null) {
            relationView.initData(false);
        }
    }

    private void showShareDialog() {
        if (mInviteFriendDialog == null) {
//            mInviteFriendDialog = new InviteFriendDialog(getContext(), InviteFriendDialog.INVITE_GRAB_FRIEND, 0, 0, 0, null);
            mInviteFriendDialog = new InviteFriendDialog(getContext(), "", new InviteFriendDialog.IInviteDialogCallBack() {
                @Override
                public Observable<ApiResult> getKouLingTokenObservable() {
                    String code = String.format("inframeskr://relation/bothfollow?inviterId=%s", MyUserInfoManager.INSTANCE.getUid());
                    KouLingServerApi kouLingServerApi = ApiManager.getInstance().createService(KouLingServerApi.class);
                    return kouLingServerApi.setTokenByCode(code);
                }

                @Override
                public String getInviteDialogText(String kouling) {
                    return SkrKouLingUtils.genReqFollowKouling(kouling);
                }
            });
        }
        mInviteFriendDialog.show();
    }

    @Override
    public boolean useEventBus() {
        return true;
    }

    @Override
    protected void onFragmentVisible() {
        super.onFragmentVisible();
        selectPosition(mRelationVp.getCurrentItem());
    }

    /**
     * 别人关注的事件,所有的关系都是从我出发
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(FollowNotifyEvent event) {
        if (this.getFragmentVisible() == true) {
            selectPosition(mRelationVp.getCurrentItem());
        }
    }

    /**
     * 自己主动关注或取关事件
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(RelationChangeEvent event) {
        if (this.getFragmentVisible() == true) {
            selectPosition(mRelationVp.getCurrentItem());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(RemarkChangeEvent event) {
        if (this.getFragmentVisible() == true) {
            selectPosition(mRelationVp.getCurrentItem());
        }
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

        if (mInviteFriendDialog != null) {
            mInviteFriendDialog.dismiss(false);
        }

        U.getSoundUtils().release(getTAG());
    }
}
