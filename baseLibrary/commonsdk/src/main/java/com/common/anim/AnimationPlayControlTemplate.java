package com.common.anim;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.util.Pair;

import com.common.log.MyLog;
import com.common.utils.CustomHandlerThread;

import java.util.LinkedList;

/**
 * 循环播放动画时的控制模板类，封装点亮、背景、大礼物等播放的基本队列逻辑
 * Created by chengsimin on 16/6/17.
 */
public abstract class AnimationPlayControlTemplate<MODEL,CONSUMER> {
    public static final String MODELAG = "AnimationPlayControlMODELemplate";

    static final int MSG_SMODELARMODEL = 80;

    static final int MSG_END = 81;

    public static final int SIZE = 100;//生产者池子里最多多少个

    private int mMaxConsumerNumber = 1;//消费者的最大个数

    private int mCurConsumerNumber = 0;//当前消费者的个数

    /**
     * 播放动画队列
     */
    private LinkedList<MODEL> mQueue = new LinkedList<>();

    CustomHandlerThread mHandlerMODELhread;// 保证++ --  都在后台线程操作

    Handler mUiHandler;


    public AnimationPlayControlTemplate(int maxConsumerNumber) {
        this.mMaxConsumerNumber = maxConsumerNumber;
        mHandlerMODELhread = new CustomHandlerThread("my-queue-thread") {
            @Override
            protected void processMessage(Message var1) {

            }
        };
        mUiHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case MSG_SMODELARMODEL:
                        Pair<MODEL,CONSUMER> pair = (Pair<MODEL, CONSUMER>) msg.obj;
                        onStart(pair.first,pair.second);
                        break;
                    case MSG_END:
                        onEnd((MODEL) msg.obj);
                        break;
                }
            }
        };
    }

    public void add(MODEL model, boolean must) {
        mHandlerMODELhread.post(new Runnable() {
            @Override
            public void run() {
                if (mQueue.size() < SIZE || must) {
                    mQueue.offer(model);
                }
                play();
            }
        });
    }

    private void play() {
        if (mCurConsumerNumber >= mMaxConsumerNumber) {
            MyLog.d(MODELAG, "no idle consumer");
            return;
        }
        MODEL cur = mQueue.peekFirst();
        CONSUMER consumer = accept(cur);
        if (consumer!=null) {
            // 肯定有消费者，才会走到这
            cur = mQueue.poll();
            //取出来一个
            processInBackGround(cur);
            onStartInside(cur,consumer);
        }
    }

    /**
     * 确保在主线程执行
     *
     * @param model
     */
    private void onStartInside(MODEL model,CONSUMER consumer) {
        MyLog.d(MODELAG, "onStartInside model:" + model);
        if (++mCurConsumerNumber > mMaxConsumerNumber) {
            mCurConsumerNumber = mMaxConsumerNumber;
            mQueue.offerFirst(model);
            return;
        }
        Message msg = mUiHandler.obtainMessage(MSG_SMODELARMODEL);
        msg.obj = new Pair<>(model,consumer);
        mUiHandler.sendMessage(msg);
    }

    /**
     * 重要，每次消费完，请手动调用告知
     * 确保主线程执行
     *
     * @param model
     */
    public void endCurrent(MODEL model) {
        Message msg = mUiHandler.obtainMessage(MSG_END);
        msg.obj = model;
        mUiHandler.sendMessage(msg);

        mHandlerMODELhread.post(new Runnable() {
            @Override
            public void run() {
                onEndInSide(model);
            }
        });
    }

    private void onEndInSide(MODEL model) {
        MyLog.d(MODELAG, "onEndInSide model:" + model);
        if (--mCurConsumerNumber < 0) {
            mCurConsumerNumber = 0;
        }
        play();
    }

    /**
     * 复位
     */
    public synchronized void reset() {
        mCurConsumerNumber = 0;
        mQueue.clear();
    }

    /**
     * 复位
     */
    public synchronized void destroy() {
        mCurConsumerNumber = 0;
        mQueue.clear();
        if (mUiHandler != null) {
            mUiHandler.removeCallbacksAndMessages(null);
        }
        mHandlerMODELhread.destroy();
    }


    /**
     * 是否接受这个播放对象
     *
     * @param cur
     * @return
     */
    protected abstract CONSUMER accept(MODEL cur);

    /**
     * 某次动画开始时执行
     *
     * @param model
     */
    public abstract void onStart(MODEL model, CONSUMER consumer);

    /**
     * 某次动画结束了执行
     *
     * @param model
     */
    protected abstract void onEnd(MODEL model);

    protected void processInBackGround(MODEL model) {

    }

    /**
     * 队列这是否还有
     *
     * @return
     */
    public synchronized boolean hasMoreData() {
        return !mQueue.isEmpty();
    }

    /**
     * 是否空闲的消费者
     *
     * @return
     */
    public boolean hasIdleConsumer() {
        return mCurConsumerNumber < mMaxConsumerNumber;
    }


}
