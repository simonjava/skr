package com.wali.live.watchsdk.contest.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.base.log.MyLog;
import com.base.mvp.specific.RxRelativeLayout;
import com.base.utils.display.DisplayUtils;
import com.mi.live.data.push.model.contest.ContestAnswerMsgExt;
import com.mi.live.data.push.model.contest.QuestionInfoModel;
import com.wali.live.watchsdk.R;
import com.wali.live.watchsdk.contest.cache.ContestCurrentCache;
import com.wali.live.watchsdk.contest.media.ContestMediaHelper;
import com.wali.live.watchsdk.eventbus.EventClass;

import org.greenrobot.eventbus.EventBus;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by liuyanyan on 2018/1/15.
 */
public class AnswerView extends RxRelativeLayout {
    private final String TAG = AnswerView.class.getSimpleName() + hashCode();

    private final float RESULT_ITEM_WIDTH = 333;
    public final int COUNT_DOWN_TOTAL_NUM = 10 * 1000;//答题倒计时总时长

    public static final int MSG_HIDE_VIEW = 101;
    public static final int MSG_HIDE_REVIVAL_VIEW = 102;

    private ImageView mNotifyIv;
    private TextView mQuestionNameTv;
    private LinearLayout mAnswerContainer;
    private ResurrectionView mRevivalView;

    private boolean mIsLastAnswer;
    private AnswerView.TimerHandler mTimerHandler = new AnswerView.TimerHandler(new WeakReference<AnswerView>(this));

    private ContestMediaHelper mMediaHelper;

    private static class TimerHandler extends Handler {
        private final WeakReference<AnswerView> mView;

        private TimerHandler(WeakReference<AnswerView> mView) {
            this.mView = mView;
        }

        @Override
        public void handleMessage(Message msg) {
            AnswerView AnswerView = mView.get();
            if (AnswerView == null) {
                return;
            }
            switch (msg.what) {
                case MSG_HIDE_VIEW:
                    AnswerView.hideView();
                    break;
                case MSG_HIDE_REVIVAL_VIEW:
//                    AnswerView.mRevivalView.start();
                    break;
                default:
                    break;
            }
        }
    }

    public AnswerView(Context context) {
        super(context);
        init(context);
    }

