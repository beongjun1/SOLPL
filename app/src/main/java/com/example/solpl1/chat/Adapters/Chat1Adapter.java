package com.example.solpl1.chat.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.solpl1.chat.Activity.ChatDetailActivity;
import com.example.solpl1.R;
import com.example.solpl1.UserAccount;
import com.example.solpl1.chat.Models.ChatItem;
import com.example.solpl1.databinding.ChatRoomSampleBinding;
import com.example.solpl1.mainPost.Activities.AddNowPostActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Chat1Adapter extends RecyclerView.Adapter<Chat1Adapter.ViewHolder>{

    Context context;
    ArrayList<ChatItem> list;
    FirebaseDatabase database;
    FirebaseAuth auth;


    public Chat1Adapter(Context context, ArrayList<ChatItem> list) {
        this.context = context;
        this.list = list;
    }

    public void setFilteredList(ArrayList<ChatItem> filteredList){
        this.list = filteredList;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.chat_room_sample,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
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
                // chat_guide에 유저정보 확인
                database.getReference().child("chat").child("chat_guide")
                        .child(chatItem.getChatRoomId())
                        .child("chatUser")
                        .child(auth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                // 이미 들어왔던 유저
                                if(snapshot.exists()){
                                    Intent intent = new Intent(context, ChatDetailActivity.class);
                                    intent.putExtra("chatRoomId", chatItem.getChatRoomId());
                                    intent.putExtra("title", chatItem.getTitle());
                                    intent.putExtra("chatType", "chat_guide");
                                    context.startActivity(intent);
                                }
                                else {  // 신규 유저일때
                                    //  채팅방인원이 최대 인원일때 =>  못들어감
                                    database.getReference().child("chat").child("chat_guide")
                                            .child(chatItem.getChatRoomId()).child("userCountMax")
                                            .addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    int maxCount = snapshot.getValue(Integer.class);
                                                    database.getReference().child("chat").child("chat_guide")
                                                            .child(chatItem.getChatRoomId()).child("userCountCurrent")
                                                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                    int currentCount = snapshot.getValue(Integer.class);
                                                                    if(maxCount > currentCount){
                                                                        //  채팅방 참여가능할때
                                                                        database.getReference().child("chat").child("chat_guide")
                                                                                .child(chatItem.getChatRoomId())
                                                                                .child("chatUser")
                                                                                .child(auth.getCurrentUser().getUid())
                                                                                .setValue(false).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                    @Override
                                                                                    public void onSuccess(Void unused) {
                                                                                        // 현재 채팅방 유저 수  + 1
                                                                                        database.getReference().child("chat").child("chat_guide")
                                                                                                .child(chatItem.getChatRoomId())
                                                                                                .child("userCountCurrent").setValue(chatItem.getUserCountCurrent() + 1);
                                                                                    }
                                                                                });


                                                                        Intent intent = new Intent(context, ChatDetailActivity.class);
                                                                        intent.putExtra("chatRoomId", chatItem.getChatRoomId());
                                                                        intent.putExtra("title", chatItem.getTitle());
                                                                        intent.putExtra("chatType", "chat_guide");
                                                                        context.startActivity(intent);
                                                                    }
                                                                    else {
                                                                        Toast.makeText(context, "방이 최대 인원 입니다.", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                }

                                                                @Override
                                                                public void onCancelled(@NonNull DatabaseError error) {

                                                                }
                                                            });
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {

                                                }
                                            });
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
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
