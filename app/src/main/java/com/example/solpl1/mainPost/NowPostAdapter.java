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
import com.example.solpl1.UserAccount;
import com.example.solpl1.databinding.DashboardRecyclerviewBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class NowPostAdapter extends RecyclerView.Adapter<NowPostAdapter.ViewHolder>{

    ArrayList<NowPost> list;
    Context context;

    public NowPostAdapter(ArrayList<NowPost> list, Context context) {
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

        NowPost model = list.get(position);
        Picasso.get()                                                                           // 포스트 이미지
                .load(model.getPostImage())
                .placeholder(R.drawable.solpl_icon)
                .into(holder.binding.nowpostImage);
        holder.binding.nowpostLike.setText(model.getPostLike()+"");
        String content = model.getPostDescription();
        if(content.equals("")){                                         // 내용 글이 없으면 textView 제거
            holder.binding.nowpostContent.setVisibility(View.GONE);
        }
        else {
            holder.binding.nowpostContent.setText(model.getPostDescription());
            holder.binding.nowpostContent.setVisibility(View.VISIBLE);
        }

        FirebaseDatabase.getInstance().getReference().child("UserAccount")
                .child(model.getPostedBy()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        UserAccount userAccount = snapshot.getValue(UserAccount.class);
                        if(userAccount.getImageUrl()== null){                           // 기본 프로필 이미지 설정
                        } else {
                            Picasso.get()                                                           //유저 프로필
                                    .load(userAccount.getImageUrl())
                                    .placeholder(R.drawable.solpl_icon)
                                    .into(holder.binding.profile);
                        }

                        holder.binding.nowpostName.setText(userAccount.getName());              // 유저이름
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
        FirebaseDatabase.getInstance().getReference()
                .child("nowPosts")
                .child(model.getPostId())
                .child("likes")
                .child(FirebaseAuth.getInstance().getUid())
                .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.exists()){
                                    holder.binding.nowpostLike.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_favorite,0,0,0);
                                } else {
                                    holder.binding.nowpostLike.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            FirebaseDatabase.getInstance().getReference()
                                                    .child("nowPosts")
                                                    .child(model.getPostId())
                                                    .child("likes")
                                                    .child(FirebaseAuth.getInstance().getUid())
                                                    .setValue(true).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void unused) {
                                                            FirebaseDatabase.getInstance().getReference()
                                                                    .child("nowPosts")
                                                                    .child(model.getPostId())
                                                                    .child("postLike")
                                                                    .setValue(model.getPostLike() + 1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                        @Override
                                                                        public void onSuccess(Void unused) {
                                                                            holder.binding.nowpostLike.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_favorite,0,0,0);

                                                                        }
                                                                    });
                                                        }
                                                    });
                                        }
                                    });
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

        DashboardRecyclerviewBinding binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = DashboardRecyclerviewBinding.bind(itemView);


        }
    }



}