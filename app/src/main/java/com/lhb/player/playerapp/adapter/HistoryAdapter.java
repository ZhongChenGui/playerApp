package com.lhb.player.playerapp.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lhb.player.playerapp.R;
import com.lhb.player.playerapp.model.bean.HistoryData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by howe.zhong
 * on 2022/8/9  13:43
 * 观看记录
 */
public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.InnerHolder> {

    private static final String TAG = "HistoryAdapter";
    private List<HistoryData.DataBean> mData = new ArrayList<>();

    @NonNull
    @Override
    public InnerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history_content, parent, false);
        return new InnerHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InnerHolder holder, int position) {
        HistoryData.DataBean dataBean = mData.get(position);
        holder.setData(dataBean);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: ..... " + dataBean.getTitle());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData != null ? mData.size() : 0;
    }

    public void setData(List<HistoryData.DataBean> dataBeans) {
        mData.clear();
        mData.addAll(dataBeans);
        notifyDataSetChanged();

    }

    public class InnerHolder extends RecyclerView.ViewHolder {
        public InnerHolder(@NonNull View itemView) {
            super(itemView);
        }

        public void setData(HistoryData.DataBean data){

        }
    }
}
