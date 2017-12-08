package com.wali.live.component;

import com.thornbirds.component.ComponentController;

/**
 * Created by yangli on 2017/8/2.
 *
 * @module 基础架构控制器
 */
public abstract class BaseSdkController extends ComponentController {

    // 系统消息
    private static final int MSG_SYSTEM_FIRST = 10000;
    public static final int MSG_ON_BACK_PRESSED = MSG_SYSTEM_FIRST;         // 返回键
    public static final int MSG_ON_ORIENT_PORTRAIT = MSG_SYSTEM_FIRST + 1;  // 竖屏
    public static final int MSG_ON_ORIENT_LANDSCAPE = MSG_SYSTEM_FIRST + 2; // 横屏
    public static final int MSG_ON_ACTIVITY_RESUMED = MSG_SYSTEM_FIRST + 3; // APP回到前台

    // 推/拉流相关消息
    private static final int MSG_STREAM_FIRST = 11000;
    public static final int MSG_END_LIVE_UNEXPECTED = MSG_STREAM_FIRST;      // 异常结束直播/观看
    public static final int MSG_END_LIVE_FOR_TIMEOUT = MSG_STREAM_FIRST + 1; // 长时间退到后台后结束直播
    public static final int MSG_OPEN_CAMERA_FAILED = MSG_STREAM_FIRST + 2;   // 打开相机失败
    public static final int MSG_OPEN_MIC_FAILED = MSG_STREAM_FIRST + 3;      // 打开麦克风失败
    public static final int MSG_ON_STREAM_SUCCESS = MSG_STREAM_FIRST + 4;    // 推/拉流成功
    public static final int MSG_ON_STREAM_RECONNECT = MSG_STREAM_FIRST + 5;  // 开始重连
    public static final int MSG_ON_LIVE_SUCCESS = MSG_STREAM_FIRST + 6;      // 开房间/进房间成功
    public static final int MSG_NEW_VIDEO_URL = MSG_STREAM_FIRST + 7;        // 获取到拉流URL, 可以开始观看了

    // 播放器相关消息
    private static final int MSG_PLAYER_FIRST = 12000;
    public static final int MSG_VIDEO_SIZE_CHANGED = MSG_PLAYER_FIRST;       // 视频尺寸更新
    public static final int MSG_PLAYER_READY = MSG_PLAYER_FIRST + 1;         // 播放器开始渲染画面
    public static final int MSG_PLAYER_ERROR = MSG_PLAYER_FIRST + 2;         // 播放器出错
    public static final int MSG_PLAYER_COMPLETED = MSG_PLAYER_FIRST + 3;     // 播放完成
    public static final int MSG_SEEK_COMPLETED = MSG_PLAYER_FIRST + 4;       // Seek完成
    public static final int MSG_UPDATE_PLAY_PROGRESS = MSG_PLAYER_FIRST + 5; // 更新播放进度
    public static final int MSG_PLAYER_SHOW_LOADING = MSG_PLAYER_FIRST + 6;  // 显示 加载图标
    public static final int MSG_PLAYER_HIDE_LOADING = MSG_PLAYER_FIRST + 7;  // 隐藏 加载图标
    //    public static final int MSG_PLAYER_START = MSG_PLAYER_FIRST + 8;
    public static final int MSG_PLAYER_PAUSE = MSG_PLAYER_FIRST + 9;

    // UI消息
    // 复合消息(多个View同时响应的消息)
    private static final int MSG_COMPOUND_FIRST = 20000;
    public static final int MSG_INPUT_VIEW_SHOWED = MSG_COMPOUND_FIRST;       // 输入框 已显示
    public static final int MSG_INPUT_VIEW_HIDDEN = MSG_COMPOUND_FIRST + 1;   // 输入框 已隐藏
    public static final int MSG_BOTTOM_POPUP_SHOWED = MSG_COMPOUND_FIRST + 2; // 底部面板/礼物页面等显示时，通知 底部按钮和弹幕区 隐藏
    public static final int MSG_BOTTOM_POPUP_HIDDEN = MSG_COMPOUND_FIRST + 3; // 底部面板/礼物页面等隐藏时，通知 底部按钮和弹幕区 显示
    public static final int MSG_FORCE_ROTATE_SCREEN = MSG_COMPOUND_FIRST + 4; // 强制旋转UI

