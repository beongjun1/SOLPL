package com.example.solpl1.chat.Fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;

import com.example.solpl1.R;
import com.example.solpl1.chat.Activity.AddChatActivity;
import com.example.solpl1.chat.Activity.ChatActivity;
import com.example.solpl1.chat.Adapters.Chat1Adapter;
import com.example.solpl1.chat.Models.ChatItem;

import com.example.solpl1.databinding.FragmentChat1Binding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class ChatFragment1 extends Fragment {


    FragmentChat1Binding binding;
    ArrayList<ChatItem> list = new ArrayList<>();
    FirebaseDatabase database;
    FirebaseAuth auth;
    public ChatFragment1(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentChat1Binding.inflate(inflater,container,false);
        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();

        Chat1Adapter adapter = new Chat1Adapter(getContext(),list);
        binding.chatRecyclerView.setAdapter(adapter);

        // chat add 버튼 누르면
        binding.chatAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddChatActivity.class );
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        binding.chatRecyclerView.setLayoutManager(layoutManager);
        database.getReference().child("chat").addValueEventListener(new ValueEventListener() {
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

    private void showBottomDialog() {

        final Dialog dialog = new Dialog(this.getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.activity_add_chat);

        binding.chatAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChatItem chatItem = new ChatItem();
                chatItem.setChatRoomBy(auth.getCurrentUser().getUid());
                //chatItem.setDescription();
                //chatItem.setTitle();
                //chatItem.setUserCountMax();
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

}