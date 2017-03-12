package com.wali.live.livesdk.live.liveshow.presenter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.RelativeLayout;

import com.base.log.MyLog;
import com.wali.live.component.presenter.ComponentPresenter;
import com.wali.live.component.view.panel.BaseBottomPanel;
import com.wali.live.livesdk.live.liveshow.LiveComponentController;
import com.wali.live.livesdk.live.liveshow.presenter.panel.LiveMagicPresenter;
import com.wali.live.livesdk.live.liveshow.presenter.panel.LivePlusPresenter;
import com.wali.live.livesdk.live.liveshow.presenter.panel.LiveSettingPresenter;
import com.wali.live.livesdk.live.liveshow.view.LivePanelContainer;
import com.wali.live.livesdk.live.liveshow.view.panel.LiveMagicPanel;
import com.wali.live.livesdk.live.liveshow.view.panel.LivePlusPanel;
import com.wali.live.livesdk.live.liveshow.view.panel.LiveSettingPanel;

/**
 * Created by yangli on 2017/2/18.
 *
 * @module 底部面板表现, 游戏直播
 */
public class PanelContainerPresenter extends ComponentPresenter<LivePanelContainer.IView>
        implements LivePanelContainer.IPresenter {
    private static final String TAG = "PanelContainerPresenter";

    private LiveSettingPanel mSettingPanel;
    private LiveMagicPanel mMagicPanel;
    private LivePlusPanel mPlusPanel;

    private LiveSettingPresenter mSettingPresenter;
    private LivePlusPresenter mPlusPresenter;
    private LiveMagicPresenter mMagicPresenter;

    public PanelContainerPresenter(
            @NonNull IComponentController componentController) {
        super(componentController);
        registerAction(LiveComponentController.MSG_ON_ORIENT_PORTRAIT);
        registerAction(LiveComponentController.MSG_ON_ORIENT_LANDSCAPE);
        registerAction(LiveComponentController.MSG_ON_BACK_PRESSED);
        registerAction(LiveComponentController.MSG_SHOW_SETTING_PANEL);
        registerAction(LiveComponentController.MSG_SHOW_PLUS_PANEL);
        registerAction(LiveComponentController.MSG_SHOW_MAGIC_PANEL);
    }

    @Override
    public void destroy() {
        super.destroy();
        if (mPlusPanel != null) {
            mPlusPresenter.destroy();
            mPlusPresenter = null;
            mPlusPanel = null;
        }
        if (mMagicPresenter != null) {
            mMagicPresenter.destroy();
            mMagicPresenter = null;
            mMagicPanel = null;
        }
    }

    private boolean showSettingPanel() {
        if (mSettingPanel == null) {
            mSettingPanel = new LiveSettingPanel((RelativeLayout) mView.getRealView());
            mSettingPresenter = new LiveSettingPresenter(mComponentController);
            mSettingPanel.setPresenter(mSettingPresenter);
            mSettingPresenter.setComponentView(mSettingPanel.getViewProxy());
        }
        return mView.showPanel(mSettingPanel);
    }

    private boolean showPlusPanel() {
        if (mPlusPanel == null) {
            mPlusPanel = new LivePlusPanel((RelativeLayout) mView.getRealView());
            mPlusPresenter = new LivePlusPresenter(mComponentController);
            mPlusPanel.setPresenter(mPlusPresenter);
            mPlusPresenter.setComponentView(mPlusPanel.getViewProxy());
        }
        return mView.showPanel(mPlusPanel);
    }

    private boolean showMagicPanel() {
        if (mMagicPanel == null) {
            mMagicPanel = new LiveMagicPanel((RelativeLayout) mView.getRealView());
            mMagicPresenter = new LiveMagicPresenter(mComponentController);
            mMagicPanel.setPresenter(mMagicPresenter);
            mMagicPresenter.setComponentView(mMagicPanel.getViewProxy());
        }
        return mView.showPanel(mMagicPanel);
    }

    @Nullable
    @Override
    protected IAction createAction() {
        return new Action();
    }

    public class Action implements IAction {
        @Override
        public boolean onAction(int source, @Nullable Params params) {
            if (mView == null) {
                MyLog.e(TAG, "onAction but mView is null, source=" + source);
                return false;
            }
            switch (source) {
                case LiveComponentController.MSG_ON_ORIENT_PORTRAIT:
                    mView.onOrientation(false);
                    return true;
                case LiveComponentController.MSG_ON_ORIENT_LANDSCAPE:
                    mView.onOrientation(true);
                    return true;
                case LiveComponentController.MSG_SHOW_SETTING_PANEL:
                    return showSettingPanel();
                case LiveComponentController.MSG_SHOW_PLUS_PANEL:
                    return showPlusPanel();
                case LiveComponentController.MSG_SHOW_MAGIC_PANEL:
                    return showMagicPanel();
                case LiveComponentController.MSG_ON_BACK_PRESSED:
                    return mView.processBackPress();
                default:
                    break;

            }
            return false;
        }
    }
}
