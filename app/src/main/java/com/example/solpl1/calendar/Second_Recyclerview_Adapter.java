package com.example.solpl1.calendar;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.solpl1.MainActivity;
import com.example.solpl1.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class Second_Recyclerview_Adapter extends RecyclerView.Adapter<Second_Recyclerview_Adapter.ViewHolder> {

    Context context;
    private List<calendar_item> items;  //리사이클러뷰 안에 들어갈 값 저장

    public Second_Recyclerview_Adapter(List<calendar_item> items) {
        this.items =  items;

    }
    public void setData(List<calendar_item> placeDatabases) {
        this.items = placeDatabases;
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
        holder.name.setText(items.get(position).getName());
//        holder.placeImageView.set(items.get(position).getImageUrl());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView placeImageView;
        TextView time;
        TextView name;
//        Button edit_schedule;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.editPlaceNameTextView);
            placeImageView = itemView.findViewById(R.id.editPlaceImageView);
            time = itemView.findViewById(R.id.editPlaceTimeTextView);
//            edit_schedule = itemView.findViewById(R.id.add_loc);
//
//            edit_schedule.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    int position = getAdapterPosition();
//                    if(position != RecyclerView.NO_POSITION){
//                        Intent intent = new Intent(itemView.getContext(), schedule_activity.class);
//                        itemView.getContext().startActivity(intent);
//
//                    }
//                }
//            });

        }
    }
}
