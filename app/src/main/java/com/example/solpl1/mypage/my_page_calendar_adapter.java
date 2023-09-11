package com.example.solpl1.mypage;

        import android.content.Context;
        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.TextView;

        import androidx.annotation.NonNull;
        import androidx.recyclerview.widget.LinearLayoutManager;
        import androidx.recyclerview.widget.RecyclerView;

        import com.example.solpl1.R;
        import com.example.solpl1.calendar.Second_Recyclerview_Adapter;
        import com.example.solpl1.calendar.calendar_item;

        import java.util.ArrayList;
        import java.util.List;

public class my_page_calendar_adapter extends RecyclerView.Adapter<my_page_calendar_adapter.ViewHolder> {

    private Context context;
    private List<String> dateList;
    private List<my_page_calendar_item> items;
    public my_page_calendar_adapter(List<String> data, List<my_page_calendar_item> items) {
        this.dateList = data;
        this.items = items;
    }
    @NonNull
    @Override
    public my_page_calendar_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_page_calendar_recycler, parent, false);
        return new my_page_calendar_adapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull my_page_calendar_adapter.ViewHolder holder, int position) {
        holder.date.setText(dateList.get(position));

        String selectedDay = dateList.get(position);
        List<my_page_calendar_item> filteredItems = filterItemsByDate(selectedDay);
        holder.adapter.setData(filteredItems);
    }

    @Override
    public int getItemCount() {
        return dateList.size(); // 날짜 목록의 크기를 반환
    }
    public void setDates(List<String> dateList) {
        this.dateList = dateList;
        notifyDataSetChanged();

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView date;
        private int position;
        private RecyclerView recyclerView;

        private my_page_calendar_Second_Recycler_adapter adapter;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.my_page_calendar_date);
            recyclerView = itemView.findViewById(R.id.my_page_second_recyclerview);

            adapter = new my_page_calendar_Second_Recycler_adapter(new ArrayList<>());
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));

        }


    }
    private List<my_page_calendar_item> filterItemsByDate(String selectedDate) {
        List<my_page_calendar_item> filteredItems = new ArrayList<>();
        Log.d("selectedDate", "selectedDate: " + selectedDate);

        if (items.size() > 0) {
            // items 리스트에 데이터가 있을 때 실행할 코드
            if (selectedDate != null) {
                for (my_page_calendar_item item : items) {
                    Log.d("getItemDate", "item.getDate(): " + item.getDate());
                    if (selectedDate.equals(item.getDate())) {
                        filteredItems.add(item);
                    }
                }
            }

        } else {
            Log.d("ItemsCheck", "items 리스트가 비어 있습니다.");
        }
        return filteredItems;
    }
}
