package com.example.solpl1.mainPost.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;

import com.example.solpl1.R;
import com.example.solpl1.UserAccount;
import com.example.solpl1.chat.Adapters.MessageAdapter;
import com.example.solpl1.databinding.ActivityMainPostBinding;
import com.example.solpl1.databinding.ActivityMainPostDetailBinding;
import com.example.solpl1.mainPost.Adapters.MainPostImageAdapter;
import com.example.solpl1.mainPost.Models.MainPost;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MainPostDetailActivity extends AppCompatActivity {
    ActivityMainPostDetailBinding binding;
    FirebaseAuth auth;
    FirebaseDatabase database;
    ArrayList<String> imageList;
    String hashTags;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainPostDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        database = FirebaseDatabase.getInstance();
        imageList = new ArrayList<>();

        //게시글 선택시 함께 넘어오는 정보
        String post_id = getIntent().getStringExtra("post_id"); //게시글 id
        String user_id = getIntent().getStringExtra("user_id"); //작성자 uid

        //포스트 정보
        database.getReference().child("post_database").child(post_id)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        MainPost mainPost = snapshot.getValue(MainPost.class);

                        binding.content.setText(mainPost.getContent());         // 게시글 내용
                        binding.cost.setText(mainPost.getCost() + "원");         // 여행 비용
                        binding.mainPostTitle.setText(mainPost.getTitle());     // 게시글 제목
                        binding.tripDate.setText(mainPost.getTrip_date());      // 여행 날짜
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        //해시태그
        hashTags = "";
        database.getReference().child("post_database").child(post_id).child("hashtags").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                        String hashTag = "#" + dataSnapshot.getValue(String.class);
                        hashTags = hashTags + hashTag;
                    }
                }
                binding.hashTags.setText(hashTags);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // 게시글 유저 정보 가져오기
        database.getReference()
                .child("UserAccount")
                .child(user_id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        UserAccount userAccount = snapshot.getValue(UserAccount.class);
                        if(userAccount.getImageUrl()== null){                                       //기본 프로필 이미지 설정
                            Picasso.get()
                                    .load(R.drawable.default_profile)
                                    .into(binding.profile);
                        } else {
                            Picasso.get()                                                           //유저 프로필
                                    .load(userAccount.getImageUrl())
                                    .placeholder(R.drawable.default_profile)
                                    .into(binding.profile);
                        }
                        String email = userAccount.getEmailId();
                        String email2 = email.substring(0, email.lastIndexOf("@"));
                        String name = userAccount.getName() + "(" + email2 + ")";
                        binding.mainPostName.setText(name);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });



        // 게시글 이미지 넣기
        MainPostImageAdapter adapter = new MainPostImageAdapter(imageList, this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.mainPostImages.setAdapter(adapter);
        binding.mainPostImages.setLayoutManager(layoutManager);


        database.getReference().child("post_database").child(post_id).child("images").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                imageList.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    String imageUrl = dataSnapshot.getValue(String.class);
                    imageList.add(imageUrl);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });









    }
}