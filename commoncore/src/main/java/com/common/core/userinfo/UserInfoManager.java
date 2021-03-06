package com.common.core.userinfo;

import android.support.v4.util.LruCache;
import android.text.TextUtils;
import android.util.SparseArray;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.common.callback.Callback;
import com.common.core.myinfo.MyUserInfoManager;
import com.common.core.userinfo.cache.BuddyCache;
import com.common.core.userinfo.event.RelationChangeEvent;
import com.common.core.userinfo.event.RemarkChangeEvent;
import com.common.core.userinfo.model.ClubInfo;
import com.common.core.userinfo.model.NoRemindInfoModel;
import com.common.core.userinfo.model.OnlineModel;
import com.common.core.userinfo.model.UserInfoModel;
import com.common.core.userinfo.remark.RemarkDB;
import com.common.core.userinfo.remark.RemarkLocalApi;
import com.common.log.MyLog;
import com.common.rxretrofit.ApiManager;
import com.common.rxretrofit.ApiMethods;
import com.common.rxretrofit.ApiObserver;
import com.common.rxretrofit.ApiResult;
import com.common.statistics.StatisticsAdapter;
import com.common.utils.U;
import com.module.ModuleServiceManager;
import com.module.common.ICallback;
import com.module.msg.IMsgService;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;

public class UserInfoManager {
    public final String TAG = "UserInfoManager";
    static final String PREF_KEY_FOLLOW_MARKER_WATER = "follow_marker_water";
    static final String PREF_KEY_HAS_PULL_REMARK = "remark_marker_water";

    /**
     * ???????????????
     */
    // ????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????

    public static final int RELATION_BLACKLIST = 5;

    public enum RELATION {
        NO_RELATION(0),           //??????
        FOLLOW(1),       //????????????????????????
        FANS(2),//????????????????????????
        FRIENDS(3);//?????????????????????

        private int value;

