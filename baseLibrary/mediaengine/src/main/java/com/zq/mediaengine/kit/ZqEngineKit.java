package com.zq.mediaengine.kit;

import android.content.Context;
import android.graphics.RectF;
import android.opengl.GLSurfaceView;
import android.os.Looper;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.TextureView;
import android.view.View;

import com.common.log.DebugLogView;
import com.common.log.MyLog;
import com.common.rxretrofit.ApiManager;
import com.common.rxretrofit.ApiResult;
import com.common.statistics.StatisticsAdapter;
import com.common.utils.CustomHandlerThread;
import com.common.utils.DeviceUtils;
import com.common.utils.U;
import com.common.videocache.MediaCacheManager;
import com.engine.EngineEvent;
import com.engine.Params;
import com.engine.UserStatus;
import com.engine.agora.AgoraOutCallback;
import com.engine.agora.effect.EffectModel;
import com.engine.api.EngineServerApi;
import com.engine.arccloud.AcrRecognizeListener;
import com.engine.arccloud.RecognizeConfig;
import com.engine.score.Score2Callback;
import com.zq.engine.avstatistics.SDataManager;
import com.zq.engine.avstatistics.datastruct.Skr;
import com.zq.mediaengine.capture.AudioCapture;
import com.zq.mediaengine.capture.AudioPlayerCapture;
import com.zq.mediaengine.capture.CameraCapture;
import com.zq.mediaengine.encoder.MediaCodecAudioEncoder;
import com.zq.mediaengine.filter.audio.APMFilter;
import com.zq.mediaengine.filter.audio.AudioFilterMgt;
import com.zq.mediaengine.filter.audio.AudioMixer;
import com.zq.mediaengine.filter.audio.AudioPreview;
import com.zq.mediaengine.filter.audio.AudioResampleFilter;
import com.zq.mediaengine.filter.audio.AudioReverbFilter;
import com.zq.mediaengine.filter.imgtex.ImgTexMixer;
import com.zq.mediaengine.filter.imgtex.ImgTexPreview;
import com.zq.mediaengine.filter.imgtex.ImgTexScaleFilter;
import com.zq.mediaengine.framework.AVConst;
import com.zq.mediaengine.framework.AudioBufFormat;
import com.zq.mediaengine.framework.AudioBufFrame;
import com.zq.mediaengine.framework.AudioCodecFormat;
import com.zq.mediaengine.framework.ImgTexFrame;
import com.zq.mediaengine.framework.SinkPin;
import com.zq.mediaengine.framework.SrcPin;
import com.zq.mediaengine.kit.agora.AgoraRTCAdapter;
import com.zq.mediaengine.kit.bytedance.BytedEffectFilter;
import com.zq.mediaengine.kit.filter.AcrRecognizer;
import com.zq.mediaengine.kit.filter.AudioDummyFilter;
import com.zq.mediaengine.kit.filter.CbAudioScorer;
import com.zq.mediaengine.kit.log.LogRunnable;
import com.zq.mediaengine.publisher.MediaMuxerPublisher;
import com.zq.mediaengine.publisher.Publisher;
import com.zq.mediaengine.publisher.RawFrameWriter;
import com.zq.mediaengine.util.audio.AudioUtil;
import com.zq.mediaengine.util.gles.GLRender;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.agora.rtc.Constants;
import io.agora.rtc.IRtcEngineEventHandler;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Response;

public class ZqEngineKit implements AgoraOutCallback {

    public final String TAG = "ZqEngineKit";
    public static final String PREF_KEY_TOKEN_ENABLE = "key_agora_token_enable";
    public static final String AUDIO_FEEDBACK_DIR = "audio_feedback";
    public static final int VIDEO_RESOLUTION_360P = 0;
    public static final int VIDEO_RESOLUTION_480P = 1;
    public static final int VIDEO_RESOLUTION_540P = 2;
    public static final int VIDEO_RESOLUTION_720P = 3;
    public static final int VIDEO_RESOLUTION_1080P = 4;

    private static final int DEFAULT_PREVIEW_WIDTH = 720;
    private static final int DEFAULT_PREVIEW_HEIGHT = 1280;

    // ??????????????????????????????, ????????????????????????????????????????????????
    private static final int SCORE_SAMPLE_RATE = 16000;

    static final int STATUS_UNINIT = 0;
    static final int STATUS_INITING = 1;
    static final int STATUS_INITED = 2;
    static final int STATUS_UNINITING = 3;
    static final int MSG_JOIN_ROOM_TIMEOUT = 11;
    static final int MSG_JOIN_ROOM_AGAIN = 12;
    static final int MSG_ROLE_CHANGE_TIMEOUT = 13;

    private static final boolean SCORE_DEBUG = false;
    private static final String SCORE_DEBUG_PATH = "/sdcard/tongzhuodeni.pcm";
    public static final boolean RECORD_FOR_DEBUG = false;
    public final boolean OPEN_AUDIO_RECORD_FOR_CALLBACK = true; // ???????????????????????????????????????

    private Context mContext;
    private Params mConfig = new Params(); // ??????????????????

    private volatile int mStatus = STATUS_UNINIT;// 0???????????? 1 ??????ing 2 ????????? 3 ??????ing
    /**
     * ??????????????????????????????????????????????????????
     * key???????????????????????? id
     */
    public HashMap<Integer, UserStatus> mUserStatusMap = new HashMap<>();
    private HashSet<View> mRemoteViewCache = new HashSet<>();
    //    private Handler mUiHandler = new Handler();
    private Disposable mMusicTimePlayTimeListener;

    private String mInitFrom;

    private CustomHandlerThread mCustomHandlerThread;

    private boolean mTokenEnable = false; // ????????????token??????
    private String mLastJoinChannelToken; // ???????????????????????????token
    private String mRoomId = ""; // ??????id
    private boolean mIsJoiningChannel = false;
//    private boolean mInChannel = false; // ????????????????????????

    private GLRender mGLRender;
    private CameraCapture mCameraCapture;
    private ImgTexScaleFilter mImgTexScaleFilter;
    private BytedEffectFilter mBytedEffectFilter;
    private ImgTexMixer mImgTexPreviewMixer;
    private ImgTexMixer mImgTexMixer;
    private ImgTexPreview mImgTexPreview;
    private Map<Integer, Integer> mRemoteUserPinMap = new HashMap<>();

    private AgoraRTCAdapter mAgoraRTCAdapter;
    private AudioDummyFilter mAudioDummyFilter;
    private AudioFilterMgt mAudioFilterMgt;
    private AudioReverbFilter mAudioReverbFilter;
    private AudioResampleFilter mScoreResampleFilter;
    private CbAudioScorer mCbAudioScorer;
    private AcrRecognizer mAcrRecognizer;

    // ???????????????
    private AudioCapture mAudioCapture;
    private AudioPlayerCapture mAudioPlayerCapture;
    private SrcPin<AudioBufFrame> mAudioLocalSrcPin;
    private SrcPin<AudioBufFrame> mAudioSendSrcPin;
    private SrcPin<AudioBufFrame> mAudioRemoteSrcPin;
    private AudioPreview mRemoteAudioPreview;
    private AudioPreview mLocalAudioPreview;

    // AEC/NS/AGC?????????
    private APMFilter mAPMFilter;
    // ???????????????????????????
    private AudioResampleFilter mAudioSendResampleFilter;
    // ???????????????????????????
    private AudioResampleFilter mAudioRecordResampleFilter;
    // ???mic, bgm?????????????????????????????????
    private AudioMixer mLocalAudioMixer;
    // ???mic, bgm, remote???????????????????????????
    private AudioMixer mRecordAudioMixer;
    private MediaCodecAudioEncoder mAudioEncoder;
    private MediaMuxerPublisher mFilePublisher;
    // ????????????????????????
    private MediaCodecAudioEncoder mHumanVoiceAudioEncoder;
    private MediaMuxerPublisher mHumanVoiceFilePublisher;
    // debug????????????
    private RawFrameWriter mRawFrameWriter;
    private RawFrameWriter mCapRawFrameWriter;
    private RawFrameWriter mBgmRawFrameWriter;

    // ????????????????????????
    private boolean mIsAudioCaptureStarted = false;
    private int mAudioCaptureRetriedCount = 0;

    // ??????????????????
    private String mCdnType = "";
    private boolean mAccEnableCache = false;
    private String mAccFullPath;
    private String mAccUrlInUse;
    private long mAccFirstStartTime;
    private long mAccStartTime;
    private boolean mIsAccPrepared;
    private boolean mAccPreparedSent = false;
    private long mAccRecoverPosition = 0;
    private int mAccRemainedLoopCount = 0;
    private int mAccRetriedCount = 0;
    private boolean mIsAccStopped = false;  // ????????????????????????
    private int lastAudioMixingPublishVolume = -1;

    // ??????????????????
    protected int mScreenRenderWidth = 0;
    protected int mScreenRenderHeight = 0;
    protected int mPreviewResolution = VIDEO_RESOLUTION_360P;
    protected int mPreviewWidthOrig = 0;
    protected int mPreviewHeightOrig = 0;
    protected int mPreviewWidth = 0;
    protected int mPreviewHeight = 0;
    protected float mPreviewFps = 0;
    protected int mPreviewMixerWidth = 0;
    protected int mPreviewMixerHeight = 0;
    protected int mTargetResolution = VIDEO_RESOLUTION_360P;
    protected int mTargetWidthOrig = 0;
    protected int mTargetHeightOrig = 0;
    protected int mTargetWidth = 0;
    protected int mTargetHeight = 0;
    protected float mTargetFps = 0;
    protected int mRotateDegrees = 0;

    protected boolean mFrontCameraMirror = false;
    protected int mCameraFacing = CameraCapture.FACING_FRONT;
    protected boolean mIsCaptureStarted = false;
    protected boolean mDelayedStartCameraPreview = false;


    protected ZqLSCredentialHolder mSCHolder = null;

    @Override
    public void onUserJoined(int uid, int elapsed) {
        MyLog.i(TAG, "onUserJoined" + " uid=" + uid + " elapsed=" + elapsed);
        // ?????????????????????????????????????????????????????????????????????
        UserStatus userStatus = ensureJoin(uid, "onUserMuteAudio");
        userStatus.setAnchor(true);
        EventBus.getDefault().post(new EngineEvent(EngineEvent.TYPE_USER_JOIN, userStatus));
        tryStartRecordForFeedback("onUserJoined");
    }

    @Override
    public void onUserOffline(int uid, int reason) {
        // ????????????
        UserStatus userStatus = mUserStatusMap.remove(uid);
        EventBus.getDefault().post(new EngineEvent(EngineEvent.TYPE_USER_LEAVE, userStatus));
        MyLog.i(TAG, "onUserOffline mUserStatusMap=" + mUserStatusMap);
        tryStopRecordForFeedback("onUserOffline", false);
    }

    @Override
    public void onUserMuteVideo(int uid, boolean muted) {
        UserStatus status = ensureJoin(uid, "onUserMuteAudio");
        status.setVideoMute(muted);
        status.setAnchor(true);
        EventBus.getDefault().post(new EngineEvent(EngineEvent.TYPE_USER_MUTE_VIDEO, status));
    }

    @Override
    public void onUserMuteAudio(int uid, boolean muted) {
        UserStatus status = ensureJoin(uid, "onUserMuteAudio");
        status.setAudioMute(muted);
        status.setAnchor(true);
        EventBus.getDefault().post(new EngineEvent(EngineEvent.TYPE_USER_MUTE_AUDIO, status));
    }

    @Override
    public void onUserEnableVideo(int uid, boolean enabled) {
        UserStatus status = ensureJoin(uid, "onUserEnableVideo");
        status.setEnableVideo(enabled);
        status.setAnchor(true);
        EventBus.getDefault().post(new EngineEvent(EngineEvent.TYPE_USER_VIDEO_ENABLE, status));
    }

    @Override
    public void onFirstRemoteVideoDecoded(int uid, int width, int height, int elapsed) {
        UserStatus status = ensureJoin(uid, "onFirstRemoteVideoDecoded");
        status.setEnableVideo(true);
        status.setFirstVideoDecoded(true);
        status.setFirstVideoWidth(width);
        status.setFirstVideoHeight(height);

        EventBus.getDefault().post(new EngineEvent(EngineEvent.TYPE_FIRST_REMOTE_VIDEO_DECODED, status));
    }

    @Override
    public void onJoinChannelSuccess(String channel, int uid, int elapsed) {
        MyLog.i(TAG, "onJoinChannelSuccess" + " channel=" + channel + " uid=" + uid + " elapsed=" + elapsed);
        mConfig.setJoinChannelSuccess(true);
        mIsJoiningChannel = false;
        initWhenInChannel();
        UserStatus userStatus = ensureJoin(uid, "onJoinChannelSuccess");
//        userStatus.setIsSelf(true);
        mConfig.setSelfUid(uid);
        EventBus.getDefault().post(new EngineEvent(EngineEvent.TYPE_USER_JOIN, userStatus));
        StatisticsAdapter.recordCalculateEvent("agora", "join_duration", System.currentTimeMillis() - mConfig.getJoinRoomBeginTs(), null);
        if (mCustomHandlerThread != null) {
            mCustomHandlerThread.removeMessage(MSG_JOIN_ROOM_TIMEOUT);
            mCustomHandlerThread.removeMessage(MSG_JOIN_ROOM_AGAIN);
        }
        tryPlayPendingMixingMusic("onJoinChannelSuccess");
        mAgoraRTCAdapter.muteLocalAudioStream(mConfig.isLocalAudioStreamMute());
        EventBus.getDefault().post(new EngineEvent(EngineEvent.TYPE_USER_SELF_JOIN_SUCCESS));
    }

    @Override
    public void onRejoinChannelSuccess(String channel, int uid, int elapsed) {
        MyLog.i(TAG, "onRejoinChannelSuccess" + " channel=" + channel + " uid=" + uid + " elapsed=" + elapsed);
        mConfig.setJoinChannelSuccess(true);
        mIsJoiningChannel = false;
        UserStatus userStatus = ensureJoin(uid, "onRejoinChannelSuccess");
//        userStatus.setIsSelf(true);
        EventBus.getDefault().post(new EngineEvent(EngineEvent.TYPE_USER_REJOIN, userStatus));
    }

    @Override
    public void onLeaveChannel(IRtcEngineEventHandler.RtcStats stats) {
        mConfig.setJoinChannelSuccess(false);
        mIsJoiningChannel = false;
    }

    @Override
    public void onClientRoleChanged(int oldRole, int newRole) {
        MyLog.i(TAG, "onClientRoleChanged" + " oldRole=" + oldRole + " newRole=" + newRole);
        mCustomHandlerThread.removeMessage(MSG_ROLE_CHANGE_TIMEOUT);
        if (mConfig.getSelfUid() > 0) {
            UserStatus userStatus = ensureJoin(mConfig.getSelfUid(), "onClientRoleChanged");
            if (newRole == Constants.CLIENT_ROLE_BROADCASTER) {
                userStatus.setAnchor(true);
                tryStartRecordForFeedback("onClientRoleChanged");
                tryPlayPendingMixingMusic("onClientRoleChanged");
            } else {
                userStatus.setAnchor(false);
                tryStopRecordForFeedback("onClientRoleChanged", false);
            }
        }
        // ???????????????????????????
        EngineEvent engineEvent = new EngineEvent(EngineEvent.TYPE_USER_ROLE_CHANGE);
        EngineEvent.RoleChangeInfo roleChangeInfo = new EngineEvent.RoleChangeInfo(oldRole, newRole);
        engineEvent.obj = roleChangeInfo;
        EventBus.getDefault().post(engineEvent);
    }

    @Override
    public void onVideoSizeChanged(int uid, int width, int height, int rotation) {

    }

    @Override
    public void onAudioMixingFinished() {
        MyLog.i(TAG, "onAudioMixingFinished");
        mConfig.setMixMusicPlaying(false);
        EngineEvent engineEvent = new EngineEvent(EngineEvent.TYPE_MUSIC_PLAY_FINISH, null);
        EventBus.getDefault().post(engineEvent);
    }

    @Override
    public void onAudioVolumeIndication(IRtcEngineEventHandler.AudioVolumeInfo[] speakers, int totalVolume) {
        List<EngineEvent.UserVolumeInfo> l = new ArrayList<>();
        for (IRtcEngineEventHandler.AudioVolumeInfo info : speakers) {
//            MyLog.i(TAG,"onAudioVolumeIndication" + " info=" + info.uid+" volume="+info.volume);
            /**
             * ???????????????????????? id ???0 ???
             */
            EngineEvent.UserVolumeInfo userVolumeInfo = new EngineEvent.UserVolumeInfo(info.uid, info.volume);
            l.add(userVolumeInfo);
        }
        if (l.isEmpty()) {
            return;
        }
        if (l.size() == 1 && l.get(0).getUid() == 0 && l.get(0).getVolume() == 0) {
            return;
        }
        EngineEvent engineEvent = new EngineEvent(EngineEvent.TYPE_USER_AUDIO_VOLUME_INDICATION, null);
        engineEvent.obj = l;
        EventBus.getDefault().post(engineEvent);
    }


