package com.common.utils;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;

import com.common.base.BaseFragment;
import com.common.base.FragmentDataListener;
import com.common.base.R;
import com.common.log.MyLog;

import java.lang.reflect.Constructor;

/**
 * 支持 v4 fragment 的一些工具方法
 * <p>
 * 参考  https://github.com/gpfduoduo/android-article/blob/master/Activity%20%2B%20%E5%A4%9AFrament%20%E4%BD%BF%E7%94%A8%E6%97%B6%E7%9A%84%E4%B8%80%E4%BA%9B%E5%9D%91.md
 */
public class FragmentUtils {

    public final static String TAG = "FragmentUtils";

    FragmentUtils() {

    }

    public BaseFragment getTopFragment(FragmentActivity activity) {
        FragmentManager fm = activity.getSupportFragmentManager();
        if (fm.getBackStackEntryCount() > 0) {
            //退出栈弹出
            String fName = fm.getBackStackEntryAt(fm.getBackStackEntryCount() - 1).getName();
            if (!TextUtils.isEmpty(fName)) {
                Fragment fragment = fm.findFragmentByTag(fName);
                if (fragment instanceof BaseFragment) {
                    return (BaseFragment) fragment;
                }
            }
        }
        return null;
    }

    /**
     * 弹出activity 顶部的fragment
     */
    public static boolean popFragment(FragmentActivity activity) {
        if (activity == null) {
            return false;
        }
        try {
//            if (immediate) {
            return activity.getSupportFragmentManager().popBackStackImmediate();
//            } else {
            // 这个方法会丢到主线程队列的末尾去执行
//                activity.getSupportFragmentManager().popBackStack();
//            }
        } catch (IllegalStateException e) {
            MyLog.e(e);
        }
        return false;
    }

