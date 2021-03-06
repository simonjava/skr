package com.module.playways.grab.createroom.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.fastjson.JSON;
import com.common.base.BaseFragment;
import com.common.core.permission.SkrAudioPermission;
import com.common.core.permission.SkrCameraPermission;
import com.common.log.MyLog;
import com.common.rxretrofit.ApiManager;
import com.common.rxretrofit.ApiMethods;
import com.common.rxretrofit.ApiObserver;
import com.common.rxretrofit.ApiResult;
import com.common.rxretrofit.ControlType;
import com.common.rxretrofit.RequestControl;
import com.common.utils.U;
import com.common.view.DebounceViewClickListener;
import com.common.view.ex.ExImageView;
import com.dialog.view.TipsDialogView;
import com.module.RouterConstants;
import com.component.busilib.verify.SkrVerifyUtils;
import com.module.playways.grab.room.GrabRoomServerApi;
import com.component.busilib.friends.SpecialModel;
import com.module.playways.grab.createroom.view.SpecialSelectView;
import com.module.playways.room.prepare.model.JoinGrabRoomRspModel;
import com.module.playways.R;

import java.util.HashMap;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;

import static com.component.busilib.beauty.JumpBeautyFromKt.FROM_CREATE_GRAB_ROOM;

/**
 * 选择房间属性
 */
public class GrabCreateSpecialFragment extends BaseFragment {

    ExImageView mIvBack;
    SpecialSelectView mSpecialView;

    SkrAudioPermission mSkrAudioPermission;
    SkrCameraPermission mCameraPermission;
    TipsDialogView mTipsDialogView;
    int mRoomType;
    SkrVerifyUtils mRealNameVerifyUtils = new SkrVerifyUtils();

    @Override
    public int initView() {
        return R.layout.grab_create_specail_fragment_layout;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        mIvBack = (ExImageView) getRootView().findViewById(R.id.iv_back);
        mSpecialView = (SpecialSelectView) getRootView().findViewById(R.id.special_view);

        Bundle bundle = getArguments();
        if (bundle != null) {
            mRoomType = bundle.getInt(GrabCreateRoomFragment.KEY_ROOM_TYPE);
        }

        mSkrAudioPermission = new SkrAudioPermission();
        mCameraPermission = new SkrCameraPermission();

        mIvBack.setOnClickListener(new DebounceViewClickListener() {
            @Override
            public void clickValid(View v) {
                U.getFragmentUtils().popFragment(GrabCreateSpecialFragment.this);
            }
        });

        mSpecialView.setSpecialSelectListner(new SpecialSelectView.SpecialSelectListner() {
            @Override
            public void onClickSpecial(SpecialModel model, List<String> music) {
                if (getActivity() != null && !getActivity().isFinishing()) {
                    if (model.getTagType() == SpecialModel.TYPE_VIDEO) {
                        mSkrAudioPermission.ensurePermission(new Runnable() {
                            @Override
                            public void run() {
                                mCameraPermission.ensurePermission(new Runnable() {
                                    @Override
                                    public void run() {
                                        mRealNameVerifyUtils.checkJoinVideoPermission(new Runnable() {
                                            @Override
                                            public void run() {
                                                // 进入视频预览
                                                ARouter.getInstance()
                                                        .build(RouterConstants.ACTIVITY_BEAUTY_PREVIEW)
                                                        .withInt("mFrom", FROM_CREATE_GRAB_ROOM)
                                                        .withSerializable("mSpecialModel", model)
                                                        .withInt("mRoomType", mRoomType)
                                                        .navigation();
                                                //createRoom(model);
                                            }
                                        });
                                    }
                                }, true);
                            }
                        }, true);
                    } else {
                        mSkrAudioPermission.ensurePermission(new Runnable() {
                            @Override
                            public void run() {
                                mRealNameVerifyUtils.checkJoinAudioPermission(model.getTagID(), new Runnable() {
                                    @Override
                                    public void run() {
                                        createRoom(model);
                                    }
                                });
                            }
                        }, true);
                    }
                }
            }
        });
    }

    /**
     * 创建房间
     */
    private void createRoom(SpecialModel model) {
        MyLog.d(getTAG(), "createRoom" + " model=" + model);
        GrabRoomServerApi grabRoomServerApi = ApiManager.getInstance().createService(GrabRoomServerApi.class);
        HashMap<String, Object> map = new HashMap<>();
        map.put("roomType", mRoomType);
        map.put("tagID", model.getTagID());

        RequestBody body = RequestBody.create(MediaType.parse(ApiManager.APPLICATION_JSON), JSON.toJSONString(map));
        ApiMethods.subscribe(grabRoomServerApi.createRoom(body), new ApiObserver<ApiResult>() {
            @Override
            public void process(ApiResult result) {
                if (result.getErrno() == 0) {
                    JoinGrabRoomRspModel grabCurGameStateModel = JSON.parseObject(result.getData().toString(), JoinGrabRoomRspModel.class);
                    grabCurGameStateModel.setHasGameBegin(false);
                    //先跳转
                    ARouter.getInstance().build(RouterConstants.ACTIVITY_GRAB_ROOM)
                            .withSerializable("prepare_data", grabCurGameStateModel)
                            .withSerializable("special_model", model)
                            .navigation();
                    //结束当前Activity
                    if (getActivity() != null) {
                        getActivity().finish();
                    }
                } else {
                    // 房间创建失败
                    U.getToastUtil().showShort("" + result.getErrmsg());
                }
            }
        }, this, new RequestControl("create-room", ControlType.CancelThis));
    }


    @Override
    public boolean useEventBus() {
        return false;
    }
}
