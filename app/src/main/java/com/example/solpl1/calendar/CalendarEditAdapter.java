package com.example.solpl1.calendar;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.solpl1.R;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CalendarEditAdapter extends RecyclerView.Adapter<CalendarEditAdapter.ViewHolder> {
    private static final int REQUEST_CODE = 1;

    private List<calendar_item> items;
    private ArrayList<String> listData;
    private Context context;
    private String randomKey; // 랜덤 키 값을 저장할 변수
    private SparseBooleanArray selectedItems = new SparseBooleanArray();
    private int prePosition = -1;

    public CalendarEditAdapter(ArrayList<String> data, List<calendar_item> items) {
        this.listData = data;
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_calendar_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.textView1.setText(listData.get(position));
        holder.onBind(position); // 초기화 및 애니메이션 설정

        // 항상 펼쳐진 상태로 변경
        holder.recyclerView.setVisibility(View.VISIBLE);

        // 데이터 필터링 및 두 번째 리사이클러뷰 어댑터에 데이터 전달
        String selectedDay = listData.get(position);
        List<calendar_item> filteredItems = filterItemsByDate(selectedDay);
        holder.adapter.setData(filteredItems);
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textView1;
        private RecyclerView recyclerView;
        Button edit_schedule;
        private int position;
        private Second_Recyclerview_Adapter adapter;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView1 = itemView.findViewById(R.id.main_item_day);
            recyclerView = itemView.findViewById(R.id.second_recyclerview);

            edit_schedule = itemView.findViewById(R.id.add_loc);

            // 두 번째 리사이클러뷰 어댑터 초기화
            adapter = new Second_Recyclerview_Adapter(new ArrayList<>());
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));

            // 랜덤 키 값 생성 및 저장
            randomKey = generateRandomKey();
            edit_schedule.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        String selectedDay = listData.get(position);

                        Intent intent = new Intent(itemView.getContext(), schedule_activity.class);
                        intent.putExtra("random_key", randomKey); // 랜덤 키 값을 전달
                        intent.putExtra("selected_day", selectedDay);
                        ((Activity) itemView.getContext()).startActivityForResult(intent, REQUEST_CODE);
                    }
                }
            });
        }

        public void onBind(int position) {
            this.position = position;
        }
    }

    private List<calendar_item> filterItemsByDate(String selectedDate) {
        List<calendar_item> filteredItems = new ArrayList<>();
        Log.d("selectedDate", "selectedDate: " + selectedDate);
        if (selectedDate != null) {
            for (calendar_item item : items) {
                Log.d("getDate", "getDate: " + item.getDate());

                if (selectedDate.equals(item.getDate())) {
                    filteredItems.add(item);
                }
            }
        }
        return filteredItems;
    }
    private String generateRandomKey() {
        // 랜덤 키 값을 생성하는 로직 구현
        // 예를 들어 UUID 라이브러리를 사용하여 랜덤 키 값을 생성할 수 있음
        // 이 키 값을 장소 데이터를 저장할 때 사용
        return UUID.randomUUID().toString();
    }
}
