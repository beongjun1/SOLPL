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
import com.example.solpl1.databinding.RecyclerviewMainPostItemBinding;
import com.example.solpl1.mainPost.Activities.MainPostDetailActivity;
import com.example.solpl1.mainPost.Activities.NowPostCommentActivity;
import com.example.solpl1.mainPost.Models.MainPost;
import com.example.solpl1.mainPost.Models.NowPost;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MainPostAdapter extends RecyclerView.Adapter<MainPostAdapter.ViewHolder>{
    ArrayList<MainPost> list;
    Context context;

    public MainPostAdapter(ArrayList<MainPost> list, Context context) {
        this.list = list;
        this.context = context;
    }
    public void setFilteredList(ArrayList<MainPost> filteredList){
        this.list = filteredList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recyclerview_main_post_item,parent,false);
        return new MainPostAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        MainPost model = list.get(position);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        FirebaseAuth auth = FirebaseAuth.getInstance();

        // 유저 정보 가져오기
        database.getReference().child("UserAccount").child(model.getId_token())
                .addValueEventListener(new ValueEventListener() {
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
                        String email = userAccount.getEmailId();
                        email = email.substring(0,email.lastIndexOf("@"));
                        String name = userAccount.getName() + "(" + email + ")";
                        holder.binding.mainPostName.setText(name);                 // 유저 이름  ex) 박정연(dsd899)
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        // 포스트 이미지
        database.getReference().child("post_database").child(model.getPost_id()).child("images")
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.exists()){
                                    for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                                        String image = dataSnapshot.getValue(String.class);

                                        Picasso.get()
                                                .load(image)
                                                .placeholder(R.drawable.ic_photo)
                                                .into(holder.binding.mainPostImages);
                                        break;
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

        //포스트 제목
        holder.binding.mainPostTitle.setText(model.getTitle());
        //게시글 날짜
        holder.binding.time.setText(model.getPost_date());

        //게시글을 눌렀을때
        holder.binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MainPostDetailActivity.class);
                intent.putExtra("post_id", model.getPost_id());
                intent.putExtra("user_id", model.getId_token());
                intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK);         // 새로운 테스크 생성-> 기존에 열어둔게 없으면 새로 생성, 기존게 있으면 그 태스크로 들어감
                context.startActivity(intent);
            }
        });





    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        RecyclerviewMainPostItemBinding binding;

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            binding = RecyclerviewMainPostItemBinding.bind(itemView);
        }
    }
}
