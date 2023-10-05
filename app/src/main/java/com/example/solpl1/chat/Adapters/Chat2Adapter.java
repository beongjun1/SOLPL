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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Chat2Adapter extends RecyclerView.Adapter<Chat2Adapter.ViewHolder>{

    Context context;
    ArrayList<ChatItem> list;
    FirebaseDatabase database;
    FirebaseAuth auth;

    public Chat2Adapter(Context context, ArrayList<ChatItem> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public Chat2Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.chat_room_sample,parent,false);
        return new Chat2Adapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Chat2Adapter.ViewHolder holder, int position) {
        ChatItem chatItem = list.get(position);
        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();

        holder.binding.chatTitle.setText(chatItem.getTitle());

        //현재 인원수
        String count = String.valueOf(chatItem.getUserCountCurrent());
        String maxCount = String.valueOf(chatItem.getUserCountMax());
        holder.binding.count.setText(count + "/" + maxCount);
        holder.binding.description.setText(chatItem.getDescription());


        // join 버튼을 누르면
        holder.binding.join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // chat_together에 유저정보 저장
                database.getReference().child("chat").child("chat_together")
                        .child(chatItem.getChatRoomId())
                        .child("chatUser")
                        .child(auth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                // 이미 들어왔던 유저
                                if(snapshot.exists()){
                                    return;
                                }
                                else {  // 신규 유저
                                    database.getReference().child("chat").child("chat_together")
                                            .child(chatItem.getChatRoomId())
                                            .child("chatUser")
                                            .child(auth.getCurrentUser().getUid())
                                            .setValue(true).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            // 현재 채팅방 유저 수  + 1
                                            database.getReference().child("chat").child("chat_together")
                                                    .child(chatItem.getChatRoomId())
                                                    .child("userCountCurrent").setValue(chatItem.getUserCountCurrent() + 1);
                                        }
                                    });
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                Intent intent = new Intent(context, ChatDetailActivity.class);
                intent.putExtra("chatRoomId", chatItem.getChatRoomId());
                intent.putExtra("title", chatItem.getTitle());
                intent.putExtra("chatType", "chat_together");
                context.startActivity(intent);
            }
        });

        database.getReference()
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