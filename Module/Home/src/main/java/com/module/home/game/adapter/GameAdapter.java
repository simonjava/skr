package com.module.home.game.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.common.base.BaseFragment;
import com.component.busilib.friends.RecommendModel;
import com.component.busilib.friends.SpecialModel;
import com.module.home.R;
import com.module.home.game.model.BannerModel;
import com.module.home.game.model.FuncationModel;
import com.module.home.game.model.QuickJoinRoomModel;
import com.module.home.game.model.RecommendRoomModel;
import com.module.home.game.viewholder.BannerViewHolder;
import com.module.home.game.viewholder.FuncationAreaViewHolder;
import com.module.home.game.viewholder.QuickRoomViewHolder;

import java.util.ArrayList;
import java.util.List;

public class GameAdapter extends RecyclerView.Adapter {

    public final static String TAG = "GameAdapter";

    BaseFragment mBaseFragment;

    GameAdapterListener mListener;

    Object[] mObjArr = new Object[4];
    List<Object> mDataList = new ArrayList<>();

    public static final int TYPE_BANNER_HOLDER = 0;       // 广告
    public static final int TYPE_FUNCATION_HOLDER = 1;    // 功能区域（做任务，排行榜，练歌房）
    public static final int TYPE_QUICK_ROOM_HOLDER = 2;   // 快速房

    public GameAdapter(BaseFragment baseFragment, GameAdapterListener gameAdapterListener) {
        this.mBaseFragment = baseFragment;
        mListener = gameAdapterListener;
    }

    public void updateBanner(BannerModel bannerModel) {
        mObjArr[TYPE_BANNER_HOLDER] = bannerModel;
        setDataList();
    }

    public void updateFuncation(FuncationModel funcationModel) {
        mObjArr[TYPE_FUNCATION_HOLDER] = funcationModel;
        setDataList();
    }

    public void updateQuickJoinRoomInfo(QuickJoinRoomModel quickJoinRoomModel) {
        mObjArr[TYPE_QUICK_ROOM_HOLDER] = quickJoinRoomModel;
        setDataList();
    }

    private void setDataList() {
        mDataList.clear();
        for (Object object : mObjArr) {
            if (object != null) {
                mDataList.add(object);
            }
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_BANNER_HOLDER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.game_banner_item_view, parent, false);
            BannerViewHolder bannerViewHolder = new BannerViewHolder(view);
            return bannerViewHolder;
        } else if (viewType == TYPE_FUNCATION_HOLDER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.game_funcation_item_view, parent, false);
            FuncationAreaViewHolder funcationAreaViewHolder = new FuncationAreaViewHolder(view, mListener);
            return funcationAreaViewHolder;
        } else if (viewType == TYPE_QUICK_ROOM_HOLDER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.game_quick_room_item_view, parent, false);
            QuickRoomViewHolder quickRoomViewHolder = new QuickRoomViewHolder(view, mBaseFragment, mListener);
            return quickRoomViewHolder;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Object obj = mDataList.get(position);
        if (obj instanceof BannerModel) {
            ((BannerViewHolder) holder).bindData((BannerModel) obj);
        } else if (obj instanceof FuncationModel) {
            ((FuncationAreaViewHolder) holder).bindData((FuncationModel) obj);
        } else if (obj instanceof QuickJoinRoomModel) {
            ((QuickRoomViewHolder) holder).bindData((QuickJoinRoomModel) obj);
        }
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    @Override
    public int getItemViewType(int position) {
        Object obj = mDataList.get(position);
        if (obj instanceof BannerModel) {
            return TYPE_BANNER_HOLDER;
        } else if (obj instanceof FuncationModel) {
            return TYPE_FUNCATION_HOLDER;
        } else if (obj instanceof QuickJoinRoomModel) {
            return TYPE_QUICK_ROOM_HOLDER;
        }
        return 0;
    }

    public interface GameAdapterListener {
        void createRoom();     //创建房间

        void selectSpecial(SpecialModel specialModel);   //选择专场

        void clickTask();  //做任务

        void clickRank();  //排行榜

        void clickPractice(); //练歌房
    }
}
