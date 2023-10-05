package com.example.solpl1.mainPost.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.solpl1.R;
import com.example.solpl1.UserAccount;
import com.example.solpl1.databinding.RecyclerviewNowPostBinding;
import com.example.solpl1.mainPost.Models.NowPost;
import com.example.solpl1.mainPost.Activities.NowPostCommentActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;


/** nowPost recyclereview 어댑터**/
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
        View view = LayoutInflater.from(context).inflate(R.layout.recyclerview_now_post,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        NowPost model = list.get(position);
        Picasso.get()                                                                           // 포스트 이미지
                .load(model.getPostImage())
                .placeholder(R.drawable.solpl_icon)
                .into(holder.binding.nowpostImage);
        holder.binding.nowPostDate.setText(getReadableDataTime(model.getPostedAt()));        // 게시글 날짜
        holder.binding.nowpostLike.setText(model.getPostLike()+"");
        holder.binding.nowpostComment.setText(model.getCommentCount()+"");
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
                        if(userAccount.getImageUrl()== null){                                       // 기본 프로필 이미지 설정
                            Picasso.get()                                                           //유저 프로필
                                    .load(R.drawable.default_profile)
                                    .into(holder.binding.profile);
                        } else {
                            Picasso.get()                                                           //유저 프로필
                                    .load(userAccount.getImageUrl())
                                    .placeholder(R.drawable.default_profile)
                                    .into(holder.binding.profile);
                        }

                        holder.binding.name.setText(userAccount.getName());              // 유저이름
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        // 좋아요 기능
        FirebaseDatabase.getInstance().getReference()
                .child("nowPosts")
                .child(model.getPostId())
                .child("likes")
                .child(FirebaseAuth.getInstance().getUid())
                .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.exists()){                                              // nowpost DB에 사용자 Uid가 있을때(사용자가 좋아요를 눌렀을때)
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
                                                    .setValue(true).addOnSuccessListener(new OnSuccessListener<Void>() { // likes DB 값 변경
                                                        @Override
                                                        public void onSuccess(Void unused) {                            //성공시 like개수 +1
                                                            FirebaseDatabase.getInstance().getReference()
                                                                    .child("nowPosts")
                                                                    .child(model.getPostId())
                                                                    .child("postLike")
                                                                    .setValue(model.getPostLike() + 1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                        @Override
                                                                        public void onSuccess(Void unused) {            // 좋아요 누른 ui로 변경
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




        // 댓글 버튼 누를때
        holder.binding.nowpostComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, NowPostCommentActivity.class);
                intent.putExtra("postId", model.getPostId());
                intent.putExtra("postedBy", model.getPostedBy());
                intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK);         // 새로운 테스크 생성-> 기존에 열어둔게 없으면 새로 생성, 기존게 있으면 그 태스크로 들어감
                context.startActivity(intent);
            }
        });




    }

    private String getReadableDataTime(Long date){
        return new SimpleDateFormat("yyyy.MM.dd a HH:mm", Locale.KOREA).format(date);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        RecyclerviewNowPostBinding binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = RecyclerviewNowPostBinding.bind(itemView);


        }
    }



}
