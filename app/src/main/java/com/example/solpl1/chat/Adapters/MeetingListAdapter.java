package com.example.solpl1.chat.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.solpl1.R;
import com.example.solpl1.chat.Activity.AddMeetingActivity;
import com.example.solpl1.chat.Activity.ChatDetailActivity;
import com.example.solpl1.chat.Models.MeetingModel;
import com.example.solpl1.databinding.DialogMeetingListBinding;
import com.example.solpl1.databinding.RecyclerviewMeetingListBinding;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class MeetingListAdapter extends RecyclerView.Adapter<MeetingListAdapter.ViewHolder>{
    ArrayList<MeetingModel> list;
    Context context;
    String chatType;
    String chatRoomId;
    FirebaseDatabase database;
    FirebaseAuth auth;
    ArrayList<String> userList =  new ArrayList<>();       // 현재 채팅방에 있는 유저들

    public MeetingListAdapter(ArrayList<MeetingModel> list, Context context, String chatType, String chatRoomId) {
        this.list = list;
        this.context = context;
        this.chatType = chatType;
        this.chatRoomId = chatRoomId;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recyclerview_meeting_list, parent, false);
        return new MeetingListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MeetingModel meetingModel = list.get(position);
        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        String nowDay = String.valueOf(year) + "/" + String.valueOf(month + 1) + "/" + dayFormat(day);
        Log.e("now date",nowDay);


        holder.binding.meetingDate.setText(meetingModel.getDate());
        holder.binding.meetingTitle.setText(meetingModel.getTitle());
        holder.binding.meetingDescription.setText(meetingModel.getDescription());


        // 지난 여행 일정은 "완료"라고 표시하기
        if(meetingModel.getDate().compareTo(nowDay) < 0){
            holder.binding.complete.setVisibility(View.VISIBLE);
        }

        // 별점주기 기능
        holder.binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 지난 여행에 한해서 별점 부여
                if(meetingModel.getDate().compareTo(nowDay) < 0){
                    showUserList(meetingModel);
                }
                else {
                    Toast.makeText(context, "아직 완료 전입니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });


        //권한
        database.getReference().child("chat").child(chatType).child(chatRoomId)
                .child("chatUser").child(auth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        boolean authority = snapshot.getValue(boolean.class);
                        if(authority){
                            holder.binding.delete.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        holder.binding.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase.getInstance().getReference().child("chat").child(chatType).child(chatRoomId)
                        .child("meeting").child(meetingModel.getMeetingId()).removeValue();
            }
        });
    }






    // chat_user_list dialog화면 보여주기
    private void showUserList(MeetingModel meetingModel) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_chat_user_list);



        RecyclerView recyclerView = dialog.findViewById(R.id.userRecyclerview);
        ChatUserListAdapter adapter = new ChatUserListAdapter(userList, context,chatType,chatRoomId,meetingModel.getMeetingId());
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);


        // adapter에 담기
        database.getReference().child("chat").child(chatType).child(chatRoomId)
                .child("meeting").child(meetingModel.getMeetingId())
                .child("users").addValueEventListener(new ValueEventListener(){
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        userList.clear();
                        for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                            String uid = dataSnapshot.getValue(String.class);
                            userList.add(uid);
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });




        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);


    }

    private int giveRating(String uid) {
        int rate = 0;
        View view1 = LayoutInflater.from(context).inflate(R.layout.dialog_meeting_description, null);
        EditText editText = view1.findViewById(R.id.meeting_description);


        AlertDialog alertDialog = new MaterialAlertDialogBuilder(context)
                .setTitle("설명")
                .setView(view1)
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).setNegativeButton("close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create();


        alertDialog.show();


        return rate;
    }

    private String dayFormat(int day){
        if(day < 10) {
            String dayStr = String.valueOf(day);
            dayStr = "0" + dayStr;
            return dayStr;
        }
        else return String.valueOf(day);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        RecyclerviewMeetingListBinding binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = RecyclerviewMeetingListBinding.bind(itemView);
        }
    }
}