    public AnswerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public AnswerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        inflate(context, R.layout.view_summit_question, this);
        initContentView();
    }

    private void initContentView() {
        mNotifyIv = (ImageView) findViewById(R.id.notify_view);
        mQuestionNameTv = (TextView) findViewById(R.id.question_name);
        mAnswerContainer = (LinearLayout) findViewById(R.id.answer_container);

        mMediaHelper = new ContestMediaHelper(getContext());
    }

    public void bindContestAnswerData(ContestAnswerMsgExt answerMsgExt) {
        MyLog.w(TAG, "bindContestAnswerData");
        QuestionInfoModel model = answerMsgExt.getQuestionInfoModel();
        bindData(model);
        if (model != null) {
            mIsLastAnswer = model.isLastQuestion();
        }
    }

    private void bindData(QuestionInfoModel model) {
        if (null != model) {
            MyLog.w(TAG, "bindData model = " + model.toString());

            mQuestionNameTv.setText(String.valueOf(model.getQuestionShowId()) + "." + model.getQuestionContent());
            List<QuestionInfoModel.QuestionInfoItem> answerList = model.getQuestionInfoItems();
            mAnswerContainer.removeAllViews();
            if (null != answerList && answerList.size() > 0) {
                int totalAnswerNum = 0;
                for (QuestionInfoModel.QuestionInfoItem item : answerList) {
                    totalAnswerNum = totalAnswerNum + item.getNum();
                }
                for (QuestionInfoModel.QuestionInfoItem item : answerList) {
                    View itemView = inflate(getContext(), R.layout.item_summit_answer_option, null);
                    TextView answererNumTv = (TextView) itemView.findViewById(R.id.answerer_num_tv);
                    answererNumTv.setText(String.valueOf(item.getNum()));

                    ImageView answererNumRate = (ImageView) itemView.findViewById(R.id.answerer_num_rate_iv);
                    RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) answererNumRate.getLayoutParams();

                    float showRate = 0;
                    if (totalAnswerNum != 0) {
                        showRate = item.getNum() / ((float) totalAnswerNum);
                    }
                    if (showRate != 1.0) {
                        layoutParams.width = (int) (showRate * DisplayUtils.dip2px(RESULT_ITEM_WIDTH));
                        answererNumRate.setLayoutParams(layoutParams);
                    }

                    if (item.isAnswer()) {
                        MyLog.w(TAG, "answer item isAnswer");
                        answererNumRate.setBackgroundResource(R.drawable.bg_corner_75px_green);
                    } else if (item.getId().equals(ContestCurrentCache.getInstance().getId())) {
                        MyLog.w(TAG, "answer item wrong id = " + item.getId());
                        answererNumRate.setBackgroundResource(R.drawable.bg_corner_left_75px_right_0px_red);
                    } else {
                        MyLog.w(TAG, "answer item other normal");
                        answererNumRate.setBackgroundResource(R.drawable.bg_corner_left_75px_right_0px_gray);
                    }
                    TextView optionTitleTv = (TextView) itemView.findViewById(R.id.answer_option_tv);
                    optionTitleTv.setText(item.getText());

                    itemView.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    mAnswerContainer.addView(itemView);
                }
                showView();
            } else {
                MyLog.w(TAG, "bindData model is null");
                hideView();
            }
        }
    }

    private void showView() {
        MyLog.w(TAG, " showCacheData = " + ContestCurrentCache.getInstance().toString());
        setVisibility(VISIBLE);
        mTimerHandler.sendEmptyMessageDelayed(MSG_HIDE_VIEW, COUNT_DOWN_TOTAL_NUM);
        initNotifyView();
    }

    private void initNotifyView() {
        mNotifyIv.setVisibility(VISIBLE);

        if (ContestCurrentCache.getInstance().hasResult()) {
            MyLog.w(TAG, " showResult");
            if (ContestCurrentCache.getInstance().isCorrect()) {
                //答对了
                mMediaHelper.playRawSource(R.raw.contest_correct);
                setNotifyIv(R.drawable.youle_live_answer_icon_right);
            } else {
                //答错了
                mMediaHelper.playRawSource(R.raw.contest_wrong);
                setNotifyIv(R.drawable.youle_live_answer_icon_wrong);

                ContestCurrentCache.getInstance().getId();
                if (ContestCurrentCache.getInstance().isUseRevival()) {
                    //复活了
                    MyLog.w(TAG, "useRevival");
                    if (mRevivalView == null) {
                        mRevivalView = (ResurrectionView) findViewById(R.id.revival_view);
                    }
                    mRevivalView.setVisibility(VISIBLE);
                    mRevivalView.start();
//                    mTimerHandler.sendEmptyMessageDelayed(MSG_HIDE_REVIVAL_VIEW, 2_000);
                    //更新复活卡数量
                    // TODO: 2018/1/18 提示更新复活卡数量
                } else {
                    MyLog.w(TAG, "showFailView");
                    EventBus.getDefault().post(new EventClass.ShowContestView(EventClass.ShowContestView.TYPE_FAIL_VIEW, EventClass.ShowContestView.ACTION_SHOW));
                }
            }
        } else if (ContestCurrentCache.getInstance().isWatchMode()) {
            MyLog.w(TAG, " gameOut watchMode");
            setNotifyIv(R.drawable.youle_live_answer_icon_watch);
        } else if (!ContestCurrentCache.getInstance().isContinue()) {
            MyLog.w(TAG, "showAnswer gameOut");
            setNotifyIv(R.drawable.youle_live_answer_icon_out);
        } else {
            MyLog.w(TAG, "showAnswer error");
            // TODO: 2018/1/13 没有结果返回的情况
            mNotifyIv.setVisibility(GONE);
        }
    }

    private void setNotifyIv(int imageRes) {
        mNotifyIv.setVisibility(VISIBLE);
        mNotifyIv.setImageResource(imageRes);
    }

    private void hideView() {
        setVisibility(GONE);
        MyLog.w(TAG, "mIsLastAnswer= " + mIsLastAnswer + " ContestCurrentCache.getInstance().isCorrect()=" + ContestCurrentCache.getInstance().isCorrect());
        if (mIsLastAnswer) {
            if (ContestCurrentCache.getInstance().isCorrect()) {
                ContestCurrentCache.getInstance().setSuccess(true);
                EventBus.getDefault().post(new EventClass.ShowContestView(EventClass.ShowContestView.TYPE_SUCCESS_VIEW, EventClass.ShowContestView.ACTION_SHOW));
            } else {
                EventBus.getDefault().post(new EventClass.ShowContestView(EventClass.ShowContestView.TYPE_AWARD_VIEW, EventClass.ShowContestView.ACTION_SHOW));
            }
        }
        resetData();
    }

    private void resetData() {
        MyLog.w(TAG, "resetData");
        mTimerHandler.removeCallbacksAndMessages(null);
        ContestCurrentCache.getInstance().clearCache();
    }

    public void destroy() {
        super.destroy();
        resetData();
        mTimerHandler.removeCallbacksAndMessages(null);
        mMediaHelper.destroy();
    }
}
