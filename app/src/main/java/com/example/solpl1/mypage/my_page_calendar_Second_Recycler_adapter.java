package com.example.solpl1.mypage;

import android.content.Context;
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
import com.example.solpl1.R;

import java.util.List;

public class my_page_calendar_Second_Recycler_adapter extends RecyclerView.Adapter<my_page_calendar_Second_Recycler_adapter.ViewHolder> {
    private List<my_page_calendar_item> items;
    Context context;


    public my_page_calendar_Second_Recycler_adapter(List<my_page_calendar_item> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public my_page_calendar_Second_Recycler_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_page_calendar_item, parent, false);
        return new my_page_calendar_Second_Recycler_adapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull my_page_calendar_Second_Recycler_adapter.ViewHolder holder, int position) {
        holder.time.setText(items.get(position).getTime());
        Log.d("Second_Recyclerview", "Time: " + items.get(position).getTime());
        holder.name.setText(items.get(position).getName());
        Log.d("Second_Recyclerview", "Time: " + items.get(position).getName());
        Log.d("Second_Recyclerview", "Time: " + items.get(position).getImageUrl());

        String imageUrl = items.get(position).getImageUrl();

        Glide.with(context)
                .load(imageUrl)
                .skipMemoryCache(true) // 메모리 캐시 비우기
                .diskCacheStrategy(DiskCacheStrategy.NONE) // 디스크 캐시 비우기
                .into(holder.placeImageView);

    }
    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setData(List<my_page_calendar_item> filteredItems) {
        this.items = filteredItems;
        Log.d("my_page_calendar_item", "setData: " + items.toString()); // Debug log
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView placeImageView;
        TextView time;
        TextView name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.my_page_editPlaceNameTextView);
            placeImageView = itemView.findViewById(R.id.my_page_editPlaceImageView);
            time = itemView.findViewById(R.id.my_page_editPlaceTimeTextView);
        }
    }
}