    /**
     * AUDIO_ROUTE_DEFAULT(-1)?????????????????????????????????
     * AUDIO_ROUTE_HEADSET(0)?????????????????????????????????
     * AUDIO_ROUTE_EARPIECE(1)?????????????????????????????????
     * AUDIO_ROUTE_HEADSETNOMIC(2)?????????????????????????????????????????????
     * AUDIO_ROUTE_SPEAKERPHONE(3)?????????????????????????????????????????????
     * AUDIO_ROUTE_LOUDSPEAKER(4)?????????????????????????????????????????????
     * AUDIO_ROUTE_HEADSETBLUETOOTH(5)???????????????????????????????????????
     *
     * @param routing
     */
    @Override
    public void onAudioRouteChanged(int routing) {
        MyLog.w(TAG, "onAudioRouteChanged ???????????????????????? routing=" + routing);
    }

    @Override
    public void onRecordingBuffer(byte[] samples) {
        // TODO: remove this later
    }

    @Override
    public void onWarning(int warn) {
        MyLog.i(TAG, "onWarning" + " warn=" + warn);
    }

    @Override
    public void onError(int error) {
        MyLog.i(TAG, "onError" + " error=" + error);
        if (error == Constants.ERR_JOIN_CHANNEL_REJECTED) {
            // ?????? channel ??????????????????token????????????token??????????????????
            if (mCustomHandlerThread != null) {
                mCustomHandlerThread.removeMessage(MSG_JOIN_ROOM_AGAIN);
                mCustomHandlerThread.removeMessage(MSG_JOIN_ROOM_TIMEOUT);
                mCustomHandlerThread.post(new Runnable() {
                    @Override
                    public void run() {
                        if (mStatus == STATUS_INITED) {
                            if (TextUtils.isEmpty(mLastJoinChannelToken)) {
                                MyLog.w(TAG, "???????????????????????????token?????????????????????????????????token");
                                joinRoomInner2(mRoomId, mConfig.getSelfUid(), getToken(mRoomId));
                            } else {
                                MyLog.w(TAG, "????????????????????????token????????????????????????????????????");
                                joinRoomInner2(mRoomId, mConfig.getSelfUid(), null);
                            }
                        }
                    }
                });

            }
        } else if (error == Constants.ERR_INVALID_TOKEN || error == Constants.ERR_TOKEN_EXPIRED) {
            // token????????????
            if (mCustomHandlerThread != null) {
                mCustomHandlerThread.removeMessage(MSG_JOIN_ROOM_AGAIN);
                mCustomHandlerThread.removeMessage(MSG_JOIN_ROOM_TIMEOUT);
                mCustomHandlerThread.post(new Runnable() {
                    @Override
                    public void run() {
                        Log.i(TAG, "token error: " + mIsJoiningChannel);
                        if (mStatus == STATUS_INITED && !mIsJoiningChannel) {
                            String token = getToken(mRoomId);
                            mIsJoiningChannel = true;
                            joinRoomInner2(mRoomId, mConfig.getSelfUid(), token);
                        }
                    }
                });
            }
        }
    }

    @Override
    public void onNetworkQuality(int uid, int txQuality, int rxQuality) {
        txQuality = (txQuality >= 7) ? 0 : txQuality;
        rxQuality = (rxQuality >= 7) ? 0 : rxQuality;
        if (txQuality > 3 || rxQuality > 3) {
            MyLog.i(TAG, "onNetworkQuality uid: " + uid + " tx: " + txQuality + " rx: " + rxQuality);
        }

        EngineEvent.NetworkQualityInfo info = new EngineEvent.NetworkQualityInfo(uid, txQuality, rxQuality);
        EngineEvent engineEvent = new EngineEvent(EngineEvent.TYPE_USER_NETWORK_QUALITY_INDICATION);
        engineEvent.obj = info;
        EventBus.getDefault().post(engineEvent);
    }

    /**
     * state	????????????
     * MEDIA_ENGINE_AUDIO_EVENT_MIXING_PLAY(710)???????????????????????????
     * MEDIA_ENGINE_AUDIO_EVENT_MIXING_PAUSED(711)???????????????????????????
     * MEDIA_ENGINE_AUDIO_EVENT_MIXING_STOPPED(713)???????????????????????????
     * MEDIA_ENGINE_AUDIO_EVENT_MIXING_ERROR(714)????????????????????????SDK ?????? errorCode ????????????????????????????????????
     * errorCode	????????????
     * MEDIA_ENGINE_AUDIO_ERROR_MIXING_OPEN(701)???????????????????????????
     * MEDIA_ENGINE_AUDIO_ERROR_MIXING_TOO_FREQUENT(702)??????????????????????????????
     * MEDIA_ENGINE_AUDIO_EVENT_MIXING_INTERRUPTED_EOF(703)?????????????????????????????????
     *
     * @param state
     * @param errorCode
     */
    @Override
    public void onAudioMixingStateChanged(int state, int errorCode) {
        MyLog.i(TAG, "onAudioMixingStateChanged" + " state=" + state + " errorCode=" + errorCode);

        if (mCustomHandlerThread != null) {
            mCustomHandlerThread.post(() -> handleAudioMixingStateChanged(state, errorCode));
        }
    }

    private void handleAudioMixingStateChanged(int state, int errorCode) {
        // ??????????????????
        if (state == Constants.MEDIA_ENGINE_AUDIO_EVENT_MIXING_PLAY) {
            if (!mIsAccPrepared) {
                mIsAccPrepared = true;
                doUploadAccStartEvent(0);
            }
            if (mAccPreparedSent) {
                return;
            }
            mAccPreparedSent = true;
        } else if (state == Constants.MEDIA_ENGINE_AUDIO_EVENT_MIXING_ERROR) {
            if (mIsAccPrepared) {
                doUploadAccStopEvent(1, errorCode);
                mAccRecoverPosition = getAudioMixingCurrentPosition();
                if (mConfig.isUseExternalAudio()) {
                    mAccRemainedLoopCount = mAudioPlayerCapture.getRemainedLoopCount();
                }
            }
            mCustomHandlerThread.postDelayed(() -> {
                if (TextUtils.isEmpty(mAccFullPath)) {
                    return;
                }
                mAccRetriedCount++;
                MyLog.i(TAG, "retry acc playback with pos: " + mAccRecoverPosition + " remainLoopCount: " + mAccRemainedLoopCount);
                doStopAudioMixingInternal();
                doStartAudioMixing(mAccUrlInUse, mAccRecoverPosition, mAccRemainedLoopCount);
            }, 100);
            return;
        } else if (state == Constants.MEDIA_ENGINE_AUDIO_EVENT_MIXING_STOPPED) {
            MyLog.i(TAG, "acc stopped by agora");
            mIsAccStopped = true;
        }

        EngineEvent engineEvent = new EngineEvent(EngineEvent.TYPE_MUSIC_PLAY_STATE_CHANGE, null);
        engineEvent.obj = new EngineEvent.MusicStateChange(state, errorCode);
        EventBus.getDefault().post(engineEvent);
    }

    private UserStatus ensureJoin(int uid, String from) {
        if (!mUserStatusMap.containsKey(uid)) {
            MyLog.i(TAG, "ensureJoin" + " uid=" + uid + " from=" + from);
            UserStatus userStatus = new UserStatus(uid);
            userStatus.setEnterTs(System.currentTimeMillis());
            mUserStatusMap.put(uid, userStatus);
            return userStatus;
        } else {
            return mUserStatusMap.get(uid);
        }
    }

    private static class ZqEngineKitHolder {
        private static final ZqEngineKit INSTANCE = new ZqEngineKit();
    }

    private ZqEngineKit() {
        mGLRender = new GLRender();
        mAgoraRTCAdapter = AgoraRTCAdapter.create(mGLRender);
        mAgoraRTCAdapter.setOutCallback(this);
        mAcrRecognizer = new AcrRecognizer();

        mTokenEnable = U.getPreferenceUtils().getSettingBoolean(PREF_KEY_TOKEN_ENABLE, false);
        mSCHolder = new ZqLSCredentialHolder();
        initWorkThread();
    }

    private void initWorkThread() {
        mCustomHandlerThread = new CustomHandlerThread(TAG) {
            @Override
            protected void processMessage(Message msg) {
                if (msg.what == MSG_JOIN_ROOM_AGAIN) {
                    MyLog.i(TAG, "processMessage MSG_JOIN_ROOM_AGAIN ??????????????????");
                    JoinParams joinParams = (JoinParams) msg.obj;
                    joinRoomInner(joinParams.roomID, joinParams.userId, joinParams.token);
                } else if (msg.what == MSG_JOIN_ROOM_TIMEOUT) {
                    MyLog.i(TAG, "handleMessage ??????????????????");
                    StatisticsAdapter.recordCountEvent("agora", "join_timeout", null);
                    JoinParams joinParams = (JoinParams) msg.obj;
                    joinRoomInner2(joinParams.roomID, joinParams.userId, joinParams.token);
                } else if (msg.what == MSG_ROLE_CHANGE_TIMEOUT) {
                    MyLog.i(TAG, "handleMessage ??????????????????");
                    mAgoraRTCAdapter.setClientRole(mConfig.isAnchor());
                }
            }
        };
    }

    public static ZqEngineKit getInstance() {
        return ZqEngineKitHolder.INSTANCE;
    }

    public void init(final String from, final Params params) {
        mInitFrom = from;
        mCustomHandlerThread.post(new LogRunnable("init" + " from=" + from + " params=" + params) {
            @Override
            public void realRun() {
                /**
                 * ???????????? ?????? init ????????? ???????????? runnable ??????remove???
                 */
                destroyInner();
                initInner(from, params);
            }
        });
    }

    private void initInner(String from, Params params) {
        mStatus = STATUS_INITING;
        mInitFrom = from;
        mConfig = params;
        mContext = U.app().getApplicationContext();

        // ??????config???????????????
//        mConfig.setUseExternalAudio(true);
//        mConfig.setEnableInEarMonitoring(true);
        mConfig.setUseLocalAPM(false);

        // TODO: engine???????????????????????????????????????Params??????????????????
        mConfig.setAudioSampleRate(AudioUtil.getNativeSampleRate(mContext));
        MyLog.i(TAG, "Audio native sampleRate: " + mConfig.getAudioSampleRate());

        initModules();
        mAgoraRTCAdapter.init(mConfig);
        mCbAudioScorer.init(mConfig);
        mAcrRecognizer.init(mConfig);
        if (SCORE_DEBUG) {
            mAudioDummyFilter.init(SCORE_DEBUG_PATH, mConfig);
        }
        doSetAudioEffect(mConfig.getStyleEnum(), true);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        // ???????????????????????????
        mStatus = STATUS_INITED;
        EventBus.getDefault().post(new EngineEvent(EngineEvent.TYPE_ENGINE_INITED));
    }

    private void initModules() {
        MyLog.i(TAG, "Latency test ProductModel: " + U.getDeviceUtils().getProductModel() +
                " brand: " + U.getDeviceUtils().getProp("ro.product.brand") +
                " manufacturer: " + U.getDeviceUtils().getProp("ro.product.manufacturer") +
                " name: " + U.getDeviceUtils().getProp("ro.product.name") +
                " device: " + U.getDeviceUtils().getProp("ro.product.device"));
        MyLog.i(TAG, "isUseExternalAudio: " + mConfig.isUseExternalAudio() +
                " isUseExternalVideo: " + mConfig.isUseExternalVideo() +
                " isUseExternalRecord: " + mConfig.isUseExternalAudioRecord() +
                " isEnableAudioLowLatency: " + mConfig.isEnableAudioLowLatency() +
                " accMixLatency: " + mConfig.getConfigFromServerNotChange().getAccMixingLatencyOnSpeaker() +
                " " + mConfig.getConfigFromServerNotChange().getAccMixingLatencyOnHeadset());

        if (mConfig.isEnableAudio()) {
            initAudioModules();
        }

        if (mConfig.isEnableVideo()) {
            initVideoModules();
            mBytedEffectFilter.initDyEffects();
        }
    }

