package com.example.solpl1.mainPost;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.solpl1.R;
import com.example.solpl1.UserAccount;
import com.example.solpl1.databinding.NowpostCommentRecyclerviewBinding;
import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder>{

    Context context;
    ArrayList<Comment> list;

    public CommentAdapter(Context context, ArrayList<Comment> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.nowpost_comment_recyclerview,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Comment comment = list.get(position);
        holder.binding.comment.setText(comment.getCommentBody());
        String text = TimeAgo.using(comment.getCommentedAt());
        holder.binding.time.setText(text);


        FirebaseDatabase.getInstance().getReference()
                .child("UserAccount")
                .child(comment.getCommentedBy()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        UserAccount user = snapshot.getValue(UserAccount.class);
                        if(user.getImageUrl()== null){                                       // 기본 프로필 이미지 설정
                            Picasso.get()                                                           //유저 프로필
                                    .load(R.drawable.default_profile)
                                    .into(holder.binding.profile);
                        } else {
                            Picasso.get()                                                           //유저 프로필
                                    .load(user.getImageUrl())
                                    .placeholder(R.drawable.default_profile)
                                    .into(holder.binding.profile);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        NowpostCommentRecyclerviewBinding binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = NowpostCommentRecyclerviewBinding.bind(itemView);
        }
    }



}
