package com.common.webview;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.webkit.ValueCallback;

import com.common.base.BaseActivity;
import com.common.core.permission.SkrAudioPermission;
import com.common.core.permission.SkrCameraPermission;
import com.common.log.MyLog;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static android.os.Build.VERSION_CODES.M;

abstract class CameraAdapWebActivity extends BaseActivity {
    public ValueCallback<Uri[]> mFilePathCallback;
    public ValueCallback<Uri> nFilePathCallback;
    public String mCameraPhotoPath;
    public static final int INPUT_FILE_REQUEST_CODE = 1;
    public static final int INPUT_VIDEO_CODE = 2;
    public static final int INPUT_IMAGE_CODE = 3;
    public Uri photoURI;

    SkrCameraPermission mSkrCameraPermission;
    SkrAudioPermission mSkrAudioPermission;

    public File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* 前缀 */
                ".jpg",         /* 后缀 */
                storageDir      /* 文件夹 */
        );
        mCameraPhotoPath = image.getAbsolutePath();
        return image;
    }

    public SkrCameraPermission getSkrCameraPermission() {
        return mSkrCameraPermission;
    }

    public SkrAudioPermission getSkrAudioPermission() {
        return mSkrAudioPermission;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSkrCameraPermission = new SkrCameraPermission();
        mSkrAudioPermission = new SkrAudioPermission();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSkrCameraPermission.onBackFromPermisionManagerMaybe(getParent());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode != INPUT_FILE_REQUEST_CODE && requestCode != INPUT_VIDEO_CODE && INPUT_IMAGE_CODE != requestCode) {
            super.onActivityResult(requestCode, resultCode, data);
            return;
        }

        Uri[] results = null;
        Uri mUri = null;
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case INPUT_FILE_REQUEST_CODE:
                    if (data == null) {
                        if (Build.VERSION.SDK_INT > M) {
                            mUri = photoURI;
                            results = new Uri[]{mUri};
                        } else {
                            if (mCameraPhotoPath != null) {
                                mUri = Uri.parse(mCameraPhotoPath);
                                results = new Uri[]{Uri.parse(mCameraPhotoPath)};
                            }
                        }
                    } else {
                        Uri nUri = data.getData();
                        if (nUri != null) {
                            mUri = nUri;
                            results = new Uri[]{nUri};
                        }
                    }
                    break;
                case INPUT_VIDEO_CODE:
                    mUri = data.getData();
                    results = new Uri[]{mUri};
                    break;
                case INPUT_IMAGE_CODE:
                    mUri = data.getData();
                    results = new Uri[]{mUri};
                    break;
            }

        } else {
            MyLog.d(getTAG(), "onActivityResult " + " requestCode=" + requestCode + " resultCode=" + resultCode + " data=" + data);
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            if (nFilePathCallback != null) {
                nFilePathCallback.onReceiveValue(mUri);
                nFilePathCallback = null;
            }
            return;
        } else {
            if (mFilePathCallback != null) {
                mFilePathCallback.onReceiveValue(results);
                mFilePathCallback = null;
            }
            return;
        }
    }
}
