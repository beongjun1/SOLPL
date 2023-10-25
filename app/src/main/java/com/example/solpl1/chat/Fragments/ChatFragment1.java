package com.example.solpl1.chat.Fragments;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
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
import com.example.solpl1.chat.Adapters.Chat1Adapter;
import com.example.solpl1.chat.Models.ChatItem;

import com.example.solpl1.chat.Utils.RecyclerViewDecoration;
import com.example.solpl1.databinding.FragmentChat1Binding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Locale;


public class ChatFragment1 extends Fragment {


    FragmentChat1Binding binding;
    ArrayList<ChatItem> list = new ArrayList<>();
    FirebaseDatabase database;
    FirebaseAuth auth;
    Chat1Adapter adapter;
    LinearLayoutManager layoutManager;

    public ChatFragment1(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentChat1Binding.inflate(inflater,container,false);
        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();

        adapter = new Chat1Adapter(getContext(),list);
        binding.chatRecyclerView.setAdapter(adapter);
        layoutManager = new LinearLayoutManager(getContext());
        binding.chatRecyclerView.addItemDecoration(new RecyclerViewDecoration(20));
        binding.chatRecyclerView.setLayoutManager(layoutManager);


        //chat 검색
        binding.chatSearch.clearFocus();
        binding.chatSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                chatSearch(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                chatSearch(newText);
                return true;
            }
        });




        // chat add 버튼 누르면
        binding.chatAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               showBottomDialog();
            }
        });


        database.getReference().child("chat").child("chat_guide").addValueEventListener(new ValueEventListener() {
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

    // chat search 함수
    private void chatSearch(String newText) {
        ArrayList<ChatItem> filteredList = new ArrayList<>();
        for(ChatItem item : list){
            if(item.getTitle().toLowerCase(Locale.KOREA).contains(newText.toLowerCase(Locale.KOREA))){
                filteredList.add(item);
            }
        }
        if(filteredList.isEmpty()){
            Toast.makeText(getContext(),"해당 채팅방이 없습니다.",Toast.LENGTH_SHORT).show();
        }else {
            adapter.setFilteredList(filteredList);
        }
    }

    private void showBottomDialog() {

        final Dialog dialog = new Dialog(this.getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.activity_add_chat);


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
                if(!title.getText().toString().equals("") && !description.getText().toString().equals("")
                        && !countMax.getText().toString().equals("")){
                    chatItem.setChatRoomBy(auth.getCurrentUser().getUid());
                    chatItem.setDescription(description.getText().toString());
                    chatItem.setTitle(title.getText().toString());
                    int count = Integer.parseInt(countMax.getText().toString());
                    chatItem.setUserCountMax(count);
                    chatItem.setUserCountCurrent(1);

                    DatabaseReference pushedChatRef = database.getReference().child("chat").child("chat_guide").push();
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
                                            intent.putExtra("chatType", "chat_guide");
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



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

}