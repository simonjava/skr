package com.component.feeds.presenter

import com.alibaba.fastjson.JSON
import com.common.core.myinfo.MyUserInfoManager
import com.common.core.userinfo.model.UserInfoModel
import com.common.mvp.RxLifeCyclePresenter
import com.common.rxretrofit.ApiManager
import com.common.rxretrofit.ApiMethods
import com.common.rxretrofit.ApiObserver
import com.common.rxretrofit.ApiResult
import com.component.feeds.FeedsWatchServerApi
import com.component.feeds.model.FeedsWatchModel
import com.component.feeds.view.IFeedsWatchView

class FeedsWallViewPresenter(val view: IFeedsWatchView, val userInfoModel: UserInfoModel) : RxLifeCyclePresenter() {

    private val mFeedServerApi = ApiManager.getInstance().createService(FeedsWatchServerApi::class.java)


    var mOffset = 0   //偏移量
    private val mCNT = 20  // 默认拉去的个数
    private var mLastUpdatListTime = 0L    //上次拉取请求时间戳

    init {
        addToLifeCycle()
    }

    fun getFeeds(flag: Boolean) {
        val now = System.currentTimeMillis()
        if (!flag) {
            // 10分钟更新一次吧
            if (now - mLastUpdatListTime < 10 * 60 * 1000) {
                view.requestTimeShort()
                return
            }
        }
        getFeeds(0)
    }

    fun getMoreFeeds() {
        getFeeds(mOffset)
    }

    private fun getFeeds(offset: Int) {
        var feedSongType = 1
        if (MyUserInfoManager.getInstance().uid.toInt() != userInfoModel.userId) {
            feedSongType = 2
        }
        ApiMethods.subscribe(mFeedServerApi.queryFeedsList(offset, mCNT, userInfoModel.userId, feedSongType), object : ApiObserver<ApiResult>() {
            override fun process(result: ApiResult?) {
                if (result?.errno == 0) {
                    if (offset == 0) {
                        mLastUpdatListTime = System.currentTimeMillis()
                    }
                    mOffset = result.data.getIntValue("offset")
                    val list = JSON.parseArray(result.data.getString("userSongs"), FeedsWatchModel::class.java)
                    view.addWatchList(list, offset == 0)
                } else {
                    view.requestError()
                }
            }

            override fun onNetworkError(errorType: ErrorType?) {
                super.onNetworkError(errorType)
                view.requestError()
            }

        }, this)

    }

    override fun destroy() {
        super.destroy()
    }
}