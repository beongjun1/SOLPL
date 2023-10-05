package com.example.solpl1.post_management;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.solpl1.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.internal.IdTokenListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class post_management_activity extends AppCompatActivity {
    private DatabaseReference post_database; // 실시간 데이터베이스
    private DatabaseReference user_account_database; // 실시간 데이터베이스
    private ArrayList<post_management_item> post_management_items;
    private RecyclerView post_management_recycler;
    private post_management_recycler_adapter post_management_recycler_adapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_management);
//        ActionBar actionBar = getSupportActionBar();
//        actionBar.hide();
        TextView emptyTextView = findViewById(R.id.post_text);
        post_management_recycler = findViewById(R.id.post_management_recycler_view);

        post_management_recycler_adapter = new post_management_recycler_adapter();

        post_management_recycler.setAdapter(post_management_recycler_adapter);
        post_management_recycler.setLayoutManager(new LinearLayoutManager(this));

        post_management_items = new ArrayList<>(); // 리스트 초기화

        post_database = FirebaseDatabase.getInstance().getReference("post_database");
        user_account_database = FirebaseDatabase.getInstance().getReference("UserAccount");

        post_database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                post_management_items.clear(); // 리스트 초기화

                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                FirebaseUser currentUser = firebaseAuth.getCurrentUser();

                if (currentUser != null) {
                    String currentUserIdToken = currentUser.getUid();
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        // 데이터베이스에서 각 데이터의 고유한 식별자인 Key 값을 가져오는 역할
                        String postId = postSnapshot.getKey();
                        String postUserIdToken = postSnapshot.child("id_token").getValue(String.class);

                        // 중복 확인
                        if (postUserIdToken != null && postUserIdToken.equals(currentUserIdToken) && !post_management_items.contains(postId)) {
                            // 중복이 아닌 경우에만 추가
                            user_account_database.child(postUserIdToken).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot userSnapshot) {
                                    String name = userSnapshot.child("name").getValue(String.class);
                                    if (name != null) {
                                        String time = postSnapshot.child("post_date").getValue(String.class);
                                        String title = postSnapshot.child("title").getValue(String.class);
                                        String content = postSnapshot.child("content").getValue(String.class);
                                        String profile_img = userSnapshot.child("imageUrl").getValue(String.class);

                                        ArrayList<String> urlList = new ArrayList<>();
                                        for (DataSnapshot urlSnapshot : postSnapshot.child("images").getChildren()) {
                                            String imageUrl = urlSnapshot.getValue(String.class);
                                            if (imageUrl != null) {
                                                urlList.add(imageUrl);
                                            }
                                        }

                                        post_management_item item = new post_management_item(postId,name, time, title, content, urlList,profile_img);
                                        post_management_items.add(item);
                                        post_management_recycler_adapter.setPost_management_items(post_management_items);

                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    // 에러 처리
                                    Toast.makeText(post_management_activity.this, "idToken error", Toast.LENGTH_SHORT).show();
                                }
                            });


                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // 에러 처리
            }
        });

    }
}
