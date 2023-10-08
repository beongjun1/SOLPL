package com.example.solpl1.map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
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
import com.example.solpl1.mypage.my_page_calendar_Activity;

import java.util.ArrayList;

public class place_review_recycler_adapter extends RecyclerView.Adapter<place_review_recycler_adapter.ViewHolder>{

    protected ArrayList<my_page_item> PageList;
    private Context context;

    public place_review_recycler_adapter(Context context) {
        this.context = context;

    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.place_review_recycler, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull place_review_recycler_adapter.ViewHolder holder, int position) {
        if (PageList != null) {
            holder.onBind(PageList.get(position));

        }
        final int itemPosition = holder.getAdapterPosition(); // position 값을 final로 저장
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 선택한 아이템의 정보 가져오기
                my_page_item selectedItem = PageList.get(itemPosition);
                // 다음 화면으로 정보를 전달하는 Intent 생성
                Intent intent = new Intent(context, my_page_calendar_Activity.class);
                intent.putExtra("key", selectedItem.getKey());
                // Intent를 사용하여 다음 화면으로 이동
                //context.startActivity(intent);
            }
        });
    }

    public void setReviewList(ArrayList<my_page_item> list) {
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
        ImageView review_picture;
        TextView review_username,review,rating;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            review_picture = (ImageView) itemView.findViewById(R.id.review_picture);
            review_username = (TextView) itemView.findViewById(R.id.review_username);
            rating = (TextView) itemView.findViewById(R.id.rating);
            review = (TextView) itemView.findViewById(R.id.review);
        }
        public void onBind(my_page_item item) {
            if (item != null) {
//                trip_picture.setImageResource(item.getResourceId());
                String review_img = item.getImg();
                if (review_img != null && !review_img.isEmpty()) {
                    // 이미지 리사이징을 위한 크기 지정 (원하는 크기로 변경하세요)
                    int targetWidth = 1000;
                    int targetHeight = 1000;

                    Glide.with(itemView.getContext())
                            .load(review_img)
                            .override(targetWidth, targetHeight) // 리사이징 크기 지정
                            .transition(DrawableTransitionOptions.withCrossFade()) // 페이드 효과
                            .into(review_picture);
                }
                review_username.setText(item.getDate());
                rating.setText(item.getLoc());
                review.setText(item.getKey());
            }
        }

    }
    @Override
    public int getItemViewType(int position) {
        return position;
    }
}