    @SuppressWarnings("unchecked")
    private void initAudioModules() {
        MyLog.i(TAG, "initAudioModules");
        mAudioFilterMgt = new AudioFilterMgt();
        mAudioReverbFilter = new AudioReverbFilter();
        mScoreResampleFilter = new AudioResampleFilter();
        mCbAudioScorer = new CbAudioScorer();
        // ???mic??????PCM??????
        mRawFrameWriter = new RawFrameWriter();

        if (mConfig.isUseExternalAudio()) {
            mAudioCapture = new AudioCapture(mContext);
            mAudioCapture.setSampleRate(mConfig.getAudioSampleRate());
            mAudioPlayerCapture = new AudioPlayerCapture(mContext);
            mAudioPlayerCapture.setOutFormat(new AudioBufFormat(AVConst.AV_SAMPLE_FMT_S16,
                    mConfig.getAudioSampleRate(), mConfig.getAudioChannels()));
            mRemoteAudioPreview = new AudioPreview(mContext, mConfig.isEnableAudioLowLatency());
            mLocalAudioPreview = new AudioPreview(mContext, mConfig.isEnableAudioLowLatency());
            mAPMFilter = new APMFilter();

            // debug?????????????????????
            mCapRawFrameWriter = new RawFrameWriter();
            mBgmRawFrameWriter = new RawFrameWriter();
            mAudioCapture.getSrcPin().connect((SinkPin<AudioBufFrame>) mCapRawFrameWriter.getSinkPin());
            mAudioPlayerCapture.getSrcPin().connect((SinkPin<AudioBufFrame>) mBgmRawFrameWriter.getSinkPin());

            // ??????????????????
            mAudioRemoteSrcPin = mAgoraRTCAdapter.getRemoteAudioSrcPin();
            mAudioRemoteSrcPin.connect(mRemoteAudioPreview.getSinkPin());

            if (mConfig.isUseLocalAPM()) {
                mAudioCapture.getSrcPin().connect(mAPMFilter.getSinkPin());
                mAudioLocalSrcPin = mAPMFilter.getSrcPin();

                // ??????????????????
                mAPMFilter.enableNs(true);
                mAPMFilter.setNsLevel(APMFilter.NS_LEVEL_1);
                // ??????AEC
                mAPMFilter.enableAEC(true);
            } else {
                mAudioLocalSrcPin = mAudioCapture.getSrcPin();
            }

            mAudioCapture.setAudioCaptureListener(mOnAudioCaptureListener);
            mAudioPlayerCapture.setOnPreparedListener(new AudioPlayerCapture.OnPreparedListener() {
                @Override
                public void onPrepared(AudioPlayerCapture audioPlayerCapture) {
                    MyLog.i(TAG, "AudioPlayerCapture onPrepared");
                    // TODO: ?????????????????????
                    onAudioMixingStateChanged(Constants.MEDIA_ENGINE_AUDIO_EVENT_MIXING_PLAY, 0);
                }
            });
            mAudioPlayerCapture.setOnCompletionListener(new AudioPlayerCapture.OnCompletionListener() {
                @Override
                public void onCompletion(AudioPlayerCapture audioPlayerCapture) {
                    MyLog.i(TAG, "AudioPlayerCapture onCompletion");
                    onAudioMixingFinished();
                }
            });
            mAudioPlayerCapture.setOnFirstAudioFrameDecodedListener(new AudioPlayerCapture.OnFirstAudioFrameDecodedListener() {
                @Override
                public void onFirstAudioFrameDecoded(AudioPlayerCapture audioFileCapture, long time) {
                    MyLog.i(TAG, "AudioPlayerCapture onFirstAudioFrameDecoded: " + time);
                    EngineEvent engineEvent = new EngineEvent(EngineEvent.TYPE_MUSIC_PLAY_FIRST_PKT);
                    engineEvent.setObj(time);
                    EventBus.getDefault().post(engineEvent);
                }
            });
            mAudioPlayerCapture.setOnErrorListener(new AudioPlayerCapture.OnErrorListener() {
                @Override
                public void onError(AudioPlayerCapture audioPlayerCapture, int type, long msg) {
                    MyLog.e(TAG, "AudioPlayerCapture error: " + type);
                    // TODO: ??????????????????
                    onAudioMixingStateChanged(Constants.MEDIA_ENGINE_AUDIO_EVENT_MIXING_ERROR, type);

                    EngineEvent engineEvent = new EngineEvent(EngineEvent.TYPE_MUSIC_PLAY_ERROR);
                    engineEvent.setObj(type);
                    EventBus.getDefault().post(engineEvent);
                }
            });
        } else {
            mAudioLocalSrcPin = mAgoraRTCAdapter.getLocalAudioSrcPin();

            AudioResampleFilter copyFilter = new AudioResampleFilter();
            copyFilter.setOutFormat(new AudioBufFormat(-1, -1, -1), true);
            mAgoraRTCAdapter.getRemoteAudioSrcPin().connect(copyFilter.getSinkPin());
            mAudioRemoteSrcPin = copyFilter.getSrcPin();
        }

        // ????????????????????????
        mHumanVoiceAudioEncoder = new MediaCodecAudioEncoder();
        mHumanVoiceFilePublisher = new MediaMuxerPublisher();
        mAudioLocalSrcPin.connect(mHumanVoiceAudioEncoder.getSinkPin());
        mHumanVoiceAudioEncoder.getSrcPin().connect(mHumanVoiceFilePublisher.getAudioSink());
        mHumanVoiceFilePublisher.setPubListener(mPubListener);

        // ???????????????, ??????16k?????????, ?????????
        AudioBufFormat scoreFormat = new AudioBufFormat(AVConst.AV_SAMPLE_FMT_S16, SCORE_SAMPLE_RATE, 1);
        mScoreResampleFilter.setOutFormat(scoreFormat, true);
        mScoreResampleFilter.setEnableLowLatency(false);
        if (SCORE_DEBUG) {
            mAudioDummyFilter = new AudioDummyFilter();
            mAudioLocalSrcPin.connect(mAudioDummyFilter.getSinkPin());
            mAudioDummyFilter.getSrcPin().connect(mScoreResampleFilter.getSinkPin());
            SrcPin<AudioBufFrame> scoreSrcPin = mScoreResampleFilter.getSrcPin();
            scoreSrcPin.connect(mCbAudioScorer.getSinkPin());
            scoreSrcPin.connect(mAcrRecognizer.getSinkPin());
            mAudioDummyFilter.getSrcPin().connect(mAudioFilterMgt.getSinkPin());
        } else {
            mAudioLocalSrcPin.connect(mScoreResampleFilter.getSinkPin());
            SrcPin<AudioBufFrame> scoreSrcPin = mScoreResampleFilter.getSrcPin();
            scoreSrcPin.connect(mCbAudioScorer.getSinkPin());
            scoreSrcPin.connect(mAcrRecognizer.getSinkPin());
            // ??????????????????
            mAudioLocalSrcPin.connect(mAudioFilterMgt.getSinkPin());
        }

        // ??????????????????
        mAudioFilterMgt.setFilter(mAudioReverbFilter);

        if (mConfig.isUseExternalAudio() || mConfig.isUseExternalAudioRecord()) {
            // ???????????????????????????, ????????????????????????
            mAudioRecordResampleFilter = new AudioResampleFilter();
            mRecordAudioMixer = new AudioMixer();

            if (mConfig.isUseExternalAudio()) {
                // ??????????????????
                mAudioFilterMgt.getSrcPin().connect(mLocalAudioPreview.getSinkPin());
            }
            // PCM dump
            mAudioFilterMgt.getSrcPin().connect((SinkPin<AudioBufFrame>) mRawFrameWriter.getSinkPin());

            if (mConfig.isUseExternalAudio()) {
                // ??????????????????
                mAudioSendResampleFilter = new AudioResampleFilter();
                mAudioSendResampleFilter.setOutFormat(new AudioBufFormat(AVConst.AV_SAMPLE_FMT_S16,
                        mConfig.getAudioSampleRate(), mConfig.getAudioChannels()));
                mLocalAudioMixer = new AudioMixer();
                mLocalAudioPreview.getSrcPin().connect(mAudioSendResampleFilter.getSinkPin());
                mAudioSendResampleFilter.getSrcPin().connect(mLocalAudioMixer.getSinkPin(0));
                mAudioPlayerCapture.getSrcPin().connect(mLocalAudioMixer.getSinkPin(1));
                mLocalAudioMixer.getSrcPin().connect(mAgoraRTCAdapter.getAudioSinkPin());
                mAudioSendSrcPin = mLocalAudioMixer.getSrcPin();
            } else {
                AudioResampleFilter copyFilter = new AudioResampleFilter();
                copyFilter.setOutFormat(new AudioBufFormat(-1, -1, -1), true);
                mAudioFilterMgt.getSrcPin().connect(copyFilter.getSinkPin());
                mAudioSendSrcPin = copyFilter.getSrcPin();
            }

            // ????????????
            mAudioEncoder = new MediaCodecAudioEncoder();
            mFilePublisher = new MediaMuxerPublisher();
            mAudioRecordResampleFilter.getSrcPin().connect(mAudioEncoder.getSinkPin());
            mAudioEncoder.getSrcPin().connect(mFilePublisher.getAudioSink());
            mFilePublisher.setPubListener(mPubListener);
        } else {
            mAudioFilterMgt.getSrcPin().connect((SinkPin<AudioBufFrame>) mRawFrameWriter.getSinkPin());
        }

        if (mConfig.isUseExternalAudio()) {
            toggleAEC();

            // ??????????????????
            if (mConfig.isEnableAudioLowLatency()) {
                mAudioCapture.setAudioCaptureType(AudioCapture.AUDIO_CAPTURE_TYPE_OPENSLES);
                mAudioPlayerCapture.setAudioPlayerType(AudioPlayerCapture.AUDIO_PLAYER_TYPE_OPENSLES);
                mAudioPlayerCapture.setEnableLowLatency(true);
            } else {
                mAudioCapture.setAudioCaptureType(AudioCapture.AUDIO_CAPTURE_TYPE_AUDIORECORDER);
                mAudioPlayerCapture.setAudioPlayerType(AudioPlayerCapture.AUDIO_PLAYER_TYPE_AUDIOTRACK);
                mAudioPlayerCapture.setEnableLowLatency(false);
            }
            int mixingLatency = U.getDeviceUtils().getWiredHeadsetPlugOn() ? mConfig.getConfigFromServerNotChange().getAccMixingLatencyOnHeadset()
                    : mConfig.getConfigFromServerNotChange().getAccMixingLatencyOnSpeaker();
            mLocalAudioMixer.setDelay(1, mixingLatency);
            mAudioCapture.setVolume(mConfig.getRecordingSignalVolume() / 100.f);
            mRemoteAudioPreview.setVolume(mConfig.getPlaybackSignalVolume() / 100.f);
            mLocalAudioPreview.setVolume(mConfig.getEarMonitoringVolume() / 100.f);
            mAudioPlayerCapture.setPlayoutVolume(mConfig.getAudioMixingPlayoutVolume() / 100.f);
            setAudioMixingPublishVolume(mConfig.getAudioMixingPublishVolume());
            if (mConfig.isEnableInEarMonitoring() && shouldStartAudioPreview()) {
                mLocalAudioPreview.start();
            }
        }
    }

    private void doStartAudioCapture() {
        doStartAudioCapture(false);
    }

    private void doStartAudioCapture(boolean isRetry) {
        mAudioCapture.start();

        if (!isRetry) {
            mIsAudioCaptureStarted = true;
            mAudioCaptureRetriedCount = 0;
        } else {
            mAudioCaptureRetriedCount++;
        }

        doUploadAudioCaptureStartEvent();
    }

    private void doStopAudioCapture() {
        doStopAudioCapture(false);
    }

    private void doStopAudioCapture(boolean isRetry) {
        mAudioCapture.stop();

        if (!isRetry) {
            mIsAudioCaptureStarted = false;
        }
    }

    private void doUploadAudioCaptureStartEvent() {
        MyLog.i(TAG, "doUploadAudioCaptureStartEvent");
        Skr.AudioCaptureEvent event = new Skr.AudioCaptureEvent();
        event.type = 1;
        event.errCode = 0;
        event.retriedCount = mAudioCaptureRetriedCount;
        SDataManager.getInstance().getDataHolder().addAudioCaptureEvent(event);
    }

    private void doUploadAudioCaptureFailedEvent(int errCode) {
        MyLog.i(TAG, "doUploadAudioCaptureFailedEvent");
        Skr.AudioCaptureEvent event = new Skr.AudioCaptureEvent();
        event.type = 2;
        event.errCode = errCode;
        event.retriedCount = mAudioCaptureRetriedCount;
        SDataManager.getInstance().getDataHolder().addAudioCaptureEvent(event);
    }

    private AudioCapture.OnAudioCaptureListener mOnAudioCaptureListener = new AudioCapture.OnAudioCaptureListener() {
        @Override
        public void onStatusChanged(int status) {
            MyLog.i(TAG, "AudioCapture onStatusChanged: " + status);
        }

        @Override
        public void onFirstPacketReceived(long time) {
            MyLog.i(TAG, "AudioCapture onFirstPacketReceived: " + time);
            if (mConfig.isRecordingForBusi()) {
                EngineEvent engineEvent = new EngineEvent(EngineEvent.TYPE_RECORD_AUDIO_FIRST_PKT);
                engineEvent.setObj(time);
                EventBus.getDefault().post(engineEvent);
            }
        }

        @Override
        public void onError(int errorCode) {
            MyLog.e(TAG, "AudioCapture onError err: " + errorCode);
            doUploadAudioCaptureFailedEvent(errorCode);
            mCustomHandlerThread.postDelayed(() -> {
                if (mIsAudioCaptureStarted) {
                    if (mAudioCaptureRetriedCount < 3) {
                        MyLog.i(TAG, "try to restart audio capture the " + mAudioCaptureRetriedCount + " time");
                        doStopAudioCapture(true);
                        doStartAudioCapture(true);
                    } else {
                        MyLog.e(TAG, "try to restart audio capture failed for 3times, notify app");
                        EngineEvent engineEvent = new EngineEvent(EngineEvent.TYPE_ENGINE_ERROR);
                        EventBus.getDefault().post(engineEvent);
                    }
                }
            }, 100);
        }
    };

    private Publisher.PubListener mPubListener = new Publisher.PubListener() {
        @Override
        public void onInfo(int type, long msg) {
            MyLog.i(TAG, "FilePubListener onInfo type: " + type + " msg: " + msg);
            if (type == Publisher.INFO_STOPPED) {
                EventBus.getDefault().post(new EngineEvent(EngineEvent.TYPE_RECORD_FINISHED));
            }
        }

        @Override
        public void onError(int err, long msg) {
            MyLog.e(TAG, "FilePubListener onError err: " + err + " msg: " + msg);
            EventBus.getDefault().post(new EngineEvent(EngineEvent.TYPE_RECORD_ERROR));
        }
    };

    private void connectRecord(AudioBufFormat format) {
        if (mConfig.isUseExternalAudioRecord() || mConfig.isUseExternalAudio()) {
            mAudioRecordResampleFilter.setOutFormat(format);
            mAudioRecordResampleFilter.setEnableLowLatency(false);
            // TODO: ??????????????????, ?????????????????????????????????
            mRecordAudioMixer.setMainSinkPinIndex((mConfig.isAnchor() || !mConfig.isJoinChannelSuccess()) ? 0 : 1);
            mAudioSendSrcPin.connect(mRecordAudioMixer.getSinkPin(0));
            mAudioRemoteSrcPin.connect(mRecordAudioMixer.getSinkPin(1));
            mRecordAudioMixer.getSrcPin().connect(mAudioRecordResampleFilter.getSinkPin());
        }
    }

    private void disconnectRecord() {
        if (mConfig.isUseExternalAudioRecord() || mConfig.isUseExternalAudio()) {
            // ??????????????????????????????
            if (mRecordAudioMixer.getMainSinkPinIndex() == 0) {
                mAudioRemoteSrcPin.disconnect(mRecordAudioMixer.getSinkPin(1), false);
                mAudioSendSrcPin.disconnect(mRecordAudioMixer.getSinkPin(0), false);
            } else {
                mAudioSendSrcPin.disconnect(mRecordAudioMixer.getSinkPin(0), false);
                mAudioRemoteSrcPin.disconnect(mRecordAudioMixer.getSinkPin(1), false);
            }
            mRecordAudioMixer.getSrcPin().disconnect(mAudioRecordResampleFilter.getSinkPin(), false);
        }
    }

    private void toggleAEC() {
        if (mConfig.isUseExternalAudio()) {
            if (U.getDeviceUtils().getWiredHeadsetPlugOn() || U.getDeviceUtils().getBlueToothHeadsetOn()) {
                if (mConfig.isUseLocalAPM()) {
                    // ??????APM??????????????????????????????20ms???????????????????????????????????????bypass???
                    //mAPMFilter.enableAEC(false);
                    mAPMFilter.setBypass(true);
                } else {
                    mAgoraRTCAdapter.setEnableAPM(false);
                }
            } else {
                if (mConfig.isUseLocalAPM()) {
                    //mAPMFilter.enableAEC(true);
                    mAPMFilter.setBypass(false);
                } else {
                    mAgoraRTCAdapter.setEnableAPM(true);
                }
            }
            // ????????????????????????
            //TODO  ?????????????????????????????????????????????0 ????????????????????????
            if (lastAudioMixingPublishVolume >= 0) {
                setAudioMixingPublishVolume(lastAudioMixingPublishVolume);
            } else {
                setAudioMixingPublishVolume(mConfig.getAudioMixingPublishVolume());
            }
        }
    }

    public boolean isInit() {
        return mStatus == STATUS_INITED;
    }

    /**
     * ?????????????????????????????????????????????????????????????????????????????????????????????????????????init???????????????
     * ???????????????EngineEvent.TYPE_ENGINE_INITED???????????????????????????????????????
     *
     * @return Params??????
     */
    public Params getParams() {
        return mConfig;
    }

    /**
     * ????????????
     */
    public void leaveChannel() {
        stopAudioMixing();
        mCustomHandlerThread.post(new LogRunnable("leaveChannel") {
            @Override
            public void realRun() {
                doLeaveChannel();
            }
        });
    }

    private void doLeaveChannel() {
        if (mConfig.isUseExternalAudio()) {
            mRemoteAudioPreview.stop();
            mLocalAudioPreview.stop();
            doStopAudioCapture();
            mAudioPlayerCapture.stop();
        }
        mAgoraRTCAdapter.leaveChannel();
    }

    /**
     * ????????????
     */
    public void destroy(final String from) {
        MyLog.i(TAG, "destroy" + " from=" + from);
        if (!"force".equals(from)) {
            if (mInitFrom != null && !mInitFrom.equals(from)) {
                MyLog.i(TAG, "mInitFrom=" + mInitFrom + " from=" + from + " cancel");
                return;
            }
        }
        // ???????????????????????????????????????
        mCustomHandlerThread.removeCallbacksAndMessages(null);
        // ???????????????????????????, ???????????????????????????
        stopAudioMixing();
        mCustomHandlerThread.post(new LogRunnable("destroy" + " from=" + from + " status=" + mStatus) {
            @Override
            public void realRun() {
                if (from.equals(mInitFrom)) {
                    destroyInner();
                }
            }
        });
    }

    private void destroyInner() {
        MyLog.i(TAG, "destroy inner");
        // destroy??????????????????????????????
        doStopAudioRecordingInner();
        mConfig.setAnchor(false);
        mIsAudioCaptureStarted = false;
        MyLog.i(TAG, "destroyInner1");
        if (mCustomHandlerThread != null) {
            mCustomHandlerThread.removeMessage(MSG_JOIN_ROOM_AGAIN);
            mCustomHandlerThread.removeMessage(MSG_JOIN_ROOM_TIMEOUT);
        }
        if (mStatus == STATUS_INITED) {
            mStatus = STATUS_UNINITING;
            if (mMusicTimePlayTimeListener != null && !mMusicTimePlayTimeListener.isDisposed()) {
                mMusicTimePlayTimeListener.dispose();
            }
            mConfig.setJoinChannelSuccess(false);
            mIsJoiningChannel = false;
            MyLog.i(TAG, "destroyInner11");
            {
                // ????????????????????????
                mRecordAudioMixer.getSrcPin().disconnect(false);
                mAudioRecordResampleFilter.release();
                mRecordAudioMixer.release();
            }
            MyLog.i(TAG, "destroyInner12");
            if (mConfig.isUseExternalAudio()) {
                // ???????????????Mixer, ???idx???AudioSource????????????release
                mAudioPlayerCapture.release();
                MyLog.i(TAG, "destroyInner13");
                mAudioCapture.release();
            }
            MyLog.i(TAG, "destroyInner2");
            if (mConfig.isEnableVideo() && mConfig.isUseExternalVideo()) {
                mCameraCapture.release();
                mGLRender.release();
            }
            MyLog.i(TAG, "destroyInner3");
            mAgoraRTCAdapter.destroy(true);
            mUserStatusMap.clear();
            mRemoteViewCache.clear();
            mRemoteUserPinMap.clear();
            MyLog.i(TAG, "destroyInner4");
            mConfig = new Params();
            mPendingStartMixAudioParams = null;
            mIsCaptureStarted = false;
            // ??????????????????????????????
            mStatus = STATUS_UNINIT;
            EventBus.getDefault().post(new EngineEvent(EngineEvent.TYPE_ENGINE_DESTROY, null));
            if (EventBus.getDefault().isRegistered(this)) {
                EventBus.getDefault().unregister(this);
            }
            MyLog.i(TAG, "destroyInner5");
        }
    }

    public void startRecord() {
        if (mConfig.isUseExternalAudio()) {
//            CbEngineAdapter.getInstance().startRecord();
        } else {
            U.getToastUtil().showShort("mConfig.isUseZqEngine is false ???cancel");
        }
    }

    private String getToken(String roomId) {
        MyLog.i(TAG, "getToken" + " roomId=" + roomId);
        EngineServerApi agoraTokenApi = ApiManager.getInstance().createService(EngineServerApi.class);
        if (agoraTokenApi != null) {
            Call<ApiResult> apiResultCall = agoraTokenApi.getToken(roomId);
            if (apiResultCall != null) {
                try {
                    Response<ApiResult> resultResponse = apiResultCall.execute();
                    if (resultResponse != null) {
                        ApiResult obj = resultResponse.body();
                        if (obj != null) {
                            if (obj.getErrno() == 0) {
                                String token = obj.getData().getString("token");
                                MyLog.i(TAG, "getToken ?????? token=" + token);
                                return token;
                            }
                        } else {
                            MyLog.w(TAG, "syncMyInfoFromServer obj==null");
                        }
                    }
                } catch (Exception e) {
                    MyLog.e(e);
                }
            }
        }
        return null;
    }

