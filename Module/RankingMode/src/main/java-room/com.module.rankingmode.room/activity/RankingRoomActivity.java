package com.module.rankingmode.room.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.common.base.BaseActivity;
import com.common.utils.FragmentUtils;
import com.common.utils.U;
import com.module.RouterConstants;
import com.module.rankingmode.R;
import com.module.rankingmode.prepare.model.PrepareData;
import com.module.rankingmode.room.fragment.EvaluationFragment;
import com.module.rankingmode.room.model.RoomData;
import com.module.rankingmode.room.model.RoomDataUtils;
import com.module.rankingmode.song.model.SongModel;

import java.util.ArrayList;

@Route(path = RouterConstants.ACTIVITY_RANKING_ROOM)
public class RankingRoomActivity extends BaseActivity {

    /**
     * 存起该房间一些状态信息
     */
    RoomData mRoomData = new RoomData();

    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.ranking_room_activity_layout;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        PrepareData prepareData = (PrepareData) getIntent().getSerializableExtra("prepare_data");
        if(prepareData!=null) {
            mRoomData.setGameId(prepareData.getGameId());
            mRoomData.setGameCreateTs(prepareData.getGameCreatMs());
            mRoomData.setGameStartTs(prepareData.getGameReadyInfo().getJsonGameStartInfo().getStartTimeMs());
            mRoomData.setShiftTs(prepareData.getShiftTs());

            mRoomData.setSongModel(prepareData.getSongModel());

            ArrayList<SongModel> songModelArrayList = new ArrayList<>();
            for (com.module.rankingmode.prepare.model.PlayerInfo playerInfo : prepareData.getPlayerInfoList()){
                songModelArrayList.addAll(playerInfo.getSongList());
            }
            mRoomData.setSongModelList(songModelArrayList);
            mRoomData.setRoundInfoModelList(prepareData.getGameReadyInfo().getJsonRoundInfo());
            mRoomData.setExpectRoundInfo(RoomDataUtils.findFirstRoundInfo(mRoomData.getRoundInfoModelList()));

            mRoomData.setPlayerInfoList(prepareData.getPlayerInfoList());
        }else{

        }

    }

    @Override
    public boolean useEventBus() {
        return false;
    }

    @Override
    public boolean canSlide() {
        return false;
    }

    @Override
    public boolean resizeLayoutSelfWhenKeybordShow() {
        return true;
    }
}
