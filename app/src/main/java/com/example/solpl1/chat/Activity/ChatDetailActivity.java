package com.example.solpl1.chat.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.TextView;


import com.example.solpl1.R;
import com.example.solpl1.UserAccount;
import com.example.solpl1.chat.Adapters.ChatUserListAdapter;
import com.example.solpl1.chat.Adapters.MeetingListAdapter;
import com.example.solpl1.chat.Adapters.MessageAdapter;
import com.example.solpl1.chat.Models.ChatItem;
import com.example.solpl1.chat.Models.MeetingModel;
import com.example.solpl1.chat.Models.MessageModel;
import com.example.solpl1.databinding.ActivityChatDetailBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.shawnlin.numberpicker.NumberPicker;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ChatDetailActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    ActivityChatDetailBinding binding;
    FirebaseDatabase database;
    FirebaseAuth auth;
    boolean authority;
    DrawerLayout drawerLayout;
    ArrayList<MessageModel> messageModels;
    String chatRoomId, chatType, title;
    int chatUserCount;
    String address1, address2;
    ArrayList<String> list =  new ArrayList<>();       // 현재 채팅방에 있는 유저들
    ArrayList<MeetingModel> meetingList = new ArrayList<>();    // 미팅 리스트

    // meeting 기능
    String meetingDate;
    TextView startingDate;
    NumberPicker numberPicker;
    String meetingTime;
    boolean[]  selectedUsers;
    ArrayList<Integer>  meetingUserList= new ArrayList<>();  // 미팅 유저들
    String[]  UserList;
    TextView usersTv;
    TextInputEditText editText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        //toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav,
                R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        if (savedInstanceState == null) {

        }

        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        messageModels = new ArrayList<>();
        authority = false;

        String senderId = auth.getCurrentUser().getUid();
        chatRoomId = getIntent().getStringExtra("chatRoomId");
        chatType = getIntent().getStringExtra("chatType");
        title = getIntent().getStringExtra("title");

        address1 = getIntent().getStringExtra("address1");
        address2 = getIntent().getStringExtra("address2");


        binding.textName.setText(title);


        MessageAdapter adapter = new MessageAdapter(messageModels, this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.chatRecyclerView.setAdapter(adapter);
        binding.chatRecyclerView.setLayoutManager(layoutManager);

        //채팅방  메세지 보여주기
        database.getReference().child("chat").child(chatType)
                .child(chatRoomId).child("chatMessage").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        messageModels.clear();
                        for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                            MessageModel messageModel = dataSnapshot.getValue(MessageModel.class);
                            messageModels.add(messageModel);
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });




        // 보내기 버튼을 누르면
        binding.layoutSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String message = binding.etMessage.getText().toString();
                if(message != null){
                    final MessageModel messageModel = new MessageModel(senderId, message);
                    messageModel.setTimestemp(new Date().getTime());
                    binding.etMessage.setText("");

                    database.getReference().child("chat")
                            .child(chatType)
                            .child(chatRoomId)
                            .child("chatMessage")
                            .push()
                            .setValue(messageModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {

                                }
                            });
                }
            }
        });

        //권한 확인
        Log.e("senderId" , senderId);
        address1 = "경기도";
        address2 = "의왕";
        if(chatType.equals("chat_local")){
            database.getReference().child("chat").child(chatType)
                    .child(address1).child(address2).child(chatRoomId).child("chatUser")
                    .child(senderId).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            authority = snapshot.getValue(boolean.class);
                            Log.e("authority", authority+"");
                            if(authority){
                                //binding.meetingBtn.setVisibility(View.VISIBLE);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
        }
        else {
            database.getReference().child("chat").child(chatType).child(chatRoomId).child("chatUser")
                    .child(senderId).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            authority = snapshot.getValue(boolean.class);
                            Log.e("authority", authority+"");
                            if(authority){
                                binding.meetingBtn.setVisibility(View.VISIBLE);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
        }



        // 채팅방 여행 팀 생성 => 방장만(chatUser에서 true인사람)

        binding.meetingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChatDetailActivity.this, AddMeetingActivity.class);
                intent.putExtra("chatRoomId", chatRoomId);
                intent.putExtra("chatType", chatType);
                intent.putExtra("userList", list);
                startActivity(intent);
            }
        });



        // 채팅방 유저 확인
        database.getReference().child("chat").child(chatType).child(chatRoomId)
                .child("chatUser").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        list.clear();
                        for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                            String uid = dataSnapshot.getKey();
                            list.add(uid);
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }

    // 미팅 장소
    private void meetingDescription() {
        View view1 = LayoutInflater.from(ChatDetailActivity.this).inflate(R.layout.dialog_meeting_description, null);
        editText = view1.findViewById(R.id.meeting_description);


        AlertDialog alertDialog = new MaterialAlertDialogBuilder(ChatDetailActivity.this)
                .setTitle("장소")
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
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.meetings:     // 미팅 일정 보여주기
                showBottomDialog2();
                break;
            case R.id.chat_users:       // dialog 이용해서 보여주기
                showBottomDialog();
                break;
            case R.id.chat_out:         // db에서 정보 삭제하고 나가기
                withdrawFromChatRoom();
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    // 미팅 일정 보여주기
    private void showBottomDialog2() {
        final Dialog dialog2 = new Dialog(this);
        dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog2.setContentView(R.layout.dialog_meeting_list);

        TextView textView = dialog2.findViewById(R.id.textView13);
        RecyclerView recyclerView = dialog2.findViewById(R.id.meetingRecyclerview);
        MeetingListAdapter adapter = new MeetingListAdapter(meetingList, this,chatType,chatRoomId);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);


        // 일정 순으로 정렬
        DatabaseReference meetingdb = database.getReference().child("chat").child(chatType).child(chatRoomId)
                .child("meeting");
        Query meetingOrderQuery = meetingdb.orderByChild("date");

        meetingOrderQuery.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        meetingList.clear();
                        for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                            MeetingModel meetingModel = dataSnapshot.getValue(MeetingModel.class);
                            meetingList.add(meetingModel);
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

//        // 일정이 없을때
//        if(meetingList.isEmpty()){
//            recyclerView.setVisibility(View.GONE);
//            textView.setVisibility(View.VISIBLE);
//        }
        dialog2.show();
        dialog2.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog2.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog2.getWindow().setGravity(Gravity.BOTTOM);


    }



    // chat_user_list dialog화면 보여주기
    private void showBottomDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_chat_user_list);



        RecyclerView recyclerView = dialog.findViewById(R.id.userRecyclerview);
        ChatUserListAdapter adapter = new ChatUserListAdapter(list, this,chatType,chatRoomId);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);


        database.getReference().child("chat").child(chatType).child(chatRoomId)
                .child("chatUser").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        list.clear();
                        for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                            String uid = dataSnapshot.getKey();
                            list.add(uid);
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

    // 채팅방 나가기 기능
    private void withdrawFromChatRoom() {

        database.getReference().child("chat").child(chatType).child(chatRoomId)
                .child("chatUser").child(auth.getCurrentUser().getUid()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        database.getReference().child("chat").child(chatType).child(chatRoomId)
                                .child("chatUser").addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if(snapshot.exists()){
                                            chatUserCount = 0;
                                            for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                                boolean user = dataSnapshot.getValue(boolean.class);
                                                if(user) chatUserCount++;
                                                Log.w("chatUserCount", Integer.toString(chatUserCount));
                                            }
                                            database.getReference().child("chat").child(chatType).child(chatRoomId)
                                                    .child("userCountCurrent").setValue(chatUserCount);
                                        }
                                        else {  // chatUser가 없을때
                                            database.getReference().child("chat").child(chatType).child(chatRoomId)
                                                    .child("userCountCurrent").setValue(0);
                                        }
                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                        Intent intent = new Intent(getApplicationContext(),ChatActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent);
                    }
                });
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void listToUserList(){

        ArrayList<String> tmp = new ArrayList<>();
        database.getReference().child("chat").child(chatType).child(chatRoomId)
                .child("chatUser").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                            String uid = dataSnapshot.getKey();    // uid
                            database.getInstance().getReference().child("UserAccount").child(uid).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    UserAccount userAccount = snapshot.getValue(UserAccount.class);

                                    String email = userAccount.getEmailId();
                                    String email2 = email.substring(0, email.lastIndexOf("@"));
                                    String user = userAccount.getName() + "(" + email2 + ")";

                                    tmp.add(user);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
        UserList = new String[tmp.size()];
        for(int i=0;i < UserList.length;i++){
            UserList[i] = tmp.get(i);
        }
    }



}