    // 触摸相关消息
    private static final int MSG_TOUCH_FIRST = 21000;
    public static final int MSG_ENABLE_MOVE_VIEW = MSG_TOUCH_FIRST;      // 开启滑动
    public static final int MSG_DISABLE_MOVE_VIEW = MSG_TOUCH_FIRST + 1; // 禁止滑动
    public static final int MSG_BACKGROUND_CLICK = MSG_TOUCH_FIRST + 2;  // 背景点击
    public static final int MSG_PAGE_DOWN = MSG_TOUCH_FIRST + 3;         // 下滑点击
    public static final int MSG_PAGE_UP = MSG_TOUCH_FIRST + 4;           // 上滑点击
    public static final int MSG_SWITCH_ROOM = MSG_TOUCH_FIRST + 5;       // 切换房间

    // 输入框相关消息
    private static final int MSG_INPUT_FIRST = 22000;
    public static final int MSG_SHOW_INPUT_VIEW = MSG_INPUT_FIRST;         // 请求弹起 输入框
    public static final int MSG_HIDE_INPUT_VIEW = MSG_INPUT_FIRST + 1;     // 请求隐藏 输入框
    public static final int MSG_SHOW_GAME_INPUT = MSG_INPUT_FIRST + 2;     // 请求显示 游戏输入框
    public static final int MSG_HIDE_GAME_INPUT = MSG_INPUT_FIRST + 3;     // 请求隐藏 游戏输入框
    public static final int MSG_BARRAGE_SWITCH = MSG_INPUT_FIRST + 4;      // 显示隐藏 飘屏弹幕开关
    public static final int MSG_SHOW_GAME_BARRAGE = MSG_INPUT_FIRST + 5;   // 显示 游戏弹幕
    public static final int MSG_HIDE_GAME_BARRAGE = MSG_INPUT_FIRST + 6;   // 隐藏 游戏弹幕

    // 弹出页面相关消息
    private static final int MSG_POPUP_FIRST = 23000;
    public static final int MSG_SHOW_SETTING_PANEL = MSG_POPUP_FIRST;       // 显示 设置面板
    public static final int MSG_SHOW_MAGIC_PANEL = MSG_POPUP_FIRST + 1;     // 显示 美妆面板
    public static final int MSG_SHOW_PLUS_PANEL = MSG_POPUP_FIRST + 2;      // 显示 直播加面板
    public static final int MSG_SHOW_GIFT_PANEL = MSG_POPUP_FIRST + 3;      // 显示 礼物面板
    public static final int MSG_HIDE_BOTTOM_PANEL = MSG_POPUP_FIRST + 4;    // 隐藏 底部面板
    public static final int MSG_SHOW_ATMOSPHERE_VIEW = MSG_POPUP_FIRST + 5; // 显示 氛围面板
    public static final int MSG_SHOE_GAME_ICON = MSG_POPUP_FIRST + 6;       // 展示 游戏中心Icon
    public static final int MSG_SHOW_GAME_DOWNLOAD = MSG_POPUP_FIRST + 7;   // 展示 游戏中心下载框
    public static final int MSG_SHOW_SHARE_PANEL = MSG_POPUP_FIRST + 8;     // 显示 分享面板
    public static final int MSG_SHOW_PERSONAL_INFO = MSG_POPUP_FIRST + 9;   // 显示 个人信息页
    public static final int MSG_SHOW_FOLLOW_GUIDE = MSG_POPUP_FIRST + 10;   // 显示 游戏引导页面
    public static final int MSG_FOLLOW_COUNT_DOWN = MSG_POPUP_FIRST + 11;   // 显示 游戏引导页面之前的倒计时
    public static final int MSG_SHOW_SEND_ENVELOPE = MSG_POPUP_FIRST + 12;  // 显示 发送红包页面
    public static final int MSG_SHOW_MESSAGE_PANEL = MSG_POPUP_FIRST + 13;  // 显示 私信面板
    public static final int MSG_SHOW_MENU_PANEL = MSG_POPUP_FIRST + 14;     // 显示 更多面板
    public static final int MSG_ON_MENU_PANEL_HIDDEN = MSG_POPUP_FIRST + 15;// 更多面板已隐藏
    public static final int MSG_SHOW_FANS_PANEL = MSG_POPUP_FIRST + 16;     // 显示 更多面板