    /**
     * 弹出activity fragment及其以上所有的fragment
     */
    public static boolean popFragment(BaseFragment fragment) {
        if (fragment == null) {
            return false;
        }
        if (fragment.getActivity() != null) {
            FragmentManager fragmentManager = fragment.getActivity().getSupportFragmentManager();
            if (fragmentManager != null) {
                for (Fragment f : fragmentManager.getFragments()) {
                    if (f == fragment) {
                        /**
                         *  至于int flags有两个取值：0或FragmentManager.POP_BACK_STACK_INCLUSIVE；
                         *  当取值0时，表示除了参数一指定这一层之上的所有层都退出栈，指定的这一层为栈顶层； 
                         *  当取值POP_BACK_STACK_INCLUSIVE时，表示连着参数一指定的这一层一起退出栈
                         *  另外第一个参数一般用 tag ，只有静态添加的 fragment 才用id
                         */
                        boolean b = fragmentManager.popBackStackImmediate(f.getTag(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
                        return  b;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 所有的 fragment 都通过这个方法来添加
     * 这个方法会保证 同一个 classname 的 fragment 只会在 栈中 有一个实例
     * 通过 {@method FragmentUtils.newParamsBuilder()} 构造参数
     *
     * @param params
     * @return
     */
    public BaseFragment addFragment(Params params) {
        // 这里用 类名做TAG 保证了
        String tag = params.targetFragment.getName();

        FragmentManager fragmentManager = params.fragmentActivity.getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        Object fragmentObject = fragmentManager.findFragmentByTag(tag);

        if (fragmentObject != null
                && (fragmentObject instanceof BaseFragment)) {
            BaseFragment oldFragment = null;
            oldFragment = (BaseFragment) fragmentManager.findFragmentByTag(tag);
            if (oldFragment != null) {
                MyLog.d(TAG, "addFragment" + " oldFragment!=null");
                if (params.bundle == null) {
                    if (!oldFragment.isAdded()) {
                        ft.add(params.containerViewId, oldFragment, tag);
                    }
                    if (oldFragment.isHidden()) {
                        ft.show(oldFragment);
                    }
                    ft.commitAllowingStateLoss();
                    return oldFragment;
                } else {
                    ft.remove(oldFragment);
                    ft.commitAllowingStateLoss();

                    ft = fragmentManager.beginTransaction();
                }
            }
        }

        BaseFragment fragment = null;
        fragment = createFragment(params.targetFragment);
        if (fragment == null) {
            return null;
        }
        fragment.setFragmentDataListener(params.fragmentDataListener);
        if(params.dataBeforeAdd!=null){
            fragment.setData(params.dataType,params.dataBeforeAdd);
        }
        if (params.bundle != null) {
            fragment.setArguments(params.bundle);
        }
        /**
         * 将两个 Fragment 建立联系, 使得
         * getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent)
         * 能够工作
         */
        if (params.fromFragment != null) {
            fragment.setTargetFragment(params.fromFragment, fragment.getRequestCode());
        }

        if (params.hasAnimation) {
            ft.setCustomAnimations(params.enterAnim, params.exitAnim, params.enterAnim, params.exitAnim);
        }
        /**
         * 是否加到回退栈中，back键盘返回上一个fragment
         * getBackStackEntryCount 的值 跟 这个 无关。
         */
        if (params.addToBackStack) {
            ft.addToBackStack(tag);
        }
        ft.add(params.containerViewId, fragment, tag);
        if (params.allowStateLoss) {
            ft.commitAllowingStateLoss();
        } else {
            ft.commit();
        }

        return fragment;
    }

    /**
     * 最简单的构造 fragment 的方法
     *
     * @param cls
     * @return
     */
    private BaseFragment createFragment(Class<?> cls) {
        Constructor<?> ctor;
        try {
            ctor = cls.getConstructor();
            Object object = ctor.newInstance();
            if (object instanceof BaseFragment) {
                return (BaseFragment) object;
            } else {
                throw new Exception();
            }
        } catch (Exception e) {
            MyLog.e(e);
            return null;
        }
    }

    public static Params.Builder newParamsBuilder(FragmentActivity fragmentActivity, Class<? extends Fragment> fragment) {
        return new Params.Builder()
                .setFragmentActivity(fragmentActivity)
                .setTargetFragment(fragment);
    }

    /**
     * 打开一个fragment可能需要的参数
     * 使用 builder 模式
     */
    static class Params {
        /**
         * 来源于哪个 fragment 主要是 setTargetFragment 时使用
         */
        BaseFragment fromFragment;
        Class<? extends Fragment> targetFragment;
        int containerViewId = android.R.id.content;
        Bundle bundle = null;
        boolean addToBackStack = true;
        boolean hasAnimation = false;
        boolean allowStateLoss = true;
        int enterAnim = R.anim.slide_right_in;
        int exitAnim = R.anim.slide_right_out;
        FragmentActivity fragmentActivity;
        FragmentDataListener fragmentDataListener;
        /**
         * 允许在Fragment 被add之前，给fragment 设置一些值。会调用 setData 方法。
         * 虽然大部分值可以通过bundle传递，但有些值不适合bundle传递，比如一些很长的list
         * 一个很特别的回到等等。
         */
        Object dataBeforeAdd;
        int dataType; // 数据类型标识

        /**
         * 默认根据类名确定唯一性
         * 意思在同一个Activity 内 ，使用 addFragment 添加FragmentA
         * 能保证FragmentA 只有一个实例
         */
        boolean uniqueByClassName = true;

        public void setFromFragment(BaseFragment fromFragment) {
            this.fromFragment = fromFragment;
        }

        public void setTargetFragment(Class<? extends Fragment> targetFragment) {
            this.targetFragment = targetFragment;
        }

        public void setContainerViewId(int containerViewId) {
            this.containerViewId = containerViewId;
        }

        public void setBundle(Bundle bundle) {
            this.bundle = bundle;
        }

        public void setAddToBackStack(boolean addToBackStack) {
            this.addToBackStack = addToBackStack;
        }

        public void setHasAnimation(boolean hasAnimation) {
            this.hasAnimation = hasAnimation;
        }

        public void setAllowStateLoss(boolean allowStateLoss) {
            this.allowStateLoss = allowStateLoss;
        }

        public void setEnterAnim(int enterAnim) {
            this.enterAnim = enterAnim;
        }

        public void setExitAnim(int exitAnim) {
            this.exitAnim = exitAnim;
        }

        public void setFragmentActivity(FragmentActivity fragmentActivity) {
            this.fragmentActivity = fragmentActivity;
        }

        public void setFragmentDataListener(FragmentDataListener l) {
            this.fragmentDataListener = l;
        }

        public void setDataBeforeAdd(int dataType,Object dataBeforeAdd) {
            this.dataType = dataType;
            this.dataBeforeAdd = dataBeforeAdd;
        }

        public static class Builder {
            Params mParams = new Params();

            Builder() {
            }

            public Builder setFromFragment(BaseFragment fromFragment) {
                mParams.setFromFragment(fromFragment);
                return this;
            }

            public Builder setTargetFragment(Class<? extends Fragment> targetFragment) {
                mParams.setTargetFragment(targetFragment);
                return this;
            }

            public Builder setContainerViewId(int containerViewId) {
                mParams.setContainerViewId(containerViewId);
                return this;
            }

            public Builder setBundle(Bundle bundle) {
                mParams.setBundle(bundle);
                return this;
            }

            public Builder setAddToBackStack(boolean addToBackStack) {
                mParams.setAddToBackStack(addToBackStack);
                return this;
            }

            public Builder setHasAnimation(boolean hasAnimation) {
                mParams.setHasAnimation(hasAnimation);
                return this;
            }

            public Builder setAllowStateLoss(boolean allowStateLoss) {
                mParams.setAllowStateLoss(allowStateLoss);
                return this;
            }

            public Builder setEnterAnim(int enterAnim) {
                mParams.setEnterAnim(enterAnim);
                return this;
            }

            public Builder setExitAnim(int exitAnim) {
                mParams.setExitAnim(exitAnim);
                return this;
            }

            public Builder setFragmentActivity(FragmentActivity fragmentActivity) {
                mParams.setFragmentActivity(fragmentActivity);
                return this;
            }

            public Builder setFragmentDataListener(FragmentDataListener l) {
                mParams.setFragmentDataListener(l);
                return this;
            }

            public Builder setDataBeforeAdd(int dataType,Object dataBeforeAdd) {
                mParams.setDataBeforeAdd(dataType,dataBeforeAdd);
                return this;
            }

            public Params build() {
                return mParams;
            }
        }
    }


}
