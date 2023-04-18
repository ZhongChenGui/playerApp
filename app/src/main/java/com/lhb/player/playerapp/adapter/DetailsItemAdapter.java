package com.lhb.player.playerapp.adapter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.bumptech.glide.request.RequestOptions;
import com.lhb.player.playerapp.R;
import com.lhb.player.playerapp.activity.PlayActivity;
import com.lhb.player.playerapp.model.bean.DetailsData;
import com.lhb.player.playerapp.utils.CodeUtil;
import com.lhb.player.playerapp.utils.Constants;
import com.thegrizzlylabs.sardineandroid.DavResource;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by howe.zhong
 * on 2022/8/9  11:55
 */
public class DetailsItemAdapter extends RecyclerView.Adapter<DetailsItemAdapter.InnerHolder> {

    private  String mUser;
    private  String mPassword;
    private  String mUrl;
    private List<DavResource> mData = new ArrayList<>();

    public DetailsItemAdapter(){

    }

    public DetailsItemAdapter(String user, String password, String url) {
        mUser = user;
        mPassword = password;
        mUrl = url;
    }

    @NonNull
    @Override
    public InnerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_details_content, parent, false);
        return new InnerHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InnerHolder holder, int position) {
        DavResource detailsData = mData.get(position);
        holder.setData(detailsData.getName());
//        List<DavResource> childDavResources = detailsData.getChildDavResources();
//        String[] list = new String[childDavResources.size()];
//        for (int i = 0; i < childDavResources.size(); i++) {
//            list[i] = String.valueOf(childDavResources.get(i).getHref());
//        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.itemView.getContext(), PlayActivity.class);
                intent.putExtra("href", detailsData.getHref().toString());
//                intent.putExtra("data", list);
                holder.itemView.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData != null ? mData.size() : 0;
    }

    public void setData(List<DavResource> data) {
        mData.clear();
        this.mData.addAll(data);
        notifyDataSetChanged();
    }

    public class InnerHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.title)
        public TextView mTitle;
        @BindView(R.id.count)
        public TextView mCount;
        @BindView(R.id.cover)
        public ImageView mCover;

        public InnerHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @SuppressLint("CheckResult")
//        public void setData(String data, List<DavResource> childDavResources) {
        public void setData(String data) {
            mTitle.setText(data);
//            mCount.setText("共" + childDavResources.size() + "集");
//            String authHeader = CodeUtil.getB64Auth(mUser, mPassword);
//            if (childDavResources.size() > 0) {
//                RequestOptions options = new RequestOptions();
//                options.skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.ALL)
//                        .frame(1000000)
//                        .centerCrop();
//                LazyHeaders header = new LazyHeaders.Builder().addHeader("Authorization", authHeader).build();
//                Glide.with(mCover.getContext())
//                        .setDefaultRequestOptions(options)
//                        .load(new GlideUrl(mUrl + childDavResources.get(0).getHref(), header))
//                        .centerCrop()
//                        .into(mCover);
//
//            }

        }
    }
}
