package com.example.onesns;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.ViewHolder>{
    private final List<CalendarItem> mDataList;

    public CalendarAdapter(List<CalendarItem> dataList) {
        mDataList = dataList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_calendar, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CalendarItem item = mDataList.get(position);
        holder.title.setText(item.getTitle());
        holder.detail.setText(item.getDetail());
        holder.days.setText(item.getDays());
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView detail;
        TextView days;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.text_title);
            detail = itemView.findViewById(R.id.text_detail);
            days = itemView.findViewById(R.id.text_days);
        }
    }
}
