package com.example.solpl1.mainPost;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.solpl1.R;
import com.example.solpl1.UserAccount;
import com.example.solpl1.databinding.ActivityNowPostCommentBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

public class NowPostCommentActivity extends AppCompatActivity {

    ActivityNowPostCommentBinding binding;
    Intent intent;
    String postId;
    String postedBy;
    FirebaseDatabase database;
    FirebaseAuth auth;
    ArrayList<Comment> list = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNowPostCommentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        intent = getIntent();

        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();


       /*****클릭한 포스트 정보*****/
        postId = intent.getStringExtra("postId");
        postedBy = intent.getStringExtra("postedBy");


        // 포스트 정보
        database.getReference()
                .child("nowPosts")
                .child(postId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        NowPost nowPost = snapshot.getValue(NowPost.class);
                        Picasso.get()
                                .load(nowPost.getPostImage())
                                .placeholder(R.drawable.solpl_icon)
                                .into(binding.nowpostimage);
                        binding.description.setText(nowPost.getPostDescription());
                        binding.nowpostLike.setText(nowPost.getPostLike()+"");
                        binding.nowpostComment.setText(nowPost.getCommentCount()+"");
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
        // 유저 프로필,이름
        database.getReference()
                .child("UserAccount")
                .child(postedBy).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        UserAccount userAccount = snapshot.getValue(UserAccount.class);
                        if(userAccount.getImageUrl()== null){                                       // 기본 프로필 이미지 설정
                            Picasso.get()                                                           //유저 프로필
                                    .load(R.drawable.default_profile)
                                    .into(binding.profile);
                        } else {
                            Picasso.get()                                                           //유저 프로필
                                    .load(userAccount.getImageUrl())
                                    .placeholder(R.drawable.default_profile)
                                    .into(binding.profile);
                        }
                        binding.name.setText(userAccount.getName());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        // 댓글을 썼을때
        binding.commentPostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Comment comment = new Comment();
                comment.setCommentBody(binding.commentET.getText().toString());
                comment.setCommentedAt(new Date().getTime());
                comment.setCommentedBy(FirebaseAuth.getInstance().getUid());

                database.getReference()
                        .child("nowPosts")
                        .child(postId)
                        .child("comments")
                        .push()
                        .setValue(comment).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                database.getReference()
                                        .child("nowPosts")
                                        .child(postId)
                                        .child("commentCount").addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                int commentCount = 0;
                                                if (snapshot.exists()){
                                                    commentCount = snapshot.getValue(Integer.class);
                                                }
                                                database.getReference()
                                                        .child("nowPosts")
                                                        .child(postId)
                                                        .child("commentCount")
                                                        .setValue(commentCount + 1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void unused) {
                                                                binding.commentET.setText("");
                                                                Toast.makeText(NowPostCommentActivity.this, "댓글이 작성되었습니다.", Toast.LENGTH_LONG).show();
                                                            }
                                                        });
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });

                            }
                        });

            }
        });


        CommentAdapter adapter = new CommentAdapter(this,list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.commentRecylerView.setLayoutManager(layoutManager);
        binding.commentRecylerView.setAdapter(adapter);

        database.getReference()
                .child("nowPosts")
                .child(postId)
                .child("comments").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        list.clear();
                        for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                            Comment comment = dataSnapshot.getValue(Comment.class);
                            list.add(comment);
                        }
                        adapter.notifyDataSetChanged();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }
}