    /**
     * ??????agora?????????
     *
     * @param roomid
     * @param userId
     * @param isAnchor ????????????????????????
     *                 ???????????????????????????
     */
    public void joinRoom(final String roomid, final int userId, final boolean isAnchor, final String token) {
        mCustomHandlerThread.post(new LogRunnable("joinRoom" + " roomid=" + roomid + " userId=" + userId + " isAnchor=" + isAnchor + " token=" + token) {
            @Override
            public void realRun() {
                mConfig.setSelfUid(userId);
                if (mConfig.getChannelProfile() == Params.CHANNEL_TYPE_LIVE_BROADCASTING) {
                    mConfig.setAnchor(isAnchor);
                    mAgoraRTCAdapter.setClientRole(isAnchor);
                    if (isAnchor) {
                        mCustomHandlerThread.removeMessage(MSG_ROLE_CHANGE_TIMEOUT);
                        Message msg = mCustomHandlerThread.obtainMessage();
                        msg.what = MSG_ROLE_CHANGE_TIMEOUT;
                        mCustomHandlerThread.sendMessageDelayed(msg, 3000);
                    }
                }
                joinRoomInner(roomid, userId, token);
            }
        });
    }


    private void joinRoomInner(final String roomid, final int userId, final String token) {
        if (Looper.myLooper() != mCustomHandlerThread.getLooper()) {
            mCustomHandlerThread.post(new LogRunnable("joinRoomInner" + " roomid=" + roomid + " userId=" + userId + " token=" + token) {
                @Override
                public void realRun() {
                    joinRoomInner(roomid, userId, token);
                }
            });
            return;
        }
        mRoomId = roomid;
        String token2 = token;
        if (mTokenEnable) {
            MyLog.i(TAG, "joinRoomInner ????????????????????????token??? token=" + token2);
            if (TextUtils.isEmpty(token2)) {
                // ??????token2???????????????????????????token
                token2 = getToken(roomid);
            } else {
                // token????????????????????????
            }
        } else {
            MyLog.i(TAG, "joinRoomInner ?????????token???????????????????????????????????????????????????");
            if (TextUtils.isEmpty(token)) {
                // ??????token
            } else {
                // ???????????????token???
            }
        }
        joinRoomInner2(roomid, userId, token2);
    }

    private void joinRoomInner2(final String roomid, final int userId, final String token) {
        MyLog.i(TAG, "joinRoomInner2" + " roomid=" + roomid + " userId=" + userId + " token=" + token);
        mLastJoinChannelToken = token;
        doLeaveChannel();

        int retCode = 0;
        // TODO: ?????????????????????????????????????????????????????????
        if (mConfig.getScene() == Params.Scene.audiotest &&
                mConfig.isUseExternalAudio() && mConfig.isUseExternalVideo()) {
            mCustomHandlerThread.post(new Runnable() {
                @Override
                public void run() {
                    onJoinChannelSuccess(roomid, userId, 0);
                }
            });
        } else {
            retCode = mAgoraRTCAdapter.joinChannel(token, roomid, "Extra Optional Data", userId, mSCHolder);
            MyLog.i(TAG, "joinRoomInner2" + " retCode=" + retCode);
        }

        if (retCode < 0) {
            mIsJoiningChannel = false;
            HashMap map = new HashMap();
            map.put("reason", "" + retCode);
            StatisticsAdapter.recordCountEvent("agora", "join_failed", map);
            Message msg = mCustomHandlerThread.obtainMessage();
            msg.what = MSG_JOIN_ROOM_AGAIN;
            JoinParams joinParams = new JoinParams();
            joinParams.roomID = roomid;
            joinParams.token = token;
            joinParams.userId = userId;
            msg.obj = joinParams;
            mCustomHandlerThread.removeMessage(MSG_JOIN_ROOM_AGAIN);
            mCustomHandlerThread.sendMessageDelayed(msg, 4000);
        } else {
            //???????????????
            mConfig.setJoinRoomBeginTs(System.currentTimeMillis());
            Message msg = mCustomHandlerThread.obtainMessage();
            msg.what = MSG_JOIN_ROOM_TIMEOUT;
            JoinParams joinParams = new JoinParams();
            joinParams.roomID = roomid;
            joinParams.token = token;
            joinParams.userId = userId;
            msg.obj = joinParams;
            mCustomHandlerThread.removeMessage(MSG_JOIN_ROOM_TIMEOUT);
            mCustomHandlerThread.sendMessageDelayed(msg, 3000);
        }
    }

    public void setClientRole(final boolean isAnchor) {
        if (mCustomHandlerThread != null) {
            mCustomHandlerThread.post(new LogRunnable("setClientRole" + " isAnchor=" + isAnchor) {
                @Override
                public void realRun() {
                    if (mConfig.isUseExternalAudio() && mConfig.isJoinChannelSuccess()) {
                        if (isAnchor) {
                            doStartAudioCapture();
                            if (mConfig.isEnableInEarMonitoring() && shouldStartAudioPreview()) {
                                mLocalAudioPreview.start();
                            }
                        } else {
                            doStopAudioCapture();
                            mLocalAudioPreview.stop();
                        }
                    }
                    mAgoraRTCAdapter.setClientRole(isAnchor);
                    if (isAnchor != mConfig.isAnchor()) {
                        mConfig.setAnchor(isAnchor);
                        mCustomHandlerThread.removeMessage(MSG_ROLE_CHANGE_TIMEOUT);
                        Message msg = mCustomHandlerThread.obtainMessage();
                        msg.what = MSG_ROLE_CHANGE_TIMEOUT;
                        mCustomHandlerThread.sendMessageDelayed(msg, 3000);
                    }
                }
            });
        }
    }

    /*??????????????????*/

    public void setEnableSpeakerphone(final boolean enableSpeakerphone) {
        if (mCustomHandlerThread != null) {
            mCustomHandlerThread.post(new Runnable() {
                @Override
                public void run() {
                    mConfig.setEnableSpeakerphone(enableSpeakerphone);
                    mAgoraRTCAdapter.setEnableSpeakerphone(enableSpeakerphone);
                }
            });
        }
    }

    /**
     * ????????????????????????????????????
     */
    private void initWhenInChannel() {
        // ?????????????????????
        adjustRecordingSignalVolume(mConfig.getRecordingSignalVolume(), false);
        adjustPlaybackSignalVolume(mConfig.getPlaybackSignalVolume(), false);
        adjustAudioMixingPlayoutVolume(mConfig.getAudioMixingPlayoutVolume(), false);
        adjustAudioMixingPublishVolume(mConfig.getAudioMixingPublishVolume(), false);
        enableInEarMonitoring(mConfig.isEnableInEarMonitoring());

        if (mCustomHandlerThread != null) {
            mCustomHandlerThread.post(new Runnable() {
                @Override
                public void run() {
                    // ??????????????????????????????????????????
                    if (mConfig.isUseExternalAudio()) {
                        if (mConfig.isAnchor()) {
                            doStartAudioCapture();
                        }
                        if (mRemoteAudioPreview != null) {
                            mRemoteAudioPreview.start();
                        }
                    }
                }
            });
        }
    }

    /**
     * ??????????????????
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(DeviceUtils.HeadsetPlugEvent event) {
        if (event.on) {
            setEnableSpeakerphone(false);
            enableInEarMonitoring(false);
        } else {
            setEnableSpeakerphone(true);
            enableInEarMonitoring(false);
        }
        mCustomHandlerThread.post(new Runnable() {
            @Override
            public void run() {
                toggleAEC();
                if (mConfig.isUseExternalAudio() && mConfig.isEnableInEarMonitoring()) {
                    if (shouldStartAudioPreview()) {
                        mLocalAudioPreview.start();
                    } else {
                        mLocalAudioPreview.stop();
                    }
                }
            }
        });
    }

    /**
     * ??????????????????
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(DeviceUtils.IncomingCallEvent event) {
        if (TelephonyManager.EXTRA_STATE_IDLE.equals(event.state)) {
            mCustomHandlerThread.post(() -> {
                MyLog.i(TAG, "Call end, check acc status");
                if (!mConfig.isUseExternalAudio() && !TextUtils.isEmpty(mAccFullPath) && mIsAccStopped) {
                    MyLog.i(TAG, "restart acc after call end");
                    doStartAudioMixing(mAccUrlInUse, mAccRecoverPosition, mAccRemainedLoopCount);
                }
            });
        }
    }

    /**
     * ????????????????????????
     * enableLocalAudio?????????????????????????????????????????????
     * muteLocalAudioStream???????????????????????????????????????
     *
     * @param muted
     */
    public void muteLocalAudioStream(final boolean muted) {
        if (mCustomHandlerThread != null) {
            mCustomHandlerThread.post(new LogRunnable("muteLocalAudioStream muted=" + muted) {
                @Override
                public void realRun() {
                    UserStatus status = new UserStatus(mConfig.getSelfUid());
                    status.setAudioMute(muted);
                    EventBus.getDefault().post(new EngineEvent(EngineEvent.TYPE_USER_MUTE_AUDIO, status));
                    mConfig.setLocalAudioStreamMute(muted);
                    mAgoraRTCAdapter.muteLocalAudioStream(muted);
                }
            });
        }
    }

    /**
     * ??????/??????????????????????????????
     * ????????? A ????????????B C ???????????????????????????????????? A ?????????, A??????????????????
     */
    public void muteAllRemoteAudioStreams(final boolean muted) {
        if (mCustomHandlerThread != null) {
            mCustomHandlerThread.post(new LogRunnable("muteAllRemoteAudioStreams") {
                @Override
                public void realRun() {
                    mConfig.setAllRemoteAudioStreamsMute(muted);
                    mAgoraRTCAdapter.muteAllRemoteAudioStreams(muted);
                }
            });
        }
    }

    /**
     * ??????/??????????????????????????????
     * ????????? A ????????????B C ???????????????????????????????????? A ?????????
     */
    public void muteRemoteAudioStream(final int uid, final boolean muted) {

        if (mCustomHandlerThread != null) {
            mCustomHandlerThread.post(new LogRunnable("muteRemoteAudioStream" + " uid=" + uid + " muted=" + muted) {
                @Override
                public void realRun() {
                    mAgoraRTCAdapter.muteRemoteAudioStream(uid, muted);
                }
            });
        }
    }

    /**
     * ????????????????????????????
     * ????????????
     */
    public void enableInEarMonitoring(final boolean enable) {
        if (mCustomHandlerThread != null) {
            mCustomHandlerThread.post(new Runnable() {
                @Override
                public void run() {
                    enableInEarMonitoringInternal(enable, true);
                }
            });
        }
    }

    private boolean shouldStartAudioPreview() {
        return (U.getDeviceUtils().getWiredHeadsetPlugOn() || mConfig.isEnableAudioPreviewLatencyTest()) &&
                !mConfig.isEnableAudioMixLatencyTest();
    }

    private void enableInEarMonitoringInternal(boolean enable, boolean setConfig) {
        if (setConfig) {
            mConfig.setEarMonitoringSwitch(enable ? 1 : 2);
        }
        if (mConfig.isUseExternalAudio()) {
            if (!mConfig.getConfigFromServerNotChange().hasServerConfig) {
                MyLog.i(TAG, "without server config, switch audio latency mode to " + enable);
                doSetEnableAudioLowLatency(enable);
            }
            if (enable) {
                if (shouldStartAudioPreview()) {
                    mLocalAudioPreview.start();
                }
            } else {
                mLocalAudioPreview.stop();
            }
        } else {
            mAgoraRTCAdapter.enableInEarMonitoring(enable);
        }
    }

    /**
     * ??????????????????
     *
     * @param volume ??????100
     */
    public void setInEarMonitoringVolume(final int volume) {
        if (mCustomHandlerThread != null) {
            mCustomHandlerThread.post(new Runnable() {
                @Override
                public void run() {
                    mConfig.setInEarMonitoringVolume(volume);
                    if (mConfig.isUseExternalAudio()) {
                        mLocalAudioPreview.setVolume(volume / 100.f);
                    } else {
                        mAgoraRTCAdapter.setInEarMonitoringVolume(volume);
                    }
                }
            });
        }
    }

    /**
     * ????????????????????? 0~400 ????????????????????? ??????100
     *
     * @param volume
     */
    public void adjustRecordingSignalVolume(final int volume) {
        adjustRecordingSignalVolume(volume, true);
    }

    public void adjustRecordingSignalVolume(final int volume, final boolean setConfig) {
        if (mCustomHandlerThread != null) {
            mCustomHandlerThread.post(new LogRunnable("adjustRecordingSignalVolume " + volume + " setConfig: " + setConfig) {
                @Override
                public void realRun() {
                    if (setConfig) {
                        mConfig.setRecordingSignalVolume(volume);
                    }
                    if (mConfig.isUseExternalAudio()) {
                        mAudioCapture.setVolume(volume / 100.0f);
                    } else {
                        mAgoraRTCAdapter.adjustRecordingSignalVolume(volume);
                    }
                }
            });
        }
    }

    /**
     * ????????????????????? 0~400 ????????????????????? ??????100
     *
     * @param volume
     */
    public void adjustPlaybackSignalVolume(final int volume) {
        adjustPlaybackSignalVolume(volume, true);
    }

    public void adjustPlaybackSignalVolume(final int volume, final boolean setConfig) {
        if (mCustomHandlerThread != null) {
            mCustomHandlerThread.post(new LogRunnable("adjustPlaybackSignalVolume " + volume + " setConfig: " + setConfig) {
                @Override
                public void realRun() {
                    if (setConfig) {
                        mConfig.setPlaybackSignalVolume(volume);
                    }
                    if (mConfig.isUseExternalAudio()) {
                        mRemoteAudioPreview.setVolume(volume / 100.f);
                    } else {
                        mAgoraRTCAdapter.adjustPlaybackSignalVolume(volume);
                    }
                }
            });
        }
    }
    /*??????????????????*/

    /*????????????????????????*/

    private void doSetAudioEffect(Params.AudioEffect styleEnum, boolean fromInit) {
        if (styleEnum == mConfig.getStyleEnum() && !fromInit) {
            return;
        }

        mConfig.setStyleEnum(styleEnum);

        if (mAudioReverbFilter != null) {
            int type = AudioReverbFilter.AUDIO_REVERB_NONE;
            switch (styleEnum) {
                case none:
                    type = AudioReverbFilter.AUDIO_REVERB_NONE;
                    break;
                case ktv:
                    type = AudioReverbFilter.AUDIO_REVERB_NEW_CENT;
                    break;
                case rock:
                    type = AudioReverbFilter.AUDIO_REVERB_ROCK;
                    break;
                case liuxing:
                    type = AudioReverbFilter.AUDIO_REVERB_POPULAR;
                    break;
                case kongling:
                    type = AudioReverbFilter.AUDIO_REVERB_RNB;
                    break;
                default:
                    break;
            }
            mAudioReverbFilter.setReverbLevel(type);
        }
    }

    public void setAudioEffectStyle(final Params.AudioEffect styleEnum) {
        if (mCustomHandlerThread != null) {
            mCustomHandlerThread.post(new LogRunnable("setAudioEffectStyle") {
                @Override
                public void realRun() {
                    doSetAudioEffect(styleEnum, false);
                }
            });
        }
    }

    private void doSetEnableAudioPreviewLatencyTest(boolean enable) {
        mConfig.setEnableAudioPreviewLatencyTest(enable);
        if (mConfig.isUseExternalAudio()) {
            mAudioCapture.setEnableLatencyTest(enable);
            if (enable) {
                mAudioCapture.setVolume(8.0f);
                mAudioPlayerCapture.setVolume(0.f);
                mLocalAudioPreview.start();
            } else {
                mAudioCapture.setVolume(mConfig.getRecordingSignalVolume() / 100.f);
                mAudioPlayerCapture.setVolume(mConfig.getPlaybackSignalVolume() / 100.f);

                if (!U.getDeviceUtils().getWiredHeadsetPlugOn() && !U.getDeviceUtils().getBlueToothHeadsetOn()) {
                    mLocalAudioPreview.stop();
                }
            }
        }
    }

    /**
     * ?????????????????????????????????
     */
    public void setEnableAudioPreviewLatencyTest(final boolean enable) {
        if (mCustomHandlerThread != null) {
            mCustomHandlerThread.post(new LogRunnable("setEnableAudioPreviewLatencyTest: " + enable) {
                @Override
                public void realRun() {
                    if (enable) {
                        doSetEnableAudioMixLatencyTest(false);
                    }
                    doSetEnableAudioPreviewLatencyTest(enable);
                }
            });
        }
    }

    private void doSetEnableAudioMixLatencyTest(boolean enable) {
        mConfig.setEnableAudioMixLatencyTest(enable);
        if (mConfig.isUseExternalAudio()) {
            if (enable) {
                mLocalAudioPreview.stop();
            }
            setAudioMixingPublishVolume(mConfig.getAudioMixingPublishVolume());
            mAudioPlayerCapture.setEnableLatencyTest(enable);
            mLocalAudioMixer.setEnableLatencyTest(enable);
        }
    }

