package com.module.playways;

import com.common.core.myinfo.MyUserInfoManager;
import com.common.log.MyLog;
import com.common.utils.U;
import com.module.playways.rank.prepare.model.PlayerInfoModel;
import com.module.playways.rank.prepare.model.BaseRoundInfoModel;
import com.module.playways.rank.song.model.SongModel;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 针对排位模式的数据处理
 */
public class RoomDataUtils {
    /**
     * 找到首轮演唱的轮次
     *
     * @param jsonRoundInfo
     * @return
     */
    public static <T extends BaseRoundInfoModel> T findFirstRoundInfo(List<T> jsonRoundInfo) {
        Collections.sort(jsonRoundInfo, new Comparator<BaseRoundInfoModel>() {
            @Override
            public int compare(BaseRoundInfoModel r1, BaseRoundInfoModel r2) {
                return r1.getRoundSeq() - r2.getRoundSeq();
            }
        });
        return jsonRoundInfo.get(0);
    }

    /**
     * 是否是同一个轮次
     *
     * @param infoModel1
     * @param infoModel2
     * @return
     */
    public static boolean roundInfoEqual(BaseRoundInfoModel infoModel1, BaseRoundInfoModel infoModel2) {
        if (infoModel1 == null && infoModel2 == null) {
            return true;
        }
        if (infoModel1 != null) {
            return infoModel1.equals(infoModel2);
        }
        if (infoModel2 != null) {
            return infoModel2.equals(infoModel1);
        }
        return false;
    }

    /**
     * 轮次的seq是否大于
     * 1是否大于2
     *
     * @param infoModel1
     * @param infoModel2
     * @return
     */
    public static boolean roundSeqLarger(BaseRoundInfoModel infoModel1, BaseRoundInfoModel infoModel2) {
        if (infoModel2 == null) {
            // 已经是结束状态
            return false;
        }
        if (infoModel1 == null) {
            return true;
        }
        return infoModel1.getRoundSeq() > infoModel2.getRoundSeq();
    }

    /**
     * 轮次的seq是否大于
     * 1是否大于等于2
     *
     * @param infoModel1
     * @param infoModel2
     * @return
     */
    public static boolean roundSeqLargerOrEqual(BaseRoundInfoModel infoModel1, BaseRoundInfoModel infoModel2) {
        if (infoModel2 == null) {
            // 已经是结束状态
            return false;
        }
        if (infoModel1 == null) {
            return true;
        }
        return infoModel1.getRoundSeq() >= infoModel2.getRoundSeq();
    }

    /**
     * 找到轮次等于 seq 的RoundInfoModel
     *
     * @param jsonRoundInfo
     * @param seq
     * @return
     */
    public static <T extends BaseRoundInfoModel> T findRoundInfoBySeq(List<T> jsonRoundInfo, int seq) {
        for (T infoModel : jsonRoundInfo) {
            if (infoModel.getRoundSeq() == seq) {
                return infoModel;
            }
        }
        return null;
    }

    public static <T extends BaseRoundInfoModel> int getUidOfRoundInfo(T infoModel) {
        if (infoModel == null) {
            return 0;
        }
        return infoModel.getUserID();
    }

    public static <T extends BaseRoundInfoModel> int getSeqOfRoundInfo(T infoModel) {
        if (infoModel == null) {
            return 0;
        }
        return infoModel.getRoundSeq();
    }

    /**
     * 根据用户id 尝试找到该用户对应的轮次
     *
     * @param uid
     * @return
     */
    public static <T extends BaseRoundInfoModel> T getRoundInfoByUserId(BaseRoomData<T> roomData, int uid) {
        for (T infoModel : roomData.getRoundInfoModelList()) {
            if (infoModel.getUserID() == uid) {
                return infoModel;
            }
        }
        return null;
    }

    /**
     * 根据轮次信息 尝试找到该用户对应的轮次
     *
     * @return
     */
    public static <T extends BaseRoundInfoModel> T getRoundInfoBySeq(BaseRoomData<T> roomData, int seq) {
        for (T infoModel : roomData.getRoundInfoModelList()) {
            if (infoModel.getRoundSeq() == seq) {
                return infoModel;
            }
        }
        return null;
    }

    /**
     * 根据id找songmodel
     *
     * @param
     * @param uid
     * @return
     */
    public static SongModel getPlayerSongInfoUserId(List<PlayerInfoModel> playerInfos, long uid) {
        try {
            for (PlayerInfoModel infoModel : playerInfos) {
                if (infoModel.getUserInfo().getUserId() == uid) {
                    return infoModel.getSongList().get(0);
                }
            }
        } catch (Exception e) {
            MyLog.e(e);
        }

        return null;
    }

