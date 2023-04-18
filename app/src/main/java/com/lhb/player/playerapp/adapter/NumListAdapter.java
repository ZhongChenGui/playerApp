package com.lhb.player.playerapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lhb.player.playerapp.R;
import com.lhb.player.playerapp.activity.PlayActivity;
import com.thegrizzlylabs.sardineandroid.DavResource;

import java.util.List;

/**
 * Created by howe.zhong
 * on 2022/8/15  10:22
 */
public class NumListAdapter extends RecyclerView.Adapter<NumListAdapter.InnerHolder> {
    private List<DavResource> mData;
    private OnNumClickListener mListener;

    @NonNull
    @Override
    public InnerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_num_list, parent, false);
        return new InnerHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InnerHolder holder, int position) {
        String url = String.valueOf(mData.get(position).getHref());
        DavResource davResource = mData.get(position);
        String[] split = url.split("/");
//        holder.mTv.setText(split[split.length - 1]);
        holder.mTv.setText((position + 1) + "");
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onNumClick(davResource);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData != null ? mData.size() : 0;
    }

    public class InnerHolder extends RecyclerView.ViewHolder {
        private TextView mTv;

        public InnerHolder(@NonNull View itemView) {
            super(itemView);
            mTv = itemView.findViewById(R.id.num);
        }
    }

    public void setData(List<DavResource> list) {
        mData = null;
        this.mData = list;
        notifyDataSetChanged();
    }

    public void setOnNumClickListener(OnNumClickListener listener) {
        this.mListener = listener;
    }

    public interface OnNumClickListener {
        void onNumClick(DavResource davResource);
    }
}