    /**
     * ?????????????????????????????????
     */
    public void setEnableAudioMixLatencyTest(final boolean enable) {
        if (mCustomHandlerThread != null) {
            mCustomHandlerThread.post(new LogRunnable("setEnableAudioMixLatencyTest: " + enable) {
                @Override
                public void realRun() {
                    if (enable) {
                        doSetEnableAudioPreviewLatencyTest(false);
                    }
                    doSetEnableAudioMixLatencyTest(enable);
                }
            });
        }
    }

    private void doSetEnableAudioLowLatency(boolean enable) {
        if (mConfig.isUseExternalAudio()) {
            if (enable) {
                mAudioCapture.setAudioCaptureType(AudioCapture.AUDIO_CAPTURE_TYPE_OPENSLES);
                mAudioPlayerCapture.setAudioPlayerType(AudioPlayerCapture.AUDIO_PLAYER_TYPE_OPENSLES);
                mAudioPlayerCapture.setEnableLowLatency(true);
            } else {
                mAudioCapture.setAudioCaptureType(AudioCapture.AUDIO_CAPTURE_TYPE_AUDIORECORDER);
                mAudioPlayerCapture.setAudioPlayerType(AudioPlayerCapture.AUDIO_PLAYER_TYPE_AUDIOTRACK);
                mAudioPlayerCapture.setEnableLowLatency(false);
            }
            if (mRemoteAudioPreview.isEnableLowLatency() != enable) {
                MyLog.i(TAG, "recreate RemoteAudioPreview");
                mAudioRemoteSrcPin.disconnect(mRemoteAudioPreview.getSinkPin(), false);
                mRemoteAudioPreview.release();
                mRemoteAudioPreview = new AudioPreview(mContext, enable);
                mRemoteAudioPreview.setVolume(mConfig.getPlaybackSignalVolume() / 100.f);
                if (mConfig.isJoinChannelSuccess()) {
                    mRemoteAudioPreview.start();
                }
                mAudioRemoteSrcPin.connect(mRemoteAudioPreview.getSinkPin());
            }
            if (mLocalAudioPreview.isEnableLowLatency() != enable) {
                MyLog.i(TAG, "recreate LocalAudioPreview");
                mLocalAudioPreview.getSrcPin().disconnect(mAudioSendResampleFilter.getSinkPin(), false);
                mAudioFilterMgt.getSrcPin().disconnect(mLocalAudioPreview.getSinkPin(), false);
                mLocalAudioPreview.release();
                mLocalAudioPreview = new AudioPreview(mContext, enable);
                mLocalAudioPreview.setVolume(mConfig.getEarMonitoringVolume() / 100.f);
                if (mConfig.isEnableInEarMonitoring() && shouldStartAudioPreview()) {
                    if (mAudioCapture.isRecordingState()) {
                        mLocalAudioPreview.start();
                    }
                }
                mLocalAudioPreview.getSrcPin().connect(mAudioSendResampleFilter.getSinkPin());
                mAudioFilterMgt.getSrcPin().connect(mLocalAudioPreview.getSinkPin());
            }
        }
    }

    /**
     * ????????????????????????????????????
     */
    public void setEnableAudioLowLatency(final boolean enable) {
        if (mCustomHandlerThread != null) {
            mCustomHandlerThread.post(new LogRunnable("setEnableAudioLowLatency: " + enable) {
                @Override
                public void realRun() {
                    mConfig.setEnableAudioLowLatency(enable);
                    // ????????????????????????????????????????????????
                    mConfig.getConfigFromServerNotChange().setEnableAudioLowLatency(enable);
                    mConfig.getConfigFromServerNotChange().save2Pref();
                    doSetEnableAudioLowLatency(enable);
                }
            });
        }
    }

    /**
     * ????????????
     */
    public void playEffects(final EffectModel effectModel) {
        if (mCustomHandlerThread != null) {
            mCustomHandlerThread.post(new Runnable() {
                @Override
                public void run() {
                    mAgoraRTCAdapter.playEffects(effectModel);
                }
            });
        }
    }

    public List<EffectModel> getAllEffects() {
        return mAgoraRTCAdapter.getAllEffects();
    }

    /**
     * ???????????????????????????
     * <p>
     * ????????????????????????????????????????????????
     * ????????? [0.5, 2.0] ??????????????????????????????????????????????????????????????? 1.0?????????????????????????????????
     *
     * @param pitch
     */
    public void setLocalVoicePitch(double pitch) {
        MyLog.i(TAG, "setLocalVoicePitch" + " pitch=" + pitch);
        mConfig.setLocalVoicePitch(pitch);
        mAgoraRTCAdapter.setLocalVoicePitch(pitch);
    }

    /**
     * ??????????????????????????????
     */
    public void setLocalVoiceEqualization() {
        mAgoraRTCAdapter.setLocalVoiceEqualization(mConfig.getBandFrequency(), mConfig.getBandGain());
    }

    /**
     * ???????????????????????????
     *
     * @param reverbKey ???????????? Key?????????????????? 5 ??????????????? Key???????????? value ????????????
     * @param value     AUDIO_REVERB_DRY_LEVEL(0)???????????????????????????????????? dry signal??????????????? [-20, 10]???????????? dB
     *                  AUDIO_REVERB_WET_LEVEL(1)?????????????????????????????????????????? wet signal??????????????? [-20, 10]???????????? dB
     *                  AUDIO_REVERB_ROOM_SIZE(2)??????????????????????????????????????????????????????????????????????????????????????? [0, 100]???????????? dB
     *                  AUDIO_REVERB_WET_DELAY(3)???Wet signal ???????????????????????????????????? [0, 200]??????????????????
     *                  AUDIO_REVERB_STRENGTH(4)?????????????????????????????????????????? [0, 100]
     */
    public void setLocalVoiceReverb(int reverbKey, int value) {
        mConfig.setLocalVoiceReverb(reverbKey, value);
        mAgoraRTCAdapter.setLocalVoiceReverb(reverbKey, value);
    }

    PendingStartMixAudioParams mPendingStartMixAudioParams;

    public void startAudioMixing(String filePath, int cycle) {
        startAudioMixing(filePath, null, 0, cycle);
    }

    /**
     * ????????????????????????????????????
     * ????????????????????????????????? onAudioMixingFinished ??????
     *
     * @param filePath ????????????????????????????????????????????????????????????????????????d????????????????????????mp3???mp4???m4a???aac???3gp???mkv???wav ??? flac????????? Supported Media Formats???
     *                 ?????????????????????????????? /assets/ ??????????????? assets ?????????????????????
     *                 ???????????????????????????????????? /assets/ ?????????????????????????????????????????????????????????
     * @param cycle    ??????????????????????????????????????????
     *                 ???????????????????????????
     *                 -1???????????????
     */
    public void startAudioMixing(final String filePath, final String midiPath, final long mixMusicBeginOffset, final int cycle) {
        startAudioMixing(0, filePath, midiPath, mixMusicBeginOffset, cycle);
    }

    public void startAudioMixing(final int uid, final String accPath, final String midiPath, final long mixMusicBeginOffset, final int cycle) {
        if (mCustomHandlerThread != null) {
            //final String accPath = "[{\"url\":\"http://song-static.inframe.mobi/bgm/c879eba70890a7dc43ae4e2901f99c31_2.mp3\",\"cdnType\":\"aliyun\",\"enableCache\":false},{\"url\":\"http://song-static-1.inframe.mobi/bgm/c879eba70890a7dc43ae4e2901f99c31_2.mp3\",\"cdnType\":\"ksc\",\"enableCache\":false}]";
            mCustomHandlerThread.post(new LogRunnable("startAudioMixing" + " uid=" + uid + " accPath=" + accPath + " midiPath=" + midiPath + " mixMusicBeginOffset=" + mixMusicBeginOffset + " cycle=" + cycle) {
                @Override
                public void realRun() {
                    if (TextUtils.isEmpty(accPath)) {
                        MyLog.i(TAG, "??????????????????");
                        return;
                    }
                    String filePath = getUrlFromJson(accPath);
                    if (TextUtils.isEmpty(filePath)) {
                        MyLog.i(TAG, "??????????????????");
                        return;
                    }
                    boolean canGo = false;
                    if (uid <= 0) {
                        canGo = true;
                    } else {
                        UserStatus userStatus = mUserStatusMap.get(uid);
                        MyLog.i(TAG, "startAudioMixing userStatus=" + userStatus);
                        if ((userStatus == null || !mConfig.isJoinChannelSuccess()) && !mConfig.isUseExternalAudio()) {
                            MyLog.w(TAG, "???????????????????????????,?????????????????????????????????????????????");
                            canGo = false;
                        } else {
                            MyLog.w(TAG, "????????????????????????????????????");
                            canGo = true;
                        }
                    }
                    if (canGo) {
                        // ??????????????????
                        if (!TextUtils.isEmpty(mAccFullPath)) {
                            if (accPath.equals(mAccFullPath)) {
                                MyLog.w(TAG, "startAudioMixing repeatedly, ignore!");
                                return;
                            } else {
                                MyLog.w(TAG, "start another acc, stop the previous one!");
                                doStopAudioMixing();
                            }
                        }

                        mConfig.setCurrentMusicTs(0);
                        mConfig.setRecordCurrentMusicTsTs(0);
                        mConfig.setMixMusicPlaying(true);
                        mConfig.setMixMusicFilePath(filePath);
                        mConfig.setMidiPath(midiPath);
                        mConfig.setMixMusicBeginOffset(mixMusicBeginOffset);

                        startMusicPlayTimeListener();
                        EngineEvent engineEvent = new EngineEvent(EngineEvent.TYPE_MUSIC_PLAY_START);
                        EventBus.getDefault().post(engineEvent);

                        mAccFullPath = accPath;
                        mAccPreparedSent = false;
                        mAccRecoverPosition = 0;
                        mAccRemainedLoopCount = cycle;
                        mIsAccPrepared = false;
                        mAccRetriedCount = 0;
                        mAccFirstStartTime = System.currentTimeMillis();
                        mAccStartTime = mAccFirstStartTime;
                        mAccUrlInUse = getUrlToPlay();
                        doStartAudioMixing(mAccUrlInUse, 0, cycle);
                    } else {
                        mPendingStartMixAudioParams = new PendingStartMixAudioParams();
                        mPendingStartMixAudioParams.uid = uid;
                        mPendingStartMixAudioParams.filePath = accPath;
                        mPendingStartMixAudioParams.midiPath = midiPath;
                        mPendingStartMixAudioParams.mixMusicBeginOffset = mixMusicBeginOffset;
                        mPendingStartMixAudioParams.cycle = cycle;
                    }
                }
            });
        }
    }

    private void doStartAudioMixing(String url, long startOffset, int loopCount) {
        mIsAccStopped = false;
        if (mIsAccPrepared) {
            mAccStartTime = System.currentTimeMillis();
            mIsAccPrepared = false;
        }
        if (mConfig.isUseExternalAudio()) {
            mAudioPlayerCapture.start(url, startOffset, -1, loopCount);
        } else {
            mAgoraRTCAdapter.startAudioMixing(url, false, false, loopCount);
            if (startOffset > 0) {
                mAgoraRTCAdapter.setAudioMixingPosition((int) startOffset);
            }
        }
    }

    private String getUrlFromJson(String accPath) {
        try {
            JSONArray accList = new JSONArray(accPath);
            JSONObject accObj = accList.getJSONObject(0);
            String url = accObj.getString("url");
            mCdnType = accObj.getString("cdnType");
            mAccEnableCache = accObj.getBoolean("enableCache");
            return url;
        } catch (JSONException e) {
            MyLog.i(TAG, "Not a json url list!");
            mCdnType = "";
            mAccEnableCache = false;
            return accPath;
        }
    }

    private boolean isHttpUrl(String url) {
        return url.startsWith("http://") || url.startsWith("https://");
    }

    private String getUrlToPlay() {
        String url = mConfig.getMixMusicFilePath();
        if (TextUtils.isEmpty(url)) {
            return null;
        }

        String urlToPlay = url;
        if (isHttpUrl(urlToPlay) && mAccEnableCache) {
            urlToPlay = MediaCacheManager.INSTANCE.getProxyUrl(urlToPlay, true);
        }
        return urlToPlay;
    }

    private void doUploadAccStartEvent(int errCode) {
        MyLog.i(TAG, "doUploadAccStartEvent " + errCode);
        long now = System.currentTimeMillis();
        long prepareTime = -1;
        if (errCode == 0 && mAccStartTime > 0) {
            prepareTime = now - mAccStartTime;
        }

        Skr.PlayerStartInfo startInfo = new Skr.PlayerStartInfo();
        startInfo.cdnType = mCdnType;
        startInfo.enableCache = mAccEnableCache ? 1 : 0;
        startInfo.extAudio = mConfig.isUseExternalAudio() ? 1 : 0;
        startInfo.url = mConfig.getMixMusicFilePath();
        startInfo.urlInUse = mAccUrlInUse;
        startInfo.prepareTime = prepareTime;
        startInfo.errCode = errCode;
        SDataManager.getInstance().getDataHolder().addPlayerStartInfo(startInfo);
    }

    private void doUploadAccStopEvent(int stopReason, int errCode) {
        MyLog.i(TAG, "doUploadAccStopEvent reason: " + stopReason + " err: " + errCode);
        long now = System.currentTimeMillis();
        long duration = -1;
        if (mAccFirstStartTime > 0) {
            duration = now - mAccFirstStartTime;
        }

        Skr.PlayerStopInfo stopInfo = new Skr.PlayerStopInfo();
        stopInfo.cdnType = mCdnType;
        stopInfo.enableCache = mAccEnableCache ? 1 : 0;
        stopInfo.extAudio = mConfig.isUseExternalAudio() ? 1 : 0;
        stopInfo.url = mConfig.getMixMusicFilePath();
        stopInfo.urlInUse = mAccUrlInUse;
        stopInfo.duration = duration;
        stopInfo.isPrepared = mIsAccPrepared ? 1 : 0;
        stopInfo.stopReason = stopReason;
        stopInfo.errCode = errCode;
        stopInfo.retriedCount = mAccRetriedCount;
        SDataManager.getInstance().getDataHolder().addPlayerStopInfo(stopInfo);
    }

    public static class PendingStartMixAudioParams {
        int uid;
        String filePath;
        String midiPath;
        long mixMusicBeginOffset;
        int cycle;
    }

    private void tryPlayPendingMixingMusic(String from) {
        MyLog.i(TAG, "tryPlayPengdingMixingMusic" + " from=" + from);
        if (mCustomHandlerThread != null) {
            mCustomHandlerThread.post(new Runnable() {
                @Override
                public void run() {
                    if (mPendingStartMixAudioParams != null) {
                        MyLog.i(TAG, "??????????????????????????? uid=" + mPendingStartMixAudioParams.uid);
                        startAudioMixing(mPendingStartMixAudioParams.uid,
                                mPendingStartMixAudioParams.filePath,
                                mPendingStartMixAudioParams.midiPath,
                                mPendingStartMixAudioParams.mixMusicBeginOffset,
                                mPendingStartMixAudioParams.cycle);
                        mPendingStartMixAudioParams = null;
                    } else {
                        MyLog.i(TAG, "??????????????????");
                    }
                }
            });
        }
    }

    /**
     * ????????????????????????????????????
     * ?????????????????????????????????
     */
    public void stopAudioMixing() {
        if (mCustomHandlerThread != null) {
            mCustomHandlerThread.post(new LogRunnable("stopAudioMixing") {
                @Override
                public void realRun() {
                    doStopAudioMixing();
                }
            });
        }
    }

    private void doStopAudioMixing() {
        if (!TextUtils.isEmpty(mConfig.getMixMusicFilePath())) {
            doUploadAccStopEvent(0, 0);
            mConfig.setMixMusicPlaying(false);
            mConfig.setMixMusicFilePath(null);
            mConfig.setMidiPath(null);
            mConfig.setMixMusicBeginOffset(0);
            mAccFullPath = null;
            mAccUrlInUse = null;
            mIsAccPrepared = false;
            mIsAccStopped = false;
            mAccStartTime = 0;
            mAccFirstStartTime = 0;
            mAccRetriedCount = 0;
            stopMusicPlayTimeListener();
            EngineEvent engineEvent = new EngineEvent(EngineEvent.TYPE_MUSIC_PLAY_STOP);
            EventBus.getDefault().post(engineEvent);

            doStopAudioMixingInternal();
        }
        mPendingStartMixAudioParams = null;
        mConfig.setCurrentMusicTs(0);
        mConfig.setRecordCurrentMusicTsTs(0);
        mConfig.setLrcHasStart(false);
    }

    private void doStopAudioMixingInternal() {
        if (mConfig.isUseExternalAudio()) {
            mAudioPlayerCapture.stop();
        } else {
            mAgoraRTCAdapter.stopAudioMixing();
        }
    }

    /**
     * ??????????????????
     */
    public void resumeAudioMixing() {
        if (mCustomHandlerThread != null) {
            mCustomHandlerThread.post(new LogRunnable("resumeAudioMixing") {
                @Override
                public void realRun() {
                    if (!TextUtils.isEmpty(mConfig.getMixMusicFilePath())) {
                        mConfig.setMixMusicPlaying(true);
                        startMusicPlayTimeListener();
                        EngineEvent engineEvent = new EngineEvent(EngineEvent.TYPE_MUSIC_PLAY_START);
                        EventBus.getDefault().post(engineEvent);

                        if (mConfig.isUseExternalAudio()) {
                            mAudioPlayerCapture.resume();
                        } else {
                            mAgoraRTCAdapter.resumeAudioMixing();
                        }
                    }
                }
            });
        }
    }

