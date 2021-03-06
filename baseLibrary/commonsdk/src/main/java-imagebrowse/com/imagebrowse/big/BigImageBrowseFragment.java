package com.imagebrowse.big;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.common.base.BaseFragment;
import com.common.base.R;
import com.common.callback.Callback;
import com.common.image.fresco.FrescoWorker;
import com.common.log.MyLog;
import com.common.utils.FragmentUtils;
import com.common.utils.U;
import com.common.view.DebounceViewClickListener;
import com.common.view.titlebar.CommonTitleBar;
import com.common.view.viewpager.arraypageradapter.ArrayViewPagerAdapter;
import com.dialog.list.DialogListItem;
import com.dialog.list.ListDialog;
import com.imagebrowse.ImageBrowseView;
import com.imagebrowse.SlideCloseLayout;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import kotlin.Function;

/**
 * 看大图的Fragment
 */
public class BigImageBrowseFragment extends BaseFragment {
    public final String TAG = "ImageBigPreviewFragment";
    public final static String BIG_IMAGE_PATH = "big_image_path";

    ImageView mImageBg;
    CommonTitleBar mTitlebar;
    SlideCloseLayout mSlideCloseLayout;
    ViewPager mImagesVp;
    TextView mUpdaterTv;
    //ArrayList<IMAGE_DATA> mDataList = new ArrayList<>();
    Loader mLoader;
    int mLastPostion = 0;
    boolean mBackward;
    ListDialog mMenuDialog;

