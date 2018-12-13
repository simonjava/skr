package com.module.rankingmode.room;

import com.common.rxretrofit.ApiResult;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.PUT;

public interface RoomServerApi {
    /**
     * "{\n\t\"gameID\" : 20000220,\n\t\"content\" : \"hello xxx\"\n}")
     * @param body
     * @return
     */
    @PUT("http://dev.game.inframe.mobi/v1/game/chat")
    Observable<ApiResult> sendMsg(@Body RequestBody body);

    /**
     * 上报结束一轮游戏
     *
     * @param body   游戏标识 gameID (必选)
     * @return
     */
    @PUT("http://dev.game.inframe.mobi/v1/game/round/over")
    Observable<ApiResult> sendRoundOver(@Body RequestBody body);

    /**
     * 当前轮次时上报心跳
     *
     * @param body  游戏标识 gameID (必选)
     *              身份标识 userID (必选）
     * @return
     */
    @PUT("http://dev.game.inframe.mobi/v1/game/hb")
    Observable<ApiResult> sendHeartBeat(@Body RequestBody body);

    /**
     * 退出游戏
     *
     * @param body 游戏标识 gameID (必选)
     * @return
     */
    @PUT("http://dev.game.inframe.mobi/v1/game/exit")
    Observable<ApiResult> exitGame(@Body RequestBody body);

}
