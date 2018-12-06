package com.module.rankingmode.song.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;

import com.common.base.BaseFragment;
import com.common.loadsir.LoadSirManager;
import com.common.loadsir.callback.ErrorCallback;
import com.common.loadsir.callback.LoadingCallback;
import com.common.loadsir.callback.LottieEmptyCallback;
import com.common.rxretrofit.ApiObserver;
import com.common.utils.FragmentUtils;
import com.common.view.ex.ExTextView;
import com.common.view.recyclerview.RecyclerOnItemClickListener;
import com.common.view.titlebar.CommonTitleBar;
import com.kingja.loadsir.callback.Callback;
import com.kingja.loadsir.callback.SuccessCallback;
import com.kingja.loadsir.core.LoadService;
import com.module.rankingmode.R;
import com.module.rankingmode.song.adapter.SongSelectAdapter;
import com.module.rankingmode.song.model.SongModel;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;

public class SongSelectFragment extends BaseFragment {

    RelativeLayout mMainActContainer;
    CommonTitleBar mTitlebar;
    SmartRefreshLayout mSongRefreshLayout;
    RecyclerView mSongListView;

    SongSelectAdapter mSongSelectAdapter;
    List<SongModel> mSongList = new ArrayList<>();

    LoadService mLoadService;

    @Override
    public int initView() {
        return R.layout.song_select_fragment_layout;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        mMainActContainer = (RelativeLayout) mRootView.findViewById(R.id.main_act_container);
        mTitlebar = (CommonTitleBar) mRootView.findViewById(R.id.titlebar);
        mSongRefreshLayout = (SmartRefreshLayout) mRootView.findViewById(R.id.song_refreshLayout);
        mSongListView = (RecyclerView) mRootView.findViewById(R.id.song_list_view);

        mSongListView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mSongSelectAdapter = new SongSelectAdapter(new RecyclerOnItemClickListener<SongModel>() {
            @Override
            public void onItemClicked(View view, int position, SongModel model) {
                if (mFragmentDataListener != null) {
                    mFragmentDataListener.onFragmentResult(0, 0, null, model);
                    FragmentUtils.popFragment(SongSelectFragment.this);
                }
            }
        });
        mSongListView.setAdapter(mSongSelectAdapter);

        mSongSelectAdapter.setDataList(mSongList);

        mLoadService = LoadSirManager.getDefault().register(mSongRefreshLayout, new Callback.OnReloadListener() {
            @Override
            public void onReload(View v) {
                loadData(false);
            }
        });

        ExTextView tvGoPk = (ExTextView)mRootView.findViewById(R.id.tv_go_pk);
        tvGoPk.setOnClickListener(view -> {
            if (mFragmentDataListener != null) {
                mFragmentDataListener.onFragmentResult(0,0,null,null);
                FragmentUtils.popFragment(SongSelectFragment.this);
            }
        });

        // 显示空页面
        mLoadService.showCallback(LottieEmptyCallback.class);
        io.reactivex.Observable.timer(3, java.util.concurrent.TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ApiObserver<Long>() {
                    @Override
                    public void process(Long obj) {
                        loadData(true);
                    }
                });
    }

    private void loadData(boolean test) {

        mLoadService.showCallback(LoadingCallback.class);
        if (test) {
            // 显示错误页
            io.reactivex.Observable.timer(1, java.util.concurrent.TimeUnit.SECONDS)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new ApiObserver<Long>() {
                        @Override
                        public void process(Long obj) {
                            mLoadService.showCallback(ErrorCallback.class);
                        }
                    });
        } else {
            io.reactivex.Observable.timer(2, java.util.concurrent.TimeUnit.SECONDS)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new ApiObserver<Long>() {
                        @Override
                        public void process(Long obj) {
                            List<SongModel> songModels = new ArrayList<>();
                            for (int i = 0; i < 5; i++) {
                                SongModel songModel = new SongModel();
                                songModel.setSongName("点击歌曲" + i);
                                songModels.add(songModel);
                            }
                            mSongSelectAdapter.setDataList(songModels);
                            mLoadService.showCallback(SuccessCallback.class);
                        }
                    });
        }
    }

    @Override
    public boolean useEventBus() {
        return false;
    }
}
