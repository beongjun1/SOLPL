package com.example.solpl1.post_management;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.solpl1.R;

import java.util.ArrayList;

public class post_management_activity extends AppCompatActivity {

    private ArrayList<post_management_item> post_management_items;

    private RecyclerView post_management_recycler;

    private com.example.solpl1.post_management.post_management_recycler_adapter post_management_recycler_adapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_management);

        post_management_recycler = findViewById(R.id.post_management_recycler_view);

        post_management_recycler_adapter = new post_management_recycler_adapter();

        post_management_recycler.setAdapter(post_management_recycler_adapter);
        post_management_recycler.setLayoutManager(new LinearLayoutManager(this));

        post_management_items = new ArrayList<>();
        post_management_items.add(new post_management_item("홍길동","3","제목입니다","내용입니다",R.drawable.ex1));
        post_management_items.add(new post_management_item("홍길동","3","제목입니다","내용입니다dddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd",R.drawable.ex1));
        post_management_items.add(new post_management_item("홍길동","3","제목입니다","내용입니다",R.drawable.ex1));
        post_management_items.add(new post_management_item("홍길동","3","제목입니다","내용입니다",R.drawable.ex1));
        post_management_items.add(new post_management_item("홍길동","3","제목입니다","내용입니다",R.drawable.ex1));
        post_management_items.add(new post_management_item("홍길동","3","제목입니다","내용입니다",R.drawable.ex1));

        post_management_recycler_adapter.setPost_management_items(post_management_items);

    }
}
