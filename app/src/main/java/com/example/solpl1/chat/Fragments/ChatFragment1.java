package com.example.solpl1.chat.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.solpl1.R;
import com.example.solpl1.chat.Adapters.Chat1Adapter;
import com.example.solpl1.chat.Models.ChatItem;
import com.example.solpl1.databinding.FragmentChat1Binding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class ChatFragment1 extends Fragment {


    FragmentChat1Binding binding;
    ArrayList<ChatItem> list = new ArrayList<>();
    FirebaseDatabase database;
    public ChatFragment1(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentChat1Binding.inflate(inflater,container,false);
        database = FirebaseDatabase.getInstance();

        Chat1Adapter adapter = new Chat1Adapter(getContext(),list);
        binding.chatRecyclerView.setAdapter(adapter);


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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }
}