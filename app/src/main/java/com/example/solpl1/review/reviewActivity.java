package com.example.solpl1.review;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.solpl1.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class reviewActivity extends AppCompatActivity {
    private DatabaseReference user_account, location;
    private ArrayList<review_item> review_items;
    private RecyclerView recyclerView;
    private reviewAdapter reviewAdapter;
    TextView emptyTextView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        emptyTextView = findViewById(R.id.review_text);

        recyclerView = findViewById(R.id.review_recycler_view);

        reviewAdapter = new reviewAdapter();
        recyclerView.setAdapter(reviewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        review_items = new ArrayList<>();

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        String currentUserIdToken = currentUser.getUid();
        Log.d("currentUserIdToken","IdToken : "+ currentUserIdToken);
        user_account = FirebaseDatabase.getInstance().getReference("UserAccount").child(currentUserIdToken).child("reviews");

        user_account.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    String title = dataSnapshot.child("title").getValue(String.class);
                    String date = dataSnapshot.child("review_date").getValue(String.class);
                    String image = dataSnapshot.child("review_image").getValue(String.class);
                    String id = dataSnapshot.child("review_id").getValue(String.class);
                    String content = dataSnapshot.child("content").getValue(String.class);
                    String rating = dataSnapshot.child("rating").getValue(String.class);
                    String idToken = dataSnapshot.child("id_token").getValue(String.class);
                    Float ratingBar = Float.parseFloat(rating);

                    review_item reviewItem = new review_item(id,title,content,image,date,idToken,ratingBar);
                    review_items.add(reviewItem);
                }
                reviewAdapter.setReviewAdapter(review_items);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}
