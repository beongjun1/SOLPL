package com.example.solpl1.PointShop.category.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.solpl1.PointShop.barcode.barcodeActivity;
import com.example.solpl1.PointShop.category.item.category_item;
import com.example.solpl1.PointShop.detail.detailActivity;
import com.example.solpl1.PointShop.pointItem;
import com.example.solpl1.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class convenience_store_adapter extends RecyclerView.Adapter<convenience_store_adapter.ViewHolder> {
    private ArrayList<category_item> category_items;
    private String key;
    private String category;

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
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), detailActivity.class);
                intent.putExtra("key",key);
                intent.putExtra("category",category);
                v.getContext().startActivity(intent);
            }
        });
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
        private TextView pointTitle,pointCost,pointDate,pointStore;
        private ImageView pointImg;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            pointTitle =itemView.findViewById(R.id.point_title);
            pointCost =itemView.findViewById(R.id.point_cost);
            pointDate =itemView.findViewById(R.id.point_date);
            pointImg =itemView.findViewById(R.id.point_img);
            pointStore = itemView.findViewById(R.id.point_store);

        }

        public void onBind(category_item category_item) {
            pointTitle.setText(category_item.getName());
            int cost = category_item.getCost();
             key = category_item.getKey();
             category = category_item.getCategory();
            pointCost.setText(Integer.toString(cost));
            pointDate.setText(category_item.getDate());
            pointStore.setText(category_item.getStore());
            String point_img = category_item.getImgUrl();
            if (point_img != null && !point_img.isEmpty()) {
                Picasso.get().load(point_img).into(pointImg);
            }

        }
    }
}
