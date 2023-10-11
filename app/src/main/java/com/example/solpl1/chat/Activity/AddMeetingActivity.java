package com.example.solpl1.chat.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.example.solpl1.R;
import com.example.solpl1.UserAccount;
import com.example.solpl1.chat.Adapters.ChatUserListAdapter;
import com.example.solpl1.chat.Models.MeetingModel;
import com.example.solpl1.databinding.ActivityAddMeetingBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.auth.User;
import com.shawnlin.numberpicker.NumberPicker;

import java.lang.reflect.Array;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.Queue;

public class AddMeetingActivity extends AppCompatActivity {
    ActivityAddMeetingBinding binding;
    FirebaseDatabase database;
    FirebaseAuth auth;
    ArrayList<String> list =  new ArrayList<>();       // 현재 채팅방에 있는 유저들
    String chatRoomId, chatType;
    // meeting 기능
    boolean[]  selectedUsers;
    ArrayList<Integer> meetingUserList = new ArrayList<>();  // 미팅 유저들

    String[]  UserList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddMeetingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        chatRoomId = getIntent().getStringExtra("chatRoomId");
        chatType = getIntent().getStringExtra("chatType");
        ArrayList<String> userUidList = getIntent().getStringArrayListExtra("userList");
        Log.e("meetingAcitvity", chatType);

//        UserList = new String[userUidList.size()];
//        for(int i=0;i < userUidList.size();i++){
//            UserList[i] = userUidList.get(i);
//        }
//        selectedUsers = new boolean[UserList.length];




        binding.dateSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                meetingDateCalendar();
            }
        });


        // 일정 순으로 정렬
        DatabaseReference meetingdb = database.getReference().child("chat").child(chatType).child(chatRoomId)
                .child("meeting");
        Query meetingOrderQuery = meetingdb.orderByChild("date");
        meetingOrderQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // 만남 생성 확인 버튼을 누를때
        binding.meetingAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = binding.meetingTitle.getText().toString();
                String date = binding.startingDate.getText().toString();
                String description = binding.meetingDescription.getText().toString();
                if(title.equals("") || date.equals("") || description.equals("")){
                    Toast.makeText(AddMeetingActivity.this, "내용을 입력해 주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
                else {
                    MeetingModel meetingModel = new MeetingModel();
                    meetingModel.setTitle(title);
                    meetingModel.setDate(date);
                    meetingModel.setDescription(description);
                    meetingModel.setUsers(userUidList);
                    DatabaseReference meetingRef = database.getReference().child("chat").child(chatType).child(chatRoomId).child("meeting")
                            .push();
                    String meetingId = meetingRef.getKey();
                    meetingModel.setMeetingId(meetingId);

                    meetingRef.setValue(meetingModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(AddMeetingActivity.this, "만남 생성", Toast.LENGTH_SHORT).show();
                                    onBackPressed();
                                }
                            });
                }


            }
        });



    }

    // 미팅 유저 선택  ==>  보류
//    private void showUserList() {
//
//
//
//        Log.e("selectedUsers", UserList.length +"");
//        AlertDialog.Builder builder = new AlertDialog.Builder(AddMeetingActivity.this);
//
//        builder.setTitle("Members");
//        builder.setCancelable(false);
//
//        builder.setMultiChoiceItems(UserList, selectedUsers, new DialogInterface.OnMultiChoiceClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
//                Log.e("onClick", "setMultiChoiceItems onClick");
//                if(isChecked){
//                    meetingUserList.add(which);
//                } else {
//                    meetingUserList.remove(which);
//                }
//            }
//        }).setPositiveButton("확인", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                StringBuilder stringBuilder = new StringBuilder();
//                for (int i = 0; i < meetingUserList.size(); i++) {
//                    stringBuilder.append(UserList[meetingUserList.get(i)]);
//
//                    if (i != meetingUserList.size() - 1) {
//                        stringBuilder.append(", ");
//                    }
//                    binding.users.setText(stringBuilder.toString());
//                }
//            }
//        }).setNegativeButton("취소", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//
//            }
//        });
//        builder.show();
//
//
//    }

    // 미팅 날짜 정하기
    private void meetingDateCalendar() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog dialog = new DatePickerDialog(AddMeetingActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                binding.startingDate.setText(MessageFormat.format("{0}/{1}/{2}", String.valueOf(year)
                        , String.valueOf(month + 1), dayFormat(dayOfMonth)));
            }
        }, year, month , day);
        dialog.getDatePicker().setMinDate(System.currentTimeMillis());
        dialog.show();

    }
    private String dayFormat(int day){
        if(day < 10) {
            String dayStr = String.valueOf(day);
            dayStr = "0" + dayStr;
            return dayStr;
        }
        else return String.valueOf(day);
    }




}