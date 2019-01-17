package com.module.playways.grab.songselect;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.common.view.recyclerview.DiffAdapter;
import com.common.view.recyclerview.RecyclerOnItemClickListener;
import com.module.rank.R;

public class SpecialSelectAdapter extends DiffAdapter<SpecialModel, SpecialCardViewHolder> {

    RecyclerOnItemClickListener<SpecialModel> mItemClickListener;

    public SpecialSelectAdapter(RecyclerOnItemClickListener l) {
        this.mItemClickListener = l;
    }

    @NonNull
    @Override
    public SpecialCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.specail_view_holder_layout, parent, false);
        SpecialCardViewHolder itemHolder = new SpecialCardViewHolder(view);
        itemHolder.setItemClickListener(mItemClickListener);
        return itemHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SpecialCardViewHolder holder, int position) {
        SpecialModel specialModel = mDataList.get(position);
        holder.bindData(specialModel, position);
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }
}
