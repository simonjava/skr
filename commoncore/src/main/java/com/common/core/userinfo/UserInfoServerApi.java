package com.common.core.userinfo;

import com.common.rxretrofit.ApiResult;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Query;

/**
 *
 */
public interface UserInfoServerApi {

    /**
     * 拿到某个人基本的信息
     *
     * @param userID
     * @return
     */
    @GET("v1/uprofile/information")
    Observable<ApiResult> getUserInfo(@Query("userID") int userID);


    /**
     * 拿到某个人的个人主页
     *
     * @param userID
     * @return
     */
    @GET("/v1/skr/homepage")
    Observable<ApiResult> getHomePage(@Query("userID") int userID);


    /**
     * 处理关系
     *
     * @param body "toUserID" : 关系被动接受者id
     *             "action" :  创建关系 or 解除关系
     *             0  未知 RA_UNKNOWN
     *             1  创建关系 RA_BUILD
     *             2  解除关系 RA_UNBUILD
     * @return
     */
    @PUT("/v1/mate/relation")
    Observable<ApiResult> mateRelation(@Body RequestBody body);

    /**
     * 获取指定关系列表
     *
     * @param relation [必选]关系类型   0 未知  RC_UNKNOWN
     *                 1 关注  RC_Follow
     *                 2 粉丝  RC_Fans
     *                 3 好友  RC_Friend
     * @param offset   [必选]偏移
     * @param limit    [必选]限制数量,最大50
     * @return
     */
    @GET("/v1/mate/relation")
    Observable<ApiResult> getRelationList(@Query("relation") int relation,
                                          @Query("offset") int offset,
                                          @Query("limit") int limit);

    /**
     * 获取指定用户的关系数量
     *
     * @param userID
     * @return
     */
    @GET("/v1/mate/cnt")
    Observable<ApiResult> getRelationNum(@Query("userID") int userID);


    /**
     * 判断和指定某人的社交关系
     *
     * @param toUserID 指定人的id
     * @return
     */
    @GET("/v1/mate/has-relation")
    Observable<ApiResult> getRelation(@Query("toUserID") int toUserID);


    /**
     * 获取某人具体的分数
     *
     * @param userID
     * @return
     */
    @GET("/v1/score/detail")
    Observable<ApiResult> getScoreDetail(@Query("userID") int userID);


    /**
     * 获取地域排行榜列表
     * (包含自己的)
     *
     * @param category 地域类别 1国家  2省会  3市级  4区级
     * @param offset   偏移量
     * @param limit    数量
     * @return
     */
    @GET("/v1/rank/region-list")
    Observable<ApiResult> getReginRankList(@Query("category") int category,
                                           @Query("offset") int offset,
                                           @Query("limit") int limit);

    /**
     * 获取好友排行榜列表
     * (包含自己的)
     *
     * @return
     */
    @GET("/v1/rank/friend-list")
    Observable<ApiResult> getFriendRankList();


    /**
     * 获得某人的地域排行榜名次
     *
     * @param userID
     * @return
     */
    @GET("/v1/rank/region-seq")
    Observable<ApiResult> getReginRank(@Query("userID") int userID);


    @PUT("v1/report/upload")
    Observable<ApiResult> report(@Body RequestBody body);
}
