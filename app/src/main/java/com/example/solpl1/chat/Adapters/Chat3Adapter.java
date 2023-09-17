package com.example.solpl1.chat.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.solpl1.R;
import com.example.solpl1.UserAccount;
import com.example.solpl1.chat.Activity.ChatDetailActivity;
import com.example.solpl1.chat.Models.ChatItem;
import com.example.solpl1.databinding.ChatRoomSampleBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Chat3Adapter extends RecyclerView.Adapter<Chat3Adapter.ViewHolder>{

    Context context;
    ArrayList<ChatItem> list;

    public Chat3Adapter(Context context, ArrayList<ChatItem> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public Chat3Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.chat_room_sample,parent,false);
        return new Chat3Adapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Chat3Adapter.ViewHolder holder, int position) {
        ChatItem chatItem = list.get(position);
        holder.binding.chatTitle.setText(chatItem.getTitle());

        String text = Integer.toString(chatItem.getUserCountCurrent());    //현재 인원수
        holder.binding.count.setText(text);
        holder.binding.description.setText(chatItem.getDescription());

        holder.binding.join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChatDetailActivity.class);
                intent.putExtra("chatRoomId", chatItem.getChatRoomId());
                intent.putExtra("title", chatItem.getTitle());
                intent.putExtra("chatType", "chat_local");


                context.startActivity(intent);
            }
        });

        FirebaseDatabase.getInstance().getReference()
                .child("UserAccount")
                .child(chatItem.getChatRoomBy()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        UserAccount user = snapshot.getValue(UserAccount.class);
                        Picasso.get()
                                .load(user.getImageUrl())
                                .placeholder(R.drawable.default_profile)
                                .into(holder.binding.profileImage);
                        holder.binding.name.setText(user.getName());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ChatRoomSampleBinding binding;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ChatRoomSampleBinding.bind(itemView);
        }
    }
}