    /**
     * ?????????????????????????????????
     */
    public void pauseAudioMixing() {
        if (mCustomHandlerThread != null) {
            mCustomHandlerThread.post(new LogRunnable("pauseAudioMixing") {
                @Override
                public void realRun() {
                    if (!TextUtils.isEmpty(mConfig.getMixMusicFilePath())) {
                        mConfig.setMixMusicPlaying(false);
                        stopMusicPlayTimeListener();
                        EngineEvent engineEvent = new EngineEvent(EngineEvent.TYPE_MUSIC_PLAY_PAUSE);
                        EventBus.getDefault().post(engineEvent);

                        if (mConfig.isUseExternalAudio()) {
                            mAudioPlayerCapture.pause();
                        } else {
                            mAgoraRTCAdapter.pauseAudioMixing();
                        }
                    }
                }
            });
        }
    }

    private void startMusicPlayTimeListener() {
        if (mMusicTimePlayTimeListener != null && !mMusicTimePlayTimeListener.isDisposed()) {
            mMusicTimePlayTimeListener.dispose();
        }

        mMusicTimePlayTimeListener = Observable
                .interval(1000, 1000, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.computation())
                .subscribe(new Consumer<Long>() {
                    int duration = -1;

                    @Override
                    public void accept(Long aLong) throws Exception {
                        int currentPosition = getAudioMixingCurrentPosition();
                        MyLog.d(TAG, "PlayTimeListener accept timerTs=" + aLong + " currentPosition=" + currentPosition);
                        mConfig.setCurrentMusicTs(currentPosition);
                        mConfig.setRecordCurrentMusicTsTs(System.currentTimeMillis());
                        if (duration <= 0) {
                            duration = getAudioMixingDuration();
                        }
                        if (currentPosition < duration) {
                            EngineEvent engineEvent = new EngineEvent(EngineEvent.TYPE_MUSIC_PLAY_TIME_FLY_LISTENER);
                            engineEvent.obj = new EngineEvent.MixMusicTimeInfo(currentPosition, duration);
                            EventBus.getDefault().post(engineEvent);
                        } else {
                            MyLog.i(TAG, "playtime?????????,currentPostion=" + currentPosition + " duration=" + duration);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        MyLog.w(TAG, throwable.getMessage());
                    }
                });
    }

    private void stopMusicPlayTimeListener() {
        if (mMusicTimePlayTimeListener != null && !mMusicTimePlayTimeListener.isDisposed()) {
            mMusicTimePlayTimeListener.dispose();
        }
    }

    /**
     * ????????????????????????????????????
     *
     * @param volume 1-100 ??????100
     */
    public void adjustAudioMixingPlayoutVolume(final int volume) {
        adjustAudioMixingPlayoutVolume(volume, true);
    }

    public void adjustAudioMixingPlayoutVolume(final int volume, final boolean setConfig) {
        if (mCustomHandlerThread != null) {
            mCustomHandlerThread.post(new LogRunnable("adjustAudioMixingVolume" + " volume=" + volume + " setConfig=" + setConfig) {
                @Override
                public void realRun() {
                    if (setConfig) {
                        mConfig.setAudioMixingPlayoutVolume(volume);
                    }
                    if (mConfig.isUseExternalAudio()) {
                        mAudioPlayerCapture.setPlayoutVolume(volume / 100.f);
                    } else {
                        mAgoraRTCAdapter.adjustAudioMixingPlayoutVolume(volume);
                    }
                }
            });
        }
    }

    private boolean shouldMixAcc() {
        if (U.getDeviceUtils().getWiredHeadsetPlugOn() || U.getDeviceUtils().getBlueToothHeadsetOn()) {
            return true;
        } else {
            return (mConfig.getConfigFromServerNotChange().hasServerConfig &&
                    mConfig.isEnableAudioLowLatency() == mConfig.getConfigFromServerNotChange().isEnableAudioLowLatency() &&
                    mConfig.getConfigFromServerNotChange().getAccMixingLatencyOnSpeaker() > 0);
        }
    }

    // ????????????????????????????????????(??????????????????????????????????????????????????????????????????????????????????????????)
    private void setAudioMixingPublishVolume(int volume) {
        if (mConfig.isUseExternalAudio()) {
            lastAudioMixingPublishVolume = volume;
            float val = shouldMixAcc() ? volume / 100.f : 0;
            val = mConfig.isEnableAudioMixLatencyTest() ? 1.0f : val;
            MyLog.i(TAG, "setAudioMixingPublishVolume to: " + val);
            mLocalAudioMixer.setInputVolume(1, val);
        }
    }

    /**
     * ????????????????????????????????????
     *
     * @param volume 1-100 ??????100
     */
    public void adjustAudioMixingPublishVolume(final int volume, final boolean setConfig) {
        if (mCustomHandlerThread != null) {
            mCustomHandlerThread.post(new LogRunnable("adjustAudioMixingPublishVolume" + " volume=" + volume + " setConfig=" + setConfig) {
                @Override
                public void realRun() {
                    if (setConfig) {
                        mConfig.setAudioMixingPublishVolume(volume);
                    }
                    if (mConfig.isUseExternalAudio()) {
                        setAudioMixingPublishVolume(volume);
                    } else {
                        mAgoraRTCAdapter.adjustAudioMixingPublishVolume(volume);
                    }
                }
            });
        }
    }

    /**
     * @return ???????????????????????????ms
     */
    public int getAudioMixingDuration() {
        if (mConfig.isUseExternalAudio()) {
            return (int) mAudioPlayerCapture.getDuration();
        } else {
            return mAgoraRTCAdapter.getAudioMixingDuration();
        }
    }

    /**
     * @return ?????????????????????????????? ms
     */
    public int getAudioMixingCurrentPosition() {
        if (mConfig.isUseExternalAudio()) {
            return (int) mAudioPlayerCapture.getPosition();
        } else {
            return mAgoraRTCAdapter.getAudioMixingCurrentPosition();
        }
    }

    /**
     * ?????????????????????
     *
     * @param posMs
     */
    public void setAudioMixingPosition(final int posMs) {

        if (mCustomHandlerThread != null) {
            mCustomHandlerThread.post(new LogRunnable("setAudioMixingPosition" + " posMs=" + posMs) {
                @Override
                public void realRun() {
                    if (mConfig.isUseExternalAudio()) {
                        mAudioPlayerCapture.seek(posMs);
                    } else {
                        mAgoraRTCAdapter.setAudioMixingPosition(posMs);
                    }
                }
            });
        }

    }

    private long lastTrimFeedbackFileSizeTs = 0;

    private void tryStartRecordForFeedback(String from) {
        if (!OPEN_AUDIO_RECORD_FOR_CALLBACK) {
            return;
        }
        if (!mConfig.isJoinChannelSuccess()) {
            return;
        }
        boolean hasAnchor = false;
        /**
         * ???????????????id ??????????????????????????????id??????????????????????????????????????????????????????????????????
         * ????????????????????????
         */
        String anchor = "";
        for (UserStatus us : mUserStatusMap.values()) {
            MyLog.i(TAG, " us=" + us);
            if (us.isAnchor()) {
                anchor = String.valueOf(us.getUserId());
                hasAnchor = true;
            }
        }

        if (!hasAnchor) {
            MyLog.i(TAG, "????????????????????? from=" + from);
            return;
        }
        if (mConfig.isRecordingForBusi()) {
            MyLog.i(TAG, "????????????????????????????????? from=" + from);
            return;
        }
        if (mConfig.isRecordingForFeedback()) {
            MyLog.i(TAG, "????????????????????????????????? from=" + from);
            return;
        }


        if (mCustomHandlerThread != null) {
            // ????????????????????????????????????????????????????????????????????????????????????????????????????????????1s?????????????????????????????????????????? ?????????????????????????????????
            mConfig.setRecordingForFeedback(true);
            startAudioRecordingInner(getFeedbackFilepath(anchor), false, mConfig.getAudioSampleRate(), 1, 48 * 1000);
            mCustomHandlerThread.post(new LogRunnable("trimFeedbackFileSize") {
                @Override
                public void realRun() {
                    if (System.currentTimeMillis() - lastTrimFeedbackFileSizeTs > 10 * 60 * 1000) {
                        trimFeedbackFileSize();
                    }
                }
            });
        }
    }

    public String getFeedbackFilepath(String anchors) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd_HH_mm_ss");
        String fileName = String.format("%s_%s@%s.m4a", mInitFrom, simpleDateFormat.format(new Date(System.currentTimeMillis())), anchors);
        String filePath = U.getAppInfoUtils().getFilePathInSubDir(AUDIO_FEEDBACK_DIR, fileName);
        return filePath;
    }

    /**
     * ??????????????????????????????????????????size????????????
     */
    private void trimFeedbackFileSize() {
        lastTrimFeedbackFileSizeTs = System.currentTimeMillis();
        long fileSize = 0;
        File feedbackDir = U.getAppInfoUtils().getSubDirFile(AUDIO_FEEDBACK_DIR);
        if (feedbackDir.exists() && feedbackDir.isDirectory()) {
            File[] fileList = feedbackDir.listFiles();
            if (fileList == null || fileList.length == 0) {
                return;
            }
            // ????????????????????????
            Arrays.sort(fileList, new Comparator<File>() {
                public int compare(File f1, File f2) {
                    long diff = f1.lastModified() - f2.lastModified();
                    if (diff > 0)
                        return 1;
                    else if (diff == 0)
                        return 0;
                    else
                        return -1;
                }
            });
            for (int i = fileList.length - 1; i >= 0; i--) {
                if (fileList[i].isFile()) {
                    if (fileList[i].getName().endsWith(".m4a")) {
                        if (fileSize > U.getLogUploadUtils().MAX_AUDIO_FOR_FEEDBACK) {
                            //?????????
                            fileList[i].delete();
                        } else {
                            fileSize += fileList[i].length();
                        }
                    }
                }
            }
        }
    }

    public void tryStopRecordForFeedback(String from, boolean force) {
        if (!OPEN_AUDIO_RECORD_FOR_CALLBACK) {
            return;
        }
        if (force) {

        } else {
            boolean hasAnchor = false;
            for (UserStatus us : mUserStatusMap.values()) {
                MyLog.i(TAG, " us=" + us);
                if (us.isAnchor()) {
                    hasAnchor = true;
                }
            }
            if (hasAnchor) {
                MyLog.i(TAG, "?????????????????????????????? from=" + from);
                return;
            }
        }
        if (!mConfig.isRecordingForFeedback()) {
            MyLog.i(TAG, "???????????????????????????????????? from=" + from);
            return;
        }
        mConfig.setRecordingForFeedback(false);
        stopAudioRecordingInner("ForFeedback " + from);
    }

    public void startAudioRecording(final String path, final boolean recordHumanVoice) {
        if (mCustomHandlerThread != null) {
            if (mConfig.isRecordingForFeedback()) {
                mConfig.setRecordingForFeedback(false);
                stopAudioRecordingInner("??????????????????????????????");
            }
            mConfig.setRecordingForBusi(true);
            startAudioRecordingInner(path, recordHumanVoice, mConfig.getAudioSampleRate(), mConfig.getAudioChannels(), mConfig.getAudioBitrate());
        }
    }

    /**
     * ????????????????????????
     * ?????????m4a?????????
     * ?????????????????????????????????????????????????????????????????????????????? leaveChannel ??????????????????????????????????????????
     */
    private void startAudioRecordingInner(final String path, final boolean recordHumanVoice, final int sampleRate, final int channels, final int bitrate) {
        mCustomHandlerThread.post(new LogRunnable("startAudioRecording" + " path=" + path +
                " recordHumanVoice=" + recordHumanVoice + " mInChannel=" + mConfig.isJoinChannelSuccess() +
                " mConfig.isUseExternalAudioRecord()=" + mConfig.isUseExternalAudioRecord() +
                " " + sampleRate + "Hz channels: " + channels + " " + bitrate / 1000 + "kbps") {
            @Override
            public void realRun() {
                if (mStatus != STATUS_INITED) {
                    MyLog.e(TAG, "startAudioRecordingInner in invalid state: " + mStatus);
                    return;
                }

                File file = new File(path);
                if (!file.getParentFile().exists()) {
                    file.getParentFile().mkdirs();
                }
                if (file.exists()) {
                    file.delete();
                }

                if (!recordHumanVoice) {
                    // ???debug?????????????????????????????????????????????
                    connectRecord(new AudioBufFormat(AVConst.AV_SAMPLE_FMT_S16, sampleRate, channels));
                }
                if (RECORD_FOR_DEBUG) {
                    mRawFrameWriter.start(path);
                    if (mConfig.isUseExternalAudio()) {
                        String subfix = path.substring(path.lastIndexOf('.'));
                        String name = path.substring(0, path.lastIndexOf('.'));
                        mCapRawFrameWriter.start(name + "_cap" + subfix);
                        mBgmRawFrameWriter.start(name + "_bgm" + subfix);
                    }
                } else {
                    EngineEvent engineEvent = new EngineEvent(EngineEvent.TYPE_RECORD_START);
                    EventBus.getDefault().post(engineEvent);
                    if (mConfig.isUseExternalAudioRecord() || mConfig.isUseExternalAudio() || recordHumanVoice) {
                        // ?????????????????????????????????????????????
                        if (mConfig.isUseExternalAudio()) {
                            if (!mConfig.isJoinChannelSuccess() && mAudioCapture != null) {
                                doStartAudioCapture();
                            }
                        }

                        AudioCodecFormat audioCodecFormat =
                                new AudioCodecFormat(AVConst.CODEC_ID_AAC,
                                        AVConst.AV_SAMPLE_FMT_S16,
                                        sampleRate, channels, bitrate);
                        if (recordHumanVoice) {
                            if (mConfig.isUseExternalAudio() && mAudioCapture != null) {
                                audioCodecFormat.sampleRate = mAudioCapture.getSampleRate();
                                audioCodecFormat.channels = mAudioCapture.getChannels();
                                audioCodecFormat.bitrate = 64000 * audioCodecFormat.channels;
                            }
                            mHumanVoiceAudioEncoder.configure(audioCodecFormat);
                            mHumanVoiceFilePublisher.setAudioOnly(true);
                            mHumanVoiceFilePublisher.setUrl(path);
                            mHumanVoiceAudioEncoder.start();
                        } else {
                            mAudioEncoder.configure(audioCodecFormat);
                            mFilePublisher.setAudioOnly(true);
                            mFilePublisher.setUrl(path);
                            mAudioEncoder.start();
                        }
                    } else {
                        mAgoraRTCAdapter.startAudioRecording(path, Constants.AUDIO_RECORDING_QUALITY_HIGH);
                    }
                }
            }
        });
    }

    /**
     * ????????????????????????
     * <p>
     * ????????????????????????
     * ?????????????????????????????????????????? leaveChannel ????????????????????????????????? leaveChannel ??????????????????
     */
    public void stopAudioRecording() {
        if (mCustomHandlerThread != null) {
            if (mConfig.isRecordingForBusi()) {
                mConfig.setRecordingForBusi(false);
                stopAudioRecordingInner("ForBusi");
            }
        }
    }

    /**
     * ????????????????????????
     * <p>
     * ????????????????????????
     * ?????????????????????????????????????????? leaveChannel ????????????????????????????????? leaveChannel ??????????????????
     */
    private void stopAudioRecordingInner(String from) {
        mCustomHandlerThread.post(new LogRunnable("stopAudioRecordingInner from=" + from) {
            @Override
            public void realRun() {
                if (mStatus != STATUS_INITED) {
                    MyLog.e(TAG, "stopAudioRecordingInner in invalid state: " + mStatus);
                    return;
                }
                doStopAudioRecordingInner();
            }
        });
    }

    private void doStopAudioRecordingInner() {
        if (mStatus != STATUS_INITED) {
            return;
        }
        if (!RECORD_FOR_DEBUG) {
            if (mConfig.isUseExternalAudioRecord()) {
                mHumanVoiceAudioEncoder.stop();
                mAudioEncoder.stop();
                // ??????????????????????????????????????????
                if (!mConfig.isJoinChannelSuccess() && mAudioCapture != null) {
                    doStopAudioCapture();
                }
            } else {
                mAgoraRTCAdapter.stopAudioRecording();
            }
        } else {
            mRawFrameWriter.stop();
            EventBus.getDefault().post(new EngineEvent(EngineEvent.TYPE_RECORD_FINISHED));
            if (mConfig.isUseExternalAudio()) {
                mCapRawFrameWriter.stop();
                mBgmRawFrameWriter.stop();
            }
        }
        // ????????????????????????
        disconnectRecord();
    }

    public int getLineScore1() {
        return mCbAudioScorer.getScoreV1();
    }

    public void getLineScore2(int lineNum, Score2Callback callback) {
        mCbAudioScorer.getScoreV2(lineNum, callback);
    }

    /*????????????????????????*/

    /*??????????????????*/

    public void startRecognize(RecognizeConfig recognizeConfig) {
        mAcrRecognizer.startRecognize(recognizeConfig);
    }

