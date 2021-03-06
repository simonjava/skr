package com.common.webview;

import android.text.TextUtils;
import android.util.Pair;

import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.fastjson.JSONObject;
import com.common.base.BaseActivity;
import com.common.clipboard.ClipboardUtils;
import com.common.core.share.SharePanel;
import com.common.core.share.SharePlatform;
import com.common.core.share.ShareType;
import com.common.log.MyLog;
import com.common.permission.PermissionUtils;
import com.common.utils.U;
import com.jsbridge.CallBackFunction;
import com.module.RouterConstants;
import com.module.home.IHomeService;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.List;
import java.util.Map;


public class JsBridgeImpl {
    public final String TAG = "JsBridgeImpl";
    BaseActivity mBaseActivity;

    public JsBridgeImpl(BaseActivity baseActivity) {
        mBaseActivity = baseActivity;
    }

    public void share(String data, CallBackFunction function) {
        MyLog.w(TAG, "share" + " data=" + data);
        JSONObject jsonObject = JSONObject.parseObject(data);

        if (jsonObject == null) {
            MyLog.w(TAG, "share" + " jsonObject=null");
            return;
        }

        //panel/wx/icon_qq,弹出分享弹窗还是直接指定一个平台
        String param = jsonObject.getString("param");
        if (TextUtils.isEmpty(param)) {
            MyLog.w(TAG, "share" + " param=empty");
            return;
        }

        SharePanel sharePanel = null;
        ShareType shareType = ShareType.URL;
        {
            String type = jsonObject.getString("type");
            if (TextUtils.isEmpty(type)) {
                MyLog.w(TAG, "share" + " type=empty");
                return;
            }

            if ("url".equals(type)) {
                String icon = jsonObject.getString("icon");
                String des = jsonObject.getString("des");
                String title = jsonObject.getString("title");
                String url = jsonObject.getString("url");

                sharePanel = new SharePanel(mBaseActivity);
                sharePanel.setShareContent(icon, title, des, url);
                shareType = ShareType.URL;
            } else if ("image".equals(type)) {
                String url = jsonObject.getString("url");

                sharePanel = new SharePanel(mBaseActivity);
                sharePanel.setShareContent(url);
                shareType = ShareType.IMAGE_RUL;
            }
        }

        if (sharePanel == null) {
            MyLog.w(TAG, "share" + " sharePanel=null");
            return;
        }

        if ("panel".equals(param)) {
            sharePanel.show(shareType);
        } else {
            //渠道，微信好友，微信朋友圈，qq好友，qq朋友圈
            String channel = jsonObject.getString("channel");
            if (TextUtils.isEmpty(channel)) {
                MyLog.w(TAG, "share" + " channel=empty");
                return;
            }

            if ("wx".equals(param)) {
                if ("wx_friend_circle".equals(channel)) {
                    sharePanel.share(SharePlatform.WEIXIN_CIRCLE, shareType);
                } else if ("wx_friend".equals(channel)) {
                    sharePanel.share(SharePlatform.WEIXIN, shareType);
                } else {
                    MyLog.w(TAG, "share not find channel, " + " channel is " + channel);
                }
            } else if ("qq".equals(param)) {
                if ("qq_friend".equals(channel)) {
                    sharePanel.share(SharePlatform.QQ, shareType);
                } else {
                    MyLog.w(TAG, "share not find channel, " + " channel is " + channel);
                }
            } else {
                MyLog.w(TAG, "share not find param, " + " param is " + channel);
            }
        }


        function.onCallBack(getJsonObj(new Pair("errcode", "0"), new Pair("errmsg", ""), new Pair("data", "{}")).toJSONString());
    }

