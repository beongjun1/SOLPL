package com.example.solpl1.mypage;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.solpl1.R;
import com.example.solpl1.ClickableRecyclerAdapter;
import com.example.solpl1.my_page_item;

import java.util.ArrayList;

public class my_page_recycler_adapter extends RecyclerView.Adapter<my_page_recycler_adapter.ViewHolder> {

    protected ArrayList<my_page_item> PageList;
    private boolean hideButton; // 버튼 숨김 여부를 저장하는 변수

    private ClickableRecyclerAdapter.OnItemClickListener itemClickListener;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_page_recycler, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (PageList != null) {
            holder.onBind(PageList.get(position));
            holder.setButtonVisibility(hideButton); // 버튼의 숨김 여부 설정

        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setTripList(ArrayList<my_page_item> list) {
        this.PageList = list;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (PageList == null) {
            return 0;
        } else {
            return PageList.size();
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
    // 버튼 숨김 여부를 설정하는 메소드
    public void setHideButton(boolean hide) {
        this.hideButton = hide;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        Button mypageAuthButton;

        ImageView trip_picture;
        TextView trip_date;
        TextView trip_loc;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            trip_picture = (ImageView) itemView.findViewById(R.id.trip_picture);
            trip_date = (TextView) itemView.findViewById(R.id.trip_date);
            trip_loc = (TextView) itemView.findViewById(R.id.trip_loc);
            mypageAuthButton = itemView.findViewById(R.id.mypage_auth);


            mypageAuthButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        // 해당 위치의 myPageItem을 가져옵니다.
                        my_page_item clickedItem = PageList.get(position);

                        // trip_date와 trip_loc의 값을 가져옵니다.
                        String date = clickedItem.getDate();
                        String loc = clickedItem.getLoc();

                        // 가져온 값을 다음 액티비티로 전달하거나 사용할 수 있습니다.
                        // 여기에서는 Toast 메시지로 출력하는 예시를 보여드립니다.
                        Toast.makeText(itemView.getContext(), "Date: " + date + ", Loc: " + loc, Toast.LENGTH_SHORT).show();

                        // 다음 액티비티로 이동하는 코드를 작성합니다.
                        Intent intent = new Intent(itemView.getContext(), mypage_auth_activity.class);
                        intent.putExtra("date", date);
                        intent.putExtra("loc", loc);
                        itemView.getContext().startActivity(intent);
                    }
                }
            });
        }

        public void onBind(my_page_item item) {
            if (item != null) {
                trip_picture.setImageResource(item.getResourceId());
                trip_date.setText(item.getDate());
                trip_loc.setText(item.getLoc());
            }
        }
        // 버튼의 숨김 여부를 설정하는 메소드
        public void setButtonVisibility(boolean hide) {
            if (hide) {
                mypageAuthButton.setVisibility(View.GONE);
            } else {
                mypageAuthButton.setVisibility(View.VISIBLE);
            }
        }
    }
}