package com.example.solpl1.mainPost;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.solpl1.R;

import java.util.ArrayList;


public class MainPostFragment extends Fragment {

    Button nowpost_addBtn;
    RecyclerView dashboardRecyclerView;
    ArrayList<NowPostModel> nowPostModelArrayList;

    public MainPostFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_post, container, false);

        // addPostBtn을 누르면************  transaction. replace ????
        nowpost_addBtn = view.findViewById(R.id.nowpost_addNew);
        nowpost_addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddNowPostActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });


        dashboardRecyclerView = view.findViewById(R.id.nowpost_recyclerView);

        nowPostModelArrayList = new ArrayList<>();
        nowPostModelArrayList.add(new NowPostModel(R.drawable.profile,R.drawable.landscape,R.drawable.ic_bookmark,
                "박정연","내용입니다.","3","10"));
        nowPostModelArrayList.add(new NowPostModel(R.drawable.profile,R.drawable.landscape,R.drawable.ic_bookmark,
                "박정연","내용입니다.","3","10"));
        nowPostModelArrayList.add(new NowPostModel(R.drawable.profile,R.drawable.landscape,R.drawable.ic_bookmark,
                "박정연","내용입니다.","3","10"));
        nowPostModelArrayList.add(new NowPostModel(R.drawable.profile,R.drawable.landscape,R.drawable.ic_bookmark,
                "박정연","내용입니다.","3","10"));
        nowPostModelArrayList.add(new NowPostModel(R.drawable.profile,R.drawable.landscape,R.drawable.ic_bookmark,
                "박정연","내용입니다.","3","10"));
        nowPostModelArrayList.add(new NowPostModel(R.drawable.profile,R.drawable.landscape,R.drawable.ic_bookmark,
                "박정연","내용입니다.","3","10"));


        DashboardAdapter dashboardAdapter = new DashboardAdapter(nowPostModelArrayList,getContext());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        dashboardRecyclerView.setLayoutManager(layoutManager);
        dashboardRecyclerView.setNestedScrollingEnabled(false);
        dashboardRecyclerView.setAdapter(dashboardAdapter);


        return view;
    }
}