package com.component.busilib.manager;

import com.common.log.MyLog;
import com.common.utils.U;

import java.util.HashMap;
import java.util.HashSet;

/**
 * 弱红点管理类
 */
public class WeakRedDotManager {

    public final String TAG = "WeakRedDotManager";

    public final static String SP_KEY_NEW_MESSAGE_FOLLOW = "SP_KEY_NEW_FOLLOW";     //最新关注，包含自己关注和别人关注,消息页面使用 消息2 最新关注1
    public final static String SP_KEY_POSTS_COMMENT_LIKE = "SP_KEY_POSTS_COMMENT_LIKE"; // 帖子的评论或者赞
    public final static String SP_KEY_NEW_MESSAGE_SP_FOLLOW = "SP_KEY_SP_FOLLOW";   // 特别关注
    public final static String SP_KEY_NEW_MESSAGE_GIFT = "SP_KEY_GIFT";  // 礼物

    //这个先不删，之后增加的不可以占用现在的和现在已经注掉的
//    public static final int FANS_RED_ROD_TYPE = 1;
//    public static final int FRIEND_RED_ROD_TYPE = 2;
    public static final int MESSAGE_FOLLOW_RED_ROD_TYPE = 3;   // 最新关注
    //    public static final int MESSAGE_FEED_LIKE_TYPE = 4;
//    public static final int MESSAGE_FEED_COMMENT_LIKE_TYPE = 5;
//    public static final int MESSAGE_FEED_COMMENT_ADD_TYPE = 6;
    public static final int MESSAGE_POSTS_COMMENT_LIKE_TYPE = 7;  // 评论或者赞
    public static final int MESSAGE_SP_FOLLOW = 8; // 特别关注
    public static final int MESSAGE_GIFT_TYPE = 9; // 礼物

    HashMap<Integer, HashSet<WeakRedDotListener>> mMap = new HashMap<>();

    private static class WeakRedDotManagerHolder {
        private static final WeakRedDotManager INSTANCE = new WeakRedDotManager();
    }

    private WeakRedDotManager() {

    }

    public static final WeakRedDotManager getInstance() {
        return WeakRedDotManagerHolder.INSTANCE;
    }

    public synchronized void addListener(WeakRedDotListener listener) {
        MyLog.d(TAG, "addListener" + " listener=" + listener);
        for (int msgType : listener.acceptType()) {
            HashSet<WeakRedDotListener> listenerSet = mMap.get(msgType);
            if (listenerSet == null) {
                listenerSet = new HashSet<>();
                mMap.put(msgType, listenerSet);
            }
            listenerSet.add(listener);
        }
    }

    public synchronized void removeListener(WeakRedDotListener listener) {
        MyLog.d(TAG, "removeListener" + " listener=" + listener);
        for (int msgType : listener.acceptType()) {
            HashSet<WeakRedDotListener> listenerSet = mMap.get(msgType);
            if (listenerSet != null) {
                listenerSet.remove(listener);
            }
        }
    }

    /**
     * @param type
     * @param value
     * @param isFlag 是否强制刷新
     */
    public void updateWeakRedRot(int type, int value, boolean isFlag) {
//        if (type == FANS_RED_ROD_TYPE) {
//            if (U.getPreferenceUtils().getSettingInt(SP_KEY_NEW_FANS, 0) < value && !isFlag) {
//                return;
//            } else {
//                U.getPreferenceUtils().setSettingInt(SP_KEY_NEW_FANS, value);
//            }
//        }
//
//        if (type == FRIEND_RED_ROD_TYPE) {
//            if (U.getPreferenceUtils().getSettingInt(SP_KEY_NEW_FRIEND, 0) < value && !isFlag) {
//                return;
//            } else {
//                U.getPreferenceUtils().setSettingInt(SP_KEY_NEW_FRIEND, value);
//            }
//        }

        if (type == MESSAGE_FOLLOW_RED_ROD_TYPE) {
            if (U.getPreferenceUtils().getSettingInt(SP_KEY_NEW_MESSAGE_FOLLOW, 0) < value && !isFlag) {
                return;
            } else {
                U.getPreferenceUtils().setSettingInt(SP_KEY_NEW_MESSAGE_FOLLOW, value);
            }
        }

//        if (type == MESSAGE_FEED_LIKE_TYPE) {
//            if (U.getPreferenceUtils().getSettingInt(SP_KEY_FEED_LIKE, 0) < value && !isFlag) {
//                return;
//            } else {
//                U.getPreferenceUtils().setSettingInt(SP_KEY_FEED_LIKE, value);
//            }
//        }
//
//        if (type == MESSAGE_FEED_COMMENT_LIKE_TYPE) {
//            if (U.getPreferenceUtils().getSettingInt(SP_KEY_FEED_COMMENT_LIKE, 0) < value && !isFlag) {
//                return;
//            } else {
//                U.getPreferenceUtils().setSettingInt(SP_KEY_FEED_COMMENT_LIKE, value);
//            }
//        }
//
//        if (type == MESSAGE_FEED_COMMENT_ADD_TYPE) {
//            if (U.getPreferenceUtils().getSettingInt(SP_KEY_FEED_COMMENT_ADD, 0) < value && !isFlag) {
//                return;
//            } else {
//                U.getPreferenceUtils().setSettingInt(SP_KEY_FEED_COMMENT_ADD, value);
//            }
//        }

        if (type == MESSAGE_SP_FOLLOW) {
            if (U.getPreferenceUtils().getSettingInt(SP_KEY_NEW_MESSAGE_SP_FOLLOW, 0) < value && !isFlag) {
                return;
            } else {
                U.getPreferenceUtils().setSettingInt(SP_KEY_NEW_MESSAGE_SP_FOLLOW, value);
            }
        }

        if (type == MESSAGE_POSTS_COMMENT_LIKE_TYPE) {
            if (U.getPreferenceUtils().getSettingInt(SP_KEY_POSTS_COMMENT_LIKE, 0) < value && !isFlag) {
                return;
            } else {
                U.getPreferenceUtils().setSettingInt(SP_KEY_POSTS_COMMENT_LIKE, value);
            }
        }

        if (type == MESSAGE_GIFT_TYPE) {
            if (U.getPreferenceUtils().getSettingInt(SP_KEY_NEW_MESSAGE_GIFT, 0) < value && !isFlag) {
                return;
            } else {
                U.getPreferenceUtils().setSettingInt(SP_KEY_NEW_MESSAGE_GIFT, value);
            }
        }

        HashSet<WeakRedDotListener> listenerSet = mMap.get(type);
        if (listenerSet != null) {
            for (WeakRedDotListener weakRedDotListener : listenerSet) {
                weakRedDotListener.onWeakRedDotChange(type, value);
            }
        }
    }

    /**
     * 更新红点
     *
     * @param type  红点的类型
     * @param value 红点的值
     */
    public void updateWeakRedRot(int type, int value) {
        MyLog.d(TAG, "updateWeakRedRot" + " type=" + type + " value=" + value);
        updateWeakRedRot(type, value, false);
    }

    public interface WeakRedDotListener {
        /**
         * 接受关注的红点类型
         *
         * @return
         */
        int[] acceptType();

        /**
         * 红点状态改变
         *
         * @param type  红点类型
         * @param value 红点对应的值
         */
        void onWeakRedDotChange(int type, int value);
    }

}
