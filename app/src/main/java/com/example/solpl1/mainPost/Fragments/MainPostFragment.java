package com.example.solpl1.mainPost.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.solpl1.R;
import com.example.solpl1.databinding.FragmentChat1Binding;
import com.example.solpl1.databinding.FragmentMainPostBinding;
import com.example.solpl1.mainPost.Activities.AddNowPostActivity;
import com.example.solpl1.mainPost.Activities.MainPostDetailActivity;
import com.example.solpl1.mainPost.Adapters.MainPostAdapter;
import com.example.solpl1.mainPost.Adapters.NowPostAdapter;
import com.example.solpl1.mainPost.Models.MainPost;
import com.example.solpl1.mainPost.Models.NowPost;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;


public class MainPostFragment extends Fragment {
    FragmentMainPostBinding binding;
    ArrayList<MainPost> mainPostList;
    FirebaseDatabase database;
    FirebaseAuth auth;
    MainPostAdapter mainPostAdapter;



    public MainPostFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentMainPostBinding.inflate(inflater,container,false);
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        mainPostList = new ArrayList<>();

        //post 검색

        binding.mainPostSearch.clearFocus();
        binding.mainPostSearch.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.mainPostFrameLayout.setVisibility(View.VISIBLE);
            }
        });
        binding.mainPostSearch.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                binding.mainPostFrameLayout.setVisibility(View.GONE);
                return false;
            }
        });
        binding.mainPostSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                nowPostSearch(newText);
                return true;
            }
        });


        // RecyclerView 설정
        mainPostAdapter = new MainPostAdapter(mainPostList,getContext());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        binding.mainPostRecyclerView.setLayoutManager(layoutManager);
        binding.mainPostRecyclerView.setAdapter(mainPostAdapter);

        database.getReference().child("post_database").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mainPostList.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    MainPost mainPost = dataSnapshot.getValue(MainPost.class);
                    mainPost.setPost_id(dataSnapshot.getKey());
                    mainPostList.add(mainPost);
                }
                mainPostAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return binding.getRoot();
    }

    // search 함수
    private void nowPostSearch(String newText) {
        ArrayList<MainPost> filteredList = new ArrayList<>();
        for(MainPost item : mainPostList){
            if(item.getContent().toLowerCase(Locale.KOREA).contains(newText.toLowerCase(Locale.KOREA))){
                filteredList.add(item);
            }
        }
        if(filteredList.isEmpty()){
            Toast.makeText(getContext(),"해당 채팅방이 없습니다.",Toast.LENGTH_SHORT).show();
        }else {
            Collections.reverse(mainPostList);
            mainPostAdapter.setFilteredList(filteredList);
        }
    }

}