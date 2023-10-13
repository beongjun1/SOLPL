package com.example.solpl1.PointShop;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.solpl1.PointShop.category.activity.activity_store;
import com.example.solpl1.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class pointShopActivity extends AppCompatActivity {

    private ArrayList<pointItem> pointList;
    private RecyclerView recyclerView;
    private pointAdapter pointAdapter;
    LinearLayout convenience_store,gift_card, cafe, birth,bakery;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_point);
//        ActionBar actionBar = getSupportActionBar();
//        actionBar.hide();

        recyclerView = findViewById(R.id.point_recycler);
        pointList = new ArrayList<>();
        pointAdapter = new pointAdapter();

        recyclerView.setAdapter(pointAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        DatabaseReference pointShop = FirebaseDatabase.getInstance().getReference("point").child("category");
        pointShop.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot categorySnapshot : snapshot.getChildren()) {
                    String categoryName = categorySnapshot.getKey(); // 카테고리 이름 (bakery, cafe, ...)
                    for (DataSnapshot pointSnapshot : categorySnapshot.getChildren()) {
                        String title = pointSnapshot.child("title").getValue(String.class);
                        String date = pointSnapshot.child("date").getValue(String.class);
                        int cost = pointSnapshot.child("cost").getValue(Integer.class);
                        String imgUrl = pointSnapshot.child("imgUrl").getValue(String.class);
                        String key = pointSnapshot.child("key").getValue(String.class);
                        String store = pointSnapshot.child("store").getValue(String.class);
                        String category = pointSnapshot.child("category").getValue(String.class);
                        Log.d("pointShopActivity", "category: " + category);
                        pointItem pointItems = new pointItem(title,cost,store,date, imgUrl, key,category);
                        pointList.add(pointItems);
                    }
                    pointAdapter.setPointItems(pointList);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                finish();
            }
        });


        convenience_store = findViewById(R.id.convenience_store);
        gift_card = findViewById(R.id.gift_card);
        cafe = findViewById(R.id.cafe);
        birth = findViewById(R.id.birth);
        bakery = findViewById(R.id.bakery);

        convenience_store.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent( pointShopActivity.this, activity_store.class);
                intent.putExtra("category","convenience");
                intent.putExtra("name","편의점");
                startActivity(intent);
            }
        });
        gift_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent( pointShopActivity.this, activity_store.class);
                intent.putExtra("category","gift_card");
                intent.putExtra("name","상품권");
                startActivity(intent);
            }
        });
        cafe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent( pointShopActivity.this, activity_store.class);
                intent.putExtra("category","cafe");
                intent.putExtra("name","카페");
                startActivity(intent);
            }
        });
        birth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent( pointShopActivity.this, activity_store.class);
                intent.putExtra("category","birth");
                intent.putExtra("name","생일");
                startActivity(intent);
            }
        });
        bakery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent( pointShopActivity.this, activity_store.class);
                intent.putExtra("category","bakery");
                intent.putExtra("name","베이커리");
                startActivity(intent);
            }
        });
    }

}
