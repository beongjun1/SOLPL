package com.example.solpl1.PointShop;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.solpl1.PointShop.category.item.category_item;
import com.example.solpl1.R;
import com.example.solpl1.post_management.post_management_item;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class pointAdapter extends RecyclerView.Adapter<pointAdapter.ViewHolder> {

    private ArrayList<pointItem> pointItems;
    public void setPointItems(ArrayList<pointItem> list)
    {
        this.pointItems = list;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public pointAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.point_recycler,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull pointAdapter.ViewHolder holder, int position) {   
        if(pointItems!=null){
            holder.onBind(pointItems.get(position));
        }
    }

    @Override
    public int getItemCount() {
        if(pointItems!=null)
            return pointItems.size();
        else
            return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView pointTitle,pointCost,pointDate;
        private ImageView pointImg;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            pointTitle =itemView.findViewById(R.id.point_title);
            pointCost =itemView.findViewById(R.id.point_cost);
            pointDate =itemView.findViewById(R.id.point_date);
            pointImg =itemView.findViewById(R.id.point_img);
        }

        public void onBind(pointItem pointItem) {
            pointTitle.setText(pointItem.getName());
            int cost = pointItem.getCost();
            pointCost.setText(Integer.toString(cost));
            pointDate.setText(pointItem.getDate());
            String point_img = pointItem.getImgUrl();
            if (point_img != null && !point_img.isEmpty()) {
                Picasso.get().load(point_img).into(pointImg);
            }
        }
    }
}
