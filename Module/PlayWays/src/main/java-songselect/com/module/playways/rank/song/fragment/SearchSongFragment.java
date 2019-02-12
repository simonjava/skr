package com.module.playways.rank.song.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.common.base.BaseActivity;
import com.common.base.BaseFragment;
import com.common.base.FragmentDataListener;
import com.common.rxretrofit.ApiManager;
import com.common.rxretrofit.ApiMethods;
import com.common.rxretrofit.ApiObserver;
import com.common.rxretrofit.ApiResult;
import com.common.utils.FragmentUtils;
import com.common.utils.U;
import com.common.view.recyclerview.RecyclerOnItemClickListener;
import com.common.view.titlebar.CommonTitleBar;
import com.component.busilib.callback.EmptyCallback;
import com.component.busilib.callback.ErrorCallback;
import com.component.busilib.callback.LoadingCallback;
import com.kingja.loadsir.callback.Callback;
import com.kingja.loadsir.callback.SuccessCallback;
import com.kingja.loadsir.core.LoadService;
import com.kingja.loadsir.core.LoadSir;
import com.module.playways.PlayWaysActivity;
import com.module.playways.audioroom.AudioRoomActivity;
import com.module.playways.rank.prepare.fragment.AuditionFragment;
import com.module.playways.rank.prepare.fragment.AuditionPrepareResFragment;
import com.module.playways.rank.prepare.fragment.PrepareResFragment;
import com.module.playways.rank.prepare.model.PrepareData;
import com.module.playways.rank.song.SongSelectServerApi;
import com.module.playways.rank.song.adapter.SongSelectAdapter;
import com.module.playways.rank.song.model.SongModel;
import com.module.rank.R;

import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import static com.module.playways.PlayWaysActivity.KEY_GAME_TYPE;

public class SearchSongFragment extends BaseFragment {

    CommonTitleBar mTitlebar;

    RecyclerView mSearchResult;
    LinearLayoutManager mLinearLayoutManager;
    SongSelectAdapter mSongSelectAdapter;

    LoadService mLoadService;

    int mGameType;
    String mKeyword;

