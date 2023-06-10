package com.example.solpl1.mypage;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.solpl1.R;
import com.example.solpl1.ClickableRecyclerAdapter;
import com.example.solpl1.my_page_item;

import java.util.ArrayList;

public class my_page_writing_date_select_activity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_my_page);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);

        String selectedDate = "선택한 아이템의 date 값"; // 선택한 아이템의 date 값을 가져오는 로직을 구현해야 합니다.
        Intent resultIntent = new Intent();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        ClickableRecyclerAdapter adapter = new ClickableRecyclerAdapter();
        adapter.setHideButton(true);
        recyclerView.setAdapter(adapter);

        // 데이터 리스트 생성 (예시)
        ArrayList<my_page_item> itemList = new ArrayList<>();
        itemList.add(new my_page_item(R.drawable.activity_post_picture, "2023-05-21", "Location 1"));
        itemList.add(new my_page_item(R.drawable.activity_post_picture, "2023-05-22", "Location 2"));
        itemList.add(new my_page_item(R.drawable.activity_post_picture, "2023-05-23", "Location 3"));

        adapter.setHideButton(true); // 버튼 숨김 설정
        // ClickableRecyclerAdapter에 데이터 설정
        adapter.setTripList(itemList);

        adapter.setOnItemClickListener(new ClickableRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(my_page_item item) {
                String selectedDate = item.getDate(); // 선택한 아이템의 date 값을 가져옴

                Intent resultIntent = new Intent();
                resultIntent.putExtra("selectedDate", selectedDate); // 선택한 아이템의 date를 "selectedDate"라는 키로 전달
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });
    }
}