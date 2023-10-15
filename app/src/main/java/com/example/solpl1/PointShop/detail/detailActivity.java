package com.example.solpl1.PointShop.detail;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.solpl1.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class detailActivity extends AppCompatActivity {
    private TextView detailTitle,detailCost,detailStore;
    private ImageView title_img;
//    private CircleImageView store_img;
    private Button buy_btn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_point_detail);
        detailTitle = findViewById(R.id.title_id);
        detailCost = findViewById(R.id.detail_cost);
        detailStore = findViewById(R.id.detail_store);
        title_img = findViewById(R.id.title_img);
//        store_img = findViewById(R.id.circle_store);
        buy_btn = findViewById(R.id.buy_btn);

        Intent intent = getIntent();
        String key = intent.getStringExtra("key");
        Log.d("barcode_key : ", key);
        String category = intent.getStringExtra("category");
        Log.d("barcode_category : ", category);
        DatabaseReference store = FirebaseDatabase.getInstance().getReference("point").child("category").child(category).child(key);

        store.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String title = snapshot.child("title").getValue(String.class);
                String imgUrl = snapshot.child("imgUrl").getValue(String.class);
                int cost = snapshot.child("cost").getValue(Integer.class);
                String store = snapshot.child("store").getValue(String.class);

                detailTitle.setText(title);
                detailCost.setText(Integer.toString(cost));
                detailStore.setText(store);

                if (imgUrl != null && !imgUrl.isEmpty()) {
                    Glide.with(getApplicationContext())
                            .load(imgUrl)
                            .into(title_img);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}
