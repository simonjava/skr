package com.module.feeds.songmanage

import com.common.rxretrofit.ApiResult
import io.reactivex.Observable
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Query

interface FeedSongManageServerApi {
    /**
     * 获取普通歌曲标签
     */
    @GET("/v1/feed/common-song-tag-list")
    fun getFeedSongTagList(): Call<ApiResult>

    /**
     * 获取家族歌曲标签
     */
    @GET("/v1/feed/common-song-tag-list")
    fun getClubSongTagList(): Call<ApiResult>

    /**
     * 获取翻唱歌曲列表
     */
    @GET("/v1/feed/common-fanchang-list")
    fun getFeedQuickSongList(@Query("offset") offset: Int, @Query("cnt") cnt: Int, @Query("tagType") tagType: Int): Call<ApiResult>

    /**
     * 获取改编歌曲列表
     */
    @GET("/v1/feed/common-gaibian-list")
    fun getFeedChangeSongList(@Query("offset") offset: Int, @Query("cnt") cnt: Int, @Query("tagType") tagType: Int): Call<ApiResult>

    /**
     * 获取改编歌曲列表
     */
    @GET("/v1/club/list-bgm-billboard")
    fun getClubSongList(@Query("offset") offset: Int, @Query("cnt") cnt: Int, @Query("type") tagType: Int): Call<ApiResult>

    /**
     * 搜索普通歌曲
     */
    @GET("v1/feed/common-song-search")
    fun searchFeedSong(@Query("searchContent") searchContent: String): Observable<ApiResult>

    /**
     * 家族作品歌曲搜索
     */
    @GET("/v1/club/search-bgm")
    fun searchClubSong(@Query("keyword") keyword: String): Observable<ApiResult>

    @PUT("/v1/club/report-bgm-click")
    fun reportBgmClick(@Body body: RequestBody): Call<ApiResult>
}