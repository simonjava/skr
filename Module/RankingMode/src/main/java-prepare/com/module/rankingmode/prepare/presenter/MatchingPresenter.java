package com.module.rankingmode.prepare.presenter;

import com.alibaba.fastjson.JSON;
import com.common.log.MyLog;
import com.common.mvp.RxLifeCyclePresenter;
import com.common.rxretrofit.ApiManager;
import com.common.rxretrofit.ApiMethods;
import com.common.rxretrofit.ApiObserver;
import com.common.rxretrofit.ApiResult;
import com.common.utils.HandlerTaskTimer;
import com.common.utils.U;
import com.module.rankingmode.msg.event.JoinActionEvent;
import com.module.rankingmode.prepare.MatchServerApi;
import com.module.rankingmode.prepare.view.IMatchingView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;

import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import static com.common.rxretrofit.ApiManager.APPLICATION_JSOIN;

// 只处理匹配 请求匹配 取消匹配 和 收到加入游戏通知
public class MatchingPresenter extends RxLifeCyclePresenter {
    public static final String TAG = "MatchingPresenter";

    IMatchingView view;
    MatchServerApi matchServerApi;

    Disposable startMatchTask;
    Disposable cancelMatchTask;
    HandlerTaskTimer loopMatchTask;

    public MatchingPresenter(@NonNull IMatchingView view) {
        this.view = view;
        matchServerApi = ApiManager.getInstance().createService(MatchServerApi.class);
        addToLifeCycle();
    }

    //这里获取圆圈动画内的头像
    public void getLoadingUserListIcon(){

    }

    public void startLoopMatchTask(){
        MyLog.d(TAG, "startLoopMatchTask");
        disposeCancelMatchTask();

        loopMatchTask = HandlerTaskTimer.newBuilder()
                .interval(10000)
                .start(new HandlerTaskTimer.ObserverW() {
            @Override
            public void onNext(Integer integer) {
                MyLog.d(TAG, "startLoopMatchTask onNext");
                startMatch();
            }
        });
    }

    private void disposeLoopMatchTask(){
        if(loopMatchTask != null){
            loopMatchTask.dispose();
        }
    }

    public void disposeMatchTask(){
        if(startMatchTask != null && !startMatchTask.isDisposed()){
            startMatchTask.dispose();
        }
    }


    public void disposeCancelMatchTask(){
        if(cancelMatchTask != null && !cancelMatchTask.isDisposed()){
            cancelMatchTask.dispose();
        }
    }

    // 开始匹配
    private void startMatch() {
        MyLog.d(TAG, "startMatch");
        disposeMatchTask();
        // todo 短链接向服务器发送开始匹配请求
        HashMap<String, Object> map = new HashMap<>();
        map.put("mode", 1);
        map.put("playbookItemID", 1);

        RequestBody body = RequestBody.create(MediaType.parse(APPLICATION_JSOIN), JSON.toJSONString(map));
        startMatchTask = ApiMethods.subscribeWith(matchServerApi.startMatch(body), new ApiObserver<ApiResult>() {
            @Override
            public void process(ApiResult result) {
                if (result.getErrno() == 0) {
                    U.getToastUtil().showShort("开始匹配");
                } else {
                    U.getToastUtil().showShort("开始匹配错误");
                }
            }
        }, this);
    }

    // 取消匹配,重新回到开始匹配
    public void cancelMatch(int mode) {
        MyLog.d(TAG, "cancelMatch");
        disposeLoopMatchTask();
        disposeMatchTask();

        HashMap<String, Object> map = new HashMap<>();
        map.put("mode", mode);

        RequestBody body = RequestBody.create(MediaType.parse(APPLICATION_JSOIN), JSON.toJSONString(map));
        cancelMatchTask = ApiMethods.subscribeWith(matchServerApi.cancleMatch(body).retry(3), new ApiObserver<ApiResult>() {
            @Override
            public void process(ApiResult result) {
                if (result.getErrno() == 0) {
                    U.getToastUtil().showShort("取消匹配成功");
                }
            }
        }, this);
    }

    // 加入指令，即服务器通知加入房间的指令
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(JoinActionEvent joinActionEvent) {
        if (joinActionEvent != null) {
            // todo 收到服务器加入游戏的通知
            // 是否要对加入通知进行过滤
            view.matchSucess(joinActionEvent.gameId, joinActionEvent.gameCreateMs);
        }
    }
}
