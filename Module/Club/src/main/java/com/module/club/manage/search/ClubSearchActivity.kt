package com.module.club.manage.search

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.RelativeLayout
import android.widget.TextView
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.alibaba.fastjson.JSON
import com.common.base.BaseActivity
import com.common.core.userinfo.model.ClubInfo
import com.common.rxretrofit.ApiManager
import com.common.rxretrofit.ApiMethods
import com.common.rxretrofit.ApiObserver
import com.common.rxretrofit.ApiResult
import com.common.utils.U
import com.common.view.DebounceViewClickListener
import com.common.view.ex.NoLeakEditText
import com.component.busilib.model.SearchModel
import com.module.RouterConstants
import com.module.club.ClubServerApi
import com.module.club.IClubModuleService
import com.module.club.R
import com.module.club.home.viewholder.ClubListAdapter
import io.reactivex.ObservableSource
import io.reactivex.functions.Function
import io.reactivex.functions.Predicate
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit

@Route(path = RouterConstants.ACTIVITY_SEARCH_CLUB)
class ClubSearchActivity : BaseActivity() {

    lateinit var searchArea: RelativeLayout
    lateinit var cancleTv: TextView
    lateinit var searchContent: NoLeakEditText
    lateinit var recyclerView: RecyclerView

    private var adapter: ClubListAdapter? = null

    private var publishSubject: PublishSubject<SearchModel>? = null

    private var isAutoSearch: Boolean? = false      // 标记是否是自动搜索

    override fun initView(savedInstanceState: Bundle?): Int {
        return R.layout.club_search_fragment_layout
    }

    override fun initData(savedInstanceState: Bundle?) {
        searchArea = findViewById(R.id.search_area)
        cancleTv = findViewById(R.id.cancle_tv)
        searchContent = findViewById(R.id.search_content)
        recyclerView = findViewById(R.id.recycler_view)

        adapter = ClubListAdapter(object : ClubListAdapter.Listener {
            override fun onClickItem(position: Int, model: ClubInfo?) {
                model?.let {
                    U.getKeyBoardUtils().hideSoftInputKeyBoard(this@ClubSearchActivity)
                    val clubServices = ARouter.getInstance().build(RouterConstants.SERVICE_CLUB).navigation() as IClubModuleService
                    clubServices.tryGoClubHomePage(it.clubID)
                }
            }
        })
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = adapter

        cancleTv.setOnClickListener(object : DebounceViewClickListener() {
            override fun clickValid(v: View) {
                U.getKeyBoardUtils().hideSoftInputKeyBoard(this@ClubSearchActivity)
                finish()
            }
        })

        initPublishSubject()
        searchContent.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

            }

            override fun afterTextChanged(editable: Editable) {
                publishSubject?.onNext(SearchModel(editable.toString(), true))
            }
        })

        searchContent.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val keyword = searchContent.getText().toString().trim({ it <= ' ' })
                if (TextUtils.isEmpty(keyword)) {
                    U.getToastUtil().showShort("搜索内容为空")
                    return@OnEditorActionListener false
                }
                publishSubject?.onNext(SearchModel(keyword, false))
                U.getKeyBoardUtils().hideSoftInput(searchContent)
            }
            false
        })

        searchContent.postDelayed(Runnable {
            searchContent.requestFocus()
            U.getKeyBoardUtils().showSoftInputKeyBoard(this)
        }, 200)

    }

    private fun initPublishSubject() {
        publishSubject = PublishSubject.create<SearchModel>()
        ApiMethods.subscribe(
                publishSubject?.debounce(300, TimeUnit.MILLISECONDS)?.filter(Predicate<SearchModel> { s -> !TextUtils.isEmpty(s.searchContent) })?.switchMap(Function<SearchModel, ObservableSource<ApiResult>> { model ->
                    isAutoSearch = model.isAutoSearch
                    val grabRoomServerApi = ApiManager.getInstance().createService(ClubServerApi::class.java)
                    grabRoomServerApi.searchClub(model.searchContent)
                })?.retry(100), object : ApiObserver<ApiResult>() {
            override fun process(result: ApiResult) {
                if (result.errno == 0) {
                    val list = JSON.parseArray(result.data!!.getString("items"), ClubInfo::class.java)
                    adapter?.mDataList?.clear()
                    if (!list.isNullOrEmpty()) {
                        adapter?.mDataList?.addAll(list)
                    }
                    adapter?.notifyDataSetChanged()
                }
            }
        }, this)

    }

    override fun destroy() {
        super.destroy()
        publishSubject?.onComplete()
    }

    override fun canSlide(): Boolean {
        return false
    }

    override fun useEventBus(): Boolean {
        return false
    }
}