package com.module.playways.room.room.comment.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.common.log.MyLog;
import com.common.view.recyclerview.DiffAdapter;
import com.module.playways.room.room.comment.holder.CommentAudioHolder;
import com.module.playways.room.room.comment.holder.CommentDynamicHolder;
import com.module.playways.room.room.comment.holder.CommentHolder;
import com.module.playways.room.room.comment.model.CommentAudioModel;
import com.module.playways.room.room.comment.model.CommentDynamicModel;
import com.module.playways.room.room.comment.model.CommentModel;
import com.module.playways.R;

public class CommentAdapter extends DiffAdapter<CommentModel, RecyclerView.ViewHolder> {

    public static final int VIEW_HOLDER_TYPE_NORMAL = 1;  // 普通消息(即头像加上文字的普通消息)
    public static final int VIEW_HOLDER_TYPE_DYNAMIC = 2; // 图片消息(即头像加上图片的普通消息)
    public static final int VIEW_HOLDER_TYPE_AUDIO = 3;   // 语音消息(即头像加上语音的普通消息)

    public int mGameType = 0;

    CommentAdapterListener mCommentItemListener;
    CommentAudioModel mCurPlayAudioModel = null;

    public CommentAdapter(CommentAdapterListener mCommentItemListener) {
        this.mCommentItemListener = mCommentItemListener;
    }

    public void setGameType(int gameType) {
        mGameType = gameType;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        if (viewType == VIEW_HOLDER_TYPE_NORMAL) {
            if (mGameType == 0) {
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_view_holder_text_item, parent, false);
            } else {
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grab_comment_view_holder_item, parent, false);
            }
            CommentHolder commentHolder = new CommentHolder(view, mCommentItemListener);
            return commentHolder;
        } else if (viewType == VIEW_HOLDER_TYPE_DYNAMIC) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grab_comment_dynamic_view_holder_item, parent, false);
            CommentDynamicHolder commentDynamicHolder = new CommentDynamicHolder(view, mCommentItemListener);
            return commentDynamicHolder;
        } else if (viewType == VIEW_HOLDER_TYPE_AUDIO) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grab_comment_audio_view_holder_item, parent, false);
            CommentAudioHolder commentAudioHolder = new CommentAudioHolder(view, mCommentItemListener);
            return commentAudioHolder;
        }

        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        CommentModel model = mDataList.get(position);
        if (holder instanceof CommentHolder) {
            CommentHolder commentHolder = (CommentHolder) holder;
            commentHolder.bind(position, model);
        } else if (holder instanceof CommentDynamicHolder) {
            CommentDynamicHolder commentDynamicHolder = (CommentDynamicHolder) holder;
            commentDynamicHolder.bind(position, (CommentDynamicModel) model);
        } else if (holder instanceof CommentAudioHolder) {
            CommentAudioHolder commentAudioHolder = (CommentAudioHolder) holder;
            CommentAudioModel commentAudioModel = (CommentAudioModel) model;
            commentAudioHolder.bind(position, commentAudioModel);
            if (commentAudioModel == mCurPlayAudioModel) {
                commentAudioHolder.setPlay(true);
            } else {
                commentAudioHolder.setPlay(false);
            }

        }
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (mDataList.get(position).getCommentType() == CommentModel.Companion.getTYPE_DYNAMIC()) {
            return VIEW_HOLDER_TYPE_DYNAMIC;
        } else if (mDataList.get(position).getCommentType() == CommentModel.Companion.getTYPE_AUDIO()) {
            return VIEW_HOLDER_TYPE_AUDIO;
        }
        return VIEW_HOLDER_TYPE_NORMAL;
    }

    public void addToHead(CommentModel commentModel) {
        mDataList.add(0, commentModel);
        // 移除最后一个
        if (mDataList.size() > 500) {
            mDataList.remove(mDataList.size() - 1);
        }
    }

    public void setCurrentPlayAudioModel(CommentAudioModel commentAudioModel) {
        if(mCurPlayAudioModel!=commentAudioModel){
            mCurPlayAudioModel = commentAudioModel;
            notifyDataSetChanged();
        }
    }

    public interface CommentAdapterListener {
        /**
         * 头像的点击
         *
         * @param userId
         */
        void clickAvatar(int userId);

        boolean clickAudio(boolean isPlaying, CommentAudioModel commentAudioModel);
    }
}
