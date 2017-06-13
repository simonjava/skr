package com.mi.liveassistant.account.upload;

import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.mi.liveassistant.account.MyUserInfoManager;
import com.mi.liveassistant.attachment.Attachment;
import com.mi.liveassistant.attachment.AttachmentUtils;
import com.mi.liveassistant.attachment.MessageType;
import com.mi.liveassistant.common.api.ErrorCode;
import com.mi.liveassistant.common.callback.TaskCallBackWrapper;
import com.mi.liveassistant.common.log.MyLog;
import com.mi.liveassistant.common.thread.ThreadPool;
import com.mi.liveassistant.common.utils.CommonUtils;
import com.mi.liveassistant.milink.MiLinkClientAdapter;
import com.mi.liveassistant.upload.UploadTask;

import rx.Observable;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * 异步上传用户信息
 * <p>
 * Created by wuxiaoshan on 17-3-3.
 */
public class UploadRunnable implements Runnable {
    private static final String TAG = UploadRunnable.class.getSimpleName();

    public static final int DEFAULT_GENDER = 0;

    private UploadService.UploadInfo mUploadInfo;

    private long mAvatar;

    private String mAvatarMd5;

    public UploadRunnable(@NonNull UploadService.UploadInfo uploadInfo) {
        mUploadInfo = uploadInfo;
    }

    @Override
    public void run() {
        uploadUserInfo();
    }

    public void uploadUserInfo() {
        MyLog.d(TAG, "uploadUserInfo enter");
        if (!mUploadInfo.needEditUserInfo && !mUploadInfo.isFirstLogin()) {
            return;
        } else if (mUploadInfo.needEditUserInfo) {
            if (!mUploadInfo.hasInnerSex && mUploadInfo.gender <= 0) {
                mUploadInfo.gender = DEFAULT_GENDER;
            }
        }
        MyLog.w(TAG, "uploadUserInfo start,UploadUserInfo:" + mUploadInfo.toString());
        if (!mUploadInfo.hasInnerAvatar && !TextUtils.isEmpty(mUploadInfo.avatar)) {
            String localImgUrl = mUploadInfo.avatar;
            if (mUploadInfo.avatarNeedDownload) {
                localImgUrl = CommonUtils.downloadImg(mUploadInfo.avatar);
            }
            generateAtt(localImgUrl);
        } else {
            upload();
        }
    }

    private void generateAtt(String localPath) {
        MyLog.d(TAG, "generateAtt localPath =" + localPath);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(localPath, options);

        final Attachment imgAtt = new Attachment();
        imgAtt.setType(Attachment.TYPE_IMAGE);
        imgAtt.setLocalPath(localPath);
        imgAtt.setWidth(options.outWidth);
        imgAtt.setHeight(options.outHeight);

        imgAtt.setMimeType(AttachmentUtils.getMimeType(MessageType.IMAGE, imgAtt.getLocalPath()));
        //AnsyTask只能在UI线程做初始化
        ThreadPool.runOnUi(new Runnable() {
            @Override
            public void run() {
                uploadAttachment(imgAtt);
            }
        });

    }

    private void uploadAttachment(final Attachment imgAt) {
        MyLog.d(TAG, "uploadAttachment");
        UploadTask.uploadPhoto(imgAt, Attachment.AUTH_TYPE_AVATAR, new TaskCallBackWrapper() {
                    @Override
                    public void process(Object object) {
                        MyLog.d(TAG, "object = " + object);
                        if ((Boolean) object) {
                            mAvatar = Long.valueOf(System.currentTimeMillis());
                            mAvatarMd5 = imgAt.getMd5();
                            MyLog.w(TAG, "upload avartar to ksy success  mImgAtt url =" + imgAt.getUrl());
                        } else {
                            MyLog.w(TAG, "upload avartar to ksy failure");
                        }
                        Observable.just(0)
                                .observeOn(Schedulers.io())
                                .subscribe(new Action1<Integer>() {
                                    @Override
                                    public void call(Integer integer) {
                                        upload();
                                    }
                                });


                    }
                }
        );
    }

    private void upload() {
        String nickName = null;
        int gender = 0;
        if (!mUploadInfo.hasInnerNickName) {
            nickName = mUploadInfo.nickName;
        }
        if (!mUploadInfo.hasInnerSex) {
            gender = mUploadInfo.gender;
        }
        if (TextUtils.isEmpty(nickName) && mAvatar == 0 && mUploadInfo.hasInnerSex) {
            return;
        }
        if (!MiLinkClientAdapter.getsInstance().isTouristMode()) {
            UploadUserInfoRequest request = new UploadUserInfoRequest(mUploadInfo.uuid, nickName, gender, mAvatar, mAvatarMd5, !mUploadInfo.hasInnerSex);
            int retCode = request.sendRequest();
            if (retCode == ErrorCode.CODE_SUCCESS) {
                MyUserInfoManager.getInstance().syncSelfDetailInfo(mUploadInfo.uuid);
            }
            MyLog.w(TAG, "upload user info retCode=" + retCode);
        } else {
            MyLog.w(TAG, "milink is tourist mode");
        }
    }
}
