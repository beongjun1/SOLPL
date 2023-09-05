package com.example.solpl1.mypage;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.example.solpl1.R;
import com.example.solpl1.my_page_item;

import java.util.ArrayList;

public class my_page_selected_recycler extends RecyclerView.Adapter<my_page_selected_recycler.ViewHolder> {

    private ArrayList<my_page_item> PageList;
    private OnItemClickListener itemClickListener;

    public interface OnItemClickListener {
        void onItemClick(my_page_item item);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.itemClickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.selected_day_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (PageList != null) {
            holder.onBind(PageList.get(position));
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setTripList(ArrayList<my_page_item> list) {
        this.PageList = list;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (PageList == null) {
            return 0;
        } else {
            return PageList.size();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView trip_picture;
        TextView trip_date;
        TextView trip_loc;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            trip_picture = itemView.findViewById(R.id.select_picture);
            trip_date = itemView.findViewById(R.id.select_date);
            trip_loc = itemView.findViewById(R.id.select_loc);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && itemClickListener != null) {
                        itemClickListener.onItemClick(PageList.get(position));
                    }
                }
            });
        }

        public void onBind(my_page_item item) {
            if (item != null) {
                String trip_img = item.getImg();
                if (trip_img != null && !trip_img.isEmpty()) {
                    // 이미지 리사이징을 위한 크기 지정 (원하는 크기로 변경하세요)
                    int targetWidth = 1000;
                    int targetHeight = 1000;

                    Glide.with(itemView.getContext())
                            .load(trip_img)
                            .override(targetWidth, targetHeight) // 리사이징 크기 지정
                            .transition(DrawableTransitionOptions.withCrossFade()) // 페이드 효과
                            .into(trip_picture);
                }
                trip_date.setText(item.getDate());
                trip_loc.setText(item.getLoc());
            }
        }
    }
}