    //连麦/PK相关消息
    private static final int MSG_TOP_VIEW_FIRST = 24000;
    public static final int MSG_ON_LINK_MIC_START = MSG_TOP_VIEW_FIRST + 1; // 连麦 开始
    public static final int MSG_ON_LINK_MIC_STOP = MSG_TOP_VIEW_FIRST + 2;  // 连麦 结束
    public static final int MSG_ON_PK_START = MSG_TOP_VIEW_FIRST + 3;       // PK 开始
    public static final int MSG_ON_PK_STOP = MSG_TOP_VIEW_FIRST + 4;        // PK 结束

    // 详情播放相关
    private static final int MSG_DETAIL_VIDEO_FIRST = 30000;
    public static final int MSG_NEW_FEED_ID = MSG_DETAIL_VIDEO_FIRST;               // 新的Feed ID
    public static final int MSG_NEW_FEED_URL = MSG_DETAIL_VIDEO_FIRST + 1;          // 获取到URL, 可以开始播放
    public static final int MSG_SWITCH_TO_REPLAY_MODE = MSG_DETAIL_VIDEO_FIRST + 2; // 切换到回放模式(全屏播放)
    public static final int MSG_SWITCH_TO_DETAIL_MODE = MSG_DETAIL_VIDEO_FIRST + 3; // 切换到详情模式(半屏播放)
    public static final int MSG_UPDATE_LIKE_STATUS = MSG_DETAIL_VIDEO_FIRST + 4;    // 更新 点赞状态
    public static final int MSG_UPDATE_COMMENT_CNT = MSG_DETAIL_VIDEO_FIRST + 5;    // 更新 评论总数
    public static final int MSG_REPLAY_TOTAL_CNT = MSG_DETAIL_VIDEO_FIRST + 6;      // 更新 回放总数
    public static final int MSG_SHOW_COMMENT_INPUT = MSG_DETAIL_VIDEO_FIRST + 7;    // 回复 评论
    public static final int MSG_SEND_COMMENT = MSG_DETAIL_VIDEO_FIRST + 8;          // 发送 评论
    public static final int MSG_FOLD_INFO_AREA = MSG_DETAIL_VIDEO_FIRST + 9;        // 收起 信息区
    public static final int MSG_COMPLETE_USER_INFO = MSG_DETAIL_VIDEO_FIRST + 10;   // 点击回放每一条
    public static final int MSG_UPDATE_START_TIME = MSG_DETAIL_VIDEO_FIRST + 11;    // 更新 回放的录制时间(用于拉取房间消息/弹幕)
    public static final int MSG_UPDATE_TAB_TYPE = MSG_DETAIL_VIDEO_FIRST + 12;      // 更新 TAB数据(回放显示"评论"和"回放"，详情显示"详情"和评论)

    public static final int MSG_NEW_DETAIL_REPLAY = MSG_DETAIL_VIDEO_FIRST + 13;
    public static final int MSG_PLAYER_START = MSG_DETAIL_VIDEO_FIRST + 14;

    // 其他
    private static final int MSG_MORE_FIRST = 90000;
}
