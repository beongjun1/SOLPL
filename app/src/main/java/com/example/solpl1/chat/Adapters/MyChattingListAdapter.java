package com.example.solpl1.chat.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.solpl1.R;
import com.example.solpl1.chat.Models.ChatItem;
import com.example.solpl1.databinding.RecyclerviewMyChattingListBinding;

import java.util.ArrayList;

public class MyChattingListAdapter extends RecyclerView.Adapter<MyChattingListAdapter.ViewHolder>{

    ArrayList<ChatItem> chatItemList;
    Context context;

    public MyChattingListAdapter(ArrayList<ChatItem> chatItemList, Context context) {
        this.chatItemList = chatItemList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recyclerview_my_chatting_list, parent, false);
        return new MyChattingListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ChatItem chatItem = chatItemList.get(position);



    }

    @Override
    public int getItemCount() {
        return chatItemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        RecyclerviewMyChattingListBinding binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = RecyclerviewMyChattingListBinding.bind(itemView);
        }
    }
}
