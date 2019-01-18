package com.module.playways.grab.room;

import com.common.rxretrofit.ApiResult;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface GrabRoomServerApi {

    @GET("http://dev.game.inframe.mobi/v1/game/stand-sync-status")
    Observable<ApiResult> syncGameStatus(@Query("gameID") int gameID);

    /**
     * {
     *   "gameID" : 11111,
     *   "roundSeq" : 1
     * }
     * @param body
     * @return
     */
    @PUT("http://dev.game.inframe.mobi/v1/game/stand-want-sing-chance")
    Observable<ApiResult> wangSingChance(@Body RequestBody body);

    /**
     * {
     *   "gameID" : 111,
     *   "roundSeq" : 1
     * }
     * @param body
     * @return
     */
    @PUT("http://dev.game.inframe.mobi/v1/game/stand-pass-sing-no")
    Observable<ApiResult> lightOff(@Body RequestBody body);

    /**
     * {
     *   "gameID" : 30000004,
     *   "itemID" : 11,
     *   "audioURL" : "http://audio-4.xxxxxx.com",
     *   "timeMs" : 1545312998095,
     *   "sign" : "b00a2a588cbde171404fc9336bac0d5c"
     * }
     * @param body
     * @return
     */
    @PUT("http://dev.game.inframe.mobi/v1/game/stand-resource")
    Observable<ApiResult> saveRes(@Body RequestBody body);

    /**
     * {
     *   "gameID" : 111,
     *   "status" : 1
     * }
     * @param body
     * @return
     */
    @PUT("http://dev.game.inframe.mobi/v1/game/stand-swap")
    Observable<ApiResult> swap(@Body RequestBody body);

    /**
     * {
     *   "gameID" : 111
     * }
     * @param body
     * @return
     */
    @PUT("http://dev.game.inframe.mobi/v1/game/stand-exit")
    Observable<ApiResult> exitGame(@Body RequestBody body);

    @GET("http://dev.game.inframe.mobi/v1/game/stand-result")
    Observable<ApiResult> getStandResult(@Query("gameID") int gameID);
}
