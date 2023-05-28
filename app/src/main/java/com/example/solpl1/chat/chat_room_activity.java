package com.example.solpl1.chat;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.solpl1.R;

import java.util.ArrayList;

public class chat_room_activity extends AppCompatActivity {

    private ArrayList<chat_room_item> chat_room_items;

    private com.example.solpl1.chat.chat_room_recycler_adapter chat_room_recycler_adapter;

    private RecyclerView chat_room_recycler;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        chat_room_recycler = findViewById(R.id.chat_room_recycler_view);
        chat_room_recycler_adapter = new chat_room_recycler_adapter();

        chat_room_recycler.setAdapter(chat_room_recycler_adapter);
        chat_room_recycler.setLayoutManager(new LinearLayoutManager(this));

        chat_room_items = new ArrayList<>();
        //chat_room_items에 객체들을 저장 한다
        chat_room_items.add(new chat_room_item("홍길동","안녕하세요!","24","02:44 PM",R.drawable.title_logo));
        chat_room_items.add(new chat_room_item("홍길동","안녕하세요!","24","02:44 PM",R.drawable.title_logo));
        chat_room_items.add(new chat_room_item("홍길동","안녕하세요!","24","02:44 PM",R.drawable.title_logo));
        chat_room_items.add(new chat_room_item("홍길동","안녕하세요!","24","02:44 PM",R.drawable.title_logo));
        chat_room_items.add(new chat_room_item("홍길동","안녕하세요!","24","02:44 PM",R.drawable.title_logo));
        chat_room_items.add(new chat_room_item("홍길동","안녕하세요!","24","02:44 PM",R.drawable.title_logo));

        //recycler Adapter에 저장한 객체들을 보내준다
        chat_room_recycler_adapter.setChat_room_items(chat_room_items);
    }
}