    public void bindWeChat(String data, final CallBackFunction function) {
        MyLog.w(TAG, "bindWeChat" + " data=" + data);
        UMShareAPI.get(U.app()).getPlatformInfo(mBaseActivity, SHARE_MEDIA.WEIXIN, new UMAuthListener() {
            @Override
            public void onStart(SHARE_MEDIA share_media) {

            }

            @Override
            public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> data) {
                String accessToken = data.get("access_token");
                String openid = data.get("openid");
                function.onCallBack(getJsonObj(new Pair("errcode", "0"), new Pair("errmsg", ""),
                        new Pair("data", getJsonObj(new Pair("access_token", accessToken), new Pair("open_id", openid)))).toJSONString());
            }

            @Override
            public void onCancel(SHARE_MEDIA share_media, int i) {
                function.onCallBack(getJsonObj(new Pair("errcode", "1"), new Pair("errmsg", "取消授权"),
                        new Pair("data", getJsonObj(new Pair("access_token", ""), new Pair("open_id", "")))).toJSONString());
            }

            @Override
            public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
                function.onCallBack(getJsonObj(new Pair("errcode", "2"), new Pair("errmsg", throwable.getMessage()),
                        new Pair("data", getJsonObj(new Pair("access_token", ""), new Pair("open_id", "")))).toJSONString());
            }
        });
    }

    public void bindQqChat(String data, final CallBackFunction function) {
        MyLog.w(TAG, "bindQQ" + " data=" + data);
        UMShareAPI.get(U.app()).getPlatformInfo(mBaseActivity, SHARE_MEDIA.QQ, new UMAuthListener() {
            @Override
            public void onStart(SHARE_MEDIA share_media) {

            }

            @Override
            public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> data) {
                String accessToken = data.get("accessToken");
                String openid = data.get("openid");
                function.onCallBack(getJsonObj(new Pair("errcode", "0"), new Pair("errmsg", ""),
                        new Pair("data", getJsonObj(new Pair("access_token", accessToken), new Pair("open_id", openid)))).toJSONString());
            }

            @Override
            public void onCancel(SHARE_MEDIA share_media, int i) {
                function.onCallBack(getJsonObj(new Pair("errcode", "1"), new Pair("errmsg", "取消授权"),
                        new Pair("data", getJsonObj(new Pair("access_token", ""), new Pair("open_id", "")))).toJSONString());
            }

            @Override
            public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
                function.onCallBack(getJsonObj(new Pair("errcode", "2"), new Pair("errmsg", throwable.getMessage()),
                        new Pair("data", getJsonObj(new Pair("access_token", ""), new Pair("open_id", "")))).toJSONString());
            }
        });
    }

    public void getAppVersion(String data, final CallBackFunction function) {
        function.onCallBack(getJsonObj(new Pair("errcode", "0"), new Pair("errmsg", ""),
                new Pair("data", getJsonObj(new Pair("version", U.getAppInfoUtils().getVersionCode())))).toJSONString());
    }

    public void getClipboard(final CallBackFunction function) {

        function.onCallBack(getJsonObj(new Pair("errcode", "0"), new Pair("errmsg", ""),
                new Pair("data", getJsonObj(new Pair("content", ClipboardUtils.getPaste())))).toJSONString());
    }

    public void authSuccess(final CallBackFunction function) {
        IHomeService channelService = (IHomeService) ARouter.getInstance().build(RouterConstants.SERVICE_HOME).navigation();
        channelService.authSuccess();
        function.onCallBack(getJsonObj(new Pair("errcode", "0"), new Pair("errmsg", "")).toJSONString());
    }

    public void finish(final CallBackFunction function) {
        mBaseActivity.finish();
        function.onCallBack(getJsonObj(new Pair("errcode", "0"), new Pair("errmsg", "")).toJSONString());
    }

    public void checkCameraPerm(final CallBackFunction function) {
        if (U.getPermissionUtils().checkCamera(mBaseActivity)) {
            function.onCallBack(getJsonObj(new Pair("errcode", "0"), new Pair("errmsg", ""),
                    new Pair<String, Object>("data", getJsonObj(new Pair<String, Object>("camera_perm", true)))).toJSONString());
        } else {

            U.getPermissionUtils().requestCamera(new PermissionUtils.RequestPermission() {
                @Override
                public void onRequestPermissionSuccess() {
                    function.onCallBack(getJsonObj(new Pair("errcode", "0"), new Pair("errmsg", ""),
                            new Pair<String, Object>("data", getJsonObj(new Pair<String, Object>("camera_perm", true)))).toJSONString());
                }

                @Override
                public void onRequestPermissionFailure(List<String> permissions) {
                    function.onCallBack(getJsonObj(new Pair("errcode", "0"), new Pair("errmsg", ""),
                            new Pair<String, Object>("data", getJsonObj(new Pair<String, Object>("camera_perm", false)))).toJSONString());
                }

                @Override
                public void onRequestPermissionFailureWithAskNeverAgain(List<String> permissions) {
                    function.onCallBack(getJsonObj(new Pair("errcode", "0"), new Pair("errmsg", ""),
                            new Pair<String, Object>("data", getJsonObj(new Pair<String, Object>("camera_perm", false)))).toJSONString());
                }
            }, mBaseActivity);
        }
    }

    public void noMethed(final CallBackFunction function) {
        function.onCallBack(getJsonObj(new Pair("errcode", "-1"), new Pair("errmsg", "方法不存在")).toJSONString());
    }

    public static JSONObject getJsonObj(Pair<String, Object>... pairArray) {
        JSONObject jsonObject = new JSONObject();
        for (Pair<String, Object> pair : pairArray) {
            jsonObject.put(pair.first, pair.second);
        }

        return jsonObject;
    }
}
