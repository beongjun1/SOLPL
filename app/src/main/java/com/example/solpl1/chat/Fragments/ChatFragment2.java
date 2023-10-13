package com.example.solpl1.chat.Fragments;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.solpl1.R;
import com.example.solpl1.chat.Activity.ChatDetailActivity;
import com.example.solpl1.chat.Adapters.Chat2Adapter;
import com.example.solpl1.chat.Models.ChatItem;
import com.example.solpl1.chat.Utils.RecyclerViewDecoration;
import com.example.solpl1.databinding.FragmentChat2Binding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class ChatFragment2 extends Fragment {

    FragmentChat2Binding binding;
    ArrayList<ChatItem> list = new ArrayList<>();
    FirebaseDatabase database;
    FirebaseAuth auth;


    public ChatFragment2() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {// Inflate the layout for this fragment
        binding = FragmentChat2Binding.inflate(inflater,container,false);
        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();

        Chat2Adapter adapter = new Chat2Adapter(getContext(),list);
        binding.chatRecyclerView.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        binding.chatRecyclerView.addItemDecoration(new RecyclerViewDecoration(20));
        binding.chatRecyclerView.setLayoutManager(layoutManager);



        // chat add 버튼 누르면
        binding.chatAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottomDialog();
            }
        });


        database.getReference().child("chat").child("chat_together").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    ChatItem chatItem = dataSnapshot.getValue(ChatItem.class);
                    chatItem.setChatRoomId(dataSnapshot.getKey());
                    list.add(chatItem);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }


    private void showBottomDialog() {

        final Dialog dialog = new Dialog(this.getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.activity_add_chat2);


        EditText title = dialog.findViewById(R.id.chat_title);
        EditText description = dialog.findViewById(R.id.chat_description);
        EditText countMax = dialog.findViewById(R.id.chat_count_max);
        Button chatAddNewBtn = dialog.findViewById(R.id.chat_add_new_btn);
        ImageView cancelBtn = dialog.findViewById(R.id.cancelButton);

        // 방 만들기 버튼을 누르면
        chatAddNewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChatItem chatItem = new ChatItem();
                if(!title.getText().toString().equals("") && !description.getText().toString().equals("") && !countMax.getText().toString().equals("")){
                    chatItem.setChatRoomBy(auth.getCurrentUser().getUid());
                    chatItem.setDescription(description.getText().toString());
                    chatItem.setTitle(title.getText().toString());
                    int count = Integer.parseInt(countMax.getText().toString());
                    chatItem.setUserCountMax(count);

                    DatabaseReference pushedChatRef = database.getReference().child("chat").child("chat_together").push();
                    String chatRoomId = pushedChatRef.getKey();
                    chatItem.setChatRoomId(chatRoomId);
                    pushedChatRef.setValue(chatItem).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            // chatUser DB에 방장 추가
                            pushedChatRef.child("chatUser")
                                    .child(auth.getCurrentUser().getUid()).setValue(true)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Intent intent = new Intent(getContext(), ChatDetailActivity.class);
                                            intent.putExtra("chatRoomId", chatItem.getChatRoomId());
                                            intent.putExtra("chatType", "chat_together");
                                            intent.putExtra("title", chatItem.getTitle());
                                            startActivity(intent);
                                            Toast.makeText(getContext(), "채팅방 생성", Toast.LENGTH_SHORT).show();
                                        }
                                    });


                        }
                    });
                } else{
                    Toast.makeText(getContext(),"빈 칸을 채워주세요.",Toast.LENGTH_LONG).show();
                }

            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }

}