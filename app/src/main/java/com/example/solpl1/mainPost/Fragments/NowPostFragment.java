package com.example.solpl1.mainPost.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.solpl1.R;
import com.example.solpl1.UserAccount;
import com.example.solpl1.mainPost.Activities.AddNowPostActivity;
import com.example.solpl1.mainPost.Adapters.NowPostAdapter;
import com.example.solpl1.mainPost.Models.NowPost;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


public class NowPostFragment extends Fragment {

    CircleImageView profile;
    FloatingActionButton nowpost_addBtn;
    RecyclerView dashboardRecyclerView;
    ArrayList<NowPost> nowPostList;
    FirebaseDatabase database;
    FirebaseAuth auth;

    public NowPostFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_now_post, container, false);
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();


        // addPostBtn을 누르면************
        nowpost_addBtn = view.findViewById(R.id.nowpost_new_addBtn);
        nowpost_addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddNowPostActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });


        dashboardRecyclerView = view.findViewById(R.id.nowpost_recyclerView);
        nowPostList = new ArrayList<>();



        NowPostAdapter nowPostAdapter = new NowPostAdapter(nowPostList,getContext());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        dashboardRecyclerView.setLayoutManager(layoutManager);
        //dashboardRecyclerView.addItemDecoration(new DividerItemDecoration(dashboardRecyclerView.getContext(),DividerItemDecoration.VERTICAL));
        //dashboardRecyclerView.setNestedScrollingEnabled(false);
        dashboardRecyclerView.setAdapter(nowPostAdapter);

        database.getReference().child("nowPosts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                nowPostList.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    NowPost nowPost = dataSnapshot.getValue(NowPost.class);
                    nowPost.setPostId(dataSnapshot.getKey());
                    nowPostList.add(nowPost);
                }
                nowPostAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        return view;
    }
}