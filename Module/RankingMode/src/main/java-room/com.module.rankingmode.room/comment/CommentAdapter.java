package com.module.rankingmode.room.comment;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.common.view.recyclerview.DiffAdapter;
import com.common.view.recyclerview.RecyclerOnItemClickListener;
import com.module.rankingmode.R;

public class CommentAdapter extends DiffAdapter<CommentModel, RecyclerView.ViewHolder> {

    public static final int VIEW_HOLDER_TYPE_TEXT = 1;

    RecyclerOnItemClickListener mRecyclerOnItemClickListener;

    public CommentAdapter(RecyclerOnItemClickListener l) {
        mRecyclerOnItemClickListener = l;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (VIEW_HOLDER_TYPE_TEXT == viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_view_holder_item, parent, false);
            CommentHolder viewHolder = new CommentHolder(view);
            viewHolder.setListener(mRecyclerOnItemClickListener);
            return viewHolder;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        CommentModel model = mDataList.get(position);
        if (holder instanceof CommentHolder) {
            CommentHolder commentHolder = (CommentHolder) holder;
            commentHolder.bind(position, model);
        }
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    @Override
    public int getItemViewType(int position) {
        CommentModel model = mDataList.get(position);
        return VIEW_HOLDER_TYPE_TEXT;
    }
}
