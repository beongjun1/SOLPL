//package com.example.solpl1;
//
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.example.solpl1.mypage.my_page_recycler_adapter;
//
//public class ClickableRecyclerAdapter extends my_page_recycler_adapter {
//
//    private OnItemClickListener itemClickListener;
//    private boolean hideButton;
//
//    public interface OnItemClickListener {
//        void onItemClick(my_page_item item);
//    }
//
//    public void setOnItemClickListener(OnItemClickListener listener) {
//        this.itemClickListener = listener;
//    }
//
//    public void setHideButton(boolean hide) {
//        this.hideButton = hide;
//    }
//
//    @NonNull
//    @Override
//    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_page_recycler, parent, false);
//        ViewHolder viewHolder = new ViewHolder(view);
//
//        // Item view click listener
//        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                int position = viewHolder.getAdapterPosition();
//                if (itemClickListener != null && position != RecyclerView.NO_POSITION) {
//                    itemClickListener.onItemClick(PageList.get(position));
//                }
//            }
//        });
//
//        return viewHolder;
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//        super.onBindViewHolder(holder, position);
//
//        Button mypage_auth_button = holder.itemView.findViewById(R.id.mypage_auth);
//        if (hideButton) {
//            mypage_auth_button.setVisibility(View.GONE);
//        } else {
//            mypage_auth_button.setVisibility(View.VISIBLE);
//        }
//    }
//}