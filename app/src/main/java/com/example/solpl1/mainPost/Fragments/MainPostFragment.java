package com.example.solpl1.mainPost.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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


public class MainPostFragment extends Fragment {
    FragmentMainPostBinding binding;
    ArrayList<MainPost> mainPostList;
    FirebaseDatabase database;
    FirebaseAuth auth;



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


        // RecyclerView 설정
        MainPostAdapter nowPostAdapter = new MainPostAdapter(mainPostList,getContext());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        binding.mainPostRecyclerView.setLayoutManager(layoutManager);
        binding.mainPostRecyclerView.setAdapter(nowPostAdapter);

        database.getReference().child("post_database").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mainPostList.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    MainPost mainPost = dataSnapshot.getValue(MainPost.class);
                    mainPost.setPost_id(dataSnapshot.getKey());
                    mainPostList.add(mainPost);
                }
                nowPostAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });





        return binding.getRoot();
    }

}