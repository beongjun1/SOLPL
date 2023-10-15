package com.example.solpl1.PointShop;

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
import com.example.solpl1.R;
import com.example.solpl1.post_management.post_management_item;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class pointAdapter extends RecyclerView.Adapter<pointAdapter.ViewHolder> {

    private ArrayList<pointItem> pointItems;
    private String key;
    private String category;

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
        if(pointItems != null) {
            holder.onBind(pointItems.get(position));
            int adapterPosition = holder.getAdapterPosition();

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), detailActivity.class);
                    intent.putExtra("key", pointItems.get(adapterPosition).getKey());
                    intent.putExtra("category", pointItems.get(adapterPosition).getCategory());
                    v.getContext().startActivity(intent);
                }
            });
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

        public void onBind(pointItem pointItem) {
            pointTitle.setText(pointItem.getName());
            int cost = pointItem.getCost();
            key = pointItem.getKey();
            category = pointItem.getCategory();
            pointCost.setText(Integer.toString(cost));
            pointDate.setText(pointItem.getDate());
            pointStore.setText(pointItem.getStore());

            String point_img = pointItem.getImgUrl();
            if (point_img != null && !point_img.isEmpty()) {
                Picasso.get().load(point_img).into(pointImg);
            }
        }
    }
}
