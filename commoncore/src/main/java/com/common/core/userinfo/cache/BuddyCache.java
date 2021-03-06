package com.common.core.userinfo.cache;


import android.os.Build;

import com.alibaba.fastjson.JSONObject;
import com.common.cache.LruCache;
import com.common.core.userinfo.ResultCallback;
import com.common.core.userinfo.UserInfoLocalApi;
import com.common.core.userinfo.UserInfoManager;
import com.common.core.userinfo.model.ClubInfo;
import com.common.core.userinfo.model.HonorInfo;
import com.common.core.userinfo.model.UserInfoModel;
import com.common.core.userinfo.model.VerifyInfo;
import com.common.log.MyLog;
import com.module.ModuleServiceManager;

import org.jetbrains.annotations.Nullable;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.schedulers.Schedulers;

/**
 * 用户资料的cache
 */
public class BuddyCache {

    private final static String TAG = "BuddyCache";
    /**
     * 缓存
     */
    private LruCache<Integer, BuddyCacheEntry> mLruCache = null;

    private volatile static BuddyCache sInstance = null;

    private final int MAX_SIZE = 1000;

    private BuddyCache() {
        mLruCache = new LruCache<>(MAX_SIZE);
    }

    public static synchronized BuddyCache getInstance() {
        if (sInstance == null) {
            sInstance = new BuddyCache();
        }

        return sInstance;
    }

    /**
     * 批量加入缓存
     *
     * @param buddyCacheEntryList
     */
    public void putBuddyList(final List<BuddyCacheEntry> buddyCacheEntryList) {
        if (null != buddyCacheEntryList && buddyCacheEntryList.size() > 0) {
            for (BuddyCacheEntry buddyCacheEntry : buddyCacheEntryList) {
                mLruCache.put(buddyCacheEntry.uuid, buddyCacheEntry);
            }
            if (mLruCache.size() > MAX_SIZE) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    mLruCache.trimToSize(MAX_SIZE / 2);
                }
            }
        }
    }

    /**
     * 加入缓存
     *
     * @param buddyCacheEntry
     */
    public void putBuddy(BuddyCacheEntry buddyCacheEntry) {
        if (buddyCacheEntry == null) {
            MyLog.w(TAG + " put entry == null");
            return;
        }

        BuddyCacheEntry entry = getBuddyNormal(buddyCacheEntry.getUuid(), false);
        if (buddyCacheEntry.same(entry)) {
            // 已经存在了,不做操作
            return;
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("vipInfo", buddyCacheEntry.vipInfo);
        jsonObject.put("honorInfo", buddyCacheEntry.honorInfo);
        ModuleServiceManager.getInstance().getMsgService().refreshUserInfoCache(buddyCacheEntry.getUuid(), buddyCacheEntry.getName(), buddyCacheEntry.getAvatar(), jsonObject.toJSONString());
        mLruCache.put(buddyCacheEntry.getUuid(), buddyCacheEntry);
    }


    /**
     * 获取buddy
     *
     * @param uuid
     * @param queryIfNotExist 是否去服务器查询
     * @return
     */
    public BuddyCacheEntry getBuddyNormal(final int uuid, boolean queryIfNotExist) {
        return getBuddyNormal(uuid, queryIfNotExist, null);
    }

    /**
     * @param uuid
     * @param queryIfNotExist 是否去服务器查询
     * @param resultCallback  服务器回调
     * @return
     */
    public BuddyCacheEntry getBuddyNormal(final int uuid, boolean queryIfNotExist, ResultCallback<UserInfoModel> resultCallback) {
        BuddyCacheEntry result = mLruCache.get(uuid);
        if (result != null) {
            return result;
        } else {
            // 本地数据库
            syncBuddyInfoFromDB(uuid);
            if (mLruCache.get(uuid) != null) {
                return mLruCache.get(uuid);
            }
            if (queryIfNotExist) {
                // 网络查询
                UserInfoManager.getInstance().getUserInfoByUuid(uuid, false, resultCallback);
            }
        }
        return result;
    }

    /**
     * 从DB里面去找
     *
     * @param uuid
     */
    public void syncBuddyInfoFromDB(final int uuid) {
        Observable.create(new ObservableOnSubscribe<UserInfoModel>() {
            @Override
            public void subscribe(ObservableEmitter<UserInfoModel> emitter) throws Exception {
                UserInfoModel userInfoModel = UserInfoLocalApi.getUserInfoByUUid(uuid);
                if (userInfoModel != null) {
                    BuddyCacheEntry buddyCacheEntry = new BuddyCacheEntry(userInfoModel);
                    putBuddy(buddyCacheEntry);
                }
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .subscribe();

    }


    /**
     * cache entry, 字段较少,主要缓存常用的字段
     */
    public static class BuddyCacheEntry {

        private int uuid;
        private String name;
        private String avatar;

        private VerifyInfo vipInfo;
        private HonorInfo honorInfo;

        public int getUuid() {
            return uuid;
        }

        public void setUuid(int uuid) {
            this.uuid = uuid;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public VerifyInfo getVipInfo() {
            return vipInfo;
        }

        public void setVipInfo(VerifyInfo vipInfo) {
            this.vipInfo = vipInfo;
        }

        public HonorInfo getHonorInfo() {
            return honorInfo;
        }

        public void setHonorInfo(HonorInfo honorInfo) {
            this.honorInfo = honorInfo;
        }

        @Override
        public boolean equals(Object o) {
            if (o == null) {
                return false;
            }

            if (o == this) {
                return true;
            }

            if (!(o instanceof BuddyCacheEntry)) {
                return false;
            }

            BuddyCacheEntry data = (BuddyCacheEntry) o;
            return this.uuid == data.uuid;
        }

        public BuddyCacheEntry(UserInfoModel userInfoModel) {
            if (userInfoModel == null) {
                return;
            }
            this.uuid = userInfoModel.getUserId();
            this.name = userInfoModel.getNicknameRemark();
            this.avatar = userInfoModel.getAvatar();
            this.vipInfo = userInfoModel.getVipInfo();
            this.honorInfo = userInfoModel.getHonorInfo();
        }

        @Override
        public String toString() {
            return "BuddyCacheEntry{" +
                    "uuid=" + uuid +
                    ", name='" + name + '\'' +
                    ", avatar='" + avatar + '\'' +
                    ", vipInfo=" + vipInfo +
                    ", honorInfo=" + honorInfo +
                    '}';
        }

        @Override
        public int hashCode() {
            int result = 17;
            result = 31 * result + (int) (this.uuid ^ (this.uuid >>> 32));
            return result;
        }

        public boolean same(BuddyCacheEntry entry1) {
            if (entry1 != null
                    && entry1.uuid == uuid
                    && entry1.avatar.equals(avatar)
                    && entry1.name.equals(name)) {
                return true;
            }
            return false;
        }

        @Nullable
        public UserInfoModel toUserInfoModel() {
            UserInfoModel userInfoModel = new UserInfoModel();
            userInfoModel.setUserId(uuid);
            userInfoModel.setNickname(name);
            userInfoModel.setAvatar(avatar);
            return userInfoModel;
        }
    }
}