    ArrayViewPagerAdapter mPagerAdapter = new ArrayViewPagerAdapter() {
        @Override
        public View getView(LayoutInflater inflater, ViewGroup container, Object item, int position) {
            ImageBrowseView imageBrowseView = new ImageBrowseView(container.getContext());
            if (mLoader != null) {
                mLoader.load(imageBrowseView, position, item);
                mLoader.loadUpdater(mUpdaterTv, position, item, new Callback() {
                    @Override
                    public void onCallback(int r, Object obj) {
                        if(r==1){
                            setRightBtnStatus();
                        }
                    }
                });
            } else {
                //imageBrowseView.load(data);
            }
            imageBrowseView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    //U.getToastUtil().showShort("长按事件");
                    return false;
                }
            });
            imageBrowseView.setOnClickListener(new DebounceViewClickListener() {
                @Override
                public void clickValid(View v) {
                    finish();
                }
            });
            return imageBrowseView;
        }

    };


    @Override
    public int initView() {
        return R.layout.big_image_preview_fragment_layout;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        if (mLoader == null) {
            finish();
            return;
        }

        U.getSoundUtils().preLoad(TAG, R.raw.normal_back);

        mImageBg = getRootView().findViewById(R.id.image_bg);
        mTitlebar = getRootView().findViewById(R.id.titlebar);
        mSlideCloseLayout = getRootView().findViewById(R.id.slide_close_layout);
        mImagesVp = getRootView().findViewById(R.id.images_vp);
        mUpdaterTv = getRootView().findViewById(R.id.updater_tv);

        mTitlebar.getLeftTextView().setOnClickListener(new DebounceViewClickListener() {
            @Override
            public void clickValid(View v) {
                finish();
            }
        });




        mPagerAdapter.addAll(mLoader.getInitList());
        mImagesVp.setAdapter(mPagerAdapter);
        setCurrentItem(mLoader.getInitCurrentItemPostion());
        mLastPostion = mImagesVp.getCurrentItem();
        mImagesVp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //MyLog.d(TAG, "onPageScrolled" + " position=" + position + " positionOffset=" + positionOffset + " positionOffsetPixels=" + positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                MyLog.d(TAG, "onPageSelected" + " position=" + position);
                if (position > mLastPostion) {
                    mBackward = true;
                } else if (position < mLastPostion) {
                    mBackward = false;
                }
                mLastPostion = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                MyLog.d(TAG, "onPageScrollStateChanged" + " state=" + state);
                if (state == 0) {
                    if (mBackward) {
                        // 往后滑动
                        if (mLastPostion >= mPagerAdapter.getCount() - 2) {
                            if (mLoader.hasMore(true,
                                    mPagerAdapter.getCount(),
                                    mPagerAdapter.getItem(mPagerAdapter.getCount() - 1))) {
                                // 需要往后加载更多了
                                loadMore(true, mPagerAdapter.getCount() - 1);
                            }
                        }
                    } else {
                        if (mLastPostion <= 1) {
                            // 需要往后加载更多了
                            if (mLoader.hasMore(false,
                                    0,
                                    mPagerAdapter.getItem(0))) {
                                loadMore(false, 0);
                            }
                        }
                    }
                }
            }
        });

        getActivity().getWindow().getDecorView().setBackgroundColor(Color.BLACK);
        mSlideCloseLayout.setGradualBackground(getActivity().getWindow().getDecorView().getBackground());
        mImageBg.getBackground().setAlpha(255);
        mTitlebar.setAlpha(1);
        mSlideCloseLayout.setLayoutScrollListener(new SlideCloseLayout.LayoutScrollListener() {
            @Override
            public void onLayoutClosed() {
                finish();
            }

            @Override
            public void onLayoutScrolling(float alpha) {
                //和背景保持一致
                mImageBg.getBackground().setAlpha((int) (255 - alpha * 255));
                //做个效果
                mTitlebar.setAlpha(1 - (alpha * 5f));
            }

            @Override
            public void onLayoutScrollRevocer() {
                mImageBg.getBackground().setAlpha(255);
                mTitlebar.setAlpha(1);
            }

            @Override
            public boolean onHostAllowIntercept() {
                ImageBrowseView curView = (ImageBrowseView) mPagerAdapter.getPrimaryItem();
                if (curView != null && curView.hasLargerScale()) {
                    return false;
                }
                return true;
            }
        });
        setRightBtnStatus();
    }

    void setRightBtnStatus(){
        if (mLoader.hasDeleteMenu() || mLoader.hasSaveMenu()) {
            mTitlebar.getRightImageButton().setOnClickListener(new DebounceViewClickListener() {
                @Override
                public void clickValid(View v) {
                    mMenuDialog = new ListDialog(getContext());
                    List<DialogListItem> listItems = new ArrayList<>();


                    if (mLoader.hasSaveMenu()) {
                        listItems.add(new DialogListItem("保存", new Runnable() {
                            @Override
                            public void run() {
                                ImageBrowseView curView = (ImageBrowseView) mPagerAdapter.getPrimaryItem();
                                Uri uri = curView.getBaseImage().getUri();
                                File file = FrescoWorker.getCacheFileFromFrescoDiskCache(uri);
                                if (file != null && file.exists()) {
                                    String ext = U.getFileUtils().getSuffixFromUrl(uri.getPath(), "jpg");
                                    File dst = U.getFileUtils().createFileByTs(U.getAppInfoUtils().getSubDirFile("save"), "IMG_", "." + ext);
                                    U.getIOUtils().copy(file, dst);

                                    // 其次把文件插入到系统图库
                                    try {
                                        MediaStore.Images.Media.insertImage(getContext().getContentResolver(),
                                                dst.getAbsolutePath(), dst.getName(), null);
                                    } catch (FileNotFoundException e) {
                                        return;
                                    }
                                    // 最后通知图库更新
                                    Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                                    Uri uri2 = Uri.fromFile(dst);
                                    intent.setData(uri2);
                                    getContext().sendBroadcast(intent);
                                    /**
                                     * 会在save 与 手机的pictures 相册目录各存一份
                                     */
                                    mMenuDialog.dissmiss();
                                    U.getToastUtil().showLong("已保存至相册，路径为 " + dst.getPath());
                                } else {
                                    U.getToastUtil().showShort("等待文件加载成功才可保存");
                                }
                            }
                        }));
                    }

                    if (mLoader.hasDeleteMenu()) {
                        listItems.add(new DialogListItem("删除", new Runnable() {
                            @Override
                            public void run() {
                                int cp = mImagesVp.getCurrentItem();
                                if (mLoader.getDeleteListener() != null) {
                                    mLoader.getDeleteListener().onCallback(cp, mPagerAdapter.getItem(cp));
                                }
                                if (mPagerAdapter.getCount() > 0) {
                                    mPagerAdapter.remove(cp);
                                }
                                mMenuDialog.dissmiss();
                                if (mPagerAdapter.getCount() <= 0) {
                                    finish();
                                }
                            }
                        }));
                    }

                    listItems.add(new DialogListItem("取消", new Runnable() {
                        @Override
                        public void run() {
                            if (mMenuDialog != null) {
                                mMenuDialog.dissmiss();
                            }
                        }
                    }));
                    mMenuDialog.showList(listItems);
                }
            });
        } else {
            mTitlebar.getRightImageButton().setVisibility(View.GONE);
        }
    }

    void loadMore(boolean backward, int postion) {
        mLoader.loadMore(backward, postion, mPagerAdapter.getItem(postion), new Callback<List>() {
            @Override
            public void onCallback(int r, List list) {
                if (backward) {
                    mPagerAdapter.addAll(list);
                } else {
                    mPagerAdapter.addAll(0, list);
                }
            }
        });
    }

    void setCurrentItem(int postion) {
        MyLog.d(TAG, "setCurrentItem" + " postion=" + postion);
        mImagesVp.setCurrentItem(postion);
    }

    @Override
    public boolean useEventBus() {
        return false;
    }

    @Override
    public void destroy() {
        super.destroy();
        U.getSoundUtils().release(TAG);
        if (mImagesVp != null) {
            mImagesVp.clearOnPageChangeListeners();
        }
    }

    @Override
    public void finish() {
        super.finish();
        if (getActivity() instanceof BigImageBrowseActivity) {
            getActivity().finish();
        } else {
            U.getFragmentUtils().popFragment(BigImageBrowseFragment.this);
        }
    }

    @Override
    public boolean onBackPressed() {
        finish();
        return true;
    }

    @Override
    public void setData(int type, @Nullable Object data) {
        if (type == 1) {
            mLoader = (Loader) data;
        }
    }

    /**
     * 浏览组图 数据从Loader里的各个回调里拿
     *
     * @param useActivity
     * @param activity
     * @param mLoader
     */
    public static void open(boolean useActivity, FragmentActivity activity, Loader mLoader) {
        if (mLoader != null) {
            mLoader.init();
        }
        if (useActivity) {
            BigImageBrowseActivity.open(mLoader, activity);
        } else {
            U.getFragmentUtils().addFragment(FragmentUtils.newAddParamsBuilder(activity, BigImageBrowseFragment.class)
                    .addDataBeforeAdd(1, mLoader)
                    .setAddToBackStack(true)
                    .setHasAnimation(true)
                    .setEnterAnim(R.anim.fade_in_center)
                    .setExitAnim(R.anim.fade_out_center)
                    .build()
            );
        }
    }

    /**
     * 浏览单个大图
     *
     * @param useActivity
     * @param activity
     * @param path
     */
    public static void open(boolean useActivity, FragmentActivity activity, String path) {
        open(useActivity, activity, new DefaultImageBrowserLoader<String>() {
            @Override
            public void init() {

            }

            @Override
            public void load(ImageBrowseView imageBrowseView, int position, String item) {
                imageBrowseView.load(item);
            }

            @Override
            public int getInitCurrentItemPostion() {
                return 0;
            }

            @Override
            public List<String> getInitList() {
                ArrayList list = new ArrayList<>();
                list.add(path);
                return list;
            }

            @Override
            public void loadMore(boolean backward, int position, String data, Callback<List<String>> callback) {

            }

            @Override
            public boolean hasMore(boolean backward, int position, String data) {
                return false;
            }

        });
    }

}
