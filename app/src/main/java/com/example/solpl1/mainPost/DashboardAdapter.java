package com.example.solpl1.mainPost;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.solpl1.R;

import java.util.ArrayList;

public class DashboardAdapter extends RecyclerView.Adapter<DashboardAdapter.ViewHolder>{

    ArrayList<NowPostModel> list;
    Context context;

    public DashboardAdapter(ArrayList<NowPostModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.dashboard_recyclerview,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        NowPostModel model = list.get(position);
        holder.profile.setImageResource(model.getProfile());
        holder.name.setText(model.getName());
        holder.postImage.setImageResource(model.getPostImage());
        holder.like.setText(model.getLike());
        holder.comment.setText(model.getComment());
        holder.save.setImageResource(model.getSave());
        holder.content.setText(model.getContent());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        ImageView profile, postImage, save;
        TextView name, content, comment, like;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            profile = itemView.findViewById(R.id.profile);
            postImage = itemView.findViewById(R.id.nowpost_image);
            save = itemView.findViewById(R.id.nowpost_save);
            name = itemView.findViewById(R.id.nowpost_name);
            content = itemView.findViewById(R.id.nowpost_content);
            comment = itemView.findViewById(R.id.nowpost_comment);
            like = itemView.findViewById(R.id.nowpost_like);
        }
    }



}
