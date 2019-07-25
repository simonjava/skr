package com.module.home.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.common.base.BaseFragment;
import com.common.rxretrofit.ApiManager;
import com.common.utils.FragmentUtils;
import com.common.utils.SpanUtils;
import com.common.utils.U;
import com.common.view.DebounceViewClickListener;
import com.common.view.titlebar.CommonTitleBar;
import com.module.RouterConstants;
import com.module.home.event.PhoneAuthSuccessEvent;
import com.module.home.event.WithDrawSuccessEvent;
import com.module.home.inter.IWalletView;
import com.module.home.R;
import com.module.home.adapter.WalletRecordAdapter;
import com.module.home.model.WalletRecordModel;
import com.module.home.model.WithDrawInfoModel;
import com.module.home.presenter.WalletRecordPresenter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

public class WalletFragment extends BaseFragment implements IWalletView {

    RelativeLayout mMainActContainer;
    CommonTitleBar mTitlebar;
    TextView mBalanceTv;
    TextView mWithdrawTv;
    SmartRefreshLayout mRefreshLayout;
    RecyclerView mRecyclerView;

    WithDrawInfoModel mWithDrawInfoModel;

    WalletRecordAdapter mWalletRecordAdapter;

    WalletRecordPresenter mWalletRecordPresenter;

    int offset = 0; //偏移量
    int DEFAULT_COUNT = 10; //每次拉去的数量

    float balance = 0; //可用余额

    @Override
    public int initView() {
        return R.layout.wallet_fragment_layout;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        mMainActContainer = (RelativeLayout) getRootView().findViewById(R.id.main_act_container);
        mTitlebar = (CommonTitleBar) getRootView().findViewById(R.id.titlebar);
        mBalanceTv = (TextView) getRootView().findViewById(R.id.balance_tv);
        mWithdrawTv = (TextView) getRootView().findViewById(R.id.withdraw_tv);
        mRefreshLayout = (SmartRefreshLayout) getRootView().findViewById(R.id.refreshLayout);
        mRecyclerView = (RecyclerView) getRootView().findViewById(R.id.recycler_view);

        initPresenter();

        mRefreshLayout.setEnableRefresh(false);
        mRefreshLayout.setEnableLoadMore(true);
        mRefreshLayout.setEnableLoadMoreWhenContentNotFull(true);
        mRefreshLayout.setEnableOverScrollDrag(false);
        mRefreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
//                mWalletRecordPresenter.getWalletIncrRecords(offset, DEFAULT_COUNT);
                mWalletRecordPresenter.getAllWalletRecords(offset, DEFAULT_COUNT);
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mRefreshLayout.finishRefresh();
            }
        });

        mTitlebar.getLeftTextView().setOnClickListener(new DebounceViewClickListener() {
            @Override
            public void clickValid(View v) {
                //U.getSoundUtils().play(TAG, R.raw.normal_back, 500);
                if (getActivity() != null) {
                    getActivity().finish();
                }
            }
        });

        mTitlebar.getRightTextView().setOnClickListener(new DebounceViewClickListener() {
            @Override
            public void clickValid(View v) {
                U.getFragmentUtils().addFragment(FragmentUtils.newAddParamsBuilder(getActivity(), WithDrawHistoryFragment.class)
                        .setAddToBackStack(true)
                        .setHasAnimation(true)
                        .build());
            }
        });

        mWithdrawTv.setOnClickListener(new DebounceViewClickListener() {
            @Override
            public void clickValid(View v) {
                if (balance < 10) {
                    U.getToastUtil().showShort("满10元才能提现哦～");
                } else if (mWithDrawInfoModel == null) {
                    U.getToastUtil().showShort("正在加载数据");
                    mWalletRecordPresenter.getWithDrawInfo(0);
                } else if (!mWithDrawInfoModel.isIsPhoneAuth()) {
                    ARouter.getInstance()
                            .build(RouterConstants.ACTIVITY_SMS_AUTH)
                            .navigation();
                } else if (!mWithDrawInfoModel.isIsRealAuth()) {
                    mWithDrawInfoModel = null;
                    ARouter.getInstance().build(RouterConstants.ACTIVITY_WEB)
                            .withString(RouterConstants.KEY_WEB_URL, ApiManager.getInstance().findRealUrlByChannel("http://app.inframe.mobi/oauth/card"))
                            .navigation();
                } else {
                    if (!U.getNetworkUtils().hasNetwork()) {
                        U.getToastUtil().showShort("您网络异常！");
                        return;
                    }

                    ARouter.getInstance()
                            .build(RouterConstants.ACTIVITY_WITH_DRAW)
                            .navigation();
                }
            }
        });

        U.getSoundUtils().preLoad(getTAG(), R.raw.normal_back);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mWithDrawInfoModel == null) {
            mWalletRecordPresenter.getWithDrawInfo(0);
        }
    }

    private void initPresenter() {
        mWalletRecordPresenter = new WalletRecordPresenter(this);
        addPresent(mWalletRecordPresenter);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mWalletRecordAdapter = new WalletRecordAdapter();
        mRecyclerView.setAdapter(mWalletRecordAdapter);

        mWalletRecordPresenter.getBalance();
//        mWalletRecordPresenter.getWalletIncrRecords(offset, DEFAULT_COUNT);
        mWalletRecordPresenter.getAllWalletRecords(offset, DEFAULT_COUNT);
    }

    @Override
    public void destroy() {
        super.destroy();
        U.getSoundUtils().release(getTAG());
    }

    @Override
    public boolean useEventBus() {
        return true;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(WithDrawSuccessEvent event) {
        mWalletRecordPresenter.getBalance();
        mWalletRecordPresenter.getAllWalletRecords(offset, DEFAULT_COUNT);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(PhoneAuthSuccessEvent event) {
        mWithDrawInfoModel.setIsPhoneAuth(true);
    }

    @Override
    public void onGetBalanceSucess(String availableBalance, String lockedBalance) {
        if (!TextUtils.isEmpty(availableBalance)) {
            balance = Float.parseFloat(availableBalance);
        }
        SpannableStringBuilder stringBuilder = new SpanUtils()
                .append(availableBalance).setFontSize(50, true)
                .append("元").setFontSize(14, true)
                .create();
        mBalanceTv.setText(stringBuilder);
    }

    @Override
    public void onGetIncrRecords(int offset, List<WalletRecordModel> list) {
        this.offset = offset;
        if (list == null || list.size() <= 0) {
            mRefreshLayout.setEnableLoadMore(false);
            return;
        }
        mRefreshLayout.finishLoadMore();
        mWalletRecordAdapter.insertListLast(list);
        mWalletRecordAdapter.notifyDataSetChanged();
    }

    @Override
    public void onGetAllRecords(int offset, List<WalletRecordModel> list) {
        this.offset = offset;
        if (list == null || list.size() <= 0) {
            mRefreshLayout.setEnableLoadMore(false);
            mRefreshLayout.finishLoadMore();
            return;
        }
        mRefreshLayout.finishLoadMore();
        mWalletRecordAdapter.insertListLast(list);
        mWalletRecordAdapter.notifyDataSetChanged();
    }

    @Override
    public void showWithDrawInfo(WithDrawInfoModel withDrawInfoModel) {
        mWithDrawInfoModel = withDrawInfoModel;
    }
}
