package com.example.solpl1.calendar;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.solpl1.R;

import java.util.ArrayList;
import java.util.List;

public class CalendarEditAdapter extends RecyclerView.Adapter<CalendarEditAdapter.ViewHolder> {
    Second_Recyclerview_Adapter adapter;
    private List<calendar_item> items; // 메인 엑티비티에 만든 class 두 번째 어댑터에 보내기 위함

    // adapter에 들어갈 list
    private ArrayList<String> listData;

    private Context context;


    // 리사이클러뷰 접고 펴기 위한 변수
    // Item의 클릭 상태를 저장할 array 객체
    private SparseBooleanArray selectedItems = new SparseBooleanArray();
    // 직전에 클릭됐던 Item의 position
    private int prePosition = -1;

    public CalendarEditAdapter(ArrayList<String> data,  List<calendar_item> items){
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
    public void onBindViewHolder(@NonNull CalendarEditAdapter.ViewHolder holder,final int position) {
        //리사이클러뷰 넣는 부분
        holder.recyclerView.setLayoutManager( new LinearLayoutManager(context));
        adapter = new Second_Recyclerview_Adapter(items);   // 메인에서 받아온 items를 두 번째 리사이클러뷰 어댑터로 넘기기
        holder.recyclerView.setAdapter(adapter);

        holder.onBind(position);
        holder.textView1.setText(listData.get(position));   // textView 데이터 설정
//        // 해당 포지션의 일정 데이터를 가져옴
//        calendar_item item = items.get(position);
//
//        // 자식 어댑터에 일정 데이터 전달
//        adapter.setData(item.getPlaceList());
        holder.textView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int adapterPosition = holder.getAdapterPosition(); // getAdapterPosition()을 사용합니다
                if (selectedItems.get(adapterPosition)) {
                    // 펼쳐진 Item을 클릭 시
                    selectedItems.delete(adapterPosition);
                } else {
                    // 직전의 클릭됐던 Item의 클릭상태를 지움
                    selectedItems.delete(prePosition);
                    // 클릭한 Item의 position을 저장
                    selectedItems.put(adapterPosition, true);
                }
                // 해당 포지션의 변화를 알림
                if (prePosition != -1) notifyItemChanged(prePosition);
                notifyItemChanged(adapterPosition);
                // 클릭된 position 저장
                prePosition = adapterPosition;
            }
        });
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
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView1 = itemView.findViewById(R.id.main_item_day);
            recyclerView = itemView.findViewById(R.id.second_recyclerview);

            edit_schedule = itemView.findViewById(R.id.add_loc);

            edit_schedule.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION){
                        Intent intent = new Intent(itemView.getContext(), schedule_activity.class);
                        itemView.getContext().startActivity(intent);

                    }
                }
            });


        }

        public void onBind(int position) {
            this.position = position;
            changeVisibility(selectedItems.get(position));
        }
        private void changeVisibility(final boolean isExpanded) {
            // height 값을 dp로 지정해서 넣고싶으면 아래 소스를 이용
            int dpValue = 250;
            float d = context.getResources().getDisplayMetrics().density;
            int height = (int) (dpValue * d);

            // ValueAnimator.ofInt(int... values)는 View가 변할 값을 지정, 인자는 int 배열
            ValueAnimator va = isExpanded ? ValueAnimator.ofInt(0, height) : ValueAnimator.ofInt(height, 0);
            // Animation이 실행되는 시간, n/1000초
            va.setDuration(600);
            va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    // value는 height 값
                    int value = (int) animation.getAnimatedValue();
                    // imageView의 높이 변경
                    recyclerView.getLayoutParams().height = value;
                    recyclerView.requestLayout();
                    // imageView가 실제로 사라지게하는 부분
                    recyclerView.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
                }
            });
            // Animation start
            va.start();
        }
    }
}
