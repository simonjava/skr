package com.module.home.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.common.view.recyclerview.DiffAdapter;
import com.module.home.R;
import com.module.home.model.DqRecordModel;

public class DqRecordAdapter extends DiffAdapter<DqRecordModel, RecyclerView.ViewHolder> {

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dq_record_item_layout, parent, false);
        DqRecordHolder viewHolder = new DqRecordHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        DqRecordModel model = mDataList.get(position);

        DqRecordHolder reportItemHolder = (DqRecordHolder) holder;
        reportItemHolder.bind(model);
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    private class DqRecordHolder extends RecyclerView.ViewHolder {

        TextView mRecordDescTv;
        TextView mRecordTimeTv;
        TextView mRecordNumTv;

        DqRecordModel mDqRecordModel;

        public DqRecordHolder(View itemView) {
            super(itemView);
            mRecordDescTv = (TextView) itemView.findViewById(R.id.record_desc_tv);
            mRecordTimeTv = (TextView) itemView.findViewById(R.id.record_time_tv);
            mRecordNumTv = (TextView) itemView.findViewById(R.id.record_num_tv);
        }

        public void bind(DqRecordModel model) {
            this.mDqRecordModel = model;
            mRecordDescTv.setText(model.getRemark());
            mRecordTimeTv.setText(model.getDateTime());
            mRecordNumTv.setText(model.getAmountStr() + "红钻");
        }
    }
}
