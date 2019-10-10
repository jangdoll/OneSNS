package com.example.onesns;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.ViewHolder> {
    private final List<FeedItem> mDataList;

    public FeedAdapter(List<FeedItem> dataList) {
        mDataList = dataList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_feed, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FeedItem item = mDataList.get(position);
        holder.title.setText(item.getTitle());
        holder.detail.setText(item.getDetail());
        holder.days.setText(item.getDays());
        holder.userName.setText(item.getUserName());
        holder.postTime.setText(item.getPost_time());
        Glide.with(holder.itemView)
                .load(item.getUser_image())
                .placeholder(R.mipmap.ic_launcher)
                .into(holder.img_profileImage);
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView detail;
        TextView days;
        TextView userName;
        TextView postTime;
        ImageView img_profileImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.text_title);
            detail = itemView.findViewById(R.id.text_detail);
            days = itemView.findViewById(R.id.updated_time_text_view);
            userName = itemView.findViewById(R.id.text_post);
            postTime = itemView.findViewById(R.id.tv_post_time);
            img_profileImage = itemView.findViewById(R.id.img_profileImage);
        }
    }
}