    public void setRecognizeListener(AcrRecognizeListener recognizeConfig) {
        mAcrRecognizer.setRecognizeListener(recognizeConfig);
    }

    public void stopRecognize() {
        mAcrRecognizer.stopRecognize();
    }

    public void recognizeInManualMode(int lineNo) {
        mAcrRecognizer.recognizeInManualMode(lineNo);
    }

    /*??????????????????*/

    public static class JoinParams {
        public int userId;
        public String roomID;
        public String token;
    }

    // ??????????????????

    /**
     * Get {@link GLRender} instance.
     *
     * @return GLRender instance.
     */
    public GLRender getGLRender() {
        return mGLRender;
    }

    public ImgTexPreview getImgTexPreview() {
        return mImgTexPreview;
    }

    /**
     * ????????????????????????????????????
     *
     * @return BytedEffectFilter instance.
     */
    public BytedEffectFilter getBytedEffectFilter() {
        return mBytedEffectFilter;
    }

    /**
     * Get {@link CameraCapture} module instance.
     *
     * @return CameraCapture instance.
     */
    public CameraCapture getCameraCapture() {
        return mCameraCapture;
    }

    private void initVideoModules() {
        MyLog.i(TAG, "initVideoModules");
        // Camera preview
        mCameraCapture = new CameraCapture(U.app().getApplicationContext(), mGLRender);
        mImgTexScaleFilter = new ImgTexScaleFilter(mGLRender);
        mBytedEffectFilter = new BytedEffectFilter(mGLRender);
        mImgTexMixer = new ImgTexMixer(mGLRender);
        mImgTexPreviewMixer = new ImgTexMixer(mGLRender);
        mImgTexPreviewMixer.setScalingMode(0, ImgTexMixer.SCALING_MODE_CENTER_CROP);
        mImgTexPreview = new ImgTexPreview(mGLRender);

        // ????????????????????????????????????????????????
        mImgTexScaleFilter.setFlipVertical(true);
        mCameraCapture.getImgTexSrcPin().connect(mImgTexScaleFilter.getSinkPin());
        mImgTexScaleFilter.getSrcPin().connect(mBytedEffectFilter.getImgTexSinkPin());
        // ???????????????????????????
        mImgTexPreviewMixer.setFlipVertical(0, true);
        mImgTexMixer.setFlipVertical(0, true);
        mBytedEffectFilter.getSrcPin().connect(mImgTexPreviewMixer.getSinkPin(0));
        mBytedEffectFilter.getSrcPin().connect(mImgTexMixer.getSinkPin(0));
        mImgTexPreviewMixer.getSrcPin().connect(mImgTexPreview.getSinkPin());
        mImgTexMixer.getSrcPin().connect(mAgoraRTCAdapter.getVideoSinkPin());

        // set listeners
        mImgTexPreview.getGLRender().addListener(mPreviewSizeChangedListener);

        mCameraCapture.setOnCameraCaptureListener(new CameraCapture.OnCameraCaptureListener() {
            @Override
            public void onStarted() {
                MyLog.i(TAG, "CameraCapture ready");
                EventBus.getDefault().post(new EngineEvent(EngineEvent.TYPE_CAMERA_OPENED));
            }

            @Override
            public void onFirstFrameRendered() {
                MyLog.i(TAG, "CameraCapture onFirstFrameRendered");
                EventBus.getDefault().post(new EngineEvent(EngineEvent.TYPE_CAMERA_FIRST_FRAME_RENDERED));
            }

            @Override
            public void onFacingChanged(int facing) {
                MyLog.i(TAG, "CameraCapture onFacingChanged");
                mCameraFacing = facing;
                updateFrontMirror();
                EventBus.getDefault().post(new EngineEvent(EngineEvent.TYPE_CAMERA_FACING_CHANGED));
            }

            @Override
            public void onError(int err) {
                MyLog.e(TAG, "CameraCapture error: " + err);
                EventBus.getDefault().post(new EngineEvent(EngineEvent.TYPE_CAMERA_ERROR));
            }
        });

        // init with offscreen GLRender
        mGLRender.init(1, 1);
    }


    /**
     * Should be called on Activity.onResume or Fragment.onResume.
     */
    public void onResume() {
        mCustomHandlerThread.post(new Runnable() {
            @Override
            public void run() {
                if (mStatus != STATUS_INITED) {
                    return;
                }
                MyLog.i(TAG, "onResume");
                mImgTexPreview.onResume();
            }
        });
    }

    /**
     * Should be called on Activity.onPause or Fragment.onPause.
     */
    public void onPause() {
        mCustomHandlerThread.post(new Runnable() {
            @Override
            public void run() {
                if (mStatus != STATUS_INITED) {
                    return;
                }
                MyLog.i(TAG, "onPause");
                mImgTexPreview.onPause();
            }
        });
    }

    /**
     * Set GLSurfaceView as camera previewer.<br/>
     * Must set once before the GLSurfaceView created.
     *
     * @param surfaceView GLSurfaceView to be set.
     */
    public void setDisplayPreview(final GLSurfaceView surfaceView) {
        mCustomHandlerThread.post(new LogRunnable("setDisplayPreview surfaceView=" + surfaceView) {
            @Override
            public void realRun() {
                if (mStatus != STATUS_INITED) {
                    return;
                }
                MyLog.i(TAG, "setDisplayPreview " + surfaceView);
                mImgTexPreview.setDisplayPreview(surfaceView);
            }
        });
    }

    /**
     * /**
     * Set TextureView as camera previewer.<br/>
     * Must set once before the TextureView ready.
     *
     * @param textureView TextureView to be set.
     */
    public void setDisplayPreview(final TextureView textureView) {
        mCustomHandlerThread.post(new LogRunnable("setDisplayPreview textureView=" + textureView) {
            @Override
            public void realRun() {
                if (mStatus != STATUS_INITED) {
                    return;
                }
                MyLog.i(TAG, "setDisplayPreview " + textureView);
                mImgTexPreview.setDisplayPreview(textureView);
            }
        });
    }

    public View getDisplayPreview() {
        if (mImgTexPreview != null) {
            return mImgTexPreview.getDisplayPreview();
        }
        return null;
    }

    /**
     * Set rotate degrees in anti-clockwise of current Activity.
     *
     * @param rotate Degrees in anti-clockwise, only 0, 90, 180, 270 accepted.
     */
    @SuppressWarnings("SuspiciousNameCombination")
    public void setRotateDegrees(final int rotate) throws IllegalArgumentException {
        mCustomHandlerThread.post(new LogRunnable("setRotateDegrees" + " rotate=" + rotate) {
            @Override
            public void realRun() {
                int degrees = rotate % 360;
                if (degrees % 90 != 0) {
                    throw new IllegalArgumentException("Invalid rotate degrees");
                }
                if (mRotateDegrees == degrees) {
                    return;
                }
                boolean isLastLandscape = (mRotateDegrees % 180) != 0;
                boolean isLandscape = (degrees % 180) != 0;
                if (isLastLandscape != isLandscape) {
                    if (mPreviewWidth > 0 || mPreviewHeight > 0) {
                        setPreviewResolution(mPreviewHeight, mPreviewWidth);
                    }
                    if (mTargetWidth > 0 || mTargetHeight > 0) {
                        setTargetResolution(mTargetHeight, mTargetWidth);
                    }
                }
                mRotateDegrees = degrees;
                mCameraCapture.setOrientation(mRotateDegrees);
            }
        });
    }

    /**
     * get rotate degrees
     *
     * @return degrees Degrees in anti-clockwise, only 0, 90, 180, 270 accepted.
     */
    public int getRotateDegrees() {
        return mRotateDegrees;
    }

