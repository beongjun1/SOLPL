package com.example.solpl1.chat.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.solpl1.R;
import com.example.solpl1.UserAccount;

import com.example.solpl1.chat.Activity.AddMeetingActivity;
import com.example.solpl1.databinding.RecyclerviewChatUserListBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

public class ChatUserListAdapter extends RecyclerView.Adapter<ChatUserListAdapter.ViewHolder> {

    ArrayList<String> list;
    Context context;
    String chatType;
    String chatRoomId;
    String meetingId="";
    FirebaseDatabase database;
    FirebaseAuth auth;
    String name;
    float ratingScore;

    public ChatUserListAdapter(ArrayList<String> list, Context context, String chatType, String chatRoomId, String meetingId) {
        this.list = list;
        this.context = context;
        this.chatType = chatType;
        this.chatRoomId = chatRoomId;
        this.meetingId = meetingId;
    }

    public ChatUserListAdapter(ArrayList<String> list, Context context, String chatType, String chatRoomId) {
        this.list = list;
        this.context = context;
        this.chatType = chatType;
        this.chatRoomId = chatRoomId;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recyclerview_chat_user_list, parent, false);
        return new ChatUserListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String uid = list.get(position);
        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();

        // 평점주는 화면에서 넘어왔을때
        if(!meetingId.equals("")){
            holder.binding.ratingBtn.setVisibility(View.VISIBLE);
        }
        // 평점주기 기능
        holder.binding.ratingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createRatingbar(uid,name);   // 평점주는 팝업창
            }
        });


        database.getReference().child("chat").child(chatType).child(chatRoomId)
                       .child("chatUser").child(uid).addValueEventListener(new ValueEventListener() {
                   @Override
                   public void onDataChange(@NonNull DataSnapshot snapshot) {
                       boolean authority = snapshot.getValue(boolean.class);
                       if(authority){
                           holder.binding.manager.setVisibility(View.VISIBLE);
                       }
                   }

                   @Override
                   public void onCancelled(@NonNull DatabaseError error) {

                   }
               });
        database.getReference().child("UserAccount").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserAccount userAccount = snapshot.getValue(UserAccount.class);
                Picasso.get()
                        .load(userAccount.getImageUrl())
                        .placeholder(R.drawable.default_profile)
                        .into(holder.binding.profile);

                String email = userAccount.getEmailId();
                String email2 = email.substring(0, email.lastIndexOf("@"));
                name = userAccount.getName() + "(" + email2 + ")";
                holder.binding.name.setText(name);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void giveRating(String uid) {

        DatabaseReference userRatingsRef = database.getReference().child("UserAccount").child(uid)
                .child("ratings");
        userRatingsRef.child(auth.getCurrentUser().getUid()).setValue(ratingScore)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        userRatingsRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                float ratingSum = 0;
                                int ratingCnt = 0;
                                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                                    ratingSum += dataSnapshot.getValue(Float.class);
                                    ratingCnt++;
                                }
                                float userRating = ratingSum / ratingCnt;
                                database.getReference().child("UserAccount").child(uid)
                                        .child("ratingCnt").setValue(ratingCnt).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                database.getReference().child("UserAccount").child(uid)
                                                        .child("userRating").setValue(userRating).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void unused) {
                                                                Toast.makeText(context, "등록되었습니다.", Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
                                            }
                                        });

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });


                    }
                });
    }

    private void createRatingbar(String uid, String name1) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.recyclerview_meeting_rating);

        TextView nameTV = dialog.findViewById(R.id.rating_name);
        RatingBar ratingBar = dialog.findViewById(R.id.rating_score);
        Button positiveBtn = dialog.findViewById(R.id.rating_button);
        Button negativeBtn = dialog.findViewById(R.id.rating_cancel);
        nameTV.setText(name1);

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                ratingScore = rating;
            }
        });

        negativeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });

        positiveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                giveRating(uid);    // db에 등록
            }
        });
        dialog.show();
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.CENTER);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        RecyclerviewChatUserListBinding binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = RecyclerviewChatUserListBinding.bind(itemView);
        }
    }
}
