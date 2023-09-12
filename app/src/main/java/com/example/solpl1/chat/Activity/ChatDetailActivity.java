package com.example.solpl1.chat.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.solpl1.chat.Adapters.MessageAdapter;
import com.example.solpl1.chat.Models.ChatItem;
import com.example.solpl1.chat.Models.MessageModel;
import com.example.solpl1.databinding.ActivityChatDetailBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;

public class ChatDetailActivity extends AppCompatActivity {
    ActivityChatDetailBinding binding;
    FirebaseDatabase database;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChatDetailActivity.this,ChatActivity.class);
            }
        });

        getSupportActionBar().hide();
        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        ArrayList<MessageModel> messageModels = new ArrayList<>();

        String senderId = auth.getUid();
        String chatRoomId = getIntent().getStringExtra("chatRoomId");


        MessageAdapter adapter = new MessageAdapter(messageModels, this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.chatRecyclerView.setAdapter(adapter);
        binding.chatRecyclerView.setLayoutManager(layoutManager);


        //채팅방 이름
       database.getReference()
               .child("chat")
               .child(chatRoomId).addValueEventListener(new ValueEventListener() {
                   @Override
                   public void onDataChange(@NonNull DataSnapshot snapshot) {
                       ChatItem chatItem = snapshot.getValue(ChatItem.class);
                       binding.textName.setText(chatItem.getTitle());
                   }
                   @Override
                   public void onCancelled(@NonNull DatabaseError error) {

                   }
               });
       //채팅방  메세지 보여주기
        database.getReference().child("chat")
                .child(chatRoomId).child("chatMessage").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        messageModels.clear();
                        for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                            MessageModel messageModel = dataSnapshot.getValue(MessageModel.class);
                            messageModels.add(messageModel);
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });




        // 보내기 버튼을 누르면
        binding.layoutSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String message = binding.etMessage.getText().toString();
                if(message != null){
                    final MessageModel messageModel = new MessageModel(senderId, message);
                    messageModel.setTimestemp(new Date().getTime());
                    binding.etMessage.setText("");

                    database.getReference().child("chat")
                            .child(chatRoomId)
                            .child("chatMessage")
                            .push()
                            .setValue(messageModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {

                                }
                            });
                }
            }
        });
    }
}