    /**
     * Set camera capture resolution.<br/>
     * <p>
     * The set resolution would take effect on next {@link #startCameraPreview()}
     * {@link #startCameraPreview(int)} call.<br/>
     * <p>
     * Both of the set width and height must be greater than 0.
     *
     * @param width  capture width
     * @param height capture height
     */
    public void setCameraCaptureResolution(final int width, final int height) throws IllegalArgumentException {
        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException("Invalid resolution");
        }
        mCustomHandlerThread.post(new LogRunnable("setCameraCaptureResolution" + " width=" + width + " height=" + height) {
            @Override
            public void realRun() {
                mCameraCapture.setPreviewSize(width, height);
            }
        });
    }

    /**
     * Set camera capture resolution.<br/>
     * <p>
     * The set resolution would take effect on next {@link #startCameraPreview()}
     * {@link #startCameraPreview(int)} call.<br/>
     *
     * @param idx Resolution index.<br/>
     * @see #VIDEO_RESOLUTION_360P
     * @see #VIDEO_RESOLUTION_480P
     * @see #VIDEO_RESOLUTION_540P
     * @see #VIDEO_RESOLUTION_720P
     * @see #VIDEO_RESOLUTION_1080P
     */
    public void setCameraCaptureResolution(final int idx) throws IllegalArgumentException {
        if (idx < VIDEO_RESOLUTION_360P ||
                idx > VIDEO_RESOLUTION_1080P) {
            throw new IllegalArgumentException("Invalid resolution index");
        }
        mCustomHandlerThread.post(new LogRunnable("setCameraCaptureResolution" + " idx=" + idx) {
            @Override
            public void realRun() {
                int height = getShortEdgeLength(idx);
                int width = height * 16 / 9;
                mCameraCapture.setPreviewSize(width, height);
            }
        });
    }

    /**
     * Set preview resolution.<br/>
     * <p>
     * The set resolution would take effect on next {@link #startCameraPreview()}
     * {@link #startCameraPreview(int)} call, if called not in previewing mode.<br/>
     * If called in previewing mode, it would take effect immediately.<br/>
     * <p>
     * The set width and height must not be 0 at same time.
     * If one of the params is 0, the other would calculated by the actual preview view size
     * to keep the ratio of the preview view.
     *
     * @param width  preview width.
     * @param height preview height.
     */
    public void setPreviewResolution(final int width, final int height) throws IllegalArgumentException {
        if (width < 0 || height < 0 || (width == 0 && height == 0)) {
            throw new IllegalArgumentException("Invalid resolution");
        }
        mCustomHandlerThread.post(new LogRunnable("setPreviewResolution" + " width=" + width + " height=" + height) {
            @Override
            public void realRun() {
                mPreviewWidthOrig = width;
                mPreviewHeightOrig = height;
                doSetPreviewResolution();
            }
        });
    }

    /**
     * Set preview resolution index.<br/>
     * <p>
     * The set resolution would take effect on next {@link #startCameraPreview()}
     * {@link #startCameraPreview(int)} call, if called not in previewing mode.<br/>
     * If called in previewing mode, it would take effect immediately.<br/>
     *
     * @param idx Resolution index.<br/>
     * @see #VIDEO_RESOLUTION_360P
     * @see #VIDEO_RESOLUTION_480P
     * @see #VIDEO_RESOLUTION_540P
     * @see #VIDEO_RESOLUTION_720P
     * @see #VIDEO_RESOLUTION_1080P
     */
    public void setPreviewResolution(final int idx) throws IllegalArgumentException {
        if (idx < VIDEO_RESOLUTION_360P ||
                idx > VIDEO_RESOLUTION_1080P) {
            throw new IllegalArgumentException("Invalid resolution index");
        }
        mCustomHandlerThread.post(new LogRunnable("setPreviewResolution" + " idx=" + idx) {
            @Override
            public void realRun() {
                mPreviewResolution = idx;
                mPreviewWidthOrig = 0;
                mPreviewHeightOrig = 0;
                doSetPreviewResolution();
            }
        });
    }

    private void doSetPreviewResolution() {
        if (mScreenRenderWidth != 0 && mScreenRenderHeight != 0) {
            calResolution();
            mImgTexScaleFilter.setTargetSize(mPreviewWidth, mPreviewHeight);
            mImgTexPreviewMixer.setTargetSize(mPreviewWidth, mPreviewHeight);
        }
    }

    /**
     * get preview width
     *
     * @return preview width
     */
    public int getPreviewWidth() {
        return mPreviewWidth;
    }

    /**
     * get preview height
     *
     * @return preview height
     */
    public int getPreviewHeight() {
        return mPreviewHeight;
    }

    /**
     * Set preview fps.<br/>
     * <p>
     * The set fps would take effect on next {@link #startCameraPreview()}
     * {@link #startCameraPreview(int)} call.<br/>
     * <p>
     * The actual preview fps depends on the running device, may be different with the set value.
     *
     * @param fps frame rate to be set.
     */
    public void setPreviewFps(float fps) throws IllegalArgumentException {
        if (fps <= 0) {
            throw new IllegalArgumentException("the fps must > 0");
        }
        mPreviewFps = fps;
        if (mTargetFps == 0) {
            mTargetFps = mPreviewFps;
        }
    }

    /**
     * get preview frame rate
     *
     * @return preview frame rate
     */
    public float getPreviewFps() {
        return mPreviewFps;
    }

    /**
     * Get current camera preview frame rate.
     *
     * @return current camera preview frame rate
     */
    public float getCurrentPreviewFps() {
        if (mCameraCapture != null) {
            return mCameraCapture.getCurrentPreviewFps();
        } else {
            return 0;
        }
    }

    /**
     * Set streaming resolution.<br/>
     * <p>
     * The set resolution would take effect immediately if streaming started.<br/>
     * <p>
     * The set width and height must not be 0 at same time.
     * If one of the params is 0, the other would calculated by the actual preview view size
     * to keep the ratio of the preview view.
     *
     * @param width  streaming width.
     * @param height streaming height.
     */
    public void setTargetResolution(final int width, final int height) throws IllegalArgumentException {
        if (width < 0 || height < 0 || (width == 0 && height == 0)) {
            throw new IllegalArgumentException("Invalid resolution");
        }
        mCustomHandlerThread.post(new LogRunnable("setTargetResolution" + " width=" + width + " height=" + height) {
            @Override
            public void realRun() {
                mTargetWidthOrig = width;
                mTargetHeightOrig = height;
                doSetTargetResolution();
            }
        });
    }

    /**
     * Set streaming resolution index.<br/>
     * <p>
     * The set resolution would take effect immediately if streaming started.<br/>
     *
     * @param idx Resolution index.<br/>
     * @see #VIDEO_RESOLUTION_360P
     * @see #VIDEO_RESOLUTION_480P
     * @see #VIDEO_RESOLUTION_540P
     * @see #VIDEO_RESOLUTION_720P
     * @see #VIDEO_RESOLUTION_1080P
     */
    public void setTargetResolution(final int idx) throws IllegalArgumentException {
        if (idx < VIDEO_RESOLUTION_360P ||
                idx > VIDEO_RESOLUTION_1080P) {
            throw new IllegalArgumentException("Invalid resolution index");
        }
        mCustomHandlerThread.post(new LogRunnable("setTargetResolution" + " idx=" + idx) {
            @Override
            public void realRun() {
                mTargetResolution = idx;
                mTargetWidthOrig = 0;
                mTargetHeightOrig = 0;
                doSetTargetResolution();
            }
        });
    }

    private void doSetTargetResolution() {
        if (mScreenRenderWidth != 0 && mScreenRenderHeight != 0) {
            calResolution();
            mImgTexMixer.setTargetSize(mTargetWidth, mTargetHeight);
        }
    }

    /**
     * get streaming width
     *
     * @return streaming width
     */
    public int getTargetWidth() {
        return mTargetWidth;
    }

    /**
     * get streaming height
     *
     * @return streaming height
     */
    public int getTargetHeight() {
        return mTargetHeight;
    }

    /**
     * Set streaming fps.<br/>
     * <p>
     * The set fps would take effect after next streaming started.<br/>
     * <p>
     * If actual preview fps is larger than set value,
     * the extra frames will be dropped before encoding,
     * and if is smaller than set value, nothing will be done.
     * default value : 15
     *
     * @param fps frame rate.
     */
    public void setTargetFps(float fps) throws IllegalArgumentException {
        if (fps <= 0) {
            throw new IllegalArgumentException("the fps must > 0");
        }
        mTargetFps = fps;
        if (mPreviewFps == 0) {
            mPreviewFps = mTargetFps;
        }
    }

    /**
     * get streaming fps
     *
     * @return streaming fps
     */
    public float getTargetFps() {
        return mTargetFps;
    }

    /**
     * Set enable front camera mirror or not while streaming.<br/>
     * Would take effect immediately while streaming.
     *
     * @param mirror true to enable, false to disable.
     */
    public void setFrontCameraMirror(final boolean mirror) {
        mCustomHandlerThread.post(new Runnable() {
            @Override
            public void run() {
                mFrontCameraMirror = mirror;
                updateFrontMirror();
            }
        });
    }

    /**
     * check if front camera mirror enabled or not.
     *
     * @return true if mirror enabled, false if mirror disabled.
     */
    public boolean isFrontCameraMirrorEnabled() {
        return mFrontCameraMirror;
    }

    /**
     * Set initial camera facing.<br/>
     * Set before {@link #startCameraPreview()}, give a chance to set initial camera facing,
     * equals {@link #startCameraPreview(int)}.<br/>
     *
     * @param facing camera facing.
     * @see CameraCapture#FACING_FRONT
     * @see CameraCapture#FACING_BACK
     */
    public void setCameraFacing(int facing) {
        mCameraFacing = facing;
    }

    /**
     * get camera facing.
     *
     * @return camera facing
     */
    public int getCameraFacing() {
        return mCameraFacing;
    }

    /**
     * Start camera preview with default facing, or facing set by
     * {@link #setCameraFacing(int)} before.
     */
    public void startCameraPreview() {
        startCameraPreview(mCameraFacing);
    }

    /**
     * Start camera preview with given facing.
     *
     * @param facing camera facing.
     * @see CameraCapture#FACING_FRONT
     * @see CameraCapture#FACING_BACK
     */
    public void startCameraPreview(final int facing) {
        mCustomHandlerThread.post(new LogRunnable("startCameraPreview" + " facing=" + facing) {
            @Override
            public void realRun() {
                if (mStatus != STATUS_INITED) {
                    return;
                }
                mCameraFacing = facing;
                mIsCaptureStarted = true;
                if ((mPreviewWidth == 0 || mPreviewHeight == 0) &&
                        (mScreenRenderWidth == 0 || mScreenRenderHeight == 0)) {
                    if (mImgTexPreview.getDisplayPreview() != null) {
                        mDelayedStartCameraPreview = true;
                        return;
                    }
                    mScreenRenderWidth = DEFAULT_PREVIEW_WIDTH;
                    mScreenRenderHeight = DEFAULT_PREVIEW_HEIGHT;
                }
                setPreviewParams();
                mCameraCapture.start(mCameraFacing);

                // ???????????????????????????????????????
                mImgTexPreviewMixer.setEnableAutoRefresh(false, mPreviewFps);
            }
        });
    }

    /**
     * Stop camera preview.
     */
    public void stopCameraPreview() {

        mCustomHandlerThread.post(new LogRunnable("stopCameraPreview") {
            @Override
            public void realRun() {
                if (mStatus != STATUS_INITED) {
                    return;
                }
                mIsCaptureStarted = false;
                mCameraCapture.stop();
                freeFboCacheIfNeeded();

                // ???????????????????????????????????????
                if (!mRemoteUserPinMap.isEmpty()) {
                    mImgTexPreviewMixer.setEnableAutoRefresh(true, mPreviewFps);
                }
            }
        });
    }

    /**
     * Switch camera facing between front and back.
     */
    public void switchCamera() {
        mCustomHandlerThread.post(new Runnable() {
            @Override
            public void run() {
                if (mStatus != STATUS_INITED) {
                    return;
                }
                mCameraCapture.switchCamera();
            }
        });
    }

    /**
     * Get if current camera in use is front camera.<br/>
     *
     * @return true if front camera in use false otherwise.
     */
    public boolean isFrontCamera() {
        return mCameraFacing == CameraCapture.FACING_FRONT;
    }

    /**
     * Get if torch supported on current camera facing.
     *
     * @return true if supported, false if not.
     * @see #getCameraCapture()
     * @see CameraCapture#isTorchSupported()
     */
    public boolean isTorchSupported() {
        if (mCameraCapture != null) {
            return mCameraCapture.isTorchSupported();
        } else {
            return false;
        }
    }

    /**
     * Toggle torch of current camera.
     *
     * @param open true to turn on, false to turn off.
     * @see #getCameraCapture()
     * @see CameraCapture#toggleTorch(boolean)
     */
    public void toggleTorch(final boolean open) {
        mCustomHandlerThread.post(new Runnable() {
            @Override
            public void run() {
                mCameraCapture.toggleTorch(open);
            }
        });
    }

    /**
     * request screen shot with resolution of the screen
     *
     * @param screenShotListener the listener to be called when bitmap of the screen shot available
     */
    public void requestScreenShot(final GLRender.ScreenShotListener screenShotListener) {
        mCustomHandlerThread.post(new LogRunnable("requestScreenShot" + " screenShotListener=" + screenShotListener) {
            @Override
            public void realRun() {
                mImgTexPreviewMixer.requestScreenShot(screenShotListener);
            }
        });
    }

    /**
     * Set local video shown rect.
     *
     * @param x     x position for left top of logo relative to the video, between 0~1.0.
     * @param y     y position for left top of logo relative to the video, between 0~1.0.
     * @param w     width of logo relative to the video, between 0~1.0, if set to 0,
     *              width would be calculated by h and logo image radio.
     * @param h     height of logo relative to the video, between 0~1.0, if set to 0,
     *              height would be calculated by w and logo image radio.
     * @param alpha alpha value???between 0~1.0
     */
    public void setLocalVideoRect(final float x, final float y, final float w, final float h, float alpha) {
        alpha = Math.max(0.0f, alpha);
        alpha = Math.min(alpha, 1.0f);
        final float a = alpha;
        mCustomHandlerThread.post(new LogRunnable("setLocalVideoRect" + " x=" + x + " y=" + y + " w=" + w + " h=" + h + " alpha=" + alpha) {
            @Override
            public void realRun() {
                if (mImgTexPreviewMixer != null) {
                    mImgTexPreviewMixer.setRenderRect(0, x, y, w, h, a);
                }
                if (mScreenRenderWidth != 0 && mScreenRenderHeight != 0) {
                    // ????????????????????????
                    setPreviewParams();
                }
            }
        });
    }

    /**
     * Bind remote video shown rect with user id.
     *
     * @param userId which user to show
     * @param x      x position for left top of logo relative to the video, between 0~1.0.
     * @param y      y position for left top of logo relative to the video, between 0~1.0.
     * @param w      width of logo relative to the video, between 0~1.0, if set to 0,
     *               width would be calculated by h and logo image radio.
     * @param h      height of logo relative to the video, between 0~1.0, if set to 0,
     *               height would be calculated by w and logo image radio.
     * @param alpha  alpha value???between 0~1.0
     */
    public void bindRemoteVideoRect(final int userId, final float x, final float y, final float w, final float h, float alpha) {
        alpha = Math.max(0.0f, alpha);
        alpha = Math.min(alpha, 1.0f);
        final float a = alpha;

        mCustomHandlerThread.post(new LogRunnable("bindRemoteVideoRect" + " userId=" + userId + " x=" + x + " y=" + y + " w=" + w + " h=" + h + " alpha=" + alpha) {
            @Override
            public void realRun() {
                int idx;
                if (!mRemoteUserPinMap.containsKey(userId)) {
                    idx = getAvailableVideoMixerSink();
                    if (idx < 0) {
                        MyLog.e(TAG, "bindRemoteVideoRect failed!");
                        return;
                    }
                    mAgoraRTCAdapter.addRemoteVideo(userId);
                    mAgoraRTCAdapter.getRemoteVideoSrcPin(userId).connect(mImgTexPreviewMixer.getSinkPin(idx));
                    mRemoteUserPinMap.put(userId, idx);
                } else {
                    idx = mRemoteUserPinMap.get(userId);
                }

                mImgTexPreviewMixer.setScalingMode(idx, ImgTexMixer.SCALING_MODE_CENTER_CROP);
                mImgTexPreviewMixer.setRenderRect(idx, x, y, w, h, a);

                // ???????????????????????????????????????????????????????????????????????????????????????
                if (!mIsCaptureStarted) {
                    mImgTexPreviewMixer.setEnableAutoRefresh(true, mPreviewFps);
                }
            }
        });
    }

    /**
     * Unbind and remove remote video with user id.
     *
     * @param userId which user to unbind
     */
    public void unbindRemoteVideo(final int userId) {
        mCustomHandlerThread.post(new LogRunnable("unbindRemoteVideo" + " userId=" + userId) {
            @Override
            public void realRun() {
                doUnbindRemoteVideo(userId);
                mRemoteUserPinMap.remove(userId);
                freeFboCacheIfNeeded();
            }
        });
    }

    /**
     * Unbind and remove all remote video.
     */
    public void unbindAllRemoteVideo() {
        mCustomHandlerThread.post(new LogRunnable("unbindAllRemoteVideo") {
            @Override
            public void realRun() {
                for (int userId : mRemoteUserPinMap.keySet()) {
                    doUnbindRemoteVideo(userId);
                }
                mRemoteUserPinMap.clear();
                // ??????????????????????????????
                if (mImgTexPreviewMixer != null) {
                    mImgTexPreviewMixer.setRenderRect(0, 0, 0, 1.0f, 1.0f, 1.0f);
                }
                freeFboCacheIfNeeded();
            }
        });
    }

    private void doUnbindRemoteVideo(int userId) {
        DebugLogView.println(TAG, "doUnbindRemoteVideo userId=" + userId);
        SrcPin<ImgTexFrame> remoteVideoSrcPin = mAgoraRTCAdapter.getRemoteVideoSrcPin(userId);
        if (remoteVideoSrcPin != null) {
            remoteVideoSrcPin.disconnect(false);
        }
        mAgoraRTCAdapter.removeRemoteVideo(userId);
    }

    private void freeFboCacheIfNeeded() {
        // ????????????????????????????????????
        if (mIsCaptureStarted || !mRemoteUserPinMap.isEmpty()) {
            return;
        }
        mGLRender.queueEvent(new LogRunnable("freeFboCacheIfNeeded") {
            @Override
            public void realRun() {
                // ????????????fbo??????
                mGLRender.clearFboCache();
            }
        });
    }

    /**
     * ?????????????????????SDK ?????????????????????????????????????????????????????????????????????
     * ????????? enableLocalVideo (false) ???????????????????????????????????????????????????????????????????????????
     * ???????????????????????????????????????????????????????????????
     *
     * @param muted
     */
    public void muteLocalVideoStream(final boolean muted) {
        mCustomHandlerThread.post(new Runnable() {
            @Override
            public void run() {
                mAgoraRTCAdapter.muteLocalVideoStream(muted);
            }
        });
    }

    /**
     * ??????/???????????????????????????
     * ???????????????????????? muteAllRemoteVideoStreams (true) ????????????????????????????????????
     * ???????????? API ??????????????????????????? muteAllRemoteVideoStreams (false)??? muteAllRemoteVideoStreams ??????????????????
     * muteRemoteVideoStream ??????????????????
     *
     * @param uid
     * @param muted
     */
    public void muteRemoteVideoStream(final int uid, final boolean muted) {
        mCustomHandlerThread.post(new Runnable() {
            @Override
            public void run() {
                mAgoraRTCAdapter.muteRemoteVideoStream(uid, muted);
            }
        });
    }

    /**
     * ?????????????????????????????????????????????????????????
     *
     * @param muted
     */
    public void muteAllRemoteVideoStreams(final boolean muted) {
        mCustomHandlerThread.post(new Runnable() {
            @Override
            public void run() {
                mAgoraRTCAdapter.muteAllRemoteVideoStreams(muted);
            }
        });
    }

    /**
     * ??????????????????????????????decoded
     *
     * @param userId
     * @return
     */
    public boolean isFirstVideoDecoded(int userId) {
        boolean r = false;
        UserStatus userStatus = mUserStatusMap.get(userId);
        if (userStatus != null) {
            r = userStatus.isEnableVideo() && userStatus.isFirstVideoDecoded();
        }
        MyLog.i(TAG, "isFirstVideoDecoded" + " userId=" + userId + " r=" + r);
        return r;
    }

    private int getAvailableVideoMixerSink() {
        int idx = -1;
        for (int i = 1; i < mImgTexPreviewMixer.getSinkPinNum(); i++) {
            if (!mRemoteUserPinMap.containsValue(i)) {
                MyLog.i(TAG, "get available sink " + i);
                idx = i;
                break;
            }
        }
        if (idx == -1) {
            MyLog.e(TAG, "unable to get available mixer sink!");
        }
        return idx;
    }

    private int getShortEdgeLength(int resolution) {
        switch (resolution) {
            case VIDEO_RESOLUTION_360P:
                return 360;
            case VIDEO_RESOLUTION_480P:
                return 480;
            case VIDEO_RESOLUTION_540P:
                return 540;
            case VIDEO_RESOLUTION_720P:
                return 720;
            case VIDEO_RESOLUTION_1080P:
                return 1080;
            default:
                return 720;
        }
    }

    private int align(int val, int align) {
        return (val + align - 1) / align * align;
    }

    private void calResolution() {
        // ????????????????????????????????????????????????????????????
        RectF previewRect = mImgTexPreviewMixer.getRenderRect(0);
        int localRenderWidth = (int) (mScreenRenderWidth * previewRect.width());
        int localRenderHeight = (int) (mScreenRenderHeight * previewRect.height());

        if (mPreviewWidthOrig == 0 && mPreviewHeightOrig == 0) {
            int val = getShortEdgeLength(mPreviewResolution);
            if (mScreenRenderWidth > mScreenRenderHeight) {
                mPreviewMixerWidth = 0;
                mPreviewMixerHeight = val;
            } else {
                mPreviewMixerWidth = val;
                mPreviewMixerHeight = 0;
            }
            if (localRenderWidth > localRenderHeight) {
                mPreviewWidth = 0;
                mPreviewHeight = val;
            } else {
                mPreviewWidth = val;
                mPreviewHeight = 0;
            }
        } else {
            mPreviewMixerWidth = mPreviewWidthOrig;
            mPreviewMixerHeight = mPreviewHeightOrig;
        }

        if (mTargetWidthOrig == 0 && mTargetHeightOrig == 0) {
            int val = getShortEdgeLength(mTargetResolution);
            if (localRenderWidth > localRenderHeight) {
                mTargetWidth = 0;
                mTargetHeight = val;
            } else {
                mTargetWidth = val;
                mTargetHeight = 0;
            }
        }

        if (mScreenRenderWidth != 0 && mScreenRenderHeight != 0) {
            if (mPreviewMixerWidth == 0) {
                mPreviewMixerWidth = mPreviewMixerHeight * mScreenRenderWidth / mScreenRenderHeight;
            } else if (mPreviewMixerHeight == 0) {
                mPreviewMixerHeight = mPreviewMixerWidth * mScreenRenderHeight / mScreenRenderWidth;
            }
        }

        if (localRenderWidth != 0 && localRenderHeight != 0) {
            if (mPreviewWidth == 0) {
                mPreviewWidth = mPreviewHeight * localRenderWidth / localRenderHeight;
            } else if (mPreviewHeight == 0) {
                mPreviewHeight = mPreviewWidth * localRenderHeight / localRenderWidth;
            }
            if (mTargetWidth == 0) {
                mTargetWidth = mTargetHeight * localRenderWidth / localRenderHeight;
            } else if (mTargetHeight == 0) {
                mTargetHeight = mTargetWidth * localRenderHeight / localRenderWidth;
            }
        }
        mPreviewWidth = align(mPreviewWidth, 8);
        mPreviewHeight = align(mPreviewHeight, 8);
        mPreviewMixerWidth = align(mPreviewMixerWidth, 8);
        mPreviewMixerHeight = align(mPreviewMixerHeight, 8);
        mTargetWidth = align(mTargetWidth, 8);
        mTargetHeight = align(mTargetHeight, 8);

        MyLog.i(TAG, "calResolution: \n" +
                "viewRenderSize: " + mScreenRenderWidth + "x" + mScreenRenderHeight + "\n" +
                "localRenderRect: " + previewRect + "\n" +
                "localRenderSize: " + localRenderWidth + "x" + localRenderHeight + "\n" +
                "previewSize: " + mPreviewWidth + "x" + mPreviewHeight + "\n" +
                "mixerSize: " + mPreviewMixerWidth + "x" + mPreviewMixerHeight + "\n" +
                "targetSize: " + mTargetWidth + "x" + mTargetHeight);
    }

    private void updateFrontMirror() {
        if (mCameraFacing == CameraCapture.FACING_FRONT) {
            mImgTexMixer.setMirror(0, !mFrontCameraMirror);
        } else {
            mImgTexMixer.setMirror(0, false);
        }
    }

    private void setPreviewParams() {
        calResolution();
        mCameraCapture.setOrientation(mRotateDegrees);
        if (mPreviewFps == 0) {
            mPreviewFps = CameraCapture.DEFAULT_PREVIEW_FPS;
        }
        mCameraCapture.setPreviewFps(mPreviewFps);

        mImgTexScaleFilter.setTargetSize(mPreviewWidth, mPreviewHeight);
        mImgTexPreviewMixer.setTargetSize(mPreviewMixerWidth, mPreviewMixerHeight);
        mImgTexMixer.setTargetSize(mTargetWidth, mTargetHeight);
    }

    private void onPreviewSizeChanged(final int width, final int height) {
        mCustomHandlerThread.post(new LogRunnable("onPreviewSizeChanged" + " width=" + width + " height=" + height) {
            @Override
            public void realRun() {
                boolean notifySizeChanged = mScreenRenderWidth != 0 && mScreenRenderHeight != 0;
                mScreenRenderWidth = width;
                mScreenRenderHeight = height;
                setPreviewParams();
                if (mDelayedStartCameraPreview) {
                    mCameraCapture.start(mCameraFacing);
                    mDelayedStartCameraPreview = false;
                }
                if (notifySizeChanged) {
                    // TODO: notify preview size changed
                }
            }
        });
    }

    private GLRender.OnSizeChangedListener mPreviewSizeChangedListener =
            new GLRender.OnSizeChangedListener() {
                @Override
                public void onSizeChanged(int width, int height) {
                    MyLog.i(TAG, "onPreviewSizeChanged: " + width + "x" + height);
                    onPreviewSizeChanged(width, height);
                }
            };
}

