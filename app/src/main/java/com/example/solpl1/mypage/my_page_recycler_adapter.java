package com.example.solpl1.mypage;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.example.solpl1.R;
import com.example.solpl1.ClickableRecyclerAdapter;
import com.example.solpl1.my_page_item;
import com.example.solpl1.post_management.post_management_item;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class my_page_recycler_adapter extends RecyclerView.Adapter<my_page_recycler_adapter.ViewHolder> {

    protected ArrayList<my_page_item> PageList;


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_page_recycler, parent, false);
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

    @Override
    public int getItemViewType(int position) {
        return position;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        Button mypageAuthButton;

        ImageView trip_picture;
        TextView trip_date;
        TextView trip_loc;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            trip_picture = (ImageView) itemView.findViewById(R.id.trip_picture);
            trip_date = (TextView) itemView.findViewById(R.id.trip_date);
            trip_loc = (TextView) itemView.findViewById(R.id.trip_loc);


               }

        public void onBind(my_page_item item) {
            if (item != null) {
//                trip_picture.setImageResource(item.getResourceId());
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
        // 버튼의 숨김 여부를 설정하는 메소드
    }
}