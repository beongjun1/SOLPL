package com.example.solpl1.calendar;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.solpl1.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class EditCalendar extends AppCompatActivity {
    CalendarEditAdapter adapter;
    ArrayList<String> name = new ArrayList<>();
    private List<calendar_item> items = new ArrayList<calendar_item>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        RecyclerView recyclerView = findViewById(R.id.main_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CalendarEditAdapter(name, items);
        recyclerView.setAdapter(adapter);

        Intent intent = getIntent();
        String startDay = intent.getStringExtra("startDay");
        String endDay = intent.getStringExtra("endDay");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate startDate = LocalDate.parse(startDay, formatter);
        LocalDate endDate = LocalDate.parse(endDay, formatter);

        int tripDuration = endDate.getDayOfMonth() - startDate.getDayOfMonth() + 1;
        //리사이클러뷰 헤더
        for (int i = 1; i <= tripDuration; i++) {
            name.add(i+"일차");
        }




        //리사이클러뷰 안 리사이클러뷰에 들어갈 데이터
        items.add(new calendar_item( "경복궁", "vghj", "15:30~17:00"));
        items.add(new calendar_item("창덕궁", "2345", "역사"));
        items.add(new calendar_item("덕수궁", "3456", "역사"));
        items.add(new calendar_item("창경궁", "4567", "역사"));

        // adapter에 변경된 데이터를 알려주어야 화면에 반영됩니다.
        adapter.notifyDataSetChanged();

    }
}
