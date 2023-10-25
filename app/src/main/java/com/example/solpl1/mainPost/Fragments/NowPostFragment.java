package com.example.solpl1.mainPost.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.solpl1.R;
import com.example.solpl1.UserAccount;
import com.example.solpl1.chat.Models.ChatItem;
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
import java.util.Collection;
import java.util.Collections;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;


public class NowPostFragment extends Fragment {

    CircleImageView profile;
    FloatingActionButton nowpost_addBtn;
    RecyclerView dashboardRecyclerView;
    ArrayList<NowPost> nowPostList;
    FirebaseDatabase database;
    FirebaseAuth auth;
    SearchView searchView;
    NowPostAdapter nowPostAdapter;
    FrameLayout frameLayout;
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


        //post 검색
        searchView = view.findViewById(R.id.nowpost_search);
        searchView.clearFocus();
        frameLayout = view.findViewById(R.id.nowpost_frameLayout);
        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                frameLayout.setVisibility(View.VISIBLE);
            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                frameLayout.setVisibility(View.GONE);
                return false;
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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



        nowPostAdapter = new NowPostAdapter(nowPostList,getContext());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        dashboardRecyclerView.setLayoutManager(layoutManager);
        dashboardRecyclerView.setAdapter(nowPostAdapter);

        database.getReference().child("nowPosts").orderByChild("postedAt").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                nowPostList.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    NowPost nowPost = dataSnapshot.getValue(NowPost.class);
                    nowPost.setPostId(dataSnapshot.getKey());
                    nowPostList.add(nowPost);
                }
                Collections.reverse(nowPostList);
                nowPostAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        return view;
    }

    // search 함수
    private void nowPostSearch(String newText) {
        ArrayList<NowPost> filteredList = new ArrayList<>();
        for(NowPost item : nowPostList){
            if(item.getPostDescription().toLowerCase(Locale.KOREA).contains(newText.toLowerCase(Locale.KOREA))){
                filteredList.add(item);
            }
        }
        if(filteredList.isEmpty()){
            Toast.makeText(getContext(),"해당 채팅방이 없습니다.",Toast.LENGTH_SHORT).show();
        }else {
            Collections.reverse(nowPostList);
            nowPostAdapter.setFilteredList(filteredList);
        }
    }
}