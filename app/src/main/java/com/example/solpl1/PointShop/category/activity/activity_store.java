package com.example.solpl1.PointShop.category.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.solpl1.PointShop.category.adapter.convenience_store_adapter;
import com.example.solpl1.PointShop.category.item.category_item;
import com.example.solpl1.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class activity_store extends AppCompatActivity {
    private ArrayList<category_item> category_List;
    private RecyclerView recyclerView;
    private convenience_store_adapter convenience_store_adapter;
    TextView title;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_shop);
//        ActionBar actionBar = getSupportActionBar();
//        actionBar.hide();
        Intent intent = getIntent();

        String category = intent.getStringExtra("category");
        Log.d("convenience:", "intent :" + category);
        String category_title = intent.getStringExtra("name");
        title = findViewById(R.id.category_name);
        title.setText(category_title);

        recyclerView = findViewById(R.id.category_recycler);

        convenience_store_adapter = new convenience_store_adapter();

        category_List = new ArrayList<>();

        recyclerView.setAdapter(convenience_store_adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        DatabaseReference pointShop = FirebaseDatabase.getInstance().getReference("point").child("category").child(category);

        pointShop.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot pointSnapshot : snapshot.getChildren()){
                    String title = pointSnapshot.child("title").getValue(String.class);
                    String date = pointSnapshot.child("date").getValue(String.class);
                    int cost = pointSnapshot.child("cost").getValue(Integer.class);
                    String imgUrl = pointSnapshot.child("imgUrl").getValue(String.class);
                    String key = pointSnapshot.child("key").getValue(String.class);

                    category_item category_items = new category_item(title,cost,date,imgUrl,key);
                    category_List.add(category_items);
                }
                convenience_store_adapter.setCategory_items(category_List);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
