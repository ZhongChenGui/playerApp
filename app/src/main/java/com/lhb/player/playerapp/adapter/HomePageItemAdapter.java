package com.lhb.player.playerapp.adapter;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lhb.player.playerapp.R;
import com.lhb.player.playerapp.activity.DetailsActivity;
import com.lhb.player.playerapp.model.bean.WebdavResult;
import com.thegrizzlylabs.sardineandroid.DavResource;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by howe.zhong
 * on 2022/8/9  11:55
 */
public class HomePageItemAdapter extends RecyclerView.Adapter<HomePageItemAdapter.InnerHolder> {

    private List<WebdavResult.WebdavResultBean> mData = new ArrayList<>();

    @NonNull
    @Override
    public InnerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home_pager_content, parent, false);
        return new InnerHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InnerHolder holder, int position) {
        WebdavResult.WebdavResultBean dataBean = mData.get(position);
        holder.setData(dataBean.getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("onBindViewHolder", "onClick: .............. " + dataBean.getHref());
                Intent intent = new Intent(holder.itemView.getContext(), DetailsActivity.class);
                intent.putExtra("href", dataBean.getHref().toString());
                holder.itemView.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData != null ? mData.size() : 0;
    }

    public void setHomeData(List<WebdavResult.WebdavResultBean> data) {
        mData.clear();
        this.mData.addAll(data);
        notifyDataSetChanged();
    }

    public class InnerHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.title)
        public TextView mTitle;

        public InnerHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setData(String data) {
            mTitle.setText(data);
        }
    }
}
