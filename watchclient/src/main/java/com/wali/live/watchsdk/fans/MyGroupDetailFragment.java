package com.wali.live.watchsdk.fans;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.base.activity.BaseActivity;
import com.base.fragment.RxFragment;
import com.base.fragment.utils.FragmentNaviUtils;
import com.base.keyboard.KeyboardUtils;
import com.base.utils.display.DisplayUtils;
import com.base.view.BackTitleBar;
import com.mi.live.data.account.UserAccountManager;
import com.wali.live.watchsdk.R;
import com.wali.live.watchsdk.fans.model.FansGroupDetailModel;
import com.wali.live.watchsdk.fans.model.member.FansMemberModel;
import com.wali.live.watchsdk.fans.presenter.FansGroupDetailPresenter;
import com.wali.live.watchsdk.fans.presenter.IFansGroupDetailView;
import com.wali.live.watchsdk.fans.view.merge.FansDetailBasicView;

import java.util.List;

/**
 * Created by lan on 2017/11/9.
 */
public class MyGroupDetailFragment extends RxFragment implements View.OnClickListener, IFansGroupDetailView {
    private BackTitleBar mTitleBar;
    private FansDetailBasicView mDetailBasicView;

    private FansGroupDetailPresenter mFansGroupDetailPresenter;

    private long mZuid = UserAccountManager.getInstance().getUuidAsLong();
    private FansGroupDetailModel mGroupDetailModel;

    @Override
    public int getRequestCode() {
        return 0;
    }

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_my_group_detail, container, false);
    }

    @Override
    protected void bindView() {
        mTitleBar = $(R.id.title_bar);
        mTitleBar.getBackBtn().setOnClickListener(this);

        mTitleBar.getRightImageBtn().setImageResource(R.drawable.web_icon_relay_bg);
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) mTitleBar.getRightImageBtn().getLayoutParams();
        lp.rightMargin = DisplayUtils.dip2px(10f);

        mDetailBasicView = $(R.id.detail_basic_view);

        initPresenter();
    }

    private void initPresenter() {
        mFansGroupDetailPresenter = new FansGroupDetailPresenter(this);
        mFansGroupDetailPresenter.getFansGroupDetail(mZuid);
        mFansGroupDetailPresenter.getTopThreeMember(mZuid);
    }

    @Override
    public void setFansGroupDetail(FansGroupDetailModel model) {
        mGroupDetailModel = model;

        updateView();
    }

    private void updateView() {
        mTitleBar.setTitle(mGroupDetailModel.getGroupName());
        updateBasicArea();
    }

    private void updateBasicArea() {
        mDetailBasicView.setGroupDetailModel(mGroupDetailModel);
    }

    @Override
    public void setTopThreeMember(List<FansMemberModel> memberList) {
        mDetailBasicView.setTopThreeMember(memberList);
    }

    @Override
    public boolean onBackPressed() {
        if (getActivity() == null) {
            return false;
        }
        finish();
        return true;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.back_iv) {
            finish();
        }
    }

    private void finish() {
        KeyboardUtils.hideKeyboardImmediately(getActivity());
        FragmentNaviUtils.popFragment(getActivity());
    }

    public static void open(BaseActivity baseActivity) {
        FragmentNaviUtils.addFragmentToBackStack(baseActivity, R.id.main_act_container, MyGroupDetailFragment.class,
                null, true, R.anim.slide_right_in, R.anim.slide_right_out);

    }
}
