package com.example.solpl1.mainPost.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.solpl1.R;
import com.example.solpl1.databinding.RecyclerviewMainPostImageItemBinding;
import com.example.solpl1.databinding.RecyclerviewNowPostBinding;
import com.example.solpl1.mainPost.Models.MainPost;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MainPostImageAdapter extends RecyclerView.Adapter<MainPostImageAdapter.ViewHolder>{

    ArrayList<String> list;
    Context context;

    public MainPostImageAdapter(ArrayList<String> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recyclerview_main_post_image_item,parent,false);
        return new MainPostImageAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        String image = list.get(position);
        Picasso.get()
                .load(image)
                .placeholder(R.drawable.ic_photo)
                .into(holder.binding.mainPostImage);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        RecyclerviewMainPostImageItemBinding binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = RecyclerviewMainPostImageItemBinding.bind(itemView);
        }
    }

}