    /**
     * 以本地时间估算 距离 realRoundInfo 结束还有几秒
     * 假设 realRoundInfo 还有3秒结束，返回3000
     * 假设已经结束3秒了 返回 -3000
     *
     * @param roomData
     * @param realRoundInfo
     */
    public static <T extends BaseRoundInfoModel> int estimateTs2End(BaseRoomData<T> roomData, T realRoundInfo) {
        if (realRoundInfo == null) {
            MyLog.d("estimateTs2End realRoundInfo=" + realRoundInfo);
            return 0;
        }
        long ts = realRoundInfo.getSingEndMs() + roomData.getGameStartTs() + roomData.getShiftTs();
        return (int) (ts - System.currentTimeMillis());
    }

    public static <T extends BaseRoundInfoModel> boolean isMyRound(T infoModel) {
        return infoModel != null && infoModel.getUserID() == MyUserInfoManager.getInstance().getUid();
    }

    public static <T extends BaseRoundInfoModel> boolean isRobotRound(T infoModel, List<PlayerInfoModel> playerInfoModels) {
        if (infoModel != null) {
            int uid = infoModel.getUserID();
            for (PlayerInfoModel playerInfoModel : playerInfoModels) {
                if (playerInfoModel.getUserInfo().getUserId() == uid) {
                    return playerInfoModel.isSkrer();
                }
            }
        }
        return false;
    }

    public static <T extends BaseRoundInfoModel> PlayerInfoModel getPlayerInfoById(BaseRoomData<T> roomData, long uid) {
        for (PlayerInfoModel playerInfo : roomData.getPlayerInfoList()) {
            if (playerInfo.getUserInfo().getUserId() == uid) {
                return playerInfo;
            }
        }

        return null;
    }

    /**
     * @param roundInfoModel
     * @return
     */
    public static <T extends BaseRoundInfoModel> long getSongDuration(T roundInfoModel) {
        if (roundInfoModel == null) {
            return 0;
        }
        return roundInfoModel.getSingEndMs() - roundInfoModel.getSingBeginMs();
    }

    public static String getSaveAudioForAiFilePath() {
        String saveAudioForAiFilePath = U.getAppInfoUtils().getFilePathInSubDir("upload", BaseRoomData.AUDIO_FOR_AI_PATH);
        return saveAudioForAiFilePath;
    }

    public static String getSaveMatchingSocreForAiFilePath() {
        String saveAudioForAiFilePath = U.getAppInfoUtils().getFilePathInSubDir("upload", BaseRoomData.MATCHING_SCORE_FOR_AI_PATH);
        return saveAudioForAiFilePath;
    }

    public static <T extends BaseRoundInfoModel> boolean isThisUserRound(T infoModel, int userId) {
        if (infoModel != null && infoModel.getUserID() == userId) {
            return true;
        }
        return false;
    }

    /**
     * 与真实轮次是否一致
     * @param eventSeq
     * @param roomData
     * @return
     */
    public static <T extends BaseRoundInfoModel> boolean isCurrentRound(int eventSeq, BaseRoomData<T> roomData) {
        if (roomData != null) {
            if (roomData.getRealRoundInfo() == null) {
                return false;
            }

            if (roomData.getRealRoundInfo().getRoundSeq() == eventSeq) {
                return true;
            }
        }
        return false;
    }

    public static <T extends BaseRoundInfoModel> T getRoundInfoFromRoundInfoListInGrabMode(BaseRoomData<T> roomData, T roundInfoModel) {
        if (roundInfoModel == null) {
            return null;
        }
        for (T roundInfo : roomData.getRoundInfoModelList()) {
            if (roundInfo.getRoundSeq() == roundInfoModel.getRoundSeq()) {
                roundInfo.tryUpdateRoundInfoModel(roundInfoModel,false);
                return roundInfo;
            }
        }
        return null;
    }

    public static <T extends BaseRoundInfoModel> T getRoundInfoFromRoundInfoListInRankMode(BaseRoomData<T> roomData, T roundInfoModel) {
        if (roundInfoModel == null) {
            return null;
        }

        for (T roundInfo : roomData.getRoundInfoModelList()) {
            if (roundInfo.getRoundSeq() == roundInfoModel.getRoundSeq()) {
                roundInfo.tryUpdateRoundInfoModel(roundInfoModel,false);
                return roundInfo;
            }
        }

        return null;
    }
}
