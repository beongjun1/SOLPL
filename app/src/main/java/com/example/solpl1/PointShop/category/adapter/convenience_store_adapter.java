package com.example.solpl1.PointShop.category.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.solpl1.PointShop.category.item.category_item;
import com.example.solpl1.PointShop.pointItem;
import com.example.solpl1.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class convenience_store_adapter extends RecyclerView.Adapter<convenience_store_adapter.ViewHolder> {
    private ArrayList<category_item> category_items;

    public void setCategory_items(ArrayList<category_item> category_items) {
        this.category_items = category_items;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public convenience_store_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.point_recycler,parent,false   );
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull convenience_store_adapter.ViewHolder holder, int position) {
        if(category_items!=null){
            holder.onBind(category_items.get(position));
        }
    }

    @Override
    public int getItemCount() {
        if (category_items != null) {
            return category_items.size();
        } else {
            return 0;
        }
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

        public void onBind(category_item category_item) {
            pointTitle.setText(category_item.getName());
            int cost = category_item.getCost();
            pointCost.setText(Integer.toString(cost));
            pointDate.setText(category_item.getDate());
            String point_img = category_item.getImgUrl();
            if (point_img != null && !point_img.isEmpty()) {
                Picasso.get().load(point_img).into(pointImg);
            }
        }
    }
}