    @Override
    public int initView() {
        return R.layout.search_song_fragment_layout;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {

        mTitlebar = (CommonTitleBar) mRootView.findViewById(R.id.titlebar);
        mSearchResult = (RecyclerView) mRootView.findViewById(R.id.search_result);

        Bundle bundle = getArguments();
        if (bundle != null) {
            mGameType = bundle.getInt(KEY_GAME_TYPE);
        }

        mLinearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mSearchResult.setLayoutManager(mLinearLayoutManager);
        mSongSelectAdapter = new SongSelectAdapter(new RecyclerOnItemClickListener() {
            @Override
            public void onItemClicked(View view, int position, Object model) {
                U.getKeyBoardUtils().hideSoftInputKeyBoard(getActivity());
                SongModel songModel = (SongModel) model;
                if (getActivity() instanceof AudioRoomActivity) {
                    U.getToastUtil().showShort("试音房");
                    if (songModel.isAllResExist()) {
                        PrepareData prepareData = new PrepareData();
                        prepareData.setSongModel(songModel);
                        prepareData.setBgMusic(songModel.getRankUserVoice());

                        mRootView.post(new Runnable() {
                            @Override
                            public void run() {
                                U.getFragmentUtils().addFragment(FragmentUtils.newAddParamsBuilder(getActivity(), AuditionFragment.class)
                                        .setAddToBackStack(true)
                                        .setHasAnimation(true)
                                        .addDataBeforeAdd(0, prepareData)
                                        .setFragmentDataListener(new FragmentDataListener() {
                                            @Override
                                            public void onFragmentResult(int requestCode, int resultCode, Bundle bundle, Object obj) {

                                            }
                                        })
                                        .build());
                            }
                        });
                    } else {
                        U.getFragmentUtils().addFragment(FragmentUtils.newAddParamsBuilder((BaseActivity) getContext(), AuditionPrepareResFragment.class)
                                .setAddToBackStack(false)
                                .setHasAnimation(true)
                                .addDataBeforeAdd(0, songModel)
                                .setFragmentDataListener(new FragmentDataListener() {
                                    @Override
                                    public void onFragmentResult(int requestCode, int resultCode, Bundle bundle, Object obj) {

                                    }
                                })
                                .build());
                    }
                    return;
                }

                if (getActivity() instanceof PlayWaysActivity) {
                    U.getFragmentUtils().addFragment(FragmentUtils.newAddParamsBuilder((BaseActivity) getContext(), PrepareResFragment.class)
                            .setAddToBackStack(true)
                            .setNotifyHideFragment(SongSelectFragment.class)
                            .setHasAnimation(true)
                            .addDataBeforeAdd(0, songModel)
                            .addDataBeforeAdd(1, mGameType)
                            .addDataBeforeAdd(2, true)
                            .setFragmentDataListener(new FragmentDataListener() {
                                @Override
                                public void onFragmentResult(int requestCode, int resultCode, Bundle bundle, Object obj) {

                                }
                            })
                            .build());
                }
            }
        });
        mSearchResult.setAdapter(mSongSelectAdapter);

        mTitlebar.setListener(new CommonTitleBar.OnTitleBarListener() {
            @Override
            public void onClicked(View v, int action, String extra) {
                switch (action) {
                    case CommonTitleBar.ACTION_RIGHT_TEXT:
                        onBackPressed();
                        break;
                    case CommonTitleBar.ACTION_SEARCH_SUBMIT:
                        searchMusicItems(extra);
                        break;
                    case CommonTitleBar.ACTION_SEARCH_DELETE:
                        mTitlebar.getCenterSearchEditText().setText("");
                        break;
                }
            }
        });

        mTitlebar.showSoftInputKeyboard(true);


        LoadSir mLoadSir = new LoadSir.Builder()
                .addCallback(new LoadingCallback(R.drawable.wulishigedan, "数据正在努力加载中..."))
                .addCallback(new EmptyCallback(R.drawable.wulishigedan, "搜索不到歌曲呢～"))
                .addCallback(new ErrorCallback(R.drawable.wulishigedan, "请求出错了..."))
                .build();
        mLoadService = mLoadSir.register(mSearchResult, new Callback.OnReloadListener() {
            @Override
            public void onReload(View v) {
                searchMusicItems(mKeyword);
            }
        });
    }

    private void searchMusicItems(String keyword) {
        mKeyword = keyword;
        if (TextUtils.isEmpty(keyword)) {
            U.getToastUtil().showShort("搜索内容为空");
            return;
        }
        SongSelectServerApi songSelectServerApi = ApiManager.getInstance().createService(SongSelectServerApi.class);
        ApiMethods.subscribe(songSelectServerApi.searchMusicItems(keyword), new ApiObserver<ApiResult>() {
            @Override
            public void process(ApiResult result) {
                if (result.getErrno() == 0) {
                    List<SongModel> list = JSON.parseArray(result.getData().getString("items"), SongModel.class);
                    loadSongsDetailItems(list);
                } else {
                    mLoadService.showCallback(ErrorCallback.class);
                }
            }

            @Override
            public void onNetworkError(ErrorType errorType) {
                U.getToastUtil().showShort("网络异常，请检查网络后重试");
                super.onNetworkError(errorType);
            }
        }, this);
    }

    public void loadSongsDetailItems(List<SongModel> list) {
        if (list == null || list.size() == 0) {
            mLoadService.showCallback(EmptyCallback.class);
            return;
        }

        U.getKeyBoardUtils().hideSoftInputKeyBoard(getActivity());
        if (mSongSelectAdapter != null) {
            mLoadService.showSuccess();
            mSearchResult.setVisibility(View.VISIBLE);
            mSongSelectAdapter.setDataList(list);
            mSongSelectAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean useEventBus() {
        return false;
    }

    @Override
    protected boolean onBackPressed() {
        U.getKeyBoardUtils().hideSoftInputKeyBoard(getActivity());
        U.getFragmentUtils().popFragment(SearchSongFragment.this);
        return true;
    }
}
