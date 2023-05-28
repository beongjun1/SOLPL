package com.example.solpl1.chat;


import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.solpl1.R;

import java.util.ArrayList;

public class chat_recycler_adapter extends RecyclerView.Adapter<chat_recycler_adapter.ViewHolder> {


    private ArrayList<chat_item> chatList;


    @SuppressLint("NotifyDataSetChanged")
    public void setChatList(ArrayList<chat_item> list) {
        this.chatList = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_recycler, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (chatList != null) {
            holder.onBind(chatList.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView chatTitle;
        private TextView chatTime;
        private ImageView chatPicture;
        private TextView chatName;
        private TextView chatEmail;
        private TextView chatContent;
        private TextView memberCountCurrent;
        private TextView memberCountTotal;
        private Button joinButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            chatTitle = itemView.findViewById(R.id.chat_title);
            chatTime = itemView.findViewById(R.id.chat_time);
            chatPicture = itemView.findViewById(R.id.chat_picture);
            chatName = itemView.findViewById(R.id.chat_name);
            chatEmail = itemView.findViewById(R.id.chat_email);
            chatContent = itemView.findViewById(R.id.chat_content);
            memberCountCurrent = itemView.findViewById(R.id.chat_member_count_current);
            memberCountTotal = itemView.findViewById(R.id.chat_member_count_total);
            joinButton = itemView.findViewById(R.id.chat_join_button);
        }

        public void onBind(chat_item chatItem) {
            chatTitle.setText(chatItem.getTitle());
            chatTime.setText(chatItem.getTime());
            chatPicture.setImageResource(chatItem.getPictureResId());
            chatName.setText(chatItem.getName());
            chatEmail.setText(chatItem.getEmail());
            chatContent.setText(chatItem.getContent());
            memberCountCurrent.setText(chatItem.getMemberCountCurrent());
            memberCountTotal.setText(chatItem.getMemberCountTotal());
        }
    }
}
