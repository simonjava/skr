package com.wali.live.watchsdk.fans;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.base.activity.BaseActivity;
import com.base.fragment.RxFragment;
import com.base.fragment.utils.FragmentNaviUtils;
import com.base.view.BackTitleBar;
import com.wali.live.watchsdk.R;
import com.wali.live.watchsdk.fans.adapter.FansGroupAdapter;
import com.wali.live.watchsdk.fans.model.FansGroupListModel;
import com.wali.live.watchsdk.fans.presenter.FansGroupPresenter;
import com.wali.live.watchsdk.fans.presenter.IFansGroupView;

import rx.Observable;

/**
 * Created by lan on 17-6-15.
 */
public class FansGroupFragment extends RxFragment implements View.OnClickListener, IFansGroupView {
    private BackTitleBar mTitleBar;

    private RecyclerView mFansGroupRv;
    private FansGroupAdapter mFansGroupAdapter;

    private FansGroupPresenter mFansGroupPresenter;

    @Override
    public int getRequestCode() {
        return 0;
    }

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_fans_group_list, container, false);
    }

    @Override
    protected void bindView() {
        mTitleBar = $(R.id.title_bar);
        mTitleBar.getBackBtn().setText(getContext().getString(R.string.vfan_me));
        mTitleBar.getBackBtn().setOnClickListener(this);

        mFansGroupRv = $(R.id.recycler_view);
        mFansGroupRv.setLayoutManager(new LinearLayoutManager(getContext()));

        mFansGroupAdapter = new FansGroupAdapter();
        mFansGroupRv.setAdapter(mFansGroupAdapter);

        initPresenter();
    }

    private void initPresenter() {
        mFansGroupPresenter = new FansGroupPresenter(this);
        mFansGroupPresenter.getFansGroupList();
    }

    @Override
    public void getFansGroupListSuccess(FansGroupListModel model) {
        if (model.isFirst()) {
            mFansGroupAdapter.setDataList(model);
        } else {
            mFansGroupAdapter.addDataList(model);
        }
    }

    @Override
    public <T> Observable.Transformer<T, T> bindLifecycle() {
        return bindUntilEvent();
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.back_iv) {
            finish();
        }
    }

    @Override
    public boolean onBackPressed() {
        if (getActivity() == null) {
            return false;
        }
        finish();
        return true;
    }

    private void finish() {
        FragmentNaviUtils.popFragment(getActivity());
    }

    public static void open(BaseActivity activity) {
        FragmentNaviUtils.addFragmentToBackStack(activity, R.id.main_act_container, FansGroupFragment.class,
                null, true, R.anim.slide_right_in, R.anim.slide_right_out);
    }
}
