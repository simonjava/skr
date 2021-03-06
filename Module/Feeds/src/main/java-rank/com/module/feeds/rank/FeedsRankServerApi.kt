package com.module.feeds.rank

import com.common.rxretrofit.ApiResult
import io.reactivex.Observable
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface FeedsRankServerApi {

    /**
     * 全民神曲：神曲榜单标签列表
     */
    @GET("v1/feed/rank-song-tag-list")
    fun getFeedsRankTags(): Call<ApiResult>


    @GET("/v2/feed/rank-song-list")
    fun getFeedRankInfoList(@Query("offset") offset: Int,
                            @Query("cnt") cnt: Int,
                            @Query("tagType") tagType: Int): Call<ApiResult>


    @GET("v1/feed/rank-list-by-challenge-tag")
    fun getFeedRankDetailList(@Query("offset") offset: Int,
                              @Query("cnt") cnt: Int,
                              @Query("userID") userID: Int,
                              @Query("challengeID") challengeID: Long,
                              @Query("tagType") tagType: Int): Call<ApiResult>


    @GET("v2/feed/search-challenge")
    fun searchChallenge(@Query("searchContent") searchContent: String): Observable<ApiResult>
}