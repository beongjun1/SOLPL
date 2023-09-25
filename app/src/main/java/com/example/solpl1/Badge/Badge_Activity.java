package com.example.solpl1.Badge;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.solpl1.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.ktx.Firebase;

public class Badge_Activity extends AppCompatActivity {

    LinearLayout mlayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_badge);


        mlayout = findViewById(R.id.layout2);
        int trip_count =0;

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        String userEmail = user.getEmail();
        String currentUser = user.getUid();
        Log.d("idToken", "currentUserIdToken " + currentUser);

        DatabaseReference userAccount;
        DatabaseReference place;
        userAccount = FirebaseDatabase.getInstance().getReference("UserAccount");
        place = FirebaseDatabase.getInstance().getReference("trip");

        mlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userAccount.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String idToken = snapshot.child(currentUser).child("idToken").getValue(String.class);
                        Log.d("idToken2", "currentUserIdToken " + idToken);
                        if(idToken == currentUser){
                            /**
                             * Todo: 파이어베이스 데이터베이스에서 영수증 인증 횟수에 대한 기록을 가져와 횟수만큼에 대한
                             * Todo: 이미지 url UserAccount 있는 뱃지 url 바꿔 주는 로직을 구현
                             */
                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.d("Badge_Activity UserAccount", "userAccount 조회 X");
                        finish();
                    }
                });

            }
        });
    }
}
