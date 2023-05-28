package com.example.solpl1.chat;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.solpl1.R;

import java.util.ArrayList;

public class chat_room_recycler_adapter extends RecyclerView.Adapter<chat_room_recycler_adapter.ViewHolder> {

    private ArrayList<chat_room_item> chat_room_items;
    @SuppressLint("NotifyDataSetChanged")
    public void setChat_room_items(ArrayList<chat_room_item> list){
        this.chat_room_items = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_room_recycler, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if(chat_room_items!=null){
            holder.onBind(chat_room_items.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return chat_room_items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView chat_room_name;
        private TextView chat_room_message;
        private TextView chat_room_message_count;
        private TextView chat_room_time;
        private ImageView chat_room_picture;
        public ViewHolder(View view) {
            super(view);
            chat_room_name = view.findViewById(R.id.chat_room_name);
            chat_room_message = view.findViewById(R.id.chat_room_message);
            chat_room_message_count = view.findViewById(R.id.chat_room_message_count);
            chat_room_time = view.findViewById(R.id.chat_room_time);
            chat_room_picture = view.findViewById(R.id.chat_room_image);
        }

        public void onBind(chat_room_item chat_room_item) {
            chat_room_name.setText(chat_room_item.getName());
            chat_room_message.setText(chat_room_item.getMessage());
            chat_room_message_count.setText(chat_room_item.getMessage_count());
            chat_room_time.setText(chat_room_item.getTime());
            chat_room_picture.setImageResource(chat_room_item.getPictureResId());
        }
    }
}
