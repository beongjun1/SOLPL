package com.example.solpl1.calendar;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.solpl1.MainActivity;
import com.example.solpl1.R;
import com.example.solpl1.post_management.post_management_item;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.squareup.picasso.Picasso;

import java.util.List;

public class Second_Recyclerview_Adapter extends RecyclerView.Adapter<Second_Recyclerview_Adapter.ViewHolder> {

    Context context;
    private List<calendar_item> items;  //리사이클러뷰 안에 들어갈 값 저장

    public Second_Recyclerview_Adapter(List<calendar_item> items) {
        this.items = items;

        notifyDataSetChanged();
    }

    public void setData(List<calendar_item> items) {
        this.items = items;
        Log.d("Second_Recyclerview", "setData: " + items.toString()); // Debug log
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Second_Recyclerview_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.second_recyclerview_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Second_Recyclerview_Adapter.ViewHolder holder, int position) {
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

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView placeImageView;
        TextView time;
        TextView name;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.editPlaceNameTextView);
            placeImageView = itemView.findViewById(R.id.editPlaceImageView);
            time = itemView.findViewById(R.id.editPlaceTimeTextView);


        }
    }
}
