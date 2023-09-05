package com.example.solpl1.calendar;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.solpl1.R;

import java.util.List;

public class schedule_adapter extends RecyclerView.Adapter<schedule_adapter.scheduleViewHolder> {
    private List<PlaceData> placeList;

    public schedule_adapter(List<PlaceData> placeList) {
        this.placeList = placeList;
    }

    public void setData(List<PlaceData> placeList) {
        this.placeList = placeList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public schedule_adapter.scheduleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.schedule_recycler,parent,false);
       return new scheduleViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull schedule_adapter.scheduleViewHolder holder, int position) {
        PlaceData placeData = placeList.get(position);
        // 텍스트 데이터 설정
        holder.placeTextView.setText(placeData.getName());
//        String iconUrl = placeData.getIconUrl();
//        if (iconUrl != null) {
//            Glide.with(holder.itemView.getContext())
//                    .load(iconUrl)
//                    .into(holder.placeIconView);
//        } else {
//            // 이미지가 없는 경우에 대한 처리
//            // 예를 들어, 기본 이미지를 표시하거나 숨김 처리 등을 수행할 수 있습니다.
//        }
        String time = placeData.getTime();
        if(time != null){
            holder.placeTimeView.setText(placeData.getTime());
        }
        else {
            holder.placeTimeView.setText("알 수 없음");
        }

        // 이미지 로드 및 표시
        Bitmap imageBitmap = placeData.getImageBitmap();
        if (imageBitmap != null) {
            holder.placeImageView.setImageBitmap(imageBitmap);
        } else {
            // 이미지가 없는 경우에 대한 처리
            // 예를 들어, 기본 이미지를 표시하거나 숨김 처리 등을 수행할 수 있습니다.
            holder.placeImageView.setImageResource(R.drawable.solpl_icon);
        }
    }
    @Override
    public int getItemCount() {
        return placeList.size();
    }

    public class scheduleViewHolder extends RecyclerView.ViewHolder {

        private ImageView placeImageView;
        private TextView placeTextView;
        private TextView placeTimeView;
        private ImageView placeIconView;
        private View lineView;


        public scheduleViewHolder(@NonNull View itemView) {
            super(itemView);
//            placeIconView = itemView.findViewById(R.id.placeIconView);
            placeTextView = itemView.findViewById(R.id.placeNameTextView);
            placeImageView = itemView.findViewById(R.id.placeImageView);
            placeTimeView = itemView.findViewById(R.id.placeTimeTextView);

        }

    }
}
