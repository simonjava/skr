/*
 * Copyright 2017 JessYan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.common.base.delegate


import android.app.Activity
import android.os.Bundle
import android.support.v4.app.FragmentManager

import com.common.base.BaseActivity
import com.common.base.BaseFragment
import com.common.lifecycle.ActivityLifecycle
import com.common.cache.Cache
import com.common.cache.LruCache

import org.greenrobot.eventbus.EventBus


/**
 * ================================================
 * 框架要求框架中的每个 [Activity] 都需要实现此类,以满足规范
 *
 * @see BaseActivity
 * Created by JessYan on 26/04/2017 21:42
 * [Contact me](mailto:jess.yan.effort@gmail.com)
 * [Follow me](https://github.com/JessYanCoding)
 * ================================================
 */
interface IActivity {

    //    /**
    //     * 提供 AppComponent (提供所有的单例对象) 给实现类, 进行 Component 依赖
    //     *
    //     * @param appComponent
    //     */
    //    void setupActivityComponent(@NonNull AppComponent appComponent);

    /**
     * 是否使用 [EventBus]
     *
     * @return
     */
    fun useEventBus(): Boolean

    /**
     * 初始化 View, 如果 [.initView] 返回 0, 框架则不会调用 [Activity.setContentView]
     *
     * @param savedInstanceState
     * @return
     */
    fun initView(savedInstanceState: Bundle?): Int

    /**
     * 初始化数据
     *
     * @param savedInstanceState
     */
    fun initData(savedInstanceState: Bundle?)

    /**
     * 这个 Activity 是否会使用 Fragment,框架会根据这个属性判断是否注册 [FragmentManager.FragmentLifecycleCallbacks]
     * 如果返回`false`,那意味着这个 Activity 不需要绑定 Fragment,那你再在这个 Activity 中绑定继承于 [BaseFragment] 的 Fragment 将不起任何作用
     * @see ActivityLifecycle.registerFragmentCallbacks
     * @return
     */
    fun useFragment(): Boolean
}