        RELATION(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    public enum FROM {
        DB("?????????"), SERVER_PAGE("???????????????"), SERVER_INCREMENT("???????????????");
        private String desc;

        FROM(String desc) {
            this.desc = desc;
        }

        public String getDesc() {
            return desc;
        }
    }

    public static final int RA_UNKNOWN = 0;       //??????
    public static final int RA_BUILD = 1;         //????????????
    public static final int RA_UNBUILD = 2;       //????????????

    public static final int ONLINE_PULL_NONE = 0;
    public static final int ONLINE_PULL_NORMAL = 1;
    public static final int ONLINE_PULL_GAME = 2;

    UserInfoServerApi userInfoServerApi;
    IMsgService msgService;

    /**
     * ??????????????????????????????
     */
    SparseArray<String> mRemarkMap = new SparseArray<>();

    /**
     * ????????????????????????
     */
    LruCache<Integer, OnlineModel> mStatusMap = new LruCache<>(50);


    boolean hasLoadRemarkFromDB = false;

    private UserInfoManager() {
        userInfoServerApi = ApiManager.getInstance().createService(UserInfoServerApi.class);
        msgService = ModuleServiceManager.getInstance().getMsgService();
        initRemark();
    }

    public void initRemark() {
        if (!hasLoadRemarkFromDB) {
            Observable.create(new ObservableOnSubscribe<Object>() {
                @Override
                public void subscribe(ObservableEmitter<Object> emitter) throws Exception {
                    //?????????????????????????????????
                    List<RemarkDB> remarks1 = RemarkLocalApi.getRemarkList();
                    for (RemarkDB remarkDB : remarks1) {
                        if (!TextUtils.isEmpty(remarkDB.getRemarkContent())) {
                            mRemarkMap.put(remarkDB.getUserID().intValue(), remarkDB.getRemarkContent());
                        }
                    }
                    emitter.onComplete();
                }
            }).subscribeOn(Schedulers.io())
                    .subscribe();
            hasLoadRemarkFromDB = true;
        }
        // ??????????????????
        if (!U.getPreferenceUtils().getSettingBoolean(PREF_KEY_HAS_PULL_REMARK, false)) {
            Observable.create(new ObservableOnSubscribe<Object>() {
                @Override
                public void subscribe(ObservableEmitter<Object> emitter) throws Exception {
                    syncRemarkNames();
                    emitter.onComplete();
                }
            }).subscribeOn(U.getThreadUtils().singleThreadPoll())
                    .subscribe();
        }
    }

    public void clearRemark() {
        if (mRemarkMap != null) {
            mRemarkMap.clear();
        }
    }

    private static class UserAccountManagerHolder {
        private static final UserInfoManager INSTANCE = new UserInfoManager();
    }

    public static final UserInfoManager getInstance() {
        return UserAccountManagerHolder.INSTANCE;
    }


    public static abstract class UserInfoListCallback {


        /**
         * ??????????????????????????????
         */
        public abstract void onSuccess(FROM from, int offset, List<UserInfoModel> list);


    }

    public void getClubInfoByUuid(final int uuid, final  boolean isNeedRelation, final ResultCallback<ClubInfo> resultCallback){
        getUserInfoByUuid(uuid, isNeedRelation, new ResultCallback() {
            @Override
            public boolean onGetLocalDB(Object o) {
                if(o instanceof UserInfoModel){
                    resultCallback.onGetLocalDB(((UserInfoModel)o).getClubInfo().getClub());
                    return true;
                }else{
                    return false;
                }

            }

            @Override
            public boolean onGetServer(Object o) {
                if(o instanceof UserInfoModel){
                    resultCallback.onGetLocalDB(((UserInfoModel)o).getClubInfo().getClub());
                    return true;
                }else{
                    return false;
                }
            }
        });
    }

    /**
     * ???????????????????????????
     *
     * @uuid ??????id
     */
    public void getUserInfoByUuid(final int uuid, final boolean isNeedRelation, final ResultCallback resultCallback) {
        if (uuid <= 0) {
            MyLog.w(TAG, "getUserInfoByUuid Illegal parameter");
            return;
        }

        UserInfoModel local = UserInfoLocalApi.getUserInfoByUUid(uuid);
        if (local == null || resultCallback == null || (resultCallback != null && !resultCallback.onGetLocalDB(local))) {
            Observable<ApiResult> apiResultObservable = userInfoServerApi.getUserInfo(uuid);
            ApiMethods.subscribe(apiResultObservable, new ApiObserver<ApiResult>() {
                @Override
                public void process(ApiResult obj) {
                    if (obj.getErrno() == 0) {
                        final UserInfoModel jsonUserInfo = JSON.parseObject(obj.getData().toString(), UserInfoModel.class);
                        if (isNeedRelation) {
                            ApiMethods.subscribe(userInfoServerApi.getRelation(uuid), new ApiObserver<ApiResult>() {
                                @Override
                                public void process(ApiResult obj) {
                                    if (obj.getErrno() == 0) {
                                        boolean isFriend = obj.getData().getBooleanValue("isFriend");
                                        boolean isFollow = obj.getData().getBooleanValue("isFollow");
                                        jsonUserInfo.setFollow(isFollow);
                                        jsonUserInfo.setFriend(isFriend);
                                        if (resultCallback != null) {
                                            resultCallback.onGetServer(jsonUserInfo);
                                        }
                                        //???????????????,????????????
                                        insertUpdateDBAndCache(jsonUserInfo, true);
                                    } else {
                                        if (resultCallback != null) {
                                            resultCallback.onGetServer(jsonUserInfo);
                                        }
                                    }
                                }
                            });
                        } else {
                            //???????????????,????????????
                            insertUpdateDBAndCache(jsonUserInfo, false);
                            if (resultCallback != null) {
                                resultCallback.onGetServer(jsonUserInfo);
                            }
                        }

                    }
                }
            });
        } else {
            if (resultCallback != null) {
                resultCallback.onGetLocalDB(local);
            }
        }
    }

    /**
     * ?????????????????????????????????????????????????????????
     *
     * @uuid ??????id
     */
    public void getUserRelationByUuid(final int uuid, final ResultCallback resultCallback) {
        if (uuid <= 0) {
            MyLog.w(TAG, "getUserInfoByUuid Illegal parameter");
            return;
        }

        final UserInfoModel local = UserInfoLocalApi.getUserInfoByUUid(uuid);
        if (local == null || resultCallback == null || (resultCallback != null && !resultCallback.onGetLocalDB(local))) {
            ApiMethods.subscribe(userInfoServerApi.getRelation(uuid), new ApiObserver<ApiResult>() {
                @Override
                public void process(ApiResult obj) {
                    if (obj.getErrno() == 0) {
                        boolean isFriend = obj.getData().getBooleanValue("isFriend");
                        boolean isFollow = obj.getData().getBooleanValue("isFollow");
                        if (local != null) {
                            local.setFollow(isFollow);
                            local.setFriend(isFriend);
                            if (resultCallback != null) {
                                resultCallback.onGetServer(local);
                            }
                        } else {
                            UserInfoModel userInfoModel = new UserInfoModel();
                            userInfoModel.setUserId(uuid);
                            userInfoModel.setFollow(isFollow);
                            userInfoModel.setFriend(isFriend);
                            if (resultCallback != null) {
                                resultCallback.onGetServer(userInfoModel);
                            }
                        }
                    } else {
                        if (resultCallback != null) {
                            resultCallback.onGetLocalDB(local);
                        }
                    }
                }
            });
        } else {
            if (resultCallback != null) {
                resultCallback.onGetLocalDB(local);
            }
        }
    }

    public void checkIsFans(int from, int to, final ResponseCallBack<Boolean> responseCallBack) {
        if (from <= 0 || to <= 0) {
            MyLog.w(TAG, "checkIsFans" + " from=" + from + " to=" + to + " responseCallBack=" + responseCallBack);
            return;
        }
        ApiMethods.subscribe(userInfoServerApi.checkIsFans(from, to), new ApiObserver<ApiResult>() {
            @Override
            public void process(ApiResult result) {
                if (result.getErrno() == 0) {
                    boolean isFans = result.getData().getBooleanValue("isFans");
                    if (responseCallBack != null) {
                        responseCallBack.onServerSucess(isFans);
                    }
                } else {
                    if (responseCallBack != null) {
                        responseCallBack.onServerFailed();
                    }
                }
            }
        });
    }

    public void addToBlacklist(final int userID, final ResponseCallBack responseCallBack) {
        if (userID <= 0 || userID <= 0) {
            MyLog.w(TAG, "addToBlacklist" + " userID=" + userID);
            return;
        }

        HashMap<String, Object> map = new HashMap<>();
        map.put("userID", userID);
        RequestBody body = RequestBody.create(MediaType.parse(ApiManager.APPLICATION_JSON), JSON.toJSONString(map));
        ApiMethods.subscribe(userInfoServerApi.addToBlackList(body), new ApiObserver<ApiResult>() {
            @Override
            public void process(ApiResult result) {
                if (result.getErrno() == 0) {
                    boolean isFriend = result.getData().getBooleanValue("isFriend");
                    boolean isFollow = result.getData().getBooleanValue("isFollow");
                    boolean isSPFollow = result.getData().getBooleanValue("isSPFollow");
                    EventBus.getDefault().post(new RelationChangeEvent(RelationChangeEvent.BLACK_LIST_TYPE, userID, isFriend, isFollow, isSPFollow));
                    // TODO: 2019-07-03 ??????????????????????????????????????????????????????????????????
                    msgService.addToBlacklist(String.valueOf(userID), new ICallback() {
                        @Override
                        public void onSucess(Object obj) {
                            if (responseCallBack != null) {
                                responseCallBack.onServerSucess(obj);
                            }
                        }

                        @Override
                        public void onFailed(Object obj, int errcode, String message) {
                            MyLog.w(TAG, "onFailed" + " obj=" + obj + " errcode=" + errcode + " message=" + message);
                            if (responseCallBack != null) {
                                responseCallBack.onServerFailed();
                            }
                        }
                    });
                }
            }
        });
    }

    public void removeBlackList(final int userID, final ResponseCallBack responseCallBack) {
        if (userID <= 0 || userID <= 0) {
            MyLog.w(TAG, "removeBlackList" + " userID=" + userID + " responseCallBack=" + responseCallBack);
            return;
        }
        HashMap<String, Object> map = new HashMap<>();
        map.put("userID", userID);
        RequestBody body = RequestBody.create(MediaType.parse(ApiManager.APPLICATION_JSON), JSON.toJSONString(map));
        ApiMethods.subscribe(userInfoServerApi.delBlackList(body), new ApiObserver<ApiResult>() {
            @Override
            public void process(ApiResult result) {
                if (result.getErrno() == 0) {
                    boolean isFriend = result.getData().getBooleanValue("isFriend");
                    boolean isFollow = result.getData().getBooleanValue("isFollow");
                    boolean isSPFollow = result.getData().getBooleanValue("isSPFollow");
                    EventBus.getDefault().post(new RelationChangeEvent(RelationChangeEvent.BLACK_LIST_TYPE, userID, isFriend, isFollow, isSPFollow));
                    msgService.removeFromBlacklist(String.valueOf(userID), new ICallback() {
                        @Override
                        public void onSucess(Object obj) {
                            if (responseCallBack != null) {
                                responseCallBack.onServerSucess(obj);
                            }
                        }

                        @Override
                        public void onFailed(Object obj, int errcode, String message) {
                            if (responseCallBack != null) {
                                responseCallBack.onServerFailed();
                            }
                        }
                    });
                }
            }
        });
    }

    public void getBlacklistStatus(final int userID, final ResponseCallBack responseCallBack) {
        if (userID <= 0 || userID <= 0) {
            MyLog.w(TAG, "getBlacklistStatus" + " userID=" + userID + " responseCallBack=" + responseCallBack);
            return;
        }
        msgService.getBlacklistStatus(String.valueOf(userID), new ICallback() {
            @Override
            public void onSucess(Object obj) {
                if (responseCallBack != null) {
                    responseCallBack.onServerSucess(obj);
                }
            }

            @Override
            public void onFailed(Object obj, int errcode, String message) {
                if (responseCallBack != null) {
                    responseCallBack.onServerFailed();
                }
            }
        });
    }

    public void insertUpdateDBAndCache(final UserInfoModel userInfoModel, boolean hasRelation) {
        Observable.create(new ObservableOnSubscribe<UserInfoModel>() {
            @Override
            public void subscribe(ObservableEmitter<UserInfoModel> emitter) throws Exception {
                // ???????????????
                if (hasRelation) {
                    UserInfoLocalApi.insertOrUpdate(userInfoModel);
                } else {
                    UserInfoModel userInfoModelDB = UserInfoLocalApi.getUserInfoByUUid(userInfoModel.getUserId());
                    if (userInfoModelDB != null) {
                        userInfoModel.setFollow(userInfoModelDB.isFollow());
                        userInfoModel.setFriend(userInfoModelDB.isFriend());
                        userInfoModel.setSPFollow(userInfoModelDB.isSPFollow());
                    }
                    UserInfoLocalApi.insertOrUpdate(userInfoModel);
                }
                BuddyCache.getInstance().putBuddy(new BuddyCache.BuddyCacheEntry(userInfoModel));

                if (userInfoModel != null) {
                    emitter.onNext(userInfoModel);
                }
                emitter.onComplete();
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    public void mateRelation(final int userId, final int action, final boolean isOldFriend) {
        mateRelation(userId, action, isOldFriend, null);
    }

    /**
     * ????????????(????????????id)
     *
     * @param userId
     * @param action
     */
    public void mateRelation(final int userId, final int action, final boolean isOldFriend, final ResponseCallBack responseCallBack) {
        mateRelation(userId, action, isOldFriend, 0, responseCallBack);
    }


    /**
     * ????????????(?????????id)
     *
     * @param userId
     * @param action
     */
    public void mateRelation(final int userId, final int action, final boolean isOldFriend, final int roomID, final ResponseCallBack responseCallBack) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("toUserID", userId);
        map.put("action", action);
        if (roomID != 0) {
            map.put("roomID", roomID);
        }

        RequestBody body = RequestBody.create(MediaType.parse(ApiManager.APPLICATION_JSON), JSON.toJSONString(map));
        Observable<ApiResult> apiResultObservable = userInfoServerApi.mateRelation(body);
        ApiMethods.subscribe(apiResultObservable, new ApiObserver<ApiResult>() {
            @Override
            public void process(ApiResult obj) {
                if (obj.getErrno() == 0) {
                    final boolean isFriend = obj.getData().getBooleanValue("isFriend");
                    final boolean isFollow = obj.getData().getBooleanValue("isFollow");
                    final boolean isSPFollow = obj.getData().getBooleanValue("isSPFollow");
                    if (responseCallBack != null) {
                        responseCallBack.onServerSucess(isFriend);
                    }
                    if (action == RA_BUILD) {
                        StatisticsAdapter.recordCountEvent("social", "follow", null);
                        EventBus.getDefault().post(new RelationChangeEvent(RelationChangeEvent.FOLLOW_TYPE, userId, isFriend, isFollow, isSPFollow));
                    } else if (action == RA_UNBUILD) {
                        EventBus.getDefault().post(new RelationChangeEvent(RelationChangeEvent.UNFOLLOW_TYPE, userId, isFriend, isFollow, isSPFollow));
                    }
                } else {
                    MyLog.w(TAG, "process" + " obj=" + obj);
                    U.getToastUtil().showShort(obj.getErrmsg());
                }
            }
        });
    }

    /**
     * ????????????
     *
     * @param userId
     */
    public void beFriend(int userId, final ResponseCallBack responseCallBack) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("toUserID", userId);

        RequestBody body = RequestBody.create(MediaType.parse(ApiManager.APPLICATION_JSON), JSON.toJSONString(map));
        Observable<ApiResult> apiResultObservable = userInfoServerApi.beFriend(body);
        ApiMethods.subscribe(apiResultObservable, new ApiObserver<ApiResult>() {
            @Override
            public void process(ApiResult obj) {
                if (obj.getErrno() == 0) {
                    if (responseCallBack != null) {
                        responseCallBack.onServerSucess(null);
                    } else {
                        U.getToastUtil().showShort("??????????????????");
                    }
                } else {
                    if (responseCallBack != null) {
                        responseCallBack.onServerFailed();
                    } else {
                        U.getToastUtil().showShort("" + obj.getErrmsg());
                    }
                }
            }
        });
    }

    public void getMyFollow(final int pullOnlineStatus, final boolean isFromFollow, final UserInfoListCallback userInfoListCallback, final boolean needIntimacy) {
        getMyFollow(pullOnlineStatus, isFromFollow, 0, 0, userInfoListCallback, needIntimacy);
    }

    /**
     * ??????????????????
     * ??????????????????????????????
     */
    /**
     * @param pullOnlineStatus     ????????????????????????
     * @param isFromFollow         ????????????????????????
     * @param userInfoListCallback
     * @param needIntimacy         ???????????????
     */
    public void getMyFollow(final int pullOnlineStatus, final boolean isFromFollow, int roomID, int gameModel, final UserInfoListCallback userInfoListCallback, final boolean needIntimacy) {
        Observable.create(new ObservableOnSubscribe<List<UserInfoModel>>() {
            @Override
            public void subscribe(ObservableEmitter<List<UserInfoModel>> emitter) {
                //?????????????????????????????????
                LinkedHashSet<UserInfoModel> resutlSet = new LinkedHashSet();
                List<UserInfoModel> userInfoModels = UserInfoLocalApi.getFollowUserInfoList();
                resutlSet.addAll(userInfoModels);

                long followMarkerWater = U.getPreferenceUtils().getSettingLong(PREF_KEY_FOLLOW_MARKER_WATER, -1);
                if (followMarkerWater == -1) {
                    // ???????????????
                    int offset = 0;
                    int baohu = 0;
                    while (baohu < 100) {
                        baohu++;
                        Call<ApiResult> call = userInfoServerApi.listFollowsByPage(offset, 50);
                        try {
                            Response<ApiResult> response = call.execute();
                            ApiResult obj = response.body();
                            if (obj == null || obj.getData() == null || obj.getErrno() != 0) {
                                break;
                            }
                            if (offset == 0) {
                                // offset???0???????????????
                                if (obj.getData() != null) {
                                    followMarkerWater = obj.getData().getLongValue("lastIndexID");
                                }
                                // ??????????????????
                                syncRemarkNames();
                            }
                            offset = obj.getData().getIntValue("offset");
                            List<UserInfoModel> userInfoModels2 = JSON.parseArray(obj.getData().getString("contacts"), UserInfoModel.class);
                            if (userInfoModels2 == null || userInfoModels2.isEmpty()) {
                                // ???????????????
                                U.getPreferenceUtils().setSettingLong(PREF_KEY_FOLLOW_MARKER_WATER, followMarkerWater);
                                break;
                            } else {
                                // ???????????????
                                UserInfoLocalApi.insertOrUpdate(userInfoModels2);
                                // ?????????????????????
                                resutlSet.addAll(userInfoModels2);
                            }
                        } catch (IOException e) {
                            MyLog.e(e);
                            break;
                        }
                    }
                } else {
                    // ????????????
                    Call<ApiResult> call = userInfoServerApi.listFollowsByIndexId((int) followMarkerWater);
                    try {
                        Response<ApiResult> response = call.execute();
                        ApiResult obj = response.body();
                        if (obj != null && obj.getData() != null && obj.getErrno() == 0) {
                            boolean hasUpdate = false;
                            // ????????????
                            List<UserInfoModel> userInfoModels2 = JSON.parseArray(obj.getData().getString("adds"), UserInfoModel.class);
                            if (userInfoModels2 != null && !userInfoModels2.isEmpty()) {
                                UserInfoLocalApi.insertOrUpdate(userInfoModels2);
                                // ?????????
                                resutlSet.addAll(userInfoModels2);
                                hasUpdate = true;
                            }
                            // ????????????
                            List<UserInfoModel> userInfoModels3 = JSON.parseArray(obj.getData().getString("updates"), UserInfoModel.class);
                            if (userInfoModels3 != null && !userInfoModels3.isEmpty()) {
                                UserInfoLocalApi.insertOrUpdate(userInfoModels3);
                                // ?????????
                                for (UserInfoModel model : userInfoModels3) {
                                    resutlSet.remove(new UserInfoModel(model.getUserId()));
                                }
                                resutlSet.addAll(userInfoModels3);
                                hasUpdate = true;
                            }
                            // ????????????
                            List<Integer> delIds = JSON.parseArray(obj.getData().getString("dels"), Integer.class);
                            if (delIds != null && !delIds.isEmpty()) {
                                //????????????
                                UserInfoLocalApi.deleUserInfoByUUids(delIds);
                                for (Integer userId : delIds) {
                                    resutlSet.remove(new UserInfoModel(userId));
                                }
                                hasUpdate = true;
                            }
                            followMarkerWater = obj.getData().getLongValue("lastIndexID");
                            U.getPreferenceUtils().setSettingLong(PREF_KEY_FOLLOW_MARKER_WATER, followMarkerWater);
                        }
                    } catch (IOException e) {
                        MyLog.e(e);
                    }
                }
                List<UserInfoModel> resultList = new ArrayList<>();
                for (UserInfoModel userInfoModel : resutlSet) {
                    resultList.add(userInfoModel);
                }
                if (resultList.size() > 0) {
                    mStatusMap.resize(resultList.size());
                }

                if (pullOnlineStatus == ONLINE_PULL_GAME) {
                    if (resultList.size() > 100) {
                        // ??????????????????100 ??? ???????????????????????????????????????????????????????????????
                        if (userInfoListCallback != null) {
                            userInfoListCallback.onSuccess(FROM.DB, resultList.size(), resultList);
                        }
                    }
                    fillUserOnlineStatus(resultList, true, false, isFromFollow, roomID, gameModel);
                } else if (pullOnlineStatus == ONLINE_PULL_NORMAL) {
                    if (resultList.size() > 100) {
                        // ??????????????????100 ??? ???????????????????????????????????????????????????????????????
                        if (userInfoListCallback != null) {
                            userInfoListCallback.onSuccess(FROM.DB, resultList.size(), resultList);
                        }
                    }
                    fillUserOnlineStatus(resultList, false, needIntimacy, isFromFollow, roomID, gameModel);
                } else {

                }
                if (userInfoListCallback != null) {
                    userInfoListCallback.onSuccess(FROM.DB, resultList.size(), resultList);
                }
                emitter.onComplete();
            }
        })
                .subscribeOn(U.getThreadUtils().singleThreadPoll())
                .subscribe(new Consumer<List<UserInfoModel>>() {
                    @Override
                    public void accept(List<UserInfoModel> userInfoModels) throws Exception {

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                });

    }

    /**
     * ????????????
     *
     * @param offset
     * @param cnt
     * @param userInfoListCallback
     */
    public void getFans(final int offset, final int cnt, final UserInfoListCallback userInfoListCallback) {
        ApiMethods.subscribe(userInfoServerApi.listFansByPage(offset, cnt), new ApiObserver<ApiResult>() {
            @Override
            public void process(ApiResult obj) {
                if (obj.getErrno() == 0) {
                    List<UserInfoModel> list = JSON.parseArray(obj.getData().getString("fans"), UserInfoModel.class);
                    // todo ????????????????????????
//                    List<UserInfoModel> friendList = new ArrayList<>();
//                    if (list != null) {
//                        for (UserInfoModel userInfoModel : list) {
//                            if (userInfoModel.isFriend()) {
//                                friendList.add(userInfoModel);
//                            }
//                        }
//                    }
//                    if (!friendList.isEmpty()) {
//                        // ???????????????????????????
//                        UserInfoLocalApi.insertOrUpdate(friendList);
//                    }
                    int newOffset = obj.getData().getIntValue("offset");
                    if (userInfoListCallback != null) {
                        userInfoListCallback.onSuccess(FROM.SERVER_PAGE, newOffset, list);
                    }
                }
            }
        });

    }

    public void getMyFriends(final int pullOnlineStatus, final UserInfoListCallback userInfoListCallback) {
        getMyFriends(pullOnlineStatus, 0, 0, true, userInfoListCallback);
    }

    /**
     * ??????????????????
     *
     * @param pullOnlineStatus     ????????????????????????
     * @param roomID               ??????ID
     * @param gameModel            ????????????
     * @param needIntimacy         ?????????????????????
     * @param userInfoListCallback
     */
    public void getMyFriends(final int pullOnlineStatus, int roomID, int gameModel, boolean needIntimacy, final UserInfoListCallback userInfoListCallback) {
        //?????????????????????????????????
        getMyFollow(ONLINE_PULL_NONE, false, roomID, gameModel, new UserInfoListCallback() {
            @Override
            public void onSuccess(FROM from, int offset, List<UserInfoModel> list) {
                List<UserInfoModel> resultList = new ArrayList<>();
                for (UserInfoModel userInfoModel : list) {
                    if (userInfoModel.isFriend()) {
                        resultList.add(userInfoModel);
                    }
                }
                if (pullOnlineStatus == ONLINE_PULL_GAME) {
                    if (resultList.size() > 100) {
                        // ??????????????????100 ??? ???????????????????????????????????????????????????????????????
                        if (userInfoListCallback != null) {
                            userInfoListCallback.onSuccess(FROM.DB, resultList.size(), resultList);
                        }
                    }
                    fillUserOnlineStatus(resultList, true, false, roomID, gameModel);
                } else if (pullOnlineStatus == ONLINE_PULL_NORMAL) {
                    if (resultList.size() > 100) {
                        // ??????????????????100 ??? ???????????????????????????????????????????????????????????????
                        if (userInfoListCallback != null) {
                            userInfoListCallback.onSuccess(FROM.DB, resultList.size(), resultList);
                        }
                    }
                    fillUserOnlineStatus(resultList, false, true, false, roomID, gameModel);
                } else {

                }
                if (userInfoListCallback != null) {
                    userInfoListCallback.onSuccess(from, resultList.size(), resultList);
                }
            }
        }, needIntimacy);
    }

    /**
     * ??????????????????
     *
     * @param userInfoListCallback
     */
    public void searchFollow(final String key, final UserInfoListCallback userInfoListCallback) {
        Observable.create(new ObservableOnSubscribe<List<UserInfoModel>>() {
            @Override
            public void subscribe(ObservableEmitter<List<UserInfoModel>> emitter) throws Exception {
                List<UserInfoModel> userInfoModels = UserInfoLocalApi.searchFollow(key);
                emitter.onNext(userInfoModels);
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<UserInfoModel>>() {
                    @Override
                    public void accept(List<UserInfoModel> l) throws Exception {
                        if (userInfoListCallback != null) {
                            userInfoListCallback.onSuccess(FROM.DB, l.size(), l);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                });
    }

    /**
     * ??????????????????
     *
     * @param key
     * @param userInfoListCallback
     */
    public void searchFriends(final String key, final UserInfoListCallback userInfoListCallback) {
        Observable.create(new ObservableOnSubscribe<List<UserInfoModel>>() {
            @Override
            public void subscribe(ObservableEmitter<List<UserInfoModel>> emitter) throws Exception {
                List<UserInfoModel> userInfoModels = UserInfoLocalApi.searchFriends(key);
                emitter.onNext(userInfoModels);
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<UserInfoModel>>() {
                    @Override
                    public void accept(List<UserInfoModel> l) throws Exception {
                        if (userInfoListCallback != null) {
                            userInfoListCallback.onSuccess(FROM.DB, l.size(), l);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                });
    }


    /**
     * ???????????????
     *
     * @param uid
     * @param defualtName ??????????????????????????? name
     * @return
     */
    public String getRemarkName(int uid, String defualtName) {
        // ?????????????????????
        String remark = mRemarkMap.get(uid);
        if (TextUtils.isEmpty(remark)) {
            return defualtName;
        } else {
            return remark;
        }
    }


    /**
     * ???????????????
     */
    public void syncRemarkNames() {
        if (U.getPreferenceUtils().getSettingBoolean(PREF_KEY_HAS_PULL_REMARK, false)) {
            return;
        }
        /**
         * ??????????????????
         * ??????????????????????????????
         */
        // ???????????????
        int offset = 0;
        int baohu = 0;
        while (baohu < 20) {
            baohu++;
            Call<ApiResult> call = userInfoServerApi.listRemarkByPage(offset, 50);
            try {
                Response<ApiResult> response = call.execute();
                ApiResult obj = response.body();
                if (obj == null || obj.getData() == null || obj.getErrno() != 0) {
                    break;
                }
                offset = obj.getData().getIntValue("offset");
                List<RemarkDB> remarks2 = JSON.parseArray(obj.getData().getString("info"), RemarkDB.class);
                if (remarks2 == null || remarks2.isEmpty()) {
                    U.getPreferenceUtils().setSettingBoolean(PREF_KEY_HAS_PULL_REMARK, true);
                    // ???????????????
                    break;
                } else {
                    // ???????????????
                    RemarkLocalApi.insertOrUpdate(remarks2);
                    // ?????????????????????

                    // ????????????
                    for (RemarkDB remarkDB : remarks2) {
                        if (!TextUtils.isEmpty(remarkDB.getRemarkContent())) {
                            mRemarkMap.put(remarkDB.getUserID().intValue(), remarkDB.getRemarkContent());
                        }
                    }
                }
            } catch (IOException e) {
                MyLog.e(e);
                break;
            }
        }
    }


    /**
     * ???????????????
     *
     * @param remark
     * @param userId
     */
    public void updateRemark(String remark, int userId) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("remarkContent", remark);
        map.put("remarkUserID", userId);
        RequestBody body = RequestBody.create(MediaType.parse(ApiManager.APPLICATION_JSON), JSON.toJSONString(map));
        // ????????????
        ApiMethods.subscribe(userInfoServerApi.writeUserRemark(body), null);
        // ?????????????????????????????????????????????
        if (TextUtils.isEmpty(remark)) {
            mRemarkMap.remove(userId);
            //?????????????????????
            RemarkLocalApi.delete(userId);
            //???????????????
            UserInfoLocalApi.updateRemark(userId, "");
            EventBus.getDefault().post(new RemarkChangeEvent(userId, null));
        } else {
            mRemarkMap.put(userId, remark);
            //?????????????????????
            RemarkLocalApi.insertOrUpdate(userId, remark);
            //???????????????
            UserInfoLocalApi.updateRemark(userId, remark);
            EventBus.getDefault().post(new RemarkChangeEvent(userId, remark));
        }

    }

    public void fillUserOnlineStatus(final List<UserInfoModel> list, final boolean pullGameStatus, boolean isFromFollow, int roomID, int gameModel) {
        fillUserOnlineStatus(list, pullGameStatus, false, isFromFollow, roomID, gameModel);
    }

    public void fillUserOnlineStatus(final List<UserInfoModel> list, final boolean pullGameStatus, boolean needIntimacy, boolean isFromFollow, int roomID, int gameModel) {
        final HashSet<Integer> idSets = new HashSet();
        for (UserInfoModel userInfoModel : list) {
            OnlineModel onlineModel = mStatusMap.get(userInfoModel.getUserId());
            if (onlineModel == null || (needIntimacy && !onlineModel.hasQinMiCnt())) {
                idSets.add(userInfoModel.getUserId());
            } else {
                long t = System.currentTimeMillis() - onlineModel.getRecordTs();
                if (onlineModel.isOnline()) {
                    if (Math.abs(t) < 30 * 1000) {
                        // ???????????????????????????????????????id????????????
                        setUserOnlineStatus(userInfoModel, onlineModel, pullGameStatus);
                    } else {
                        idSets.add(userInfoModel.getUserId());
                    }
                } else {
                    //?????????????????????
                    if (System.currentTimeMillis() - onlineModel.getOfflineTime() > 15 * 24 * 3600 * 1000) {
                        // ??????????????????15??????????????????????????????????????????????????????
                        if (Math.abs(t) < 5 * 60 * 1000) {
                            // ???????????????????????????????????????id????????????
                            setUserOnlineStatus(userInfoModel, onlineModel, pullGameStatus);
                        } else {
                            idSets.add(userInfoModel.getUserId());
                        }
                    } else {
                        if (Math.abs(t) < 60 * 1000) {
                            // ???????????????????????????????????????id????????????
                            setUserOnlineStatus(userInfoModel, onlineModel, pullGameStatus);
                        } else {
                            idSets.add(userInfoModel.getUserId());
                        }
                    }
                }
            }
        }
        if (!idSets.isEmpty()) {
            List<List<Integer>> queryList = new ArrayList<>();

            List<Integer> lll = new ArrayList<>();
            int index = 0;
            for (Integer id : idSets) {
                if (index % 50 == 0) {
                    if (!lll.isEmpty()) {
                        queryList.add(lll);
                        lll = new ArrayList<>();
                    }
                }
                lll.add(id);
                index++;
            }
            if (!lll.isEmpty()) {
                queryList.add(lll);
            }
            final HashMap<Integer, OnlineModel> onlineMap = new HashMap<>();
            for (List<Integer> qqq : queryList) {
                Observable<HashMap<Integer, OnlineModel>> observable = null;
                if (pullGameStatus) {
                    observable = checkUserGameStatusByIds(qqq, roomID, gameModel);
                } else {
                    if (needIntimacy) {
                        observable = checkUserOnlineStatusByIdsWithIntimacy(qqq);
                    } else {
                        observable = checkUserOnlineStatusByIds(qqq);
                    }
                }

                observable.map(new Function<HashMap<Integer, OnlineModel>, List<UserInfoModel>>() {
                    @Override
                    public List<UserInfoModel> apply(HashMap<Integer, OnlineModel> map) {
                        onlineMap.putAll(map);
                        return list;
                    }
                })
                        .subscribe(new Consumer<List<UserInfoModel>>() {
                            @Override
                            public void accept(List<UserInfoModel> userInfoModels) throws Exception {

                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {

                            }
                        });
            }
            for (UserInfoModel userInfoModel : list) {
                if (idSets.contains(userInfoModel.getUserId())) {
                    OnlineModel onlineModel = onlineMap.get(userInfoModel.getUserId());
                    setUserOnlineStatus(userInfoModel, onlineModel, pullGameStatus);
                }
            }
        }
        Collections.sort(list, new Comparator<UserInfoModel>() {
            @Override
            public int compare(UserInfoModel u1, UserInfoModel u2) {
                //MyLog.d(TAG, "compare" + " u1=" + u1 + " u2=" + u2);
                // todo ????????????????????? ???????????????????????????????????????
                if (needIntimacy) {
                    // ????????????????????????
                    if (isFromFollow) {
                        if (u1.isSPFollow() == u2.isSPFollow()) {
                            // ???????????????????????? ??? ???????????????
                            if (u1.getStatus() == UserInfoModel.EF_OFFLINE && u2.getStatus() == UserInfoModel.EF_OFFLINE) {
                                // ??????????????????
                                // ?????????????????????
                                if (u1.getStatusTs() > u2.getStatusTs()) {
                                    return -1;
                                } else if (u1.getStatusTs() < u2.getStatusTs()) {
                                    return 1;
                                } else {
                                    return 0;
                                }
                            }
                            if (u1.getStatus() >= UserInfoModel.EF_ONLINE && u2.getStatus() >= UserInfoModel.EF_ONLINE) {
                                // ??????????????????
                                // ?????????????????????
                                if (u1.getStatusTs() > u2.getStatusTs()) {
                                    return -1;
                                } else if (u1.getStatusTs() < u2.getStatusTs()) {
                                    return 1;
                                } else {
                                    return 0;
                                }
                            }
                            int r = u2.getStatus() - u1.getStatus();
                            return r;
                        } else if (u1.isSPFollow()) {
                            return -1;
                        } else {
                            return 1;
                        }
                    } else {
                        // ?????? ?????? ??????????????? ????????????????????????
                        if (u1.getIntimacy() > u2.getIntimacy()) {
                            return -1;
                        } else if (u1.getIntimacy() < u2.getIntimacy()) {
                            return 1;
                        } else {
                            if (u1.getStatus() == UserInfoModel.EF_OFFLINE && u2.getStatus() == UserInfoModel.EF_OFFLINE) {
                                // ??????????????????
                                if (u1.getStatusTs() > u2.getStatusTs()) {
                                    return -1;
                                } else if (u1.getStatusTs() < u2.getStatusTs()) {
                                    return 1;
                                } else {
                                    return 0;
                                }
                            }

                            if (u1.getStatus() >= UserInfoModel.EF_ONLINE && u2.getStatus() >= UserInfoModel.EF_ONLINE) {
                                // ??????????????????

                                if (u1.getStatusTs() > u2.getStatusTs()) {
                                    return -1;
                                } else if (u1.getStatusTs() < u2.getStatusTs()) {
                                    return 1;
                                } else {
                                    return 0;
                                }

                            }
                            int r = u2.getStatus() - u1.getStatus();
                            return r;
                        }
                    }
                } else {
                    // ?????????????????????,?????????????????????
                    if (u1.getStatus() == UserInfoModel.EF_OFFLINE && u2.getStatus() == UserInfoModel.EF_OFFLINE) {
                        // ??????????????????
                        if (u1.getStatusTs() > u2.getStatusTs()) {
                            return -1;
                        } else if (u1.getStatusTs() < u2.getStatusTs()) {
                            return 1;
                        } else {
                            return 0;
                        }
                    }

                    if (u1.getStatus() >= UserInfoModel.EF_ONLINE && u2.getStatus() >= UserInfoModel.EF_ONLINE) {
                        // ??????????????????

                        if (u1.getStatusTs() > u2.getStatusTs()) {
                            return -1;
                        } else if (u1.getStatusTs() < u2.getStatusTs()) {
                            return 1;
                        } else {
                            return 0;
                        }

                    }
                    int r = u2.getStatus() - u1.getStatus();
                    return r;
                }
            }
        });
    }

    private void setUserOnlineStatus(UserInfoModel userInfoModel, OnlineModel onlineModel, boolean pullGameStatus) {
        // ???????????????????????????????????????id????????????
        if (onlineModel != null && onlineModel.isOnline()) {
            userInfoModel.setStatus(UserInfoModel.EF_ONLINE);
            userInfoModel.setStatusTs(onlineModel.getOnlineTime());
            if (pullGameStatus) {
                if (onlineModel.getBusy()) {
                    userInfoModel.setStatus(UserInfoModel.EF_ONLINE_BUSY);
                    userInfoModel.setStatusDesc("?????????");
                } else if (onlineModel.getInRoom()) {
                    userInfoModel.setStatus(UserInfoModel.EF_ONLiNE_JOINED);
                    userInfoModel.setStatusDesc("???????????????");
                } else {
                    userInfoModel.setStatusDesc("??????");
                }
            } else {
                userInfoModel.setStatusDesc("??????");
            }
        } else {
            userInfoModel.setStatus(UserInfoModel.EF_OFFLINE);
            userInfoModel.setStatusTs(onlineModel.getOfflineTime());
            String timeDesc = "";
            if (onlineModel.getOfflineTime() > 0) {
                timeDesc = U.getDateTimeUtils().formatHumanableDateForSkr(onlineModel.getOfflineTime(), System.currentTimeMillis());
            }
            // ??????
            if (TextUtils.isEmpty(timeDesc)) {
                userInfoModel.setStatusDesc("??????");
            } else {
                userInfoModel.setStatusDesc(timeDesc + "??????");
            }
        }

        if (onlineModel.hasQinMiCnt()) {
            userInfoModel.setIntimacy(onlineModel.getQinMiCntTotal());
        }
    }

    /**
     * ??????????????????????????????
     *
     * @param list
     * @return
     */
    public Observable<HashMap<Integer, OnlineModel>> checkUserOnlineStatusByIds(Collection<Integer> list) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("userIDs", list);
        RequestBody body = RequestBody.create(MediaType.parse(ApiManager.APPLICATION_JSON), JSON.toJSONString(map));

        return userInfoServerApi.checkUserOnlineStatus(body)
                .map(new Function<ApiResult, HashMap<Integer, OnlineModel>>() {
                    @Override
                    public HashMap<Integer, OnlineModel> apply(ApiResult obj) {
                        if (obj != null && obj.getData() != null && obj.getErrno() == 0) {
                            HashMap<Integer, OnlineModel> hashSet = new HashMap<>();

                            List<OnlineModel> onlineModelList = JSON.parseArray(obj.getData().getString("userOnlineList"), OnlineModel.class);
                            if (onlineModelList != null) {
                                for (OnlineModel onlineModel : onlineModelList) {
                                    onlineModel.setOnline(true);
                                    onlineModel.setRecordTs(System.currentTimeMillis());
                                    hashSet.put(onlineModel.getUserID(), onlineModel);
                                    mStatusMap.put(onlineModel.getUserID(), onlineModel);
                                }
                            }
                            List<OnlineModel> offlineModelList = JSON.parseArray(obj.getData().getString("userOfflineList"), OnlineModel.class);
                            if (offlineModelList != null) {
                                for (OnlineModel offlineModel : offlineModelList) {
                                    offlineModel.setOnline(false);
                                    offlineModel.setRecordTs(System.currentTimeMillis());
                                    hashSet.put(offlineModel.getUserID(), offlineModel);
                                    mStatusMap.put(offlineModel.getUserID(), offlineModel);
                                }
                            }
                            return hashSet;
                        }
                        return null;
                    }
                });
    }

    /**
     * ??????????????????????????????,???????????????
     *
     * @param list
     * @return
     */
    public Observable<HashMap<Integer, OnlineModel>> checkUserOnlineStatusByIdsWithIntimacy(List<Integer> list) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("userIDs", list);
        RequestBody body = RequestBody.create(MediaType.parse(ApiManager.APPLICATION_JSON), JSON.toJSONString(map));
        return userInfoServerApi.checkUserOnlineStatusWithIntimacy(body)
                .map(new Function<ApiResult, HashMap<Integer, OnlineModel>>() {
                    @Override
                    public HashMap<Integer, OnlineModel> apply(ApiResult obj) {
                        if (obj != null && obj.getData() != null && obj.getErrno() == 0) {
                            HashMap<Integer, OnlineModel> hashSet = new HashMap<>();
                            List<OnlineModel> onlineModelList = JSON.parseArray(obj.getData().getString("userStatus"), OnlineModel.class);

                            if (onlineModelList != null) {
                                for (OnlineModel onlineModel : onlineModelList) {
                                    if (onlineModel.getOnlineTime() > 0) {
                                        onlineModel.setOnline(true);
                                    } else {
                                        onlineModel.setOnline(false);
                                    }
                                    onlineModel.setRecordTs(System.currentTimeMillis());
                                    hashSet.put(onlineModel.getUserID(), onlineModel);
                                    mStatusMap.put(onlineModel.getUserID(), onlineModel);
                                }
                            }

                            return hashSet;
                        }
                        return null;
                    }
                });
    }


    /**
     * @param list
     * @param roomID
     * @param gameModel ????????????V2??????: 5   ???K???MicMode: 8  ????????????: 9
     * @return
     */
    public Observable<HashMap<Integer, OnlineModel>> checkUserGameStatusByIds(Collection<Integer> list, int roomID, int gameModel) {

        HashMap<String, Object> map = new HashMap<>();
        map.put("userIDs", list);
        map.put("roomID", roomID);
        map.put("mode", gameModel);
        RequestBody body = RequestBody.create(MediaType.parse(ApiManager.APPLICATION_JSON), JSON.toJSONString(map));

        return userInfoServerApi.checkUserGameStatus(body)
                .map(new Function<ApiResult, HashMap<Integer, OnlineModel>>() {
                    @Override
                    public HashMap<Integer, OnlineModel> apply(ApiResult obj) {
                        if (obj != null && obj.getData() != null && obj.getErrno() == 0) {
                            HashMap<Integer, OnlineModel> hashSet = new HashMap<>();
                            {
                                List<Integer> busyStatusUserList = JSON.parseArray(obj.getData().getString("busyStatusUserList"), Integer.class);
                                if (busyStatusUserList != null) {
                                    for (int uid : busyStatusUserList) {
                                        OnlineModel onlineModel = new OnlineModel();
                                        onlineModel.setUserID(uid);
                                        onlineModel.setOnline(true);
                                        onlineModel.setBusy(true);
                                        onlineModel.setRecordTs(System.currentTimeMillis());
                                        hashSet.put(onlineModel.getUserID(), onlineModel);
                                        mStatusMap.put(onlineModel.getUserID(), onlineModel);
                                    }
                                }
                            }
                            {
                                List<Integer> inRoomStatusUserList = JSON.parseArray(obj.getData().getString("inRoomStatusUserList"), Integer.class);
                                if (inRoomStatusUserList != null) {
                                    for (int uid : inRoomStatusUserList) {
                                        OnlineModel onlineModel = new OnlineModel();
                                        onlineModel.setUserID(uid);
                                        onlineModel.setOnline(true);
                                        onlineModel.setInRoom(true);
                                        onlineModel.setRecordTs(System.currentTimeMillis());
                                        hashSet.put(onlineModel.getUserID(), onlineModel);
                                        mStatusMap.put(onlineModel.getUserID(), onlineModel);
                                    }
                                }
                            }
                            {
                                List<Integer> inviteStatusUserList = JSON.parseArray(obj.getData().getString("inviteStatusUserList"), Integer.class);
                                if (inviteStatusUserList != null) {
                                    for (int uid : inviteStatusUserList) {
                                        OnlineModel onlineModel = new OnlineModel();
                                        onlineModel.setUserID(uid);
                                        onlineModel.setOnline(true);
                                        onlineModel.setRecordTs(System.currentTimeMillis());
                                        hashSet.put(onlineModel.getUserID(), onlineModel);
                                        mStatusMap.put(onlineModel.getUserID(), onlineModel);
                                    }
                                }
                            }
                            {
                                List<JSONObject> userOfflineList = JSON.parseArray(obj.getData().getString("userOfflineList"), JSONObject.class);
                                if (userOfflineList != null) {
                                    for (JSONObject jsonObject : userOfflineList) {
                                        OnlineModel onlineModel = new OnlineModel();
                                        onlineModel.setUserID(jsonObject.getIntValue("userID"));
                                        onlineModel.setOnline(false);
                                        onlineModel.setOfflineTime(jsonObject.getLongValue("offlineTime"));
                                        onlineModel.setRecordTs(System.currentTimeMillis());
                                        hashSet.put(onlineModel.getUserID(), onlineModel);
                                        mStatusMap.put(onlineModel.getUserID(), onlineModel);
                                    }
                                }
                            }
                            return hashSet;
                        }
                        return null;
                    }
                });
    }
}
