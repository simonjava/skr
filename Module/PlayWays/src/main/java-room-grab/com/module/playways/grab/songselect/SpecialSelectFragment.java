package com.module.playways.grab.songselect;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;

import com.common.base.BaseFragment;
import com.common.utils.FragmentUtils;
import com.common.utils.U;
import com.common.view.ex.ExImageView;
import com.common.view.recyclerview.RecyclerOnItemClickListener;
import com.common.view.titlebar.CommonTitleBar;
import com.component.busilib.constans.GameModeType;
import com.module.playways.grab.prepare.GrabMatchFragment;
import com.module.playways.rank.prepare.model.PrepareData;
import com.module.playways.rank.song.model.SongModel;
import com.module.rank.R;

import java.util.ArrayList;
import java.util.List;

public class SpecialSelectFragment extends BaseFragment {

    RelativeLayout mMainActContainer;
    CommonTitleBar mTitleView;
    ExImageView mSelectLogoIv;
    RecyclerView mContentRv;
    SpecialSelectAdapter mSpecialSelectAdapter;
    LinearLayoutManager mLinearLayoutManager;

    @Override
    public int initView() {
        return R.layout.special_select_fragment_layout;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        mMainActContainer = (RelativeLayout) mRootView.findViewById(R.id.main_act_container);
        mTitleView = (CommonTitleBar) mRootView.findViewById(R.id.title_view);
        mSelectLogoIv = (ExImageView) mRootView.findViewById(R.id.select_logo_iv);
        mContentRv = (RecyclerView) mRootView.findViewById(R.id.content_rv);
        mLinearLayoutManager = new LinearLayoutManager(getContext());
        mContentRv.setLayoutManager(mLinearLayoutManager);
        mSpecialSelectAdapter = new SpecialSelectAdapter(new RecyclerOnItemClickListener<SpecialModel>() {
            @Override
            public void onItemClicked(View view, int position, SpecialModel model) {
                goMatchFragment(model.getId());
            }
        });
        mContentRv.setAdapter(mSpecialSelectAdapter);
        loadData();
    }

    private void loadData() {
        //TODO TEST
        List<SpecialModel> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            SpecialModel specialModel = new SpecialModel();
            specialModel.setId(i);
            specialModel.setSpecialName("周杰伦专场");
            list.add(specialModel);
        }
        mSpecialSelectAdapter.setDataList(list);
    }

    @Override
    public boolean useEventBus() {
        return false;
    }

    public void goMatchFragment(int specialId) {
        PrepareData prepareData = new PrepareData();
        prepareData.setGameType(GameModeType.GAME_MODE_GRAB);
        SongModel songModel = new SongModel();
        songModel.setItemID(specialId);
        prepareData.setSongModel(songModel);
        U.getFragmentUtils().addFragment(FragmentUtils.newAddParamsBuilder(getActivity(), GrabMatchFragment.class)
                .setAddToBackStack(true)
                .addDataBeforeAdd(0, prepareData)
                .setHasAnimation(true)
                .build()
        );

    